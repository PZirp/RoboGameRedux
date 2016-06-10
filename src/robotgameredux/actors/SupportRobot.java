package robotgameredux.actors;

import java.util.ArrayList;

import robotgameredux.core.GameManager;
import robotgameredux.core.GameWorld;
import robotgameredux.core.Vector2;
import robotgameredux.graphic.VisualSup;
import robotgameredux.input.Command;
import robotgameredux.input.RobotStates;
import robotgameredux.input.SupportRobotController;
import robotgameredux.systems.AttackInteractionSystem;
import robotgameredux.systems.GameSystem;
import robotgameredux.systems.MovementSystem;
import robotgameredux.systems.StandardSupportSystem;
import robotgameredux.systems.SupportInteractionSystem;
import robotgameredux.systems.SupportSystem;
import robotgameredux.tools.HealthPack;
import robotgameredux.tools.UsableTool;
import robotgameredux.weapons.Bullet;

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
	
	public void update() {
		if (this.getCurrentCommand() != null) {
			this.getCurrentCommand().execute();
		}
		
		if (this.getState() != RobotStates.INACTIVE) {
			/*if (this.getState() == RobotStates.MOVING) {
				super.move();
			} */ if (this.getState() == RobotStates.TAKE_OBJECT){
				//TODO					
			} /*else if (this.getState() == RobotStates.USE_OBJECT) {
				//Mostra un dialog con tutti gli oggetti utilizzabili su sé stesso, e mette quello scelto come attivo
				/*
				 * No, il dialog lo mostra il controller, che setta il tool attivo, al ciclo di update seguente qui verrà
				 * chiamato il metodo use dell'oggetto	*/
			} else if (this.getState() == RobotStates.GIVE_OBJECT) {
				if (this.getCoords().dst(this.getTarget()) <= 1.5) {
					this.setState(RobotStates.INACTIVE);
					this.setTarget(null);
				} else {
					this.setState(RobotStates.INACTIVE);
				}
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
	private void giveObject() {
		if (this.getEnergy() != 0) {
				this.setState(RobotStates.INACTIVE);
				this.setTarget(null);
			}
	}
	
	public void addTool(UsableTool tool) {
		tools.add(tool);
	}
	
	public ArrayList<UsableTool> getTools() {
		return tools;
	}

	@Override
	public UsableTool getActiveTool(Integer i) {
		return tools.get(i);
	}

	public SupportSystem getSupportSystem() {
		return supportSystem;
	}
	
	public SupportInteractionSystem getInteractionSystem() {
		return interactionSystem;
	}
	
	
	private ArrayList<UsableTool> tools;
	private SupportRobotController reference;
	private RobotStates state;
	private SupportSystem supportSystem;
	private SupportInteractionSystem interactionSystem;

	
}
