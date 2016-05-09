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
				Vector2 distance = this.getCoords().sub(currentAction.getOrigin());
				Vector2 dest = this.getCoords().add(distance);
				if (reference.isTileFree(dest)) {
					reference.releaseTile(this.getCoords());
					this.setCoords(dest);
					reference.occupyTile(dest);
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