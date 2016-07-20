package robotgameredux.input;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import robotgameredux.Commands.RobotSupportCommand;
import robotgameredux.Commands.RobotSupportInteractCommand;
import robotgameredux.CommandsInterfaces.Command;
import robotgameredux.core.Coordinates;
import robotgameredux.core.GameManager;
import robotgameredux.enums.RobotStates;
import robotgameredux.gameobjects.Actor;
import robotgameredux.gameobjects.SupportRobot;
import robotgameredux.graphic.Sprite;
import robotgameredux.tools.ToolsDialog;
import robotgameredux.tools.UsableTool;

public class SupportRobotController implements PropertyChangeListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 160442651970251872L;

	public SupportRobotController(GameManager gameManager) {
		this.gameManager = gameManager;
		this.activeRobot = null;
		this.robots = new ArrayList<SupportRobot>();
		this.actionSelector = new SupportDialog3(null, false);
		this.toolSelector = new ToolsDialog(null, false);
		currentInput = null;
		this.target = null;

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
				SupportRobot robot = robots.get(i);
				if (robot.getCoords().equals(currentInput) && robot.getState() == RobotStates.IDLE) {
					Actor r = gameManager.hasActiveRobot();
					if (r == null) {
						trovato = activateRobot(robot);
					}
					if (r != null && r.equals(robot))
						trovato = activateRobot(robot);
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
			case USE_OBJECT:
				ArrayList<UsableTool> tools = activeRobot.getTools();
				if (!tools.isEmpty()) {
					if (toolSelector.getSelected() == null) {
						toolSelector.showTools(tools);
					} else if (toolSelector.getSelected() == -1) {
						activeRobot.setState(RobotStates.IDLE);
						toolSelector.resetSelected();
						activeRobot = null;
						robotInput = null;
					} else if (toolSelector.getSelected() != null && currentInput != null) {
						this.target = currentInput;
					}
					if (target != null) {
						Command c = new RobotSupportCommand(toolSelector.getSelected(), target, activeRobot);
						activeRobot.setCommand(c);
						activeRobot = null;
						toolSelector.resetSelected();
						target = null;
						robotInput = null;

					}
				}
				break;
			case TAKE_OBJECT:
				if (currentInput != null) {
					interactRobot(RobotStates.TAKE_OBJECT);
				}
				break;
			case RECHARGE:
				if (currentInput != null) {
					interactRobot(RobotStates.RECHARGE);

				}
				break;
			case PUSH_OBSTACLE:
				if (currentInput != null) {
					interactRobot(RobotStates.PUSH_OBSTACLE);
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
		Command c = new RobotSupportInteractCommand(activeRobot, currentInput);
		activeRobot.setCommand(c);
		activeRobot.setState(state);
		activeRobot = null;
		robotInput = null;
	}

	/**
	 * Attiva il robot selezionato
	 * 
	 * @param robot
	 */

	private Boolean activateRobot(SupportRobot robot) {
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
	 * Se l'utente sceglie una mossa di movimento, genera un comando che
	 * contiene il robot selezionato e la destinazione
	 */

	private void moveRobot() {
		ActorMovementCommand mc = new ActorMovementCommand(activeRobot, currentInput);
		activeRobot.setCommand(mc);
		activeRobot.setState(RobotStates.MOVING);
		activeRobot = null;
	}

	/**
	 * Aggiunge un robot all'array del controllore
	 * 
	 * @param robot
	 */

	public void addRobot(SupportRobot robot) {
		robots.add(robot);
	}

	/**
	 * Imposta l'input ricevuto come input corrente
	 * 
	 * @param currentInput
	 */

	public void setInput(Coordinates currentInput) {
		this.currentInput = currentInput;
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {

		if (arg0.getPropertyName() == "EMPTY_TOOLS") {
			actionSelector.removeTakeObjectButton();
		}
		if (arg0.getPropertyName() == "NO_MORE_OBSTACLES") {
			actionSelector.removePushObstacleButton();
		}
	}

	private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
		inputStream.defaultReadObject();
		this.actionSelector = new SupportDialog3(null, false);
		this.toolSelector = new ToolsDialog(null, false);
	}

	private GameManager gameManager;
	private Coordinates currentInput;
	private ArrayList<SupportRobot> robots;
	private SupportRobot activeRobot;
	private Coordinates target;
	private RobotStates robotInput;
	private int i = 0;
	private Boolean trovato = false;
	transient private SupportDialog3 actionSelector;
	transient private ToolsDialog toolSelector;

	private class SupportDialog3 extends JDialog {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1588371913027267615L;
		private JButton moveButton;
		private JButton takeObjectButton;
		private JButton doNothingButton;
		private JButton useObjectButton;
		private JButton pushButton;
		private JButton rechargeButton;

		public SupportDialog3(JFrame owner, boolean modal) {
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
			moveButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					robotInput = RobotStates.MOVING;
					setVisible(false);

				}
			});

			takeObjectButton = new JButton("Prendi oggetto");
			this.add(takeObjectButton);
			takeObjectButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					robotInput = RobotStates.TAKE_OBJECT;
					setVisible(false);

				}
			});

			pushButton = new JButton("Spingi ostacolo");
			this.add(pushButton);
			pushButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					robotInput = RobotStates.PUSH_OBSTACLE;
					setVisible(false);

				}
			});

			doNothingButton = new JButton("Non fare nulla");
			this.add(doNothingButton);
			doNothingButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					robotInput = RobotStates.DO_NOTHING;
					setVisible(false);

				}
			});

			useObjectButton = new JButton("Usa oggetto");
			this.add(useObjectButton);
			useObjectButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					robotInput = RobotStates.USE_OBJECT;
					setVisible(false);

				}
			});

			rechargeButton = new JButton("Ricaricati");
			this.add(rechargeButton);
			rechargeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					robotInput = RobotStates.RECHARGE;
					setVisible(false);

				}
			});
		}

		public void removeTakeObjectButton() {
			this.remove(takeObjectButton);
		}

		public void removePushObstacleButton() {
			this.remove(this.pushButton);
		}
	}

}