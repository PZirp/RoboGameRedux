package robotgameredux.actors;


import robotgameredux.core.GameWorld;
import robotgameredux.core.Vector2;
import robotgameredux.graphic.Sprite;
import robotgameredux.graphic.Visual;
import robotgameredux.input.Faction;

public abstract class RobotController {
  /* RobotController (creato in GameWorld) crea i robot e ne gestisce gli input.
   * Riceve gli input dal listener del Panel GameWorld direttamente, tramite il riferimento che GameWorld ha al controller
   * Se l'input punta ad un robot gestito da questo controller, viene visualizzato un custom JDialog con pulsanti per effettuare diverse azioni
   * Il controller usa un metodo "getInput" dal JDialog per ricavare l'input desiderato (Il JDialog non ha riferimenti al controller per evitare tight-coupling) 
   */
	
	GameWorld gameWorld;
	Vector2 currentInput;
	
	public RobotController (GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}
	
	public abstract Robot createRobot(Vector2 position);
	
	public abstract void update();
	
	public void setInput(Vector2 currentInput) {
		this.currentInput = currentInput;
	}
	
	
	
	/*
	 * Se c'è già un robot attivo, il Dialog non va visualizzato nuovamente.
	 */
	
	
	public void addRobotToScreen(Sprite sprite) {
		this.gameWorld.add(sprite);
	}
		
	public void updateMap(Vector2 oldPos, Vector2 coords) {
		gameWorld.releaseTile(oldPos);
		gameWorld.occupyTile(coords);
	}
	
	//Da modificare facendo ritornare l'enum Faction in base a di chi è il robot
	//Ci dovrebbe essere un metodo "checkFaction" o qualcosa del genere qui
	

}
