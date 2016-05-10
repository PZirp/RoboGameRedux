package robotgameredux.actors;

import robotgameredux.core.ActionObject;
import robotgameredux.core.GameWorld;
import robotgameredux.core.ObstacleState;
import robotgameredux.core.Vector2;
import robotgameredux.graphic.ObstacleSprite;
import robotgameredux.graphic.Visual;
import robotgameredux.input.RobotStates;

/*
 * Penso che il controllore degli ostacoli e delle stazioni sarà il GameWorld stesso
 */


public class Obstacle extends GameObject{
	
	public Obstacle (GameWorld reference, Vector2 coords) {
		super(coords);
		this.sprite = new ObstacleSprite(this);
		reference.add(sprite);
		this.reference = reference;
	}
	
	public int getWeight() {
		return this.weight;
	}
	
	public int getResistence() {
		return this.resistence;
	}
	
	public void render() {
		System.out.println("Posizione dell'ostacolo: " + this.getCoords().toString());
		sprite.update();
	}

	public void push(ActionObject action) {
		//Set state to beingPushed > save action to this.action for use in the update method
		this.currentAction = action;
		this.state = ObstacleState.BEING_PUSHED;
	}
	
	public void destroy(ActionObject action) {
		//Set state to destroy > save action to this.action for use in the update method
		this.currentAction = action;
		this.state = ObstacleState.BEING_ATTACKED;
	}
	
	public void update() {
			
		if (this.state == ObstacleState.BEING_PUSHED) {
			if (currentAction.getStrenght() >= this.weight) {
				// Posizione dell'attaccante rispetto all'ostacolo: Potrebbe essere utile per l'IA, per sapere dove stanno i robot nemici rispetto alla CPU
				//Vector2 relativePosition = this.getCoords().sub(currentAction.getOrigin(), this.getCoords());

				/*
				 * Cosa fa: Calcolo la direzione in cui devo muovermi (direction) e la sommo alla posizone attuale. 
				 * Se la nuova posizitione è libera, sposto l'ostacolo.
				 * In questo caso basta calcolare la direzione in cui viene spinto, dato che la posizione del robo che spinge non è importante
				 * e l'ostacolo spinto si muove solo di una casella, posso quindi direttamente sommare il risultato per ottenere la nuova posizione.
				 * E avessi voluto, per esempio, orientare l'ostacolo verso il robot che spinge, avrei dovuto calcolare la posizione relativa del robot
				 * rispetto all'ostacolo (sottraendo la posizione del robot alla posizione dell'ostacolo).
				 */

				// Direzione in cui si muoverà l'ostacolo dopo essere stato colpito dal robot
				
				Vector2 direction = Vector2.sub(this.getCoords(), currentAction.getOrigin());
				System.out.println(direction.toString() + "DIREZIONE");
				Vector2 newPosition = direction.add(this.getCoords());
				if (reference.isTileFree(newPosition)) {
					reference.releaseTile(this.getCoords());
					this.setCoords(newPosition);
					reference.occupyTile(newPosition);
				}
			}
		}
		if (this.state == ObstacleState.BEING_ATTACKED) {
			if (currentAction.getStrenght() >= this.resistence) {
				reference.removeFromWorld(this);
				reference.remove(this.sprite);
			}
			//Altrimenti lancia un'eccezione?
		}
		
		this.state = ObstacleState.IDLE;
	}
	 
	public void setAction(ActionObject currentAction) {
		this.currentAction = currentAction;
	}
	
	public void setState(ObstacleState state) {
		this.state = state;
	}
	
	
	
	// No setWeight o setResistence perchè sono valori pre-impostati non modificabili
	
	private ObstacleSprite sprite;
	private final int resistence = 7;
	private final int weight = 5;
	private Boolean beingPushed = false;
	private int direction;
	private GameWorld reference;
	private ActionObject currentAction;
	private ObstacleState state;
}