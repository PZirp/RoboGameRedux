package robotgameredux.players;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import robotgameredux.actors.AttackRobot;
import robotgameredux.actors.Robot;
import robotgameredux.actors.SupportRobot;
import robotgameredux.input.RobotStates;

public class Player implements PropertyChangeListener, Serializable {
	
	private Boolean active;
	private Boolean moved;
	private ArrayList<AttackRobot> attackRobots;
	private ArrayList<SupportRobot> supportRobots;
	private PropertyChangeSupport propertyChange;
	
	
	
	
	
	
	public Player() {
		this.active = false;
		this.moved = true;
		this.attackRobots = new ArrayList<AttackRobot>();
		this.supportRobots = new ArrayList<SupportRobot>();
		this.propertyChange = new PropertyChangeSupport(this);
	}
	
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	public Boolean isActve() {
		return this.active;
	}
	
	public void setMoved(Boolean moved) {
		this.moved = moved;
	}

	public Boolean hasMoved() {
		return this.moved;
	}
	
	public void addRobot(AttackRobot robot) {
		attackRobots.add(robot);
	}
	
	public void addRobot(SupportRobot robot) {
		supportRobots.add(robot);
	}

	
	/* La condizione di sconfitta è vera quando il giocatore non ha più robot attaccanti oppure tutti i robot attaccanti sono disattivati 
	 * e non ha robot di supporto in campo 
	 */
	
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
	
	
	
	//Se trova anche un solo robot attivo e non distrutto non hai ancora perso
	
	private Boolean checkLost() {
		RobotStates state;
		
		/* Controlla se il gicoatore non ha più robot attaccanti, vuol dire che ha perso perchè non può più combattere */
		
		if (attackRobots.isEmpty()) {
			return true;
		}
		
		/*Se il giocatore ha ancora dei robot attaccanti, allora controlla se sono tutti disattivati. */
		
		for (int i = 0; i < attackRobots.size(); i++) {
			state = attackRobots.get(i).getState();
			if (state != RobotStates.INACTIVE && state != RobotStates.DESTROYED) {
				//JOptionPane.showMessageDialog(null, "Ho perso");

				return false;				
			}			
		}
		
		/* Se il giocatore non ha più robot attaccanti attivi, allora controlla se ha dei robot di supporto attivi. Se non li ha, non può dare energia hai robot
		 * attaccanti, quindi ha perso perchè non può combattere */
		
		if (supportRobots.isEmpty()) 
			return true;
		
		for (int i = 0; i < supportRobots.size(); i++) {
			state = supportRobots.get(i).getState();
			if (state != RobotStates.INACTIVE && state != RobotStates.DESTROYED) {
				
				return false;				
			}			
		}
		
		JOptionPane.showMessageDialog(null, "Ho perso");
		this.propertyChange.firePropertyChange("PLAYER_LOST", this, null);

		return false;
	}
	
	public void yieldTurn() {
		for (AttackRobot r : attackRobots) {
			if (r.getState() != RobotStates.TURN_OVER) {
				return;
			}
		}
		
		for (SupportRobot r : supportRobots) {
			if (r.getState() != RobotStates.TURN_OVER) {
				return;
			}
		}
		
		JOptionPane.showMessageDialog(null, "Mossi tutti");
		//active = false;
		moved = true;
		this.propertyChange.firePropertyChange("ALL_MOVED",	this, null);
		
	}
	
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChange.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChange.removePropertyChangeListener(listener);
    }
	
	public void propertyChange(PropertyChangeEvent arg0) {
		if (arg0.getPropertyName() == "DESTROYED") {
			Robot r = (Robot) arg0.getOldValue();
			removeRobot(r);
		}
		if (arg0.getPropertyName() == "DEACTIVATED") {
			Robot r = (Robot) arg0.getOldValue();
			checkLost();
			yieldTurn();
		}
		if (arg0.getPropertyName() == "TURN_OVER") {
			Robot r = (Robot) arg0.getOldValue();
			yieldTurn();
		}
		
	}
	
}
 