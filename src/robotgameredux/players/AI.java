package robotgameredux.players;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import robotgameredux.Commands.RobotAttackCommand;
import robotgameredux.Commands.RobotMovementCommand;
import robotgameredux.CommandsInterfaces.Command;
import robotgameredux.TargetImplementations.RobotTarget;
import robotgameredux.actors.AttackRobot;
import robotgameredux.actors.Robot;
import robotgameredux.actors.SupportRobot;
import robotgameredux.core.GameWorld;
import robotgameredux.core.RobotFactory;
import robotgameredux.core.Coordinates;
import robotgameredux.input.RobotStates;
import robotgameredux.weapons.Weapon;

public class AI implements IPlayer, PropertyChangeListener, Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1884744116804446163L;
		
	public AI(GameWorld gameWorld, RobotFactory actorManager) {
		this.lost = false;
		this.active = false;
		this.moved = true;
		this.gameWorld = gameWorld;
		this.actorManager = actorManager;
		this.attackRobots = new ArrayList<AttackRobot>();
		this.supportRobots = new ArrayList<SupportRobot>();
		this.propertyChange = new PropertyChangeSupport(this);
	}
	

	public void setRF(RobotFactory rf) {
		this.actorManager = rf;
	}
	
	public void update() {
		for (AttackRobot a : attackRobots) {
			if (a.getState() == RobotStates.IDLE) {
				JOptionPane.showMessageDialog(null, "Ecco");
				Coordinates pos = a.getCoords();
				//ArrayList<Vector2> possiblePaths = gameWorld.pathfind(pos, a.getRange());
				attemptAttack(a);
				choosePath(a);
				return;
			}
		}
	}
	
	
	public void attemptAttack(AttackRobot robot) {
		
		ArrayList<Coordinates> paths = gameWorld.pathfind(robot.getCoords(), 20);
		
		for (Coordinates v : paths) {
			if (actorManager.isRobot(v)) {
				RobotTarget target = actorManager.getTarget(v);
				if (target.getFaction() != robot.getFaction()) {
					//Weapon w = robot.getWeapons().get(0);
					Command c = new RobotAttackCommand(0, v, robot);
					robot.setCommand(c);
					robot.setState(RobotStates.ACTIVE);

				}
			}
		}
		
	}
	
	public Coordinates choosePath(Robot robot) {
		
		//ArrayList<Vector2> paths = gameWorld.pathfind(robot.getCoords(), robot.getRange());
		
		Coordinates tempV = null;
		Coordinates dest = null;
		int tempDist = 0;
		int dist = 0;
		boolean occupied = false;
		tempV = robot.getCoords();
				
		do {
			tempV = new Coordinates(tempV.getX()+1, tempV.getY());
			if(gameWorld.isTileFree(tempV) == false) {
				occupied = true;
				tempV = new Coordinates(tempV.getX()-1, tempV.getY());
			} else {
				tempDist = robot.getCoords().dst(tempV);
				if (tempDist == robot.getRange()-1) {
					occupied = true;
				}
			}
		} while(occupied != true);
		
		if (tempDist > dist) {
			dist = tempDist;
			dest = tempV;
		}
		occupied = false;
		tempV = robot.getCoords();
		
		do {
			tempV = new Coordinates(tempV.getX()-1, tempV.getY());
			if(gameWorld.isTileFree(tempV) == false) {
				occupied = true;
				tempV = new Coordinates(tempV.getX()+1, tempV.getY());
			} else {
				tempDist = robot.getCoords().dst(tempV);
				if (tempDist == robot.getRange()-1) {
					occupied = true;
				}
			}
		} while(occupied != true);
		
		if (tempDist > dist) {
			dist = tempDist;
			dest = tempV;
		}
		occupied = false;
		tempV = robot.getCoords();
		
		do {
			tempV = new Coordinates(tempV.getX(), tempV.getY()+1);
			if(gameWorld.isTileFree(tempV) == false) {
				occupied = true;
				tempV = new Coordinates(tempV.getX(), tempV.getY()-1);
			} else {
				tempDist = robot.getCoords().dst(tempV);
				if (tempDist == robot.getRange()-1) {
					occupied = true;
				}
			}
		} while(occupied != true);
		
		if (tempDist > dist) {
			dist = tempDist;
			dest = tempV;
		}
		occupied = false;
		tempV = robot.getCoords();
		
		do {
			tempV = new Coordinates(tempV.getX(), tempV.getY()-1);
			if(gameWorld.isTileFree(tempV) == false) {
				occupied = true;
				tempV = new Coordinates(tempV.getX(), tempV.getY()+1);
			} else {
				tempDist = robot.getCoords().dst(tempV);
				if (tempDist == robot.getRange()-1) {
					occupied = true;
				}
			}
		} while(occupied != true);
		
		if (tempDist > dist) {
			dist = tempDist;
			dest = tempV;
		}
		occupied = false;
		
		return dest;
	}

	public void chooseAction(Robot robot) {
		//for (Vector2 v : possiblePaths) {
			/*if (actorManager.isRobot(v)) {
				if(actorManager.getTarget(v).getFaction() != robot.getFaction()) {
					robot.setState(RobotStates.ACTIVE);
					RobotAttackCommand cmd = new RobotAttackCommand(1, v, (AttackRobot) robot);
					robot.setCommand(cmd);
				}
			} else {*/
				robot.setState(RobotStates.ACTIVE);
				
				/*Vector2 dest = choosePath(robot);
				RobotMovementCommand cmd2 = new RobotMovementCommand(robot, dest);
				robot.setCommand(cmd2);*/
			//}
		//}
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
		
		/* Controlla se il gicoatore non ha più robot attaccanti, vuol dire che ha perso perchè non può più combattere */
		
		if (attackRobots.isEmpty()) {
			setLost();
		} else {
			for(AttackRobot ar : attackRobots) {
				if (ar.getState() != RobotStates.INACTIVE && ar.getState() != RobotStates.DESTROYED) {
					return;
				}
			}			
		}
		
		/*Se il giocatore ha ancora dei robot attaccanti, allora controlla se sono tutti disattivati. */
		
		
		/* Se il giocatore non ha più robot attaccanti attivi, allora controlla se ha dei robot di supporto attivi. Se non li ha, non può dare energia hai robot
		 * attaccanti, quindi ha perso perchè non può combattere */
		
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
	
	
	private void removeRobot(Robot r) {
		for(int i = 0; i < attackRobots.size(); i++) {
			if (attackRobots.get(i).equals(r)) {
				attackRobots.remove(i);
				checkLost();
			}
		}
		for(int i = 0; i < supportRobots.size(); i++) {
			if (supportRobots.get(i).equals(r)) 
				supportRobots.remove(i);
		}
	}
	
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if (arg0.getPropertyName() == "DESTROYED") {
			Robot r = (Robot) arg0.getOldValue();
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
	private GameWorld gameWorld;
	private RobotFactory actorManager;

}
