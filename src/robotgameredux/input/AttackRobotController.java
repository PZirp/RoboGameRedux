package robotgameredux.input;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import robotgameredux.Commands.ActorMovementCommand;
import robotgameredux.Commands.RobotAttackCommand;
import robotgameredux.Commands.RobotAttackInteractCommand;
import robotgameredux.CommandsInterfaces.Command;
import robotgameredux.core.Coordinates;
import robotgameredux.core.GameManager;
import robotgameredux.enums.RobotStates;
import robotgameredux.gameobjects.Actor;
import robotgameredux.gameobjects.AttackRobot;
import robotgameredux.graphic.Sprite;
import robotgameredux.tools.ToolsDialog;
import robotgameredux.weapons.Weapon;

public class AttackRobotController implements PropertyChangeListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6442391257205423409L;

	public AttackRobotController(GameManager gameManager) {
		this.gameManager = gameManager;
		this.activeRobot = null;
		this.robots = new ArrayList<AttackRobot>();
		this.actionSelector = new RobotActionDialog(null, false);
		this.weaponSelector = new ToolsDialog(null, false);
		currentInput = null;
	}

	/**
	 * Metodo che interpreta l'input dell'utente. Prima controlla se l'input è
	 * effettivamente da eseguire. Nel caso in cui ci sia un robot attivo che
	 * non è sotto il controllo di questo controllore. Se non c'è alcun robot
	 * attivo, il robot selezionato viene attivato e si analizza l'input.
	 */
	public void parseInput() {
		i = 0;
		trovato = false;
		if (this.currentInput != null && activeRobot == null) {
			while (!trovato && i < robots.size()) {
				AttackRobot robot = robots.get(i);
				if (robot.getCoords().equals(currentInput) && robot.getState() == RobotStates.IDLE) {
					Actor r = gameManager.hasActiveRobot();
					if (r == null) {
						trovato = activateRobot(robot);
					}
					if (r != null && r.equals(robot)) {
						trovato = activateRobot(robot);
					}
				}
				i++;
			}
		} else if (activeRobot != null) {
			switch (robotInput) {
			case IDLE:
				break;
			case DO_NOTHING:
				doNothing();
				break;
			case MOVING:
				gameManager.highlight(activeRobot);
				if (currentInput != null)
					moveRobot();
				break;
			case ATTACKING:
				ArrayList<Weapon> weapons = activeRobot.getWeapons();
				if (weaponSelector.getSelected() == null) {
					weaponSelector.showWeapons(weapons);
				} else if (weaponSelector.getSelected() == -1) {
					activeRobot.setState(RobotStates.IDLE);
					weaponSelector.resetSelected();
					activeRobot = null;
					robotInput = null;
				} else if (weaponSelector.getSelected() != null && currentInput != null) {
					this.target = currentInput;
				}
				if (target != null) {
					Command c = new RobotAttackCommand(weaponSelector.getSelected(), target, activeRobot);
					activeRobot.setCommand(c);
					activeRobot.setState(RobotStates.ATTACKING);
					activeRobot = null;
					weaponSelector.resetSelected();
					target = null;
					robotInput = null;
				}

				break;
			case DESTROY_OBSTACLE:
				if (currentInput != null) {
					interactRobot(RobotStates.DESTROY_OBSTACLE);
				}
				break;
			case RECHARGE:
				if (currentInput != null) {
					interactRobot(RobotStates.RECHARGE);
				}
				break;
			case TAKE_WEAPON:
				if (currentInput != null) {
					interactRobot(RobotStates.TAKE_WEAPON);
				}
				break;
			}
		}
		currentInput = null;
	}

	/**
	 * Genera un interactCommand in base al comando selezionato passato come
	 * paramatro esplicito
	 * 
	 * @param state
	 */

	private void interactRobot(RobotStates state) {
		Command c = new RobotAttackInteractCommand(activeRobot, currentInput);
		activeRobot.setCommand(c);
		activeRobot.setState(state);
		activeRobot = null;
		robotInput = null;
	}

	/**
	 * Imposta l'input ricevuto come input corrente
	 * 
	 * @param currentInput
	 */

	public void setInput(Coordinates currentInput) {
		this.currentInput = currentInput;
	}

	/**
	 * Se l'utente sceglie una mossa di movimento, genera un comando che
	 * contiene il robot selezionato e la destinazione
	 */

	private void moveRobot() {
		ActorMovementCommand mc = new ActorMovementCommand(activeRobot, currentInput);
		activeRobot.setCommand(mc);
		activeRobot = null;
		robotInput = null;

	}

	/**
	 * Attiva il robot selezionato
	 * 
	 * @param robot
	 */

	private Boolean activateRobot(AttackRobot robot) {
		actionSelector.showAction(robot.getSprite());
		activeRobot = robot;
		robot.setState(RobotStates.ACTIVE);
		currentInput = null;
		return true;
	}

	/**
	 * Se viene selezionato il comando per non compiere alcuna azione,
	 * semplicemente disattiva il robot per il turno
	 */

	private void doNothing() {
		activeRobot.setState(RobotStates.TURN_OVER);
		activeRobot = null;
	}

	/**
	 * Aggiunge un robot all'array del controllore
	 * 
	 * @param robot
	 */

	public void addRobot(AttackRobot robot) {
		robots.add(robot);
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {

		if (arg0.getPropertyName() == "EMPTY_WEAPONS") {
			actionSelector.removeTakeWeaponButton();
		}
		if (arg0.getPropertyName() == "NO_MORE_OBSTACLES") {
			actionSelector.removeDestroyObstacleButton();
		}

	}

	private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
		inputStream.defaultReadObject();
		this.actionSelector = new RobotActionDialog(null, false);
		this.weaponSelector = new ToolsDialog(null, false);
	}

	private GameManager gameManager;
	private Coordinates currentInput;
	private ArrayList<AttackRobot> robots;
	private AttackRobot activeRobot;
	transient private RobotActionDialog actionSelector;
	private Coordinates target;
	transient private ToolsDialog weaponSelector;
	// private Boolean emptyWeapons;
	// Variabili di lavoro
	private RobotStates robotInput;
	private int i = 0;
	private Boolean trovato = false;

	private class RobotActionDialog extends JDialog {

		private JButton moveButton;
		private JButton attackButton;
		private JButton doNothingButton;
		private JButton destroyButton;
		private JButton rechargeButton;
		private JButton takeWeapon;
		private AttackRobotController controller;

		public RobotActionDialog(JFrame owner, boolean modal) {
			super(owner, true);
			this.setLayout(new GridLayout(0, 1));
			this.setUndecorated(true);
			this.initButtons();
			this.pack();
		}

		public void showAction(Sprite sprite) {
			this.setLocationRelativeTo(sprite);
			this.setVisible(true);
		}

		private void initButtons() {
			moveButton = new JButton("Muovi");
			this.add(moveButton);
			moveButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					robotInput = RobotStates.MOVING;
					setVisible(false);
				}
			});

			attackButton = new JButton("Usa arma");
			this.add(attackButton);
			attackButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					robotInput = RobotStates.ATTACKING;
					setVisible(false);
				}
			});

			takeWeapon = new JButton("Prendi arma");
			this.add(takeWeapon);
			takeWeapon.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					robotInput = RobotStates.TAKE_WEAPON;
					setVisible(false);
				}
			});

			destroyButton = new JButton("Distruggi ostacolo");
			this.add(destroyButton);
			destroyButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					robotInput = RobotStates.DESTROY_OBSTACLE;
					setVisible(false);
				}
			});

			rechargeButton = new JButton("Ricaricati");
			this.add(rechargeButton);
			rechargeButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					robotInput = RobotStates.RECHARGE;
					setVisible(false);
				}
			});

			doNothingButton = new JButton("Non fare nulla");
			this.add(doNothingButton);
			doNothingButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					robotInput = RobotStates.DO_NOTHING;
					setVisible(false);
				}
			});

		}

		public void removeTakeWeaponButton() {
			this.remove(takeWeapon);
		}

		public void removeDestroyObstacleButton() {
			this.remove(this.destroyButton);
		}
	}
}

// In futuro passare lo state del world come parametro di questo metodo
/*
 * Clicco un bottone sul dialog e setto lo stato del robot di conseguenza.
 * Prendo l'indice del robot e lo tengo come attivo. Al click successivo parte
 * l'else qui sotto. Switcho in base allo stato del robot e faccio qualcosa.
 * Potenzialmente potrei settare uno stato anche nel GameWorld per fare sapere a
 * tutti gli altri controllori che c'è un robot che deve selezionare la meta o
 * un bersaglio in modo tale da non far apparire altri dialogs se clicca su dei
 * robot
 */

/*
 * Robot r = getReference().hasActiveRobot(); if (r != null) { Boolean t = true;
 * for (AttackRobot a : this.robots) { if (!a.equals(r)) t = false; } if(t ==
 * true) { return; } if (r != activeRobot) { return; } }
 */

/*
 * if (!checkRobot()) if (activeRobot != null) return;
 */

/*
 * i = 0; trovato = false; if (this.currentInput != null && activeRobot == null
 * && !checkRobot()) { while (!trovato && i < robots.size()) { AttackRobot robot
 * = robots.get(i); if (robot.getCoords().equals(currentInput) &&
 * robot.getState() == RobotStates.IDLE){ trovato = activateRobot(robot); } i++;
 * } }
 */