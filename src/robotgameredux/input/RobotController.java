package robotgameredux.input;


import robotgameredux.core.GameManager;
import robotgameredux.core.GameWorld;
import robotgameredux.core.Vector2;
import robotgameredux.graphic.Sprite;
import robotgameredux.graphic.Visual;

public abstract class RobotController {
  /* RobotController (creato in GameWorld) crea i robot e ne gestisce gli input.
   * Riceve gli input dal listener del Panel GameWorld direttamente, tramite il riferimento che GameWorld ha al controller
   * Se l'input punta ad un robot gestito da questo controller, viene visualizzato un custom JDialog con pulsanti per effettuare diverse azioni
   * Il controller usa un metodo "getInput" dal JDialog per ricavare l'input desiderato (Il JDialog non ha riferimenti al controller per evitare tight-coupling) 
   */
	
	GameManager gameManager;
	Vector2 currentInput;
	
	public RobotController (GameManager gameManager) {
		this.gameManager = gameManager;
	}
	
	public abstract void parseInput();
	
	public void setInput(Vector2 currentInput) {
		this.currentInput = currentInput;
	}
	
	public GameManager getReference() {
		return gameManager;
	}
	
	
	
	/*
	 * Se c'è già un robot attivo, il Dialog non va visualizzato nuovamente.
	 */
	
	
	//Da modificare facendo ritornare l'enum Faction in base a di chi è il robot
	//Ci dovrebbe essere un metodo "checkFaction" o qualcosa del genere qui
	

}
