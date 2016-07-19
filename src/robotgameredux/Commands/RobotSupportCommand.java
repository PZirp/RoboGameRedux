package robotgameredux.Commands;

import java.io.Serializable;

import robotgameredux.CommandsInterfaces.SupportCommandInterface;
import robotgameredux.core.Coordinates;
import robotgameredux.enums.Faction;
import robotgameredux.enums.RobotStates;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;
import robotgameredux.gameobjects.SupportRobot;
import robotgameredux.tools.UsableTool;

public class RobotSupportCommand implements SupportCommandInterface<SupportRobot>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8562057930386490438L;

	public RobotSupportCommand(Integer activeObjectIndex, Coordinates target, SupportRobot robot) {
		this.activeObjectIndex = activeObjectIndex;
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
		return robot.getSupportSystem().execute(this);

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
	 * Rimuove l'energia (costo dell'azione) dal robot che ha ricevuto il
	 * comando
	 * 
	 * @param l'energia
	 *            da rimuovere
	 */

	@Override
	public void removeEnergy(int e) {
		robot.removeEnergy(e);
	}

	/**
	 * Preleva l'oggetto selezionato tramite input da quelli disponibili nel
	 * robot
	 * 
	 * @return UsableTool - l'oggetto scelto
	 */

	@Override
	public UsableTool getActiveTool() {
		return robot.getActiveTool(activeObjectIndex);
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
	 * Ritorna le coordinate dell'obiettivo di questo comando
	 * 
	 * @return target - l'obiettivo del comando
	 */

	@Override
	public Coordinates getTarget() {
		return target;
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
	 * Rimuove l'oggetto indicato (usato nell'esecuzione del comando) dal robot
	 * che ha ricevuto il comando
	 * 
	 * @param l'oggetto
	 *            da rimuovere
	 */

	@Override
	public void removeUsedTool(UsableTool tool) {
		robot.removeUsedTool(tool);
	}

	private Integer activeObjectIndex;
	private Coordinates target;
	private SupportRobot robot;

}