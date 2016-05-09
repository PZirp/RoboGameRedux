package robotgameredux.actors;

import java.util.ArrayList;

import robotgameredux.core.ActionObject;
import robotgameredux.core.Vector2;
import robotgameredux.input.RobotStates;
import robotgameredux.weapons.Projectile;
import robotgameredux.weapons.Weapon;

public class AttackRobot extends Robot{

	public AttackRobot(AttackRobotController reference, Vector2 coords) {
		super(reference, coords);
		this.attReference = reference;
		this.weapons = new ArrayList<Weapon>();		
	}
	
	public void update() {
		if (this.getState() != RobotStates.INACTIVE) {
			if (this.getState() == RobotStates.MOVING) {
				super.move();
			} else if (this.getState() == RobotStates.ATTACKING){
				if (this.getTarget() != null) {
					attack();
				}
			} else if (this.getState() == RobotStates.DESTROY_OBSTACLE) {
				System.out.println("BANZAIIIIIIIIIIIIII");
				if (this.getCoords().dst(this.getTarget()) <= 1) {
					ActionObject ob = new ActionObject(this.getCoords(), this.strenght, this.getTarget());
					attReference.deliverDestroy(ob);
					this.setState(RobotStates.INACTIVE);
					this.setTarget(null);
				} else {
					this.setState(RobotStates.INACTIVE);
				}
			}
			
		}
	}

	private void attack() { 
		if (this.getEnergy() != 0) {
			if(activeWeapon.hasBullets()) {
				System.out.println("BUDI");
				Projectile proj = activeWeapon.fire(this.getTarget());
				attReference.addProjectileToWorld(proj);
				this.setState(RobotStates.INACTIVE);
				this.setTarget(null);
			}
			
		}
	}
	
	public void addWeapon(Weapon weapon) {
		this.weapons.add(weapon);
	}
	
	public void setActiveWeapon() {
		this.activeWeapon = this.weapons.get(0);
		System.out.println("Yo");
	}	

	private ArrayList<Weapon> weapons;
	private Weapon activeWeapon;
	private int strenght = 10;
	private AttackRobotController attReference;


}
