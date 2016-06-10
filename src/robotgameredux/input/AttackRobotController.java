package robotgameredux.input;

import java.util.ArrayList;

import javax.swing.JFrame;

import robotgameredux.actors.AttackRobot;
import robotgameredux.core.GameManager;
import robotgameredux.core.GameWorld;
import robotgameredux.core.Vector2;
import robotgameredux.weapons.Weapon;
import robotgameredux.weapons.WeaponDialog;

public class AttackRobotController extends RobotController{

	public AttackRobotController (GameManager gameManager) {
		super(gameManager);
		this.activeRobot = null;
		this.robots = new ArrayList<AttackRobot>();
		this.actionSelector = new RobotActionDialog((JFrame)gameManager.getParent(), false);
		this.weaponSelector = new WeaponDialog((JFrame) gameManager.getParent(), false);
		currentInput = null;
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
		//System.out.println(robotInput.toString() + "AAAAAAAAAAAAAAAAAAAAAAA");
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
		else if (activeRobot != null) 
			switch(robotInput) {
			case IDLE: 
				break;
			case DO_NOTHING: 
				doNothing();
				break;
			case MOVING: 
				if(currentInput != null)
					moveRobot();
				break;
			case ATTACKING: 
				ArrayList<Weapon> weapons = activeRobot.getWeapons();
				if(weaponSelector.getSelected() == null) {
					weaponSelector.showWeapons(weapons);
				}
				else if (weaponSelector.getSelected() != null && currentInput != null) {
					this.target = currentInput;
				}
				if (target != null) {
					Command c = new AttackCommand(weaponSelector.getSelected(), target, activeRobot);
					activeRobot.setCommand(c);
					activeRobot = null;
					weaponSelector.resetSelected();
					target = null;
				}
				break;
			case DESTROY_OBSTACLE:
				if(currentInput != null) {
					Command c = new AtkInteractCommand(activeRobot, currentInput);
					activeRobot.setCommand(c);
					activeRobot.setState(RobotStates.DESTROY_OBSTACLE);
					/*activeRobot.setTarget(currentInput);
					activeRobot.setState(RobotStates.DESTROY_OBSTACLE);*/
					activeRobot = null;
				}
				break;
			case RECHARGE: 
				if (currentInput != null) {
					Command c = new AtkInteractCommand(activeRobot, currentInput);
					activeRobot.setCommand(c);
					activeRobot.setState(RobotStates.RECHARGE);
					activeRobot = null;
				}
				break;
			}
			currentInput = null;
	}
	
	private void moveRobot() { 
		MovementCommand mc = new  MovementCommand(activeRobot, currentInput);
		activeRobot.setCommand(mc);
		//move.setInput(currentInput);
		//move.execute(activeRobot);
		activeRobot = null;
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
	
	//ActionObject currentAction;
	private ArrayList<AttackRobot> robots;
	private AttackRobot activeRobot;	
	private RobotActionDialog actionSelector;
	private Vector2 target;		
	private WeaponDialog weaponSelector;

	//Variabili di lavoro
	private RobotStates robotInput;
	private int i = 0;
	private Boolean trovato = false;
	
}


//OLD
/*if (robotInput == RobotStates.DO_NOTHING) {
doNothing();
} 
//try catch qui per InsufficientEnergyException?
//else if (currentInput != null) {
else if (robotInput == RobotStates.MOVING) {
	//Possibile TileOccupiedException? Catch qui
	//System.out.println(currentInput.toString() + "00000000000000000000000000000000000000000000000000000000000000000");
	moveRobot();
} else if (robotInput == RobotStates.ATTACKING) {
	ArrayList<Weapon> weapons = activeRobot.getWeapons();
	if(weaponSelector.getSelected() == null) {
		//System.out.println("HEYOOOOOOOOO ECCOMI QUI BROOOOOOOOOO");
		weaponSelector.showWeapons(weapons);
	}
	else if (weaponSelector.getSelected() != null && currentInput != null) {
		System.out.println("TARGET ACQUISITIOOOOOOOOON");
		this.target = currentInput;
	}
	if (target != null) {
		Command c = new AttackCommand(weaponSelector.getSelected(), target, activeRobot);
		activeRobot.setCommand(c);
		activeRobot = null;
		weaponSelector.resetSelected();
		target = null;
	}
	//System.out.println("CURRENT INPUT ATTACK: " + currentInput.toString());
	//Creare AttackCommand
	/*activeRobot.setTarget(currentInput);
	activeRobot.setState(RobotStates.ATTACKING);
	activeRobot = null;99
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
}*/

