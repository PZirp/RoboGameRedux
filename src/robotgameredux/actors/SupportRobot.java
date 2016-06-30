package robotgameredux.actors;

import java.util.ArrayList;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.Commands.Command;
import robotgameredux.core.GameManager;
import robotgameredux.core.GameWorld;
import robotgameredux.core.Vector2;
import robotgameredux.graphic.VisualSup;
import robotgameredux.input.RobotStates;
import robotgameredux.input.SupportRobotController;
import robotgameredux.systemInterfaces.AttackInteractionSystem;
import robotgameredux.systemInterfaces.MovementSystem;
import robotgameredux.systemInterfaces.SupportInteractionSystem;
import robotgameredux.systemInterfaces.SupportSystem;
import robotgameredux.systemsImplementations.StandardSupportSystem;
import robotgameredux.tools.HealthPack;
import robotgameredux.tools.UsableTool;
import robotgameredux.weapons.Bullet;
import robotgameredux.weapons.Weapon;

public class SupportRobot extends Robot implements Support{
	
	
	public SupportRobot(Vector2 coords, MovementSystem ms, SupportSystem sm, SupportInteractionSystem is) {
		super(coords, ms);
		this.tools = new ArrayList<UsableTool>();
		tools.add(new HealthPack());
		tools.add(new HealthPack());
		tools.add(new HealthPack());
		this.supportSystem = sm;
		this.interactionSystem = is;
	}
	
	public void update() throws InvalidTargetException, InsufficientEnergyException {

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
	}

	/*
	 * Decisione da prendere.
	 * Far passare l'oggetto (ed i proiettili, e le altri interazioni) come oggetto primo per il gameWorld per farle arrivare al target che poi
	 * le usa su di sè.
	 * O farsi passare il target dal gameworld, ed utilizzare un metodo esposto tipo applyEffect() che prende come argomento l'oggetto e lo usa?
	 * Oppure passare il riferimento direttamente all'oggetto e gg; No, conviene passare l'oggetto tramite metodo, perchè altrimenti bisogna 
	 * esporre troppi metodi get/set che rovinano l'incapsulamento... Ma in effetti anche se passo l'oggetto quei metodi devo esporli. 
	 * !!!I metodi get e set non rovinano l'incapsulamento!!!
	 */
		
	public void addTool(UsableTool tool) {
		tools.add(tool); //fare add(tool.clone())
	}
	
	public ArrayList<UsableTool> getTools() {
		return tools; //ritornare un clone
	}

	
	public UsableTool getActiveTool(Integer i) {
		return tools.get(i);
	}
	
	public void removeUsedTool(UsableTool tool) {
		this.tools.remove(tool);
	}

	public SupportSystem getSupportSystem() {
		return supportSystem;
	}
	
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
