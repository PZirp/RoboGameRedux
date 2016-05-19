package robotgameredux.actors;

import java.util.ArrayList;

import javax.swing.JFrame;

import robotgameredux.core.ActionObject;
import robotgameredux.core.GameWorld;
import robotgameredux.core.Vector2;
import robotgameredux.input.Faction;
import robotgameredux.input.RobotActionDialog;
import robotgameredux.input.RobotStates;
import robotgameredux.weapons.Bullet;
import robotgameredux.weapons.Weapon;

public class AttackRobotController extends RobotController{

	public AttackRobotController (GameWorld gameWorld) {
		super(gameWorld);
		//this.gameWorld = gameWorld;
		this.activeRobot = null;
		this.robots = new ArrayList<AttackRobot>();
		this.actionSelector = new RobotActionDialog((JFrame)gameWorld.getParent(), false);
		currentInput = null;
		this.move = new MoveCommand(); 
	}
	

	/*
	 * Se c'è già un robot attivo, il Dialog non va visualizzato nuovamente.
	 */
	
	public void parseInput() {
				
		//In futuro passare lo state del world come parametro di questo metodo
		/*
		* Clicco un bottone sul dialog e setto lo stato del robot di conseguenza. Prendo l'indice del robot e lo tengo come attivo.
		* Al click successivo parte l'else qui sotto. Switcho in base allo stato del robot e faccio qualcosa.
		* Potenzialmente potrei settare uno stato anche nel GameWorld per fare sapere a tutti gli altri controllori che c'è
		* un robot che deve selezionare la meta o un bersaglio in modo tale da non far apparire altri dialogs se clicca su dei robot
		*/

		robotInput = actionSelector.getInput();
		i = 0;
		trovato = false;	
		//Quando implementerò la scelta casuale del robot attivo, questo primo if dove controllo se è stato cliccato un robot e lo cerco deve essere eliminato
		if (this.currentInput != null && activeRobot == null) {
			while (!trovato && i < robots.size()) {
				AttackRobot robot = robots.get(i);
				if (robot.getCoords().x == currentInput.x && robot.getCoords().y == currentInput.y){
					trovato = activateRobot(robot);
				}
				i++;
			}			
		}
		else if (activeRobot != null && robotInput != null) {
			if (robotInput == RobotStates.DO_NOTHING) {
				doNothing();
			} 
			//try catch qui per InsufficientEnergyException?
			else if (currentInput != null) {
				if (robotInput == RobotStates.MOVING) {
					//Possibile TileOccupiedException? Catch qui
					moveRobot();
				} else if (robotInput == RobotStates.ATTACKING) {
					System.out.println("CURRENT INPUT ATTACK: " + currentInput.toString());
					activeRobot.setTarget(currentInput);
					activeRobot.setState(RobotStates.ATTACKING);
					activeRobot = null;
					/*if (gameWorld.isEnemeyAt(currentInput/*, new Base()) == true) {
						activeRobot.setState(RobotStates.INACTIVE);
						activeRobot = null;
					}*/
					
					/*if (gameWorld.isEnemeyAt(currentInput)) {
						activeRobot.setActiveWeapon();
						activeRobot.setTarget(currentInput);
						activeRobot.setState(RobotStates.ATTACKING);
						activeRobot = null;
					}*/
					
				} else if (robotInput == RobotStates.DESTROY_OBSTACLE) {
					if(gameWorld.isObstacle(currentInput)) {
						activeRobot.setTarget(currentInput);
						activeRobot.setState(RobotStates.DESTROY_OBSTACLE);
						activeRobot = null;
					}
				} else if (robotInput == RobotStates.PUSH_OBSTACLE) {
					if(gameWorld.isObstacle(currentInput)) {
						activeRobot.setTarget(currentInput);
						activeRobot.setState(RobotStates.PUSH_OBSTACLE);
						activeRobot = null;
					}
				}
			} //Da rimuovere
			else {
				System.out.println("Aspetto il secondo input");
			}
		}
		currentInput = null;
	}
	
	private void moveRobot() { 
		move.setInput(currentInput);
		move.execute(activeRobot);
		activeRobot = null;

		// da fare nel sistema di movimento non più qui
		/*if (activeRobot.getCoords().dst(currentInput) == 0) {
			System.out.println("Sei già sulla tile scelta");
			activeRobot.setState(RobotStates.INACTIVE);
			activeRobot = null;
		} else if (gameWorld.isTileFree(currentInput)) {
			activeRobot.setDest(currentInput);
			actionSelector.resetInput();
			activeRobot.setState(RobotStates.MOVING);
			activeRobot = null;
		} else {
			System.out.println("Tile scelta occupata!");
			activeRobot.setState(RobotStates.INACTIVE);
			activeRobot = null;
		}*/
	}
	
	//Anche questo e doNothing() potrebbero essere messi nel controller base
	private Boolean activateRobot(AttackRobot robot) {
		actionSelector.showAction(robot.getSprite());
		activeRobot = robot; 
		robot.setState(RobotStates.ACTIVE);
		currentInput = null;
		return true;
	}
		
	private void doNothing() {
		activeRobot.setState(RobotStates.DO_NOTHING);
		actionSelector.resetInput();
		activeRobot = null;
	}

	public void addRobot(AttackRobot robot) {
		robots.add(robot);
	}
	
	/*public void addProjectileToWorld(Projectile projectile) {
		this.gameWorld.addProjectile(projectile);
	}*/
	//Da modificare facendo ritornare l'enum Faction in base a di chi è il robot
	/*NON MI SERVONO PIù QUI:
	public Boolean isRobot(Vector2 target) {
		System.out.println("TARGET COORDINATES isRobot" + target.toString());

		for (Robot r : robots) {
			if (r.getCoords().x == target.x && r.getCoords().y == target.y) { 
				this.target = r;
				return true;
			}
		}
		return false;		
	}
	
	public Boolean isFriendly(Vector2 target) {
		for (Robot r : robots) {
			if (r.getCoords().x == target.x && r.getCoords().y == target.y && r.getFaction() == Faction.FRIEND) { 
				this.target = r;
				return true;
			}
		}
		return false;	
		
	}*/
	
	/*public void deliverAttack(Weapon wpn) {
		System.out.println("TARGET COORDINATES del" + target.getCoords().toString());
		this.target.setHealth(wpn.getDamage()); 
		target = null;
	}*/
	
	/*public void deliverPush(ActionObject obj) {
		gameWorld.pushObstacle(obj);
	}
	
	public void deliverDestroy(ActionObject obj) {
		gameWorld.destroyObstacle(obj);
	}*/
	
	//ActionObject currentAction;
	private ArrayList<AttackRobot> robots;
	private AttackRobot activeRobot;	
	private RobotActionDialog actionSelector;
	private Robot target;	
	//Variabili di lavoro
	private RobotStates robotInput;
	private int i = 0;
	private Boolean trovato = false;
	private MoveCommand move;

	
}
