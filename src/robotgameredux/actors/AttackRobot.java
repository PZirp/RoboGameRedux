package robotgameredux.actors;

import java.util.ArrayList;

import robotgameredux.core.ActionObject;
import robotgameredux.core.BattleSystem;
import robotgameredux.core.GameWorld;
import robotgameredux.core.MovementSystem;
import robotgameredux.core.Vector2;
import robotgameredux.graphic.Visual;
import robotgameredux.input.RobotStates;
import robotgameredux.weapons.Bullet;
import robotgameredux.weapons.Weapon;

public class AttackRobot extends Robot{

	public AttackRobot(GameWorld reference, Vector2 coords, BattleSystem bs, MovementSystem ms) {
		super(reference, coords, ms);
		this.setSprite(new Visual(this));
		getReference().add(this.getSprite());
		this.weapons = new ArrayList<Weapon>();
		this.battleSystem = bs;
		
	}
	

	//Il metodo update va rivisto, perchè deve fare in modo che in base allo stato in cui si trova faccia qualcosa in vari 
	public void update() {
		if (this.getState() != RobotStates.INACTIVE) {
			if (this.getState() == RobotStates.MOVING) {
				super.move();
			} else if (this.getState() == RobotStates.ATTACKING){
				if (this.getTarget() != null) {
					attack();
				}
			} else if (this.getState() == RobotStates.DESTROY_OBSTACLE) {
				if (this.getCoords().dst(this.getTarget()) <= 1.5) {
					ActionObject ob = new ActionObject(this.getCoords(), this.strenght, this.getTarget());
					getReference().destroyObstacle(ob);
					this.setState(RobotStates.INACTIVE);
					this.setTarget(null);
				} else {
					this.setState(RobotStates.INACTIVE);
				}
			} else if (this.getState() == RobotStates.PUSH_OBSTACLE) {
				if (this.getCoords().dst(this.getTarget()) <= 1.5) {
					ActionObject ob = new ActionObject(this.getCoords(), this.strenght, this.getTarget());
					getReference().pushObstacle(ob);
					this.setState(RobotStates.INACTIVE);
					this.setTarget(null);
				} else {
					this.setState(RobotStates.INACTIVE);
				}
			}
		}
	}

	private void attack() { 
		/*if (this.getEnergy() != 0) {
			if(activeWeapon.hasBullets()) {
				System.out.println("BUDI");
				Bullet proj = activeWeapon.fire();
				this.setState(RobotStates.INACTIVE);
				this.setTarget(null);
			}
		}*/
		
		setActiveWeapon();
		if (battleSystem.attemptAttack(this, getTarget())) {
			this.setState(RobotStates.INACTIVE);
		}
	}
	
	public void addWeapon(Weapon weapon) {
		this.weapons.add(weapon);
	}
	
	public void setActiveWeapon() {
		//Test, da buttare ovviamente
		this.activeWeapon = this.weapons.get(0);
		System.out.println("Yo");
	}	
	
	public Weapon getActiveWeapon() {
		return activeWeapon;
	}
	
	private BattleSystem battleSystem;
	private ArrayList<Weapon> weapons;
	private Weapon activeWeapon;
	private int strenght = 10;
	//private AttackRobotController reference;
	private RobotStates state;

}
