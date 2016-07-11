package robotgameredux.actors;

import java.util.ArrayList;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.core.Coordinates;
import robotgameredux.input.RobotStates;
import robotgameredux.systemInterfaces.AttackInteractionSystem;
import robotgameredux.systemInterfaces.BattleSystem;
import robotgameredux.systemInterfaces.MovementSystem;
import robotgameredux.systemsImplementations.StandardBattleSystem;
import robotgameredux.weapons.Pistol;
import robotgameredux.weapons.Weapon;

public class AttackRobot extends Robot{

	public AttackRobot(Coordinates coords, StandardBattleSystem bs, MovementSystem ms, AttackInteractionSystem is) {
		super(coords, ms);
		this.weapons = new ArrayList<Weapon>();
		this.battleSystem = bs;
		this.interactionSystem = is;
		
	}

	/*public void update() throws InvalidTargetException, InsufficientEnergyException {
		
	
		
		Boolean res = false;
		if (this.getCurrentCommand() != null) {
			res = this.getCurrentCommand().execute();
		}
		
		if (res == true) {
			//era IDLE
			setState(RobotStates.TURN_OVER);
		}
		if (getEnergy() <= 0 && getState() != RobotStates.INACTIVE) {
			setState(RobotStates.INACTIVE);
			getPropertyChange().firePropertyChange("DEACTIVATED", this, null);
			return;
		}
	}*/

	public void addWeapon(Weapon w2) {
		/*Se il robot già possiede un'arma dello stesso tipo, invece di aggiungere un doppione, si aggiungono i proiettili a quella che si ha già*/
		
		for (Weapon w : weapons) {
			if (w.isSameWeapon(w2)) {
				w.addBullets(w2.getBulletCount());
				return;
			}
		}
		
		this.weapons.add(w2); //Fare add(weapon.clone) per incapsulare
	}
	
	public Weapon getActiveWeapon(Integer i) {
		return weapons.get(i); 
	}
	
	public ArrayList<Weapon> getWeapons() {
		return weapons; //Return clone
	}

	public BattleSystem getBattleSystem() {
		return battleSystem;
	}
	
	public AttackInteractionSystem getInteractionSystem() {
		return interactionSystem;
	}
	
	public String toString() {
		return super.toString() + " [Weapon = " + weapons.toString() + "]";
	}
	
	public boolean equals(Object otherObject) {
		if (!super.equals(otherObject)) return false;
		AttackRobot other = (AttackRobot) otherObject;
		//Anche qui, aggiungere gli equals per i sistemi
		return weapons.equals(other.weapons) && battleSystem == other.battleSystem && interactionSystem == other.interactionSystem;
	}
	
	public AttackRobot clone() {
		AttackRobot clone = (AttackRobot) super.clone();
		clone.weapons = new ArrayList<Weapon>(weapons);
		clone.interactionSystem = interactionSystem;
		clone.battleSystem = battleSystem;
		return clone;
	}
	
	private AttackInteractionSystem interactionSystem;
	private BattleSystem battleSystem;
	private ArrayList<Weapon> weapons;





}
