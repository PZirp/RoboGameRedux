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

	/**
	 * Aggiunge l'arma specificata come parametro al robot. Se l'arma è già presente nell'array del robot, vengono aggiunti i proiettili all'arma.
	 * @param w2 l'arma da aggiungere
	 */
	
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
	
	/**
	 * Ritorna l'arma specificata dall'indice del parametro
	 * @param i l'indice dell'arma nell'array
	 * @return l'arma scelta
	 */
	
	//Non ritorna un clone perchè l'arma deve essere alterata
	
	public Weapon getActiveWeapon(Integer i) {
		return weapons.get(i); 
	}
	
	/**
	 * Ritorna una copia dell'array delle armi del robot
	 * @return l'array delle armi (clone)
	 */
	
	public ArrayList<Weapon> getWeapons() {
		return weapons; //Return clone
	}

	/**
	 * Ritorna un riferimento al sistema di combattimento usato dall'istanza del robot di attacco
	 * @return il sistema di combattimento usato
	 */
	
	public BattleSystem getBattleSystem() {
		return battleSystem;
	}
	
	/**
	 * Ritorna un riferimento al sistema di interazione usato dall'istanza del robot di attacco
	 * @return il sistema di interazione usato
	 */
	
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
