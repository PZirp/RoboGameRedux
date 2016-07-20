package robotgameredux.systemsImplementations;

import java.io.Serializable;

import robotgameredux.CommandsInterfaces.AttackCommandInterface;
import robotgameredux.TargetInterfaces.TargetInterface;
import robotgameredux.core.Coordinates;
import robotgameredux.core.IActorManager;
import robotgameredux.enums.WeaponType;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;
import robotgameredux.systemInterfaces.BattleSystem;
import robotgameredux.weapons.IBullet;
import robotgameredux.weapons.Weapon;;

/**
 * Classe che implementa una modalità "standard" per eseguire il comando di attacco.
 * Quando un attore con la capacità di attaccare viene creato, è possibile aggiungere
 * questo sistema standard se non si vuole dare un comportamento specifico all'attacco
 * dell'attore.
 * In particolare questa implementazione permette di attaccare solo nelle quattro direzioni
 * (su, giù, destra e sinistra) una volta per turno con un arma selezionata, e non tiene
 * conto di potenziali ostacoli presenti tra chi attacca e l'obiettivo.
 * Questa classe implementa l'interfaccia BattleSystem.
 * 
 * @author Paolo Zirpoli
 */

public class StandardBattleSystem implements BattleSystem, Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4306228035835923018L;

	public StandardBattleSystem(IActorManager rf) {
		this.actorsManager = rf;
	}

	/**
	 * Metodo che esegue la funzione di attacco. Prende come argomento un
	 * AttackCommandInterface generico, che può incapsulare un qualunque tipo di
	 * entità capace di attaccare. Per prima cosa controlla che l'attaccante
	 * abbia sufficiente energia. Se così non dovesse essere, lancia una
	 * InsufficientEnergiException. Poi controlla che il target sia valido
	 * tramite un actorManager. Se il target è valido, controlla che sia in una
	 * posizione valida. Se una delle due condizioni di cui sopra non sia
	 * verificata, lancia una InvalidTargetException. Controlla il tipo di arma,
	 * e l'appartenenza alle varie fazioni. Infine esegue l'attacco vero e
	 * proprio. Usa il metodo fire dell'interfaccia Weapon per creare un bullet,
	 * che incapsula i dati sull'attacco. Rimuove l'energia necessaria per
	 * operare l'arma dall'attaccante, e colpisce l'obiettivo.
	 * 
	 * @param AttackCommandInterface<T>,
	 *            interfaccia che rappresenta l'attaccante
	 * @return true se l'attacco ha successo, false altrimenti
	 * @throws InvalidTargetException
	 * @throws InsufficientEnergiException
	 */

	@Override
	public <T> Boolean execute(AttackCommandInterface<T> command)
			throws InvalidTargetException, InsufficientEnergyException {

		Coordinates target = command.getTarget();
		Weapon weapon = command.getActiveWeapon();

		if (command.getEnergy() < weapon.getCost()) {
			throw new InsufficientEnergyException(command, weapon.getCost());
		}

		TargetInterface<?> targeted = actorsManager.getTarget(target);
		Boolean result = checkTrajectory(command.getCoords(), command.getTarget());

		if (targeted == null || result == false) {
			throw new InvalidTargetException(command);
		}
		if (weapon.getType() == WeaponType.OFFENSIVE && targeted.getFaction() == command.getFaction()) {
			throw new InvalidTargetException(command);
		}
		if (weapon.getType() == WeaponType.DEFENSIVE && targeted.getFaction() != command.getFaction()) {
			throw new InvalidTargetException(command);
		}
		IBullet bullet = weapon.fire();
		command.removeEnergy(weapon.getCost());
		bullet.hit(targeted);
		return true;
	}

	/**
	 * Metodo che analizza la traiettoria di un attacco. In questa
	 * implementazione, l'obiettivo del proiettile si deve trovare sopra, sotto,
	 * a sinistra o a destra dell'origine dell'attacco.
	 * 
	 * @param origin
	 *            punto d'origine dell'attacco
	 * @param target
	 *            obiettivo dell'attacco
	 * @return true se la mossa è consentita, false altrimenti
	 */

	private Boolean checkTrajectory(Coordinates origin, Coordinates target) {
		Coordinates direction = origin
				.sub(target)/* Coordinates.sub(origin, target) */;
		if (direction.getX() > 0 && direction.getY() == 0) {
			return true;
		}
		if (direction.getX() < 0 && direction.getY() == 0) {
			return true;
		}
		if (direction.getX() == 0 && direction.getY() > 0) {
			return true;
		}
		if (direction.getX() == 0 && direction.getY() < 0) {
			return true;
		}
		if (direction.getX() == 0 && direction.getY() == 0) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return getClass().getName() + "[ActorManager: " + actorsManager.toString() + "]";
	}

	@Override
	public StandardBattleSystem clone() {
		try {
			StandardBattleSystem clone = (StandardBattleSystem) super.clone();
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
		StandardBattleSystem other = (StandardBattleSystem) otherObject;
		return actorsManager.equals(other.actorsManager);
	}

	private IActorManager actorsManager;

}
