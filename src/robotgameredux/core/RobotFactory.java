package robotgameredux.core;

import java.util.ArrayList;

import robotgameredux.actors.AttackRobot;
import robotgameredux.actors.Robot;
import robotgameredux.actors.RobotType;
import robotgameredux.actors.SupportRobot;
import robotgameredux.graphic.Sprite;
import robotgameredux.graphic.Visual;
import robotgameredux.graphic.VisualSup;
import robotgameredux.input.AttackRobotController;
import robotgameredux.input.Faction;
import robotgameredux.input.SupportRobotController;
import robotgameredux.systems.StandardBattleSystem;
import robotgameredux.systems.MovementSystem;
import robotgameredux.systems.StandardSupportSystem;
import robotgameredux.systems.SupportInteractionSystem;
import robotgameredux.systems.AttackInteractionSystem;
import robotgameredux.systems.StandardAttackInteractionSystem;
import robotgameredux.tools.HealthPack;
import robotgameredux.weapons.Weapon;

//ActorManager

public class RobotFactory {
	
	private MovementSystem ms;
	private StandardSupportSystem sm;
	private AttackInteractionSystem is;
	private SupportInteractionSystem iss;
	
	public RobotFactory(GameManager reference, AttackRobotController attackRobotController, SupportRobotController supportRobotController, StandardBattleSystem battleSystem, MovementSystem ms, StandardSupportSystem sm, AttackInteractionSystem is, SupportInteractionSystem iss) {
		this.reference = reference;
		this.attackRobotController = attackRobotController;
		this.supportRobotController = supportRobotController;
		this.is = is;
		this.attackRobots= new ArrayList<AttackRobot>();
		this.supportRobots= new ArrayList<SupportRobot>();
		this.battleSystem = battleSystem;
		this.ms = ms;
		this.sm = sm;
		this.iss = iss;
	}
	
	public Robot createRobot(Faction faction, Vector2 position, RobotType type) {
		
		if (type == RobotType.ATTACK) {
			return createAttack(faction, position);
		} else if (type == RobotType.SUPPORT) {
			return createSupport(faction, position);
		}
		
		return null;
	}
	
	private AttackRobot createAttack(Faction faction, Vector2 position) {
		AttackRobot newRobot = new AttackRobot(position, battleSystem, ms, is);
		newRobot.setFaction(faction);
		newRobot.addWeapon(new Weapon());
		Sprite spr = new Visual(newRobot);
		newRobot.addSprite(spr);
		this.attackRobotController.addRobot(newRobot);
		this.attackRobots.add(newRobot);
		//newRobot.addInteractionSystem(is);
		return newRobot;
	}
	
	private SupportRobot createSupport(Faction faction, Vector2 position) {
		SupportRobot newRobot = new SupportRobot(position, ms, sm, iss);
		newRobot.setFaction(faction);
		newRobot.addTool(new HealthPack());
		Sprite spr = new VisualSup(newRobot);
		newRobot.addSprite(spr);
		this.supportRobotController.addRobot(newRobot);
		this.supportRobots.add(newRobot);
		//newRobot.addInteractionSystem(is);
		return newRobot;
	}
	
	/*
	 * Controlla se esiste un robot che si trova nella posizione indicata, se esiste lo ritorna
	 * @param Posizione in cui si deve trovare il robot
	 * @return null se non c'è nessun robot alla posizione indicata, il robot trovato altrimenti
	 */
	
	public Robot isRobot(Vector2 target) {
		for (Robot r : attackRobots) {
			if (r.getCoords().dst(target) == 0) 
				return r;
		}
		for (Robot r : supportRobots) {
			if (r.getCoords().dst(target) == 0) 
				return r;
		}
		
		return null;
	}
	
	
	
	public void remove(Robot robot) {
		attackRobots.remove(robot);
		supportRobots.remove(robot);
	}
	
	private GameManager reference;
	private AttackRobotController attackRobotController;
	private SupportRobotController supportRobotController;
	private ArrayList<AttackRobot> attackRobots;
	private ArrayList<SupportRobot> supportRobots;
	private StandardBattleSystem battleSystem;
}
