package robotgameredux.Commands;

import java.io.Serializable;

import robotgameredux.CommandsInterfaces.AttackCommandInterface;
import robotgameredux.core.Coordinates;
import robotgameredux.enums.Faction;
import robotgameredux.enums.RobotStates;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;
import robotgameredux.gameobjects.AttackRobot;
import robotgameredux.weapons.Weapon;

public class RobotAttackCommand implements AttackCommandInterface<AttackRobot>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7414566496896963385L;

	public RobotAttackCommand(Integer activeWeaponIndex, Coordinates target, AttackRobot robot) {
		this.activeWeaponIndex = activeWeaponIndex;
		this.target = target;
		this.robot = robot;
	}

	/**
	 * Esegue questo comando. Tramite il riferimento al robot che ha ricevuto il
	 * comando, chiama il metodo execute(Command) del sistema a cui si riferisce
	 * questo comando.
	 */

	@Override
	public Boolean execute() throws InvalidTargetException, InsufficientEnergyException {
		robot.setCommand(null);
		return robot.getBattleSystem().execute(this);

	}

	/**
	 * Ritorna l'energia del robot che ha ricevuto il comando
	 * 
	 * @return l'energia del robot
	 */

	@Override
	public int getEnergy() {
		return robot.getEnergy();
	}

	/**
	 * Ritorna le coordinate dell'obiettivo di questo comando
	 * 
	 * @return target - l'obiettivo del comando
	 */

	@Override
	public Coordinates getTarget() {
		return target;
	}

	/**
	 * Ritorna un riferimento al robot che ha ricevuto il comando
	 * 
	 * @return robot - riferimento al robot
	 */

	public AttackRobot getRobot() {
		return robot;
	}

	/**
	 * Preleva l'arma selezionata tramite input da quelle disponibili nel robot
	 * 
	 * @return Weapon - l'arma scelta
	 */

	@Override
	public Weapon getActiveWeapon() {
		return robot.getActiveWeapon(activeWeaponIndex);
	}

	/**
	 * Ritorna la fazione di appartenenza del robot
	 * 
	 * @return la fazione del robot (enum Faction)
	 */

	@Override
	public Faction getFaction() {
		return robot.getFaction();
	}

	/**
	 * Aggiorna lo stato del robot che ha ricevuto il comando
	 * 
	 * @param il
	 *            nuovo stato del robot (enum RobotStates)
	 */

	@Override
	public void setState(RobotStates state) {
		robot.setState(state);
	}

	/**
	 * Rimuove l'energia (costo dell'azione) dal robot che ha ricevuto il
	 * comando
	 * 
	 * @param l'energia
	 *            da rimuovere
	 */

	@Override
	public void removeEnergy(int n) {
		robot.removeEnergy(n);

	}

	/**
	 * Ritorna le coordinate del robot che ha ricevuto il comando
	 * 
	 * @param le
	 *            coordinate del robot
	 */

	@Override
	public Coordinates getCoords() {
		return robot.getCoords();
	}

	private Integer activeWeaponIndex;
	private Coordinates target;
	private AttackRobot robot;

}
