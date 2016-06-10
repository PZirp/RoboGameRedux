package robotgameredux.actors;

import java.util.ArrayList;

import robotgameredux.core.Vector2;
import robotgameredux.input.Attacker;
import robotgameredux.input.RobotStates;
import robotgameredux.systems.StandardBattleSystem;
import robotgameredux.systems.AttackInteractionSystem;
import robotgameredux.systems.BattleSystem;
import robotgameredux.systems.MovementSystem;
import robotgameredux.weapons.Weapon;

public class AttackRobot extends Robot implements Attacker{

	public AttackRobot(Vector2 coords, StandardBattleSystem bs, MovementSystem ms, AttackInteractionSystem is) {
		super(coords, ms);
		this.weapons = new ArrayList<Weapon>();
		this.battleSystem = bs;
		this.interactionSystem = is;
		
	}
	

	//Il metodo update va rivisto, perchè deve fare in modo che in base allo stato in cui si trova faccia qualcosa in vari 
	public void update() {
		
		if (this.getCurrentCommand() != null) {
			this.getCurrentCommand().execute();
		}
		
		if (this.getState() != RobotStates.INACTIVE) {
			/*if (this.getState() == RobotStates.MOVING) {
				super.move();
			} else*/ /*if (this.getState() == RobotStates.ATTACKING){
				if (this.getTarget() != null) {
				//	attack();
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
			}*/
		}
	}

	public void addWeapon(Weapon weapon) {
		this.weapons.add(weapon);
	}
	
	
	/*private void attack() { 
Active weapon deve venire dal controller tramite input ovviamente
		setActiveWeapon();
		if (battleSystem.attemptAttack(this, getTarget())) {
			this.setState(RobotStates.INACTIVE);
		}
	}*/
	

	/*public void setActiveWeapon() {
		//Test, da buttare ovviamente
		this.activeWeapon = this.weapons.get(0);
		System.out.println("Yo");
	}*/	
	
	@Override
	public Weapon getActiveWeapon(Integer i) {
		return weapons.get(i);
	}
	
	public ArrayList<Weapon> getWeapons() {
		return weapons;
	}

	@Override
	public BattleSystem getBattleSystem() {
		return battleSystem;
	}
	
	public AttackInteractionSystem getInteractionSystem() {
		return interactionSystem;
	}
	
	private AttackInteractionSystem interactionSystem;
	private BattleSystem battleSystem;
	private ArrayList<Weapon> weapons;



}
