package robotgameredux.actors;

import java.util.ArrayList;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.CommandsInterfaces.Command;
import robotgameredux.core.GameManager;
import robotgameredux.core.GameWorld;
import robotgameredux.core.Coordinates;
import robotgameredux.graphic.FSupportRobotSprite;
import robotgameredux.input.RobotStates;
import robotgameredux.input.SupportRobotController;
import robotgameredux.systemInterfaces.AttackInteractionSystem;
import robotgameredux.systemInterfaces.MovementSystem;
import robotgameredux.systemInterfaces.SupportInteractionSystem;
import robotgameredux.systemInterfaces.SupportSystem;
import robotgameredux.systemsImplementations.StandardSupportSystem;
import robotgameredux.tools.HealthPack;
import robotgameredux.tools.UsableTool;
import robotgameredux.weapons.PistolBullet;
import robotgameredux.weapons.Pistol;

public class SupportRobot extends Robot{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6334439551075353024L;
	public SupportRobot(Coordinates coords, MovementSystem ms, SupportSystem sm, SupportInteractionSystem is) {
		super(coords, ms);
		this.tools = new ArrayList<UsableTool>();
		this.supportSystem = sm;
		this.interactionSystem = is;
	}
	
	/*public void update() throws InvalidTargetException, InsufficientEnergyException {

		Boolean res = false;
		if (this.getCurrentCommand() != null) {
			res = this.getCurrentCommand().execute();
		}
		if (res == true) {
			setState(RobotStates.TURN_OVER);
		}
		if (getEnergy() <= 0 && getState() != RobotStates.INACTIVE) {
			setState(RobotStates.INACTIVE);
			getPropertyChange().firePropertyChange("DEACTIVATED", this, null);
			return;
		}
	}*/


	/**
	 * Aggiunge il tool specificato come parametro al robot.
	 * @param tool il tool da aggiungere
	 */
		
	public void addTool(UsableTool tool) {
		tools.add(tool); //fare add(tool.clone())
	}
	

	/**
	 * Ritorna una copia dell'array dei  tool del robot
	 * @return l'array dei tool (clone)
	 */
	
	
	public ArrayList<UsableTool> getTools() {
		return tools; //ritornare un clone
	}

	

	/**
	 * Ritorna il tool specificato dall'indice del parametro
	 * @param i l'indice del tool nell'array
	 * @return il tool scelto
	 */
	
	
	public UsableTool getActiveTool(Integer i) {
		return tools.get(i);
	}
	
	/**
	 * Rimuove il tool usato specificato
	 * @param tool da rimuovere
	 */
	
	public void removeUsedTool(UsableTool tool) {
		this.tools.remove(tool);
	}

	/**
	 * Ritorna un riferimento al sistema di supporto di una istanza del robot di supporto
	 * @return il sistema di supporto
	 */
	
	public SupportSystem getSupportSystem() {
		return supportSystem;
	}
	

	/**
	 * Ritorna un riferimento al sistema di interazione usato dall'istanza del robot di supporto
	 * @return il sistema di interazione usato
	 */
	
	public SupportInteractionSystem getInteractionSystem() {
		return interactionSystem;
	}
	
	public String toString() {
		return super.toString() + " [Tools = " + tools.toString() + "]"; 
	}
	
	public boolean equals(Object otherObject) {
		if (!super.equals(otherObject)) return false;
		SupportRobot other = (SupportRobot) otherObject;
		//Anche qui, aggiungere gli equals per i sistemi
		return tools.equals(other.tools) && supportSystem == other.supportSystem && interactionSystem == other.interactionSystem;
	}
	
	public SupportRobot clone() {
		SupportRobot clone = (SupportRobot) super.clone();
		clone.tools = new ArrayList<UsableTool>(tools);
		clone.supportSystem = supportSystem;
		clone.interactionSystem = interactionSystem;
		return clone;
	}
	
	private ArrayList<UsableTool> tools;
	private SupportSystem supportSystem;
	private SupportInteractionSystem interactionSystem;

	
}
