package robotgameredux.Commands;

import java.io.Serializable;

import robotgameredux.CommandsInterfaces.InteractCommandInterface;
import robotgameredux.core.Coordinates;
import robotgameredux.enums.RobotStates;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;
import robotgameredux.gameobjects.SupportRobot;
import robotgameredux.tools.UsableTool;
import robotgameredux.weapons.Weapon;

public class RobotSupportInteractCommand implements InteractCommandInterface<SupportRobot>, Serializable {

	/*
	 * Implementazione dell'interfaccia SupInteractCommandInterface<T> per il
	 * tipo SupportRobot
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = -6872378134853586932L;

	public RobotSupportInteractCommand(SupportRobot robot, Coordinates target) {
		this.robot = robot;
		this.target = target;
	}

	/**
	 * Esegue questo comando. Tramite il riferimento al robot che ha ricevuto il
	 * comando, chiama il metodo execute(Command) del sistema a cui si riferisce
	 * questo comando.
	 */

	@Override
	public Boolean execute() throws InvalidTargetException, InsufficientEnergyException {
		robot.setCommand(null);
		return robot.getInteractionSystem().execute(this);
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
	 * Ritorna lo stato attuale del robot che ha ricevuto il comando
	 * 
	 * @return lo stato del robot (enum RobotStates)
	 */

	@Override
	public RobotStates getState() {
		return robot.getState();
	}

	/**
	 * Aggiorna lo stato del robot che ha ricevuto il comando
	 * 
	 * @param state
	 *            nuovo stato del robot (enum RobotStates)
	 */

	@Override
	public void setState(RobotStates state) {
		robot.setState(state);
	}

	/**
	 * Ritorna le coordinate del robot che ha ricevuto il comando
	 * 
	 * @return le coordinate del robot
	 */

	@Override
	public Coordinates getCoords() {
		return robot.getCoords();
	}

	/**
	 * Ritorna la forza del robot che ha ricevuto il comando
	 * 
	 * @return la forza del robot
	 */

	@Override
	public int getStrenght() {
		return robot.getStrenght();
	}

	/**
	 * Aggiunge il quantitativo di energia indicata al robot (ricarica)
	 * 
	 * @param charge
	 *            l'energia da aggiungere al robot
	 */

	@Override
	public void addEnergy(Integer charge) {
		robot.addEnergy(charge);
	}

	/**
	 * Rimuove l'energia (costo dell'azione) dal robot che ha ricevuto il
	 * comando
	 * 
	 * @param n
	 *         l'energia da rimuovere
	 */

	@Override
	public void removeEnergy(int n) {
		robot.removeEnergy(n);
	}

	/**
	 * Aggiunge l'oggett scelto a quelli disponibili per il robot
	 * 
	 * @param t
	 *         l'oggetto da aggiungere al robot
	 */

	@Override
	public void addTool(UsableTool t) {
		robot.addTool(t);
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

	private SupportRobot robot;
	private Coordinates target;

	public void addWeapon(Weapon w) {}

}
