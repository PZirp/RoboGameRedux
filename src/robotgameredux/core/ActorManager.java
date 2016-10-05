package robotgameredux.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

import robotgameredux.TargetInterfaces.RobotTarget;
import robotgameredux.enums.Faction;
import robotgameredux.enums.RobotStates;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;
import robotgameredux.gameobjects.Actor;
import robotgameredux.gameobjects.AttackRobot;
import robotgameredux.gameobjects.SupportRobot;
import robotgameredux.graphic.EAttackRobotSprite;
import robotgameredux.graphic.ESupportRobotSprite;
import robotgameredux.graphic.FAttackRobotSprite;
import robotgameredux.graphic.FSupportRobotSprite;
import robotgameredux.graphic.Sprite;
import robotgameredux.input.AttackRobotController;
import robotgameredux.input.SupportRobotController;
import robotgameredux.players.CPUController;
import robotgameredux.players.Player;
import robotgameredux.systemsImplementations.AttackInteractionSystem;
import robotgameredux.systemsImplementations.StandardBattleSystem;
import robotgameredux.systemsImplementations.StandardMovementSystem;
import robotgameredux.systemsImplementations.SupportInteractionSystem;
import robotgameredux.systemsImplementations.StandardSupportSystem;
import robotgameredux.tools.HealthPack;
import robotgameredux.weapons.AIGun;
import robotgameredux.weapons.Pistol;
import robotgameredux.weapons.Shield;

public class ActorManager implements PropertyChangeListener, Serializable, IActorManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ActorManager(GameManager gameManager, IGameWorld gameWorld, AttackRobotController attackRobotController,
			SupportRobotController supportRobotController, Player player, CPUController ai) {
		this.gameManager = gameManager;
		this.gameWorld = gameWorld;
		this.attackRobotController = attackRobotController;
		this.supportRobotController = supportRobotController;
		this.robots = new ArrayList<Actor>();
		this.player = player;
		this.ai = ai;
	}

	/**
	 * Crea un robot attaccante standard. Installa i sistemi standard di
	 * movimento, combatimento e interazione. Fornisce il robot di una pistola
	 * (arma offensiva) ed uno scudo (arma difensiva). Associa una sprite al
	 * robot e la aggiunge allo schermo. Setta la fazione di appartenenza, ed in
	 * base ad essa lega il robot o al controllore interattivo o a quello
	 * gestito dalla CPU. In fine occupa la tile sulla quale si trova il robot
	 * 
	 * @param faction
	 * 			la fazione a cui apparterrà il robot
	 * @param position
	 * 			la posizione di partenza del robot
	 */

	public void createStandardAttack(Faction faction, Coordinates position) {
		AttackRobot newRobot = new AttackRobot(position, new StandardBattleSystem(this),
				new StandardMovementSystem(gameWorld), new AttackInteractionSystem(gameWorld));
		newRobot.setFaction(faction);
		Sprite spr;
		this.robots.add(newRobot);
		newRobot.addPropertyChangeListener(this);
		if (faction == Faction.FRIEND) {
			spr = new FAttackRobotSprite(newRobot);
			newRobot.setSprite(spr);
			newRobot.addWeapon(new Pistol());
			newRobot.addWeapon(new Shield());
			this.attackRobotController.addRobot(newRobot);
			player.addRobot(newRobot);
			newRobot.addPropertyChangeListener(player);
		} else {
			spr = new EAttackRobotSprite(newRobot);
			newRobot.setSprite(spr);
			newRobot.addWeapon(new AIGun());
			this.ai.addRobot(newRobot);
			newRobot.addPropertyChangeListener(ai);
		}
		gameManager.addToScreen(spr, 1);
		gameWorld.occupyTile(newRobot.getCoords());
	}

	/**
	 * Crea un robot di supporto standard. Installa i sistemi standard di
	 * movimento, supporto e interazione. Associa una sprite al robot e la
	 * aggiunge allo schermo. Setta la fazione di appartenenza, ed in base ad
	 * essa lega il robot o al controllore interattivo o a quello gestito dalla
	 * CPU. In fine occupa la tile sulla quale si trova il robot
	 * 
	 * @param faction
	 * 			la fazione a cui apparterrà il robot
	 * @param position
	 * 			la posizione di partenza del robot	 */

	public void createSupport(Faction faction, Coordinates position) {
		SupportRobot newRobot = new SupportRobot(position, new StandardMovementSystem(gameWorld),
				new StandardSupportSystem(this), new SupportInteractionSystem(gameWorld));
		newRobot.setFaction(faction);
		Sprite spr;
		newRobot.addPropertyChangeListener(this);
		if (faction == Faction.FRIEND) {
			spr = new FSupportRobotSprite(newRobot);
			newRobot.setSprite(spr);
			for (int i = 0; i < 4; i++) {
				newRobot.addTool(new HealthPack());
			}
			this.supportRobotController.addRobot(newRobot);
			player.addRobot(newRobot);
			newRobot.addPropertyChangeListener(player);
		} else {
			spr = new ESupportRobotSprite(newRobot);
			newRobot.setSprite(spr);
			for (int i = 0; i < 99; i++) {
				newRobot.addTool(new HealthPack());
			}
			this.ai.addRobot(newRobot);
			newRobot.addPropertyChangeListener(ai);
		}

		this.robots.add(newRobot);
		gameManager.addToScreen(spr, 1);
		gameWorld.occupyTile(newRobot.getCoords());
	}

	/**
	 * Controlla se esiste un robot che si trova nella posizione indicata
	 * 
	 * @param Posizione
	 *            in cui si cerca il robot
	 * @return null se non c'è nessun robot alla posizione indicata, il robot
	 *         trovato altrimenti
	 */

	/*
	 * public Boolean isRobot(Coordinates target) { for (Robot r : robots) { if
	 * (r.getCoords().equals(target)) return true; } return false; }
	 */

	/**
	 * Ritorna un oggetto RobotTarget che fornisce i metodi per interagire con
	 * un robot quando è bersaglio di un attacco
	 * 
	 * @param target
	 *           le coordinate del target
	 * @return il robot se è nelle coordinate giuste, null altrimenti
	 */

	@Override
	public RobotTarget getTarget(Coordinates target) {
		for (Actor r : robots) {
			if (r.getCoords().equals(target))
				return new RobotTarget(r);
		}
		return null;
	}

	/**
	 * Termina il turno del robot attivo
	 */

	public void setTurnOver() {
		activeRobot.setState(RobotStates.TURN_OVER);
	}

	/**
	 * Rimuove il robot specificato dall'ambiente
	 * 
	 * @param robot
	 */

	private void remove(Actor robot) {
		gameManager.removeRobot(robot.clone());
		robots.remove(robot);
	}

	/**
	 * Causa l'aggiornamento della sprite degli attori
	 */

	public void render() {
		for (Actor r : robots) {
			r.render();
		}
	}

	/**
	 * All'inizio di un nuovo turno, reimposta i robot allo stato IDLE
	 */

	public void resetRobots() {
		for (Actor r : robots) {
			if (r.getEnergy() > 0) {
				r.setState(RobotStates.IDLE);
			}
		}
	}

	/**
	 * Causa l'aggiornamento dello stato del robot attivo
	 * 
	 * @throws InvalidTargetException
	 * 			Se il target non è valido
	 * @throws InsufficientEnergyException
	 * 			Se non c'è energia sufficiente per eseguire il comando
	 */

	public void updateActiveRobot() throws InsufficientEnergyException, InvalidTargetException {
		if (activeRobot != null) {
			activeRobot.update();
		}
	}

	/**
	 * Controlla se c'è un robot attivo al momento
	 * 
	 * @return un clone del robot attivo
	 */

	public Actor hasActiveRobot() {
		if (activeRobot != null)
			return activeRobot.clone();
		return null;
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if (arg0.getPropertyName() == "DESTROYED") {
			Actor r = (Actor) arg0.getOldValue();
			remove(r);
		}
		if (arg0.getPropertyName() == "ACTIVE") {
			activeRobot = (Actor) arg0.getOldValue();
		}
		if (arg0.getPropertyName() == "TURN_OVER" || arg0.getPropertyName() == "IDLE") {
			activeRobot = null;
		}
	}

	private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
		inputStream.defaultReadObject();
		Sprite spr;
		for (Actor r : robots) {
			if (r instanceof AttackRobot) {
				if (r.getFaction() == Faction.FRIEND) {
					spr = new FAttackRobotSprite(r);
					r.setSprite(spr);
				} else if (r.getFaction() == Faction.ENEMY) {
					spr = new EAttackRobotSprite(r);
					r.setSprite(spr);
				}
			}
			if (r instanceof SupportRobot) {
				if (r.getFaction() == Faction.FRIEND) {
					spr = new FSupportRobotSprite(r);
					r.setSprite(spr);
				} else if (r.getFaction() == Faction.ENEMY) {
					spr = new ESupportRobotSprite(r);
					r.setSprite(spr);
				}
			}
		}
	}

	public void postDeserialization() {
		for (Actor r : robots) {
			gameManager.addToScreen(r.getSprite(), 1);
		}
	}

	private CPUController ai;
	private Player player;
	private Actor activeRobot;
	private IGameWorld gameWorld;
	private GameManager gameManager;
	private AttackRobotController attackRobotController;
	private SupportRobotController supportRobotController;
	private ArrayList<Actor> robots;
}
