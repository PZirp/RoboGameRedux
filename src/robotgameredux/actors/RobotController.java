package robotgameredux.actors;

import java.util.ArrayList;

import javax.swing.JFrame;

import robotgameredux.core.GameWorld;
import robotgameredux.core.Vector2;
import robotgameredux.graphic.Visual;
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
	Robot activeRobot;
	Vector2 currentInput;
	RobotActionDialog actionSelector;
	
	//Variabili di lavoro
	RobotStates robotInput;
	int i = 0;
	Boolean trovato = false;	
	
	public RobotController (GameWorld gameWorld) {
		this.gameWorld = gameWorld;
		this.activeRobot = null;
		this.robots = new ArrayList<Robot>();
		this.actionSelector = new RobotActionDialog((JFrame)gameWorld.getParent(), false);
		currentInput = null;
	}
	
	public Robot createRobot(Vector2 position) {
		Robot robot = new Robot(this);
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
		
		if (this.currentInput != null && activeRobot == null) {
			while (!trovato && i < robots.size()) {
				Robot robot = robots.get(i);
				if (robot.getCoords().x == currentInput.x && robot.getCoords().y == currentInput.y){
					trovato = true;
					actionSelector.showAction();
					activeRobot = robot; 
					robot.setState(RobotStates.ACTIVE);
					currentInput = null;
				}
				i++;
			}			
		}
		else if (activeRobot != null && robotInput != null) {
			if (robotInput == RobotStates.INACTIVE) {
				activeRobot.setState(RobotStates.INACTIVE);
				actionSelector.resetInput();
				activeRobot = null;
			} 
			//try catch qui per InsufficientEnergyException?
			else if (currentInput != null) {
				if (robotInput == RobotStates.MOVING) {
					//Possibile TileOccupiedException?
					if (gameWorld.isTileFree(currentInput)) {
						activeRobot.setState(RobotStates.MOVING);
						activeRobot.setDest(currentInput);
						actionSelector.resetInput();
						activeRobot = null;
					}
				}
			} //Da rimuovere
			else {
				System.out.println("Aspetto il secondo input");
			}
			currentInput = null;
		}
		currentInput = null;
	}
	
	public void addToScreen(Visual sprite) {
		this.gameWorld.add(sprite);
	}
	
	public void updateMap(Vector2 oldPos, Vector2 coords) {
		gameWorld.releaseTile(oldPos);
		gameWorld.occupyTile(coords);
	}
	
}
