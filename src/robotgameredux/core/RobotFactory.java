package robotgameredux.core;

import java.util.ArrayList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import robotgameredux.actors.AttackRobot;
import robotgameredux.actors.Faction;
import robotgameredux.actors.Robot;
import robotgameredux.actors.RobotType;
import robotgameredux.actors.SupportRobot;
import robotgameredux.graphic.Sprite;
import robotgameredux.graphic.Visual;
import robotgameredux.graphic.VisualSup;
import robotgameredux.input.AttackRobotController;
import robotgameredux.input.RobotStates;
import robotgameredux.input.SupportRobotController;
import robotgameredux.systemInterfaces.AttackInteractionSystem;
import robotgameredux.systemInterfaces.MovementSystem;
import robotgameredux.systemInterfaces.SupportInteractionSystem;
import robotgameredux.systemsImplementations.StandardAttackInteractionSystem;
import robotgameredux.systemsImplementations.StandardBattleSystem;
import robotgameredux.systemsImplementations.StandardMovementSystem;
import robotgameredux.systemsImplementations.StandardSupportInteractionSystem;
import robotgameredux.systemsImplementations.StandardSupportSystem;
import robotgameredux.tools.HealthPack;
import robotgameredux.weapons.Weapon;

//ActorManager

public class RobotFactory implements PropertyChangeListener, Serializable {
	
	//private MovementSystem ms;
	//private StandardSupportSystem sm;
	//private AttackInteractionSystem is;
	//private SupportInteractionSystem iss;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//public RobotFactory(GameManager reference, AttackRobotController attackRobotController, SupportRobotController supportRobotController, StandardBattleSystem battleSystem, MovementSystem ms, StandardSupportSystem sm, AttackInteractionSystem is, SupportInteractionSystem iss) {
	public RobotFactory(GameManager reference, GameWorld gameWorld, AttackRobotController attackRobotController, SupportRobotController supportRobotController, AttackRobotController AIattackRobotController) {
		this.reference = reference;
		this.gameWorld = gameWorld;
		this.attackRobotController = attackRobotController;
		this.supportRobotController = supportRobotController;
		this.robots = new ArrayList<Robot>();
		this.AIattackRobotController = AIattackRobotController;
		//this.attackRobots= new ArrayList<AttackRobot>();
		//this.supportRobots= new ArrayList<SupportRobot>();
	}
	
	/*public Robot createRobot(Faction faction, Vector2 position, RobotType type) {
		
		if (type == RobotType.ATTACK) {
			return createStandardAttack(faction, position);
		} else if (type == RobotType.SUPPORT) {
			return createSupport(faction, position);
		}
		
		return null;
	}*/
	
	public AttackRobot createStandardAttack(Faction faction, Vector2 position) {
		AttackRobot newRobot = new AttackRobot(position, new StandardBattleSystem(this, gameWorld), new StandardMovementSystem(gameWorld), new StandardAttackInteractionSystem(gameWorld));
		newRobot.setFaction(faction);
		newRobot.addWeapon(new Weapon());
		Sprite spr = new Visual(newRobot);
		newRobot.addSprite(spr);
		if(faction == Faction.FRIEND) this.attackRobotController.addRobot(newRobot);
		else this.AIattackRobotController.addRobot(newRobot);
		this.robots.add(newRobot);
		return newRobot;
	}
	
	public SupportRobot createSupport(Faction faction, Vector2 position) {
		SupportRobot newRobot = new SupportRobot(position, new StandardMovementSystem(gameWorld), new StandardSupportSystem(this), new StandardSupportInteractionSystem(gameWorld));
		newRobot.setFaction(faction);
		newRobot.addTool(new HealthPack());
		Sprite spr = new VisualSup(newRobot);
		newRobot.addSprite(spr);
		this.supportRobotController.addRobot(newRobot);
		this.robots.add(newRobot);
		return newRobot;
	}
	
	/*
	 * Controlla se esiste un robot che si trova nella posizione indicata, se esiste lo ritorna
	 * @param Posizione in cui si deve trovare il robot
	 * @return null se non c'è nessun robot alla posizione indicata, il robot trovato altrimenti
	 */
	
	public Robot isRobot(Vector2 target) {
		for (Robot r : robots) {
			if (r.getCoords().dst(target) == 0) 
				return r;
		}
	/*	for (Robot r : supportRobots) {
			if (r.getCoords().dst(target) == 0) 
				return r;
		}*/
		
		return null;
	}
	
	
	
	public void remove(Robot robot) {
		robots.remove(robot);
		//supportRobots.remove(robot);
	}
	
	public void resetRobots() {
		for (Robot r : robots) {
			r.setState(RobotStates.IDLE);
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if (arg0.getPropertyName() == "DESTROYED") {
			Robot r = (Robot) arg0.getOldValue();
			remove(r);
		}
		
	}
	
	private GameWorld gameWorld;
	private GameManager reference;
	private AttackRobotController attackRobotController;
	private SupportRobotController supportRobotController;
//	private ArrayList<AttackRobot> attackRobots;
//	private ArrayList<SupportRobot> supportRobots;
	private ArrayList<Robot> robots;
	//private StandardBattleSystem battleSystem;
	AttackRobotController AIattackRobotController;
}
