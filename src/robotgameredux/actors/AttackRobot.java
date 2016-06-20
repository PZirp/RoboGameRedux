package robotgameredux.actors;

import java.util.ArrayList;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
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

	public void update() throws InvalidTargetException, InsufficientEnergyException {
		if (this.getCurrentCommand() != null) {
			this.getCurrentCommand().execute();
		}
	}

	public void addWeapon(Weapon weapon) {
		//Fare in modo che se si sta aggiungendo un'arma che il robot già ha, si aumentano i proiettili ma non si aggiunge un doppione
		this.weapons.add(weapon); //Fare add(weapon.clone) per incapsulare
	}
	
	@Override
	public Weapon getActiveWeapon(Integer i) {
		return weapons.get(i); 
	}
	
	public ArrayList<Weapon> getWeapons() {
		return weapons; //Return clone
	}

	@Override
	public BattleSystem getBattleSystem() {
		return battleSystem;
	}
	
	public AttackInteractionSystem getInteractionSystem() {
		return interactionSystem;
	}
	
	public String toString() {
		return super.toString() + " [Weapon = " + weapons.toString() + "]";
	}
	
	private AttackInteractionSystem interactionSystem;
	private BattleSystem battleSystem;
	private ArrayList<Weapon> weapons;



}
