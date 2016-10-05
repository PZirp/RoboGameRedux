package robotgameredux.gameobjects;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import robotgameredux.CommandsInterfaces.Command;
import robotgameredux.core.Coordinates;
import robotgameredux.enums.Faction;
import robotgameredux.enums.RobotStates;
import robotgameredux.exceptions.CriticalStatusException;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;
import robotgameredux.graphic.Sprite;
import robotgameredux.systemInterfaces.MovementSystem;

/**
 * Classe che modella un attore base da utilizzare all'interno
 * dell'applicazione. Contiene campi che rappresentano valori comuni a tutti gli
 * attori, come l'energia, i punti vita, la forza, il raggio d'azione e la
 * difesa. Implementa dei metodi per la gestione degli stessi, sovrascrivibili
 * in caso di necessità. Contiene inoltre la sprite associata all'attore scelto.
 * 
 * Contiene un campo Command che rappresenta il comando che l'attore eseguirà al
 * prossimo ciclo di update. Abilità comune a tutti gli attori è il potersi
 * muovere, quindi questa classe base mantiene un riferimento ad un sistema di
 * movimento.
 */

public abstract class Actor extends GameObject {

	/**
	 * Costruttore. Costruisce un attore base. Inizializza i vari campi relativi
	 * alle statistiche dell'attore con dei valori di default, eventualmente
	 * modificabili tramite altri metodi.
	 * 
	 * @param coords
	 *            - le coordinate in cui si troverà l'attore al momento della
	 *            creazione
	 * @param ms
	 *            - il sistema di movimento scelto al momento della costruzione
	 */

	public Actor(Coordinates coords, MovementSystem ms) {
		super(coords);
		this.movementSystem = ms;
		this.state = RobotStates.IDLE;
		this.health = 100;
		this.range = 5;
		this.energy = 100;
		this.defense = 5;
		this.strenght = 10;
		this.propertyChange = new PropertyChangeSupport(this);
	}

	/**
	 * Metodo che causa l'update della sprite in base allo stato corrente
	 * dell'actor a cui è associata.
	 * 
	 * @see robotgameredux.gameobjects.GameObject#render()
	 */

	@Override
	public void render() {

		/*
		 * if(energy == 0) { System.out.print("Energia insufficiente "); }
		 * System.out.println("Posizione: X = " + this.getCoords().getX() +
		 * " Y = " + this.getCoords().getY()); System.out.println(
		 * "Salute del robot: " + health + getClass().getName());
		 * System.out.println("Energia del robot: " + this.energy);
		 */
		sprite.update();
	}

	/**
	 * Metodo che aggiorna lo stato dell'attore. Se è presente un comando, esso
	 * viene eseguito. In base al risultato ritornato dall'esecuzione del
	 * comando, l'attore finisce il turno o continua l'esecuzione all'update
	 * successivo. Se al termine dell'esecuzione il robot non ha più energia,
	 * viene messo in stato INATTIVO, ed un PropertyChangeEven viene generato
	 * per rendere noto il cambiamento a tutti i listener registrati
	 * sull'attore.
	 */

	@Override
	public void update() throws InvalidTargetException, InsufficientEnergyException {
		Boolean res = false;
		if (currentCommand != null) {
			res = currentCommand.execute();
			// JOptionPane.showMessageDialog(null, "AggiornO");
		}
		if (res == true) {
			setState(RobotStates.TURN_OVER);
		}
		if (energy <= 0 && state != RobotStates.INACTIVE) {
			setState(RobotStates.INACTIVE);
			propertyChange.firePropertyChange("DEACTIVATED", this, null);
			return;
		}
	}

	/*
	 * Metodi relativi all'energia
	 */

	/**
	 * Ritorna il livello di energia attuale dell'attore
	 * 
	 * @return L'energia attuale
	 */

	public int getEnergy() {
		return this.energy;
	}

	/**
	 * Rimuove la quantità specificata di energia dall'attore
	 * 
	 * @param energy 
	 * 			l'energia da rimuovere
	 */

	public void removeEnergy(int energy) {
		this.energy = this.energy - energy;
		if (energy <= 0) {
			// this.state = RobotStates.INACTIVE;
			// propertyChange.firePropertyChange("DEACTIVATED", this, null);
		}
	}

	/**
	 * Aggiunge la quantità specificata di energia all'attore. Se l'attore era
	 * senza energia, e quindi in stato INATTIVO, viene riattivato e un
	 * PropertyChangeEvent è generato
	 * 
	 * @param energy 
	 * 		l'energia da aggiungere
	 */

	public void addEnergy(int energy) {
		if (this.energy == 0) {
			this.setState(RobotStates.TURN_OVER);
			propertyChange.firePropertyChange("REACTIVATED", this, null);
		}
		this.energy = this.energy + energy;
		if (this.energy > 100) {
			this.energy = 100;
		}
	}

	/**
	 * Setta lo stato dell'attore secondo quello indicato dal parametro. E'
	 * generato un PropertyChangeEvent per informare tutti i listener
	 * dell'attore.
	 * 
	 * @param state
	 *            nuovo stato dell'attore
	 */

	public void setState(RobotStates state) {
		this.state = state;
		propertyChange.firePropertyChange(this.state.toString(), this, null);
	}

	/**
	 * Ritorna lo stato corrente dell'attore
	 * 
	 * @return lo stato sotto forma di enum RobotStates
	 */

	public RobotStates getState() {
		return this.state;
	}

	/**
	 * Imposta la spirte dell'attore
	 * 
	 * @param sprite
	 *            la sprite da settare
	 */

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	/**
	 * Ritorna la sprite impostata per l'attore
	 * 
	 * @return la sprite
	 */

	public Sprite getSprite() {
		return sprite;
	}

	/*
	 * Metodi relativi alla salute
	 */

	/**
	 * Ritorna il livello di salute attuale dell'attore
	 * 
	 * @return la salute
	 */

	public int getHealth() {
		return this.health;
	}

	// heal serve per gli oggetti di guarigione

	/**
	 * Metodo usato per aggiungere salute all'attore (per esempio un oggetto di
	 * guarigione). In questa implementazione, l'attore ha una quantità massima
	 * di salute pari a 100, quindi se la quantià di salute aggiunta è maggiore,
	 * la salute dell'attore si fermerà a 100
	 * 
	 * @param health
	 *            salute da aggiungere.
	 */

	public void heal(int health) {
		int newHealth = this.health + health;
		if (newHealth > 100)
			this.health = 100;
		else
			this.health = newHealth;
	}

	/**
	 * Metodo usato per rimuovere salute dall'attore, per esempio in seguito ad
	 * un attacco nemico. Se la salute risultante è = 0, il robot è distrutto ed
	 * è generato un PropertyChangeEvent che notifica i listener
	 * 
	 * @param damage
	 * 		il danno da applicare
	 * @throws CriticalStatusException
	 *             nel caso la salute scenda sotto il 25% del massimo
	 */

	public void damage(int damage) throws CriticalStatusException {

		this.health = health - damage;

		if (health <= 25 && health > 0) {
			throw new CriticalStatusException(this.clone());
		}

		if (this.health <= 0) {
			// Mandare un propertychangedevent che dice che è morto per farlo
			// rimuovere
			propertyChange.firePropertyChange("DESTROYED", this, null);
		}
	}

	/**
	 * Ritorna il livello di difesa dell'attore
	 * 
	 * @return la difesa
	 */

	public int getDefense() {
		return this.defense;
	}

	/**
	 * Metodo usato per aggiungere punti alla difesa dell'attore. In questa
	 * implementazione ha un massimo di 20 punti.
	 * 
	 * @param defense
	 * 			la difesa da aggiungere
	 */

	public void setDefense(int defense) {
		int newDefense = this.defense + defense;
		if (newDefense > 20) {
			this.defense = 20;
		} else {
			this.defense = newDefense;
		}
	}

	/**
	 * Ritorna la fazione a cui appartiene l'attore
	 * 
	 * @return la fazione come oggetto dell'enum Faction
	 */

	public Faction getFaction() {
		return this.faction;
	}

	// Eliminabile setFaction

	public void setFaction(Faction faction) {
		this.faction = faction;
	}

	/**
	 * Ritorna il range di movimento dell'attore
	 * 
	 * @return il range
	 */

	public int getRange() {
		return range;
	}

	/**
	 * Ritorna la forza dell'attore
	 * 
	 * @return la forza
	 */

	public int getStrenght() {
		return strenght;
	}

	/*
	 * public void addSprite(Sprite sprite) { this.sprite = sprite; }
	 */

	/**
	 * Setta il comando attuale da far eseguire all'attore.
	 * 
	 * @param command
	 *            da eseguire al prossimo ciclo di update
	 */

	public void setCommand(Command command) {
		this.currentCommand = command;
	}

	/*
	 * public Command getCurrentCommand() { return currentCommand; }
	 */

	/**
	 * Ritorna un riferimento al sistema di movimento usato dall'istanza
	 * dell'attore
	 * 
	 * @return il sistema di movimento
	 */

	public MovementSystem getMovementSystem() {
		return movementSystem;
	}

	@Override
	public String toString() {
		return super.toString() + " [Health = " + health + " Range = " + range + " Energy = " + energy + " Defense = "
				+ defense + " Strenght = " + strenght + " Faction = " + faction.toString() + " State = "
				+ state.toString();
	}

	@Override
	public boolean equals(Object otherObject) {
		if (!super.equals(otherObject))
			return false;
		Actor other = (Actor) otherObject;
		// Aggiungere gli equals dei command e dei sistemi
		return faction.equals(other.faction) && state.equals(other.state) && health == other.health
				&& range == other.range && energy == other.energy && defense == other.defense
				&& strenght == other.strenght && propertyChange.equals(other.propertyChange)
				&& movementSystem == other.movementSystem;
	}

	@Override
	public Actor clone() {
		Actor cloned = (Actor) super.clone();
		cloned.health = health;
		cloned.range = range;
		cloned.energy = energy;
		cloned.defense = defense;
		cloned.strenght = strenght;
		cloned.faction = faction;
		cloned.state = state;
		cloned.propertyChange = propertyChange;
		cloned.movementSystem = movementSystem;
		return cloned;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChange.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChange.removePropertyChangeListener(listener);
	}

	/*
	 * public PropertyChangeSupport getPropertyChange() { return propertyChange;
	 * }
	 */

	private Command currentCommand;
	private MovementSystem movementSystem;
	private Faction faction;
	private RobotStates state;
	private int health;
	private int range;
	private int energy;
	private int defense;
	private int strenght;
	transient private Sprite sprite;
	private PropertyChangeSupport propertyChange;

}
