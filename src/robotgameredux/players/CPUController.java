package robotgameredux.players;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import robotgameredux.Commands.ActorMovementCommand;
import robotgameredux.Commands.RobotAttackCommand;
import robotgameredux.Commands.RobotSupportCommand;
import robotgameredux.CommandsInterfaces.Command;
import robotgameredux.TargetImplementations.RobotTarget;
import robotgameredux.core.ActorManager;
import robotgameredux.core.Coordinates;
import robotgameredux.core.GameWorld;
import robotgameredux.core.IActorManager;
import robotgameredux.core.IGameWorld;
import robotgameredux.enums.RobotStates;
import robotgameredux.gameobjects.Actor;
import robotgameredux.gameobjects.AttackRobot;
import robotgameredux.gameobjects.SupportRobot;

public class CPUController implements IPlayer, PropertyChangeListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1884744116804446163L;

	public CPUController(GameWorld gameWorld, ActorManager actorManager) {
		this.lost = false;
		this.active = false;
		this.moved = true;
		this.gameWorld = gameWorld;
		this.actorManager = actorManager;
		this.attackRobots = new ArrayList<AttackRobot>();
		this.supportRobots = new ArrayList<SupportRobot>();
		this.propertyChange = new PropertyChangeSupport(this);
	}

	public void setRF(ActorManager rf) {
		this.actorManager = rf;
	}

	public void update() {
		for (int i = 0; i < this.attackRobots.size(); i++) {
			if (attackRobots.get(i).getState() == RobotStates.IDLE) {
				attackRobots.get(i).setState(RobotStates.ACTIVE);
				boolean res;
				res = attemptAttack(attackRobots.get(i));
				if (res == false) {
					res = attemptMovement(attackRobots.get(i), i);
				}
				return;
			}
		}

		for (int i = 0; i < this.supportRobots.size(); i++) {
			if (supportRobots.get(i).getState() == RobotStates.IDLE) {
				supportRobots.get(i).setState(RobotStates.ACTIVE);
				boolean res;
				res = attemptHeal(supportRobots.get(i));
				if (res == false) {
					res = attemptMovement(supportRobots.get(i), i + attackRobots.size());
				}
				return;
			}
		}
	}

	public boolean attemptHeal(SupportRobot robot) {
		ArrayList<Coordinates> paths = gameWorld.pathfind(robot.getCoords(), 20);
		for (Coordinates v : paths) {
			RobotTarget target = actorManager.getTarget(v);
			if (target != null) {
				if (target.getFaction() == robot.getFaction() && target.getHealth() < 50) {
					Command c = new RobotSupportCommand(0, v, robot);
					robot.setCommand(c);
					robot.setState(RobotStates.USE_OBJECT);
					return true;
				}
			}
		}
		return false;
	}

	public boolean attemptAttack(AttackRobot robot) {
		ArrayList<Coordinates> paths = gameWorld.pathfind(robot.getCoords(), 20);
		for (Coordinates v : paths) {
			RobotTarget target = actorManager.getTarget(v);
			if (target != null) {
				if (target.getFaction() != robot.getFaction()) {
					Command c = new RobotAttackCommand(0, v, robot);
					robot.setCommand(c);
					robot.setState(RobotStates.ATTACKING);
					return true;
				}
			}
		}
		return false;
	}

	public Coordinates choosePath(Actor robot, int i) {
		Coordinates tempV = null;
		int tempDist = 0;
		boolean occupied = false;
		tempV = robot.getCoords();
		Directions dir = Directions.getDirection();
		switch (dir) {
		case RIGHT:
			do {
				tempV = new Coordinates(tempV.getX() + 1, tempV.getY());
				if (gameWorld.isTileFree(tempV) == false) {
					occupied = true;
					tempV = new Coordinates(tempV.getX() - 1, tempV.getY());
				} else {
					tempDist = robot.getCoords().dst(tempV);
					if (tempDist == robot.getRange() - 1 || tempDist >= robot.getEnergy()) {
						occupied = true;
					}
				}

			} while (occupied != true);
			break;
		case LEFT:
			do {
				tempV = new Coordinates(tempV.getX() - 1, tempV.getY());
				if (gameWorld.isTileFree(tempV) == false) {
					occupied = true;
					tempV = new Coordinates(tempV.getX() + 1, tempV.getY());
				} else {
					tempDist = robot.getCoords().dst(tempV);
					if (tempDist == robot.getRange() - 1 || tempDist >= robot.getEnergy()) {
						occupied = true;
					}
				}
			} while (occupied != true);
			break;
		case DOWN:
			do {
				tempV = new Coordinates(tempV.getX(), tempV.getY() + 1);
				if (gameWorld.isTileFree(tempV) == false) {
					occupied = true;
					tempV = new Coordinates(tempV.getX(), tempV.getY() - 1);
				} else {
					tempDist = robot.getCoords().dst(tempV);
					if (tempDist == robot.getRange() - 1 || tempDist >= robot.getEnergy()) {
						occupied = true;
					}
				}
			} while (occupied != true);
			break;
		case UP:
			do {
				tempV = new Coordinates(tempV.getX(), tempV.getY() - 1);
				if (gameWorld.isTileFree(tempV) == false) {
					occupied = true;
					tempV = new Coordinates(tempV.getX(), tempV.getY() + 1);
				} else {
					tempDist = robot.getCoords().dst(tempV);
					if (tempDist == robot.getRange() - 1 || tempDist >= robot.getEnergy()) {
						occupied = true;
					}
				}
			} while (occupied != true);
			break;
		}

		return tempV;

	}

	public boolean attemptMovement(Actor robot, int i) {
		Coordinates dest = null;
		do {
			dest = choosePath(robot, i);
		} while (dest.equals(robot.getCoords()));
		ActorMovementCommand cmd2 = new ActorMovementCommand(robot, dest);
		robot.setCommand(cmd2);
		return true;
	}

	public void addRobot(AttackRobot robot) {
		attackRobots.add(robot);
	}

	public void addRobot(SupportRobot robot) {
		supportRobots.add(robot);
	}

	@Override
	public void setActive(Boolean active) {
		this.active = active;
	}

	@Override
	public Boolean isActve() {
		return active;
	}

	@Override
	public void resetMoved() {
		this.moved = false;
	}

	@Override
	public Boolean hasMoved() {
		return moved;
	}

	private void checkLost() {
		RobotStates state;
		/*
		 * Controlla se il gicoatore non ha più robot attaccanti, vuol dire che
		 * ha perso perchè non può più combattere
		 */

		if (attackRobots.isEmpty()) {
			setLost();
		} else {
			for (AttackRobot ar : attackRobots) {
				if (ar.getState() != RobotStates.INACTIVE && ar.getState() != RobotStates.DESTROYED) {
					return;
				}
			}
		}

		/*
		 * Se il giocatore ha ancora dei robot attaccanti, allora controlla se
		 * sono tutti disattivati.
		 */

		/*
		 * Se il giocatore non ha più robot attaccanti attivi, allora controlla
		 * se ha dei robot di supporto attivi. Se non li ha, non può dare
		 * energia hai robot attaccanti, quindi ha perso perchè non può
		 * combattere
		 */

		if (supportRobots.isEmpty()) {
			setLost();
		} else {
			for (int i = 0; i < supportRobots.size(); i++) {
				state = supportRobots.get(i).getState();
				if (state != RobotStates.INACTIVE && state != RobotStates.DESTROYED) {
					return;
				}
			}
		}
		setLost();
	}

	private void yieldTurn() {
		for (AttackRobot r : attackRobots) {
			if (r.getState() != RobotStates.TURN_OVER && r.getState() != RobotStates.INACTIVE) {
				return;
			}
		}
		for (SupportRobot r : supportRobots) {
			if (r.getState() != RobotStates.TURN_OVER && r.getState() != RobotStates.INACTIVE) {
				return;
			}
		}
		moved = true;
		active = false;
	}

	private void setLost() {
		this.lost = true;
		this.propertyChange.firePropertyChange("AI_LOST", this, null);
	}

	private void removeRobot(Actor r) {
		for (int i = 0; i < attackRobots.size(); i++) {
			if (attackRobots.get(i).equals(r)) {
				attackRobots.remove(i);
				checkLost();
			}
		}
		for (int i = 0; i < supportRobots.size(); i++) {
			if (supportRobots.get(i).equals(r))
				supportRobots.remove(i);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if (arg0.getPropertyName() == "DESTROYED") {
			Actor r = (Actor) arg0.getOldValue();
			removeRobot(r);
		}
		if (arg0.getPropertyName() == "DEACTIVATED") {
			checkLost();
			yieldTurn();
		}
		if (arg0.getPropertyName() == "TURN_OVER") {
			yieldTurn();
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChange.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChange.removePropertyChangeListener(listener);
	}

	private Boolean lost;
	private Boolean active;
	private Boolean moved;
	private ArrayList<AttackRobot> attackRobots;
	private ArrayList<SupportRobot> supportRobots;
	private PropertyChangeSupport propertyChange;
	private IGameWorld gameWorld;
	private IActorManager actorManager;

	private enum Directions {
		UP, DOWN, RIGHT, LEFT;

		private static Random rand = new Random();

		private static Directions getDirection() {
			return values()[rand.nextInt(4)];
		}
	}

}
