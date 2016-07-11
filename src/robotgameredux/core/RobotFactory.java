package robotgameredux.core;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import robotgameredux.TargetImplementations.RobotTarget;
import robotgameredux.actors.AttackRobot;
import robotgameredux.actors.Faction;
import robotgameredux.actors.Robot;
import robotgameredux.actors.SupportRobot;
import robotgameredux.graphic.Sprite;
import robotgameredux.graphic.Visual;
import robotgameredux.graphic.VisualSup;
import robotgameredux.input.AttackRobotController;
import robotgameredux.input.RobotStates;
import robotgameredux.input.SupportRobotController;
import robotgameredux.players.AI;
import robotgameredux.players.Player;
import robotgameredux.systemsImplementations.StandardAttackInteractionSystem;
import robotgameredux.systemsImplementations.StandardBattleSystem;
import robotgameredux.systemsImplementations.StandardMovementSystem;
import robotgameredux.systemsImplementations.StandardSupportInteractionSystem;
import robotgameredux.systemsImplementations.StandardSupportSystem;
import robotgameredux.tools.HealthPack;
import robotgameredux.weapons.Shield;
import robotgameredux.weapons.Pistol;

//ActorManager

public class RobotFactory implements PropertyChangeListener, Serializable, IActorManager {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public RobotFactory(GameManager gameManager, GameWorld gameWorld, AttackRobotController attackRobotController, SupportRobotController supportRobotController, AttackRobotController AIattackRobotController, Player player, AI ai) {
		this.gameManager = gameManager;
		this.gameWorld = gameWorld;
		this.attackRobotController = attackRobotController;
		this.supportRobotController = supportRobotController;
		this.robots = new ArrayList<Robot>();
		this.AIattackRobotController = AIattackRobotController;
		this.player = player;
		this.ai = ai;
	}

	public void createStandardAttack(Faction faction, Coordinates position) {
		AttackRobot newRobot = new AttackRobot(position, new StandardBattleSystem(this), new StandardMovementSystem(gameWorld), new StandardAttackInteractionSystem(gameWorld));
		newRobot.setFaction(faction);
		newRobot.addWeapon(new Pistol());
		newRobot.addWeapon(new Shield());
		Sprite spr = new Visual(newRobot);
		newRobot.setSprite(spr);
		this.robots.add(newRobot);
		newRobot.addPropertyChangeListener(this);
		if(faction == Faction.FRIEND) {
			this.attackRobotController.addRobot(newRobot);
			player.addRobot(newRobot);
			newRobot.addPropertyChangeListener(player);
		} else {
			//this.AIattackRobotController.addRobot(newRobot);
			this.ai.addRobot(newRobot);
			newRobot.addPropertyChangeListener(ai);

		}
		gameManager.addToScreen(spr, 1);
		gameWorld.occupyTile(newRobot.getCoords());
	
	}
	
	public void createSupport(Faction faction, Coordinates position) {
		SupportRobot newRobot = new SupportRobot(position, new StandardMovementSystem(gameWorld), new StandardSupportSystem(this), new StandardSupportInteractionSystem(gameWorld));
		newRobot.setFaction(faction);
		newRobot.addTool(new HealthPack());
		Sprite spr = new VisualSup(newRobot);
		newRobot.setSprite(spr);
		newRobot.addPropertyChangeListener(this);
		newRobot.addPropertyChangeListener(player);
		if(faction == Faction.FRIEND) {
			this.supportRobotController.addRobot(newRobot);
			player.addRobot(newRobot);
		} else {
			this.ai.addRobot(newRobot);
			//this.AIattackRobotController.addRobot(newRobot);
			newRobot.addPropertyChangeListener(ai);
		}	
		
		this.robots.add(newRobot);
		gameManager.addToScreen(spr, 1);
		gameWorld.occupyTile(newRobot.getCoords());
	}
	
	/**
	 * Controlla se esiste un robot che si trova nella posizione indicata, se esiste lo ritorna
	 * @param Posizione in cui si deve trovare il robot
	 * @return null se non c'è nessun robot alla posizione indicata, il robot trovato altrimenti
	 */
	
	public Boolean isRobot(Coordinates target) {
		for (Robot r : robots) {
			if (r.getCoords().equals(target)) 
				return true;
		}
		return false;
	}

	public RobotTarget getTarget(Coordinates target) {
		for (Robot r : robots) {
			if (r.getCoords().equals(target)) 
				return new RobotTarget(r);
		}	
		return null;
	}
	
	public void setTurnOver() {
		JOptionPane.showMessageDialog(null, "TURN OVER " + activeRobot.toString());

		activeRobot.setState(RobotStates.TURN_OVER);
	}
	
	private void remove(Robot robot) {
		gameManager.removeRobot(robot.clone());
		robots.remove(robot);		
	}
	
	public void render() {
		for (Robot r : robots) {
			r.render();
		}
	}
	
	public void resetRobots() {
		for (Robot r : robots) {
			if (r.getEnergy() > 0) {
				r.setState(RobotStates.IDLE);
			}
		}
	}
	
	public void updateActiveRobot() throws InsufficientEnergyException, InvalidTargetException {
		if (activeRobot != null) {
			activeRobot.update();
		}
	}
		
	public Robot hasActiveRobot() {
		if (activeRobot != null) return activeRobot.clone();
		return null;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if (arg0.getPropertyName() == "DESTROYED") {
			Robot r = (Robot) arg0.getOldValue();
			//gameManager.getPane().remove(r.getSprite());
			remove(r);			
		}
		if (arg0.getPropertyName() == "ACTIVE") {
			//JOptionPane.showMessageDialog(null, "Attivato");
			
			activeRobot = (Robot) arg0.getOldValue();
			//JOptionPane.showMessageDialog(null, activeRobot.toString());
		}
		if (arg0.getPropertyName() == "TURN_OVER" || arg0.getPropertyName() == "IDLE") {
			activeRobot = null;
		}		
	}
	

	private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
		inputStream.defaultReadObject();
		
		for (Robot r : robots) {
			if (r instanceof AttackRobot) {
				Sprite spr = new Visual(r);
				r.setSprite(spr);
				
			}
			if (r instanceof SupportRobot) {
				Sprite spr = new VisualSup(r);
				r.setSprite(spr);
			}
		}	
	}
	
	public void postDeserialization() {
		for (Robot r : robots) {
			gameManager.addToScreen(r.getSprite(), 1);
		}
	}
	
	private AI ai;
	private Player player;
	private Robot activeRobot;
	private GameWorld gameWorld;
	private GameManager gameManager;
	private AttackRobotController attackRobotController;
	private SupportRobotController supportRobotController;
//	private ArrayList<AttackRobot> attackRobots;
//	private ArrayList<SupportRobot> supportRobots;
	private ArrayList<Robot> robots;
	AttackRobotController AIattackRobotController;
}
