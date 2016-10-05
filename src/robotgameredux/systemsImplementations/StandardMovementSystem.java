package robotgameredux.systemsImplementations;

import java.io.Serializable;
import java.util.ArrayList;

import robotgameredux.CommandsInterfaces.MovementCommandInterface;
import robotgameredux.core.Coordinates;
import robotgameredux.core.IGameWorld;
import robotgameredux.enums.RobotStates;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;
import robotgameredux.systemInterfaces.MovementSystem;


/**
 * Classe che implementa una modalità standard per eseguire il comando di movimento.
 * Quando un attore viene creato, è possibile aggiungere questo sistema standard se 
 * non si vuole dare un comportamento specifico al movimento dell'attore.
 * Questo sistema permette di muoversi solo nelle quattro direzioni
 * (su, giù, destra e sinistra) una volta per turno, tenendo conto del fatto che la
 * strada scelta deve essere priva di ostacoli e caselle occupate.
 * Questa classe implementa l'interfaccia MovementSystem.
 * 
 * @author Paolo Zirpoli
 */

public class StandardMovementSystem implements MovementSystem, Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1841818356815052104L;

	public StandardMovementSystem(IGameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}
	
	/**
	 * Metodo che esegue la funzione di movimento. Prende come argomento un
	 * MovementCommandInterface generico, che può incapsulare un qualunque tipo di
	 * entità capace di muoversi.
	 * Se la variabile follow è null, significa che sta iniziando un nuovo movimento.
	 * Vengono calcolati i possibili cammini che l'attore può seguire tramite l'oggetto IGameWorld.
	 * Viene controllata l'energia dell'attore che intende muoversi. In caso di energia 
	 * insufficiente, lancia una InsufficientEnergiException e l'attore viene rimesso in stato
	 * Idle. 
	 * Altrimento è eseguito il metodo beginMovement. Se questo ritorna false, il comando è impossibile
	 * e viene lanciata l'eccezzione InvalidTargetException.
	 * 
	 * Se follow != null, allora si sta continuando un moviemento iniziato in un ciclo precedente.
	 * Viene eseguito il metodo continueMovement, che se ritorna true causa la fine dell'esecuzione.
	 * 
	 * 
	 * @param command,
	 *            interfaccia che rappresenta il comando di movimento
	 * @return true se il movimento ha successo o termina, false altrimenti
	 * @throws InvalidTargetException
	 * 			Se il target non è valido
	 * @throws InsufficientEnergyException
	 * 			Se non c'è energia sufficiente per eseguire il comando
	 */

	@Override
	public <T> Boolean execute(MovementCommandInterface<T> command)
			throws InvalidTargetException, InsufficientEnergyException {
		Integer dist = command.getCoords().dst(command.getDestination());
		if (follow == null) {
			possiblePaths = gameWorld.pathfind(command.getCoords(), command.getRange());
			if (command.getEnergy() == 0 || command.getEnergy() < dist) {
				command.resetActor();
				gameWorld.disablePath(possiblePaths);
				throw new InsufficientEnergyException(command, dist);
			}
			if (!beginMovement(command)) {
				command.resetActor();
				gameWorld.disablePath(possiblePaths);
				throw new InvalidTargetException(command, "Impossibile eseguire il movimento");
			}
			gameWorld.disablePath(possiblePaths);
		} else {
			if (continueMovement(command))
				return true;
		}
		return false;
	}

	/**
	 * Inizia l'azione di movimento. Controlla se la destinazione è disponbile (non occupata) e 
	 * se rientra nel range di movimento dell'attore. Se non è così, ritorna false.
	 * Se si genera il cammino da seguire in base alla posizone di partenza dell'attore e la posizione
	 * della destinazione.
	 * Generato il cammino controlla che questo sia libero, nel caso non lo fosse ritorna false.
	 * Il costo in energia del comando è dato dalla distanza coperta nel movimento.
	 *  
	 * @param command
	 * @return true se il movimento è possibile, false altrimenti
	 */
	
	private <T> Boolean beginMovement(MovementCommandInterface<T> command) {
		Coordinates destination = command.getDestination();
		Integer dist = command.getCoords().dst(destination);
		if (destinationCheck(destination, command.getCoords()) && dist < command.getRange()) {
			generatePath(destination, command.getCoords());
			if (!collisionDetection()) {
				return false;
			}
			energyExpenditure = dist; //command.getCoords().dst(destination);
			command.setState(RobotStates.MOVING);
			return true;
		} else {
			command.resetActor();
			return false;
		}
	}
	
	/**
	 * Continua il movimento iniziato in un ciclo precedente.
	 * Imposta le coordinate dell'attore uguali alla prima coordinata nell'array follow. 
	 * La prima coordinata viene rimossa, e la tile precedente liberata.
	 * Se il robot è arrivato alla destinazione, il metodo movementComplete è chiamato.
	 * Viene rimosso il costo del movimento dall'energia dell'attore, che viene rimesso in stato idle.
	 * Follow è posto a null.
	 * @param command
	 * @return true se il movimento è concluso, false se deve continuare nel prossimo ciclo.
	 */

	private <T> Boolean continueMovement(MovementCommandInterface<T> command) {
		Coordinates oldPos = command.getCoords();
		command.setCoords(follow.get(0));
		follow.remove(0);
		gameWorld.releaseTile(oldPos);
		if (command.getCoords().equals(command.getDestination())) {
			movementComplete(command.getDestination(), oldPos);
			command.removeEnergy(energyExpenditure);
			energyExpenditure = 0;
			command.resetActor();
			follow = null;
			return true;
		}
		return false;
	}

	/**
	 * Controlla se la destinazione è valida. Controlla per prima cosa se ricade nelle 
	 * caselle raggiungibili dal robot.
	 * Se la distanza tra la destinazione e la posizione corrente è 0, allora l'attore è già
	 * sulla posizione scelta e non c'è movimento da fare.
	 * Se la tile scelta è occupata, il comando non va eseguito.
	 * @param destination
	 * @param current
	 * @return true se la destinazione è valida, false altrimenti
	 */
	
	private Boolean destinationCheck(Coordinates destination, Coordinates current) {
		if (validDestination(destination)) {
			if (current.dst(destination) == 0) {
				return false;
			} else if (gameWorld.isTileFree(destination)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * Controlla se la strada scelta per il movimento è libera da ostacoli o caselle occupate
	 * @return true se la strada è libera, false altrimenti.
	 */
	
	private Boolean collisionDetection() {
		for (Coordinates v : follow) {
			if (!gameWorld.isTileFree(v)) {
				follow = null;
				return false;
			}
		}
		return true;
	}

	/**
	 * Genera un cammino che raggiunge la destinazione dalla posizione corrente dell'attore.
	 * @param destination
	 * @param current
	 * @return follow
	 * 			ArrayList di Coordinates che rappresenta il cammino da seguire 
	 */
	
	private ArrayList<Coordinates> generatePath(Coordinates destination, Coordinates current) {
		Coordinates direction = destination.sub(current);
		follow = new ArrayList<Coordinates>();
		
		//Destra
		if (direction.getX() > 0) {
			for (int i = 1; i <= destination.dst(current); i++) {
				follow.add(new Coordinates(current.getX() + i, current.getY()));
			}
		}
		//Sinistra
		if (direction.getX() < 0) {
			for (int i = 1; i <= destination.dst(current); i++) {
				follow.add(new Coordinates(current.getX() - i, current.getY()));
			}
		}
		//Su
		if (direction.getY() > 0) {
			for (int i = 1; i <= destination.dst(current); i++) {
				follow.add(new Coordinates(current.getX(), current.getY() + i));
			}
		}
		//Giù
		if (direction.getY() < 0) {
			for (int i = 1; i <= destination.dst(current); i++) {
				follow.add(new Coordinates(current.getX(), current.getY() - i));
			}
		}
		return follow;
	}

	private Boolean validDestination(Coordinates destination) {
		for (Coordinates c : possiblePaths) {
			if (c.equals(destination))
				return true;
		}
		return false;
	}
	
	/**
	 * Libera la casella di partenza e occupa la nuova.
	 * @param destination
	 * 			la casella di destinazione
	 * @param oldPos
	 * 			la casella di partenza
	 */

	private void movementComplete(Coordinates destination, Coordinates oldPos) {
		gameWorld.releaseTile(oldPos);
		gameWorld.occupyTile(destination);
	}

	@Override
	public String toString() {
		return getClass().getName() + "[EnergyExpenditure: " + energyExpenditure + " GameWorld: " + gameWorld.toString()
				+ " PossiblePaths: " + possiblePaths.toString() + " Follow: " + follow.toString() + "]";
	}

	@Override
	public StandardMovementSystem clone() {
		try {
			StandardMovementSystem clone = (StandardMovementSystem) super.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	@Override
	public boolean equals(Object otherObject) {
		if (otherObject == null)
			return false;
		if (getClass() != otherObject.getClass())
			return false;
		StandardMovementSystem other = (StandardMovementSystem) otherObject;
		return energyExpenditure == other.energyExpenditure && gameWorld.equals(other.gameWorld)
				&& possiblePaths.equals(other.possiblePaths) && follow.equals(other.follow);
	}

	private int energyExpenditure;
	private IGameWorld gameWorld;
	private ArrayList<Coordinates> possiblePaths;
	private ArrayList<Coordinates> follow;
}
