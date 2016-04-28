package robotgameredux.actors;

import java.util.ArrayList;

import javax.swing.JFrame;

import robotgameredux.core.GameWorld;
import robotgameredux.core.Vector2;
import robotgameredux.input.RobotActionDialog;
import robotgameredux.input.RobotStates;

public class RobotController {
  /* RobotController (creato in GameWorld) crea i robot e ne gestisce gli input.
   * Riceve gli input dal listener del Panel GameWorld direttamente, tramite il riferimento che GameWorld ha al controller
   * Se l'input punta ad un robot gestito da questo controller, viene visualizzato un custom JDialog con pulsanti per effettuare diverse azioni
   * Il controller usa un metodo "getInput" dal JDialog per ricavare l'input desiderato (Il JDialog non ha riferimenti al controller per evitare tight-coupling) 
   */
	
	GameWorld gameWorld;
	ArrayList<Robot> robots;
	Integer activeIndex;
	Vector2 currentInput;
	RobotActionDialog actionSelector;
	
	//Variabili di lavoro
	RobotStates robotInput;
	int i = 0;
	Boolean trovato = false;	
	
	public RobotController (GameWorld gameWorld) {
		this.gameWorld = gameWorld;
		this.activeIndex = null;
		this.robots = new ArrayList<Robot>();
		this.actionSelector = new RobotActionDialog((JFrame)gameWorld.getParent(), false);
		currentInput = null;
	}
	
	public Robot createRobot(Vector2 position) {
		Robot robot = new Robot(gameWorld);
		robot.setCoords(position);
		robots.add(robot);
		return robot;
	}
	
	public void setInput(Vector2 currentInput) {
		this.currentInput = currentInput;
	}
	
	
	/*
	 * Se c'è già un robot attivo, il Dialog non va visualizzato nuovamente.
	 */
	
	public void update() {
				
		/*
		* Clicco un bottone sul dialog e setto lo stato del robot di conseguenza. Prendo l'indice del robot e lo tengo come attivo.
		* Al click successivo parte l'else qui sotto. Switcho in base allo stato del robot e faccio qualcosa.
		* Potenzialmente potrei settare uno stato anche nel GameWorld per fare sapere a tutti gli altri controllori che c'è
		* un robot che deve selezionare la meta o un bersaglio in modo tale da non far apparire altri dialogs se clicca su dei robot
		*/

		robotInput = actionSelector.getInput();
		i = 0;
		trovato = false;	
		
		if (this.currentInput != null && activeIndex == null) {
			while (!trovato && i < robots.size()) {
				Robot robot = robots.get(i);
				System.out.println(robot.getCoords());
				System.out.println(this.currentInput.x + "--4");
				if (robot.getCoords().x == currentInput.x && robot.getCoords().y == currentInput.y){
					trovato = true;
					actionSelector.showAction();
					//System.out.println("Il robot è alla posizione: " + robot.getCoords().toString());
					activeIndex = i; 
					//System.out.println("Il robot è attivo prima del click? " + robot.getState().toString());
					robot.setState(RobotStates.ACTIVE);
					//System.out.println("Il robot è attivo dopo il click? " + robot.getState().toString());
					currentInput = null;
				}
				i++;
			}			
		}
		else if (activeIndex != null && robotInput != null) {
			//System.out.println("HO TROVATO UN INPUT");			
			if (robotInput == RobotStates.INACTIVE) {
				robots.get(activeIndex).setState(RobotStates.INACTIVE);
				actionSelector.resetInput();
				activeIndex = null;
			}
			else if (currentInput != null) {
				if (robotInput == RobotStates.MOVING) {
					robots.get(activeIndex).setState(RobotStates.MOVING);
					robots.get(activeIndex).setDest(currentInput);
					actionSelector.resetInput();
					activeIndex = null;
				}
			} //Da rimuovere
			else {
				System.out.println("Aspetto il secondo input");
			}
			currentInput = null;
		}
		System.out.println("Fine update controller");
		currentInput = null;
	}	
}

/*		if (currentInput != null) {
if (activeIndex == null) {
	for (int i = 0; i < robots.size(); i++) {
		Robot robot = robots.get(i);
		if (robot.getCoords().x == currentInput.x && robot.getCoords().y == currentInput.y){
			actionSelector.showAction();
			System.out.println("Il robot è alla posizione: " + robot.getCoords().toString());
			//activeIndex = i; //Se viene selezionato "Non fare nulla" questo non va fatto
			System.out.println("Il robot è attivo prima del click? " + robot.getState().toString());
			robot.setState(RobotStates.ACTIVE);
			System.out.println("Il robot è attivo dopo il click? " + robot.getState().toString());
			actionSelector.showAction();

			if (actionSelector.getInput() != RobotStates.INACTIVE) {
				RobotStates a = actionSelector.getInput();
				System.out.println("Preso dal dialog");
				System.out.println(a);
				robot.setState(a);
				activeIndex = i;
			}
			
		}
	}
	currentInput = null;
}
else {
	Robot active = robots.get(activeIndex);
	//active.setState(RobotStates.MOVING);
	switch(active.getState()) { 
		case MOVING:
			if (currentInput.x != active.getCoords().x || currentInput.y != active.getCoords().y) {
				active.setDest(currentInput);
				active.setState(RobotStates.MOVING);		
			}
			break;
		case ATTACKING :
			;
		case ACTIVE:
			;
		case INACTIVE:
			;
	}
	
	if (currentInput.x != active.getCoords().x || currentInput.y != active.getCoords().y) {
		active.setDest(currentInput);
		active.setState(RobotStates.MOVING);		
	}
	currentInput = null;
	activeIndex = null;
}
}*/