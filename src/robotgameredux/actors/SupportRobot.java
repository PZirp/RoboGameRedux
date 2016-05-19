package robotgameredux.actors;

import java.util.ArrayList;

import robotgameredux.core.ActionObject;
import robotgameredux.core.GameWorld;
import robotgameredux.core.MovementSystem;
import robotgameredux.core.Vector2;
import robotgameredux.graphic.VisualSup;
import robotgameredux.input.RobotStates;
import robotgameredux.tools.HealthPack;
import robotgameredux.tools.UsableTool;
import robotgameredux.weapons.Bullet;

public class SupportRobot extends Robot{

	public SupportRobot(GameWorld reference, Vector2 coords, MovementSystem ms) {
		super(reference, coords, ms);
		this.setSprite(new VisualSup(this));
		getReference().add(this.getSprite());
		this.tools = new ArrayList<UsableTool>();
		tools.add(new HealthPack());
		tools.add(new HealthPack());
		tools.add(new HealthPack());
	}
	
	public void update() {
		if (this.getState() != RobotStates.INACTIVE) {
			if (this.getState() == RobotStates.MOVING) {
				super.move();
			} else if (this.getState() == RobotStates.TAKE_OBJECT){
			
					
			} else if (this.getState() == RobotStates.USE_OBJECT) {
				//Mostra un dialog con tutti gli oggetti utilizzabili su s� stesso, e mette quello scelto come attivo
				/*
				 * No, il dialog lo mostra il controller, che setta il tool attivo, al ciclo di update seguente qui verr�
				 * chiamato il metodo use dell'oggetto
				 */
				useObject();
				this.setState(RobotStates.INACTIVE);
			} else if (this.getState() == RobotStates.GIVE_OBJECT) {
				if (this.getCoords().dst(this.getTarget()) <= 1.5) {
					this.setState(RobotStates.INACTIVE);
					this.setTarget(null);
				} else {
					this.setState(RobotStates.INACTIVE);
				}
			}
		}
	}
	
	private void useObject() {
		activeTool.use(this);
	}
	/*
	 * Decisione da prendere.
	 * Far passare l'oggetto (ed i proiettili, e le altri interazioni) come oggetto primo per il gameWorld per farle arrivare al target che poi
	 * le usa su di s�.
	 * O farsi passare il target dal gameworld, ed utilizzare un metodo esposto tipo applyEffect() che prende come argomento l'oggetto e lo usa?
	 * Oppure passare il riferimento direttamente all'oggetto e gg; No, conviene passare l'oggetto tramite metodo, perch� altrimenti bisogna 
	 * esporre troppi metodi get/set che rovinano l'incapsulamento... Ma in effetti anche se passo l'oggetto quei metodi devo esporli. 
	 * !!!I metodi get e set non rovinano l'incapsulamento!!!
	 */
	private void giveObject() {
		if (this.getEnergy() != 0) {
				//Projectile proj = activeWeapon.fire(this.getTarget());
				//reference.addProjectileToWorld(proj);
				this.setState(RobotStates.INACTIVE);
				this.setTarget(null);
			}
	}
	
	public void setActiveTool(int i) {
		activeTool = tools.get(i);
	}
	
	public void addTool(UsableTool tool) {
		tools.add(tool);
	}
	
	public ArrayList<UsableTool> getTools() {
		return tools;
	}
	
	
	private ArrayList<UsableTool> tools;
	private UsableTool activeTool;
	private SupportRobotController reference;
	private RobotStates state;
	
}
