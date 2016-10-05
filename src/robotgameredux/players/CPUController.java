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
import robotgameredux.TargetInterfaces.RobotTarget;
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


	/**
	 * Aggiorna i robot controllati dalla CPU.
	 * L'ordine è prima tutti i robot attaccanti, poi quelli di supporto.
	 * Quando un robot è in stato TURN_OVER non viene aggiornato.
	 */

	public void update() {
		for (AttackRobot r : attackRobots) {
			if (r.getState() == RobotStates.IDLE) {
				r.setState(RobotStates.ACTIVE);
				boolean res;
				res = attemptAttack(r);
				if (res == false) {
					res = attemptMovement(r);
				}
				return;
			}
		}
		for (SupportRobot r : supportRobots) {
			if (r.getState() == RobotStates.IDLE) {
				r.setState(RobotStates.ACTIVE);
				boolean res;
				res = attemptHeal(r);
				if (res == false) {
					res = attemptMovement(r);
				}
				return;
			}
		}
	}

	/**
	 * Quando è il turno di un robot di supporto, se c'è un robot alleato nel raggio d'azione 
	 * con meno di 50 di vita, viene curato.
	 * @param robot 
	 * 			il robot attualmente attivo
	 * @return true se l'azione è avvenuta, false altrimenti
	 */
	
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

	/**
	 * Quando è il turno di un robot attaccante, se c'è un nemico nel raggio d'azione
	 * questo lo attacca. 
	 @param robot 
	 * 			il robot attualmente attivo 
	 * @return true se l'azione è avvenuta, false altrimenti
	 */
	
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

	/**
	 * Quando è il turno di un robot, se non ha effettuato altre azioni,
	 * viene mosso 
	 @param robot 
	 * 			il robot attualmente attivo 
	 * @return true se l'azione è avvenuta, false altrimenti
	 */
	
	public boolean attemptMovement(Actor robot) {
		Coordinates dest = null;
		do {
			dest = choosePath(robot);
		} while (dest.equals(robot.getCoords()));
		ActorMovementCommand cmd2 = new ActorMovementCommand(robot, dest);
		robot.setCommand(cmd2);
		return true;
	}
	
	/**
	 * Sceglie casualmente una direzione in cui muovere il robot.
	 * Ne calcola anche il cammino per vedere se risulta valido.
	 * @param robot
	 * 			il robot da muovere
	 * @return Coordinates
	 * 			le coordinate della destinazione scelta
	 */
	
	public Coordinates choosePath(Actor robot) {
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


	/**
	 * Aggiunge il robot specificato come paramentro alla collezione 
	 * del controllore
	 * @param robot
	 * 			il robot da aggiungere
	 */
	
	public void addRobot(AttackRobot robot) {
		attackRobots.add(robot);
	}

	/**
	 * Aggiunge il robot specificato come paramentro alla collezione 
	 * del controllore
	 * @param robot
	 * 			il robot da aggiungere
	 */
	
	public void addRobot(SupportRobot robot) {
		supportRobots.add(robot);
	}

	/**
	 * Attiva questo controllore durante il suo turno
	 * @param active
	 * 			il nuovo stato di attivazione
	 */
	
	@Override
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * Ritorna lo stato di attivazione del controllore
	 * @return lo stato di attivazione
	 */
	
	@Override
	public Boolean isActve() {
		return active;
	}

	/**
	 * Reimposta il campo moved a false
	 */
	
	@Override
	public void resetMoved() {
		this.moved = false;
	}

	/**
	 * Ritorna lo stato corrente del campo moved
	 * @return moved
	 * 			true se il controllore ha mosso tutti i suoi robot in questo turno, false altrimenti
	 */
	
	@Override
	public Boolean hasMoved() {
		return moved;
	}
	
	/**
	 * Controlla se questo controllore ha perso la partita
	 */

	private void checkLost() {
		RobotStates state;
		
		if (attackRobots.isEmpty()) {
			setLost();
		} else {
			for (AttackRobot ar : attackRobots) {
				if (ar.getState() != RobotStates.INACTIVE && ar.getState() != RobotStates.DESTROYED) {
					return;
				}
			}
		}

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

	/**
	 * Termina il turno se non ci sono più robot in attesa di essere mossi
	 */
	
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

	/**
	 * Se il controllore ha perso, il campo lost è settato a true ed 
	 * è generato un PropertyChangeEvent
	 */
	
	private void setLost() {
		this.lost = true;
		this.propertyChange.firePropertyChange("AI_LOST", this, null);
	}

	/**
	 * Rimuove il robot specificato nel parametro
	 * @param r
	 * 		il robot da rimuovere
	 */
	
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
	
	/**
	 * Gestisce i PropertyChangeEvent
	 * @param arg0
	 * 			l'evento da gestire
	 */

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

	/**
	 * Aggiunge un PropertyChangeListener
	 * @param listener
	 * 			il listener da aggiungere
	 */
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChange.addPropertyChangeListener(listener);
	}

	
	/**
	 * Rimuove un PropertyChangeListener
	 * @param listener
	 * 			il listener da rimuovere
	 */
	
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

		/**
		 * Seleziona pseudo-casualmente una direzione in cui muoversi;
		 * @return la direzione scelta
		 */
		
		private static Directions getDirection() {
			return values()[rand.nextInt(4)];
		}
	}

	public void setRF(ActorManager rf) {
		this.actorManager = rf;
	}
	
}
