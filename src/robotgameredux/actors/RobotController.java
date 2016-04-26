package robotgameredux.actors;

import java.util.Vector;
import robotgameredux.core.GameWorld;
import robotgameredux.core.Vector2;
import robotgameredux.input.RobotStates;

public class RobotController {
  /* RobotController (creato in GameWorld) crea i robot e ne gestisce gli input.
   * Riceve gli input dal listener del Panel GameWorld direttamente, tramite il riferimento che GameWorld ha al controller
   * Se l'input punta ad un robot gestito da questo controller, viene visualizzato un custom JDialog con pulsanti per effettuare diverse azioni
   * Il controller usa un metodo "getInput" dal JDialog per ricavare l'input desiderato (Il JDialog non ha riferimenti al controller per evitare tight-coupling) 
   */
	
	GameWorld gameWorld;
	Vector<Robot> robots;
	Integer activeIndex;
	Vector2 currentInput;
	
	public RobotController (GameWorld gameWorld) {
		this.gameWorld = gameWorld;
		this.activeIndex = null;
		this.robots = new Vector<Robot>();
	}
	
	public Robot createRobot(Vector2 position) {
		Robot robot = new Robot(gameWorld);
		robot.setCoords(position);
		robots.addElement(robot);
		return robot;
	}
	
	public void setInput(Vector2 currentInput) {
		this.currentInput = currentInput;
	}
	
	
	public void update() {
		if (currentInput != null) {
			if (activeIndex == null) {
				for (int i = 0; i < robots.size(); i++) {
					Robot robot = robots.get(i);
					if (robot.getCoords().x == currentInput.x && robot.getCoords().y == currentInput.y){
						System.out.println("Il robot ï¿½ alla posizione: " + robot.getCoords().toString());
						activeIndex = i;
						System.out.println("Il robot è attivo prima del click? " + robot.getState().toString());
						robot.setState(RobotStates.ACTIVE);
						System.out.println("Il robot è attivo dopo il click? " + robot.getState().toString());
					}
				}
				currentInput = null;
			}
			else {
				Robot active = robots.get(activeIndex);
				if (currentInput.x != active.getCoords().x || currentInput.y != active.getCoords().y) {
					active.setDest(currentInput);
					active.setState(RobotStates.MOVING);		
				}
				currentInput = null;
				activeIndex = null;
			}
		}
	}
	
}

