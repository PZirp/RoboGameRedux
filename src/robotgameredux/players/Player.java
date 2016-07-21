package robotgameredux.players;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import robotgameredux.enums.RobotStates;
import robotgameredux.gameobjects.Actor;
import robotgameredux.gameobjects.AttackRobot;
import robotgameredux.gameobjects.SupportRobot;

public class Player implements PropertyChangeListener, IPlayer, Serializable {

	public Player() {
		this.lost = false;
		this.active = false;
		this.moved = true;
		this.attackRobots = new ArrayList<AttackRobot>();
		this.supportRobots = new ArrayList<SupportRobot>();
		this.propertyChange = new PropertyChangeSupport(this);
	}

	/**
	 * Attiva questo player durante il suo turno
	 * @param active
	 * 			il nuovo stato di attivazione
	 */
	
	@Override
	public void setActive(Boolean active) {
		this.active = active;
	}


	/**
	 * Ritorna lo stato di attivazione del player
	 * @return lo stato di attivazione
	 */
	
	
	@Override
	public Boolean isActve() {
		return this.active;
	}

	/**
	 * Reimposta il campo moved a false
	 */
	
	@Override
	public void resetMoved() {
		// this.moved = moved;
		this.moved = false;
	}

	/**
	 * Ritorna lo stato corrente del campo moved
	 * @return moved
	 * 			true se il player ha mosso tutti i suoi robot in questo turno, false altrimenti
	 */
	
	@Override
	public Boolean hasMoved() {
		return this.moved;
	}

	
	
	public void addRobot(AttackRobot robot) {
		attackRobots.add(robot);
	}

	public void addRobot(SupportRobot robot) {
		supportRobots.add(robot);
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
	 * Se il player ha perso, il campo lost � settato a true ed 
	 * � generato un PropertyChangeEvent
	 */
	
	private void setLost(Boolean b) {
		this.lost = b;
		if (lost == true) {
			JOptionPane.showMessageDialog(null, "Ho perso");
			this.propertyChange.firePropertyChange("PLAYER_LOST", this, null);
		}

	}
	
	/**
	 * Controlla se questo player ha perso la partita
	 */

	private void checkLost() {
		RobotStates state;

		/*
		 * La condizione di sconfitta � vera quando il giocatore non ha pi� robot
		 * attaccanti oppure tutti i robot attaccanti sono disattivati e non ha
		 * robot di supporto in campo
		 */
	
		/*
		 * Controlla se il gicoatore non ha pi� robot attaccanti vivi o attivati, vuol dire che
		 * ha perso perch� non pu� pi� combattere
		 */

		if (attackRobots.isEmpty()) {
			setLost(true);
		} else {
			for (AttackRobot ar : attackRobots) {
				if (ar.getState() != RobotStates.INACTIVE && ar.getState() != RobotStates.DESTROYED) {
					return;
				}
			}
		}

		/*
		 * Se il giocatore non ha pi� robot attaccanti attivi, allora controlla
		 * se ha dei robot di supporto attivi. Se non li ha, non pu� dare
		 * energia hai robot attaccanti, quindi ha perso perch� non pu�
		 * combattere
		 */

		if (supportRobots.isEmpty()) {
			setLost(true);
		} else {
			for (int i = 0; i < supportRobots.size(); i++) {
				state = supportRobots.get(i).getState();
				if (state != RobotStates.INACTIVE && state != RobotStates.DESTROYED) {
					return;
				}
			}
		}

		setLost(true);
	}

	/**
	 * Termina il turno se non ci sono pi� robot in attesa di essere mossi
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
		this.propertyChange.firePropertyChange("ALL_MOVED", this, null);
	}

	/**
	 * Rimuove un PropertyChangeListener
	 * @param listener
	 * 			il listener da rimuovere
	 */
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChange.addPropertyChangeListener(listener);
	}

	/**
	 * Aggiunge un PropertyChangeListener
	 * @param listener
	 * 			il listener da aggiungere
	 */
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChange.removePropertyChangeListener(listener);
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


	private Boolean lost;
	private Boolean active;
	private Boolean moved;
	private ArrayList<AttackRobot> attackRobots;
	private ArrayList<SupportRobot> supportRobots;
	private PropertyChangeSupport propertyChange;

	
}
