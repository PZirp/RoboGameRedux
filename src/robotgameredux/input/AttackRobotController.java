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
import javax.swing.JOptionPane;

import robotgameredux.Commands.RobotAttackInteractCommand;
import robotgameredux.Commands.RobotAttackCommand;
import robotgameredux.Commands.RobotMovementCommand;
import robotgameredux.CommandsInterfaces.Command;
import robotgameredux.actors.AttackRobot;
import robotgameredux.actors.Robot;
import robotgameredux.actors.SupportRobot;
import robotgameredux.core.GameManager;
import robotgameredux.core.GameWorld;
import robotgameredux.core.Coordinates;
import robotgameredux.graphic.Sprite;
import robotgameredux.tools.ToolsDialog;
import robotgameredux.weapons.Pistol;
import robotgameredux.weapons.Weapon;

public class AttackRobotController implements PropertyChangeListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6442391257205423409L;


	public AttackRobotController (GameManager gameManager) {
		//super(gameManager);
		this.gameManager = gameManager;
		this.activeRobot = null;
		this.robots = new ArrayList<AttackRobot>();
		this.actionSelector = new RobotActionDialog(null, false);
		this.weaponSelector = new ToolsDialog(null, false);
		currentInput = null;
	}

	/*
	 * Se c'è già un robot attivo, il Dialog non va visualizzato nuovamente.
	 */
	
	/*public Boolean checkRobot() {
		Robot r = getReference().hasActiveRobot();
		if (r != null) {
			if (r == activeRobot) { return true; }
			for (AttackRobot a : robots) {
				if (a.equals(r) && robotInput == null) { return true; }
			}
		}		
		//currentInput = null;
		return false;
	}*/
	
	/*public void postSerialization() {
		this.actionSelector = new RobotActionDialog3(null, false);
		this.weaponSelector = new ToolsDialog(null, false);
	}*/
	
	
	/**
	 * Metodo che interpreta l'input dell'utente.
	 * Prima controlla se l'input è effettivamente da eseguire. Nel caso in cui ci sia un robot attivo che non è sotto il controllo di questo controllore.
	 * Se non c'è alcun robot attivo, il robot selezionato viene attivato e si analizza l'input.
	 */
	public void parseInput() {
	

		i = 0;
		trovato = false;
		if (this.currentInput != null && activeRobot == null) {
			while (!trovato && i < robots.size()) {
				AttackRobot robot = robots.get(i);
				if (robot.getCoords().equals(currentInput) && robot.getState() == RobotStates.IDLE){
					Robot r = gameManager.hasActiveRobot();
					if (r == null) {
						trovato = activateRobot(robot);
					}
					if (r != null && r.equals(robot)) {
						trovato = activateRobot(robot);
					}
				}
				i++;
			}
		}		
		else if (activeRobot != null) { 
			switch(robotInput) {
			case IDLE: 
				break;
			case DO_NOTHING: 
				doNothing();
				break;
			case MOVING: 
				gameManager.highlight(activeRobot);
				if(currentInput != null)
					moveRobot();
				break;
			case ATTACKING: 
				ArrayList<Weapon> weapons = activeRobot.getWeapons();
				if(weaponSelector.getSelected() == null) {
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
				if(currentInput != null) {
					Command c = new RobotAttackInteractCommand(activeRobot, currentInput);
					activeRobot.setCommand(c);
					activeRobot.setState(RobotStates.DESTROY_OBSTACLE);
					activeRobot = null;
					robotInput = null;
				}
				break;
			case RECHARGE: 
				if (currentInput != null) {
					Command c = new RobotAttackInteractCommand(activeRobot, currentInput);
					activeRobot.setCommand(c);
					activeRobot.setState(RobotStates.RECHARGE);
					activeRobot = null;
					robotInput = null;
				}
				break;
			case TAKE_WEAPON: 
				if (currentInput != null) {
					Command c = new RobotAttackInteractCommand(activeRobot, currentInput);
					activeRobot.setCommand(c);
					activeRobot.setState(RobotStates.TAKE_WEAPON);
					activeRobot = null;
					robotInput = null;
				}
				break;
			}
		//	currentInput = null;
		}
		currentInput = null;
	}
	
	public void setInput(Coordinates currentInput) {
		this.currentInput = currentInput;
	}
	
	private void moveRobot() { 
		//activeRobot.setState(RobotStates.MOVING);
		RobotMovementCommand mc = new  RobotMovementCommand(activeRobot, currentInput);
		activeRobot.setCommand(mc);
		activeRobot = null;
		robotInput = null;

	}
	
	//Anche questo e doNothing() potrebbero essere messi nel controller base
	private Boolean activateRobot(AttackRobot robot) {
		actionSelector.showAction(robot.getSprite());
		activeRobot = robot; 
		robot.setState(RobotStates.ACTIVE);
		currentInput = null;
		return true;
	}
		
	private void doNothing() {
		activeRobot.setState(RobotStates.TURN_OVER);
		//actionSelector.resetInput();
		activeRobot = null;
		//robotInput = null;
	}

	public void addRobot(AttackRobot robot) {
		robots.add(robot);
	}
	
	public Boolean hasAtiveRobot() {
		if (activeRobot != null) return true;
		return false;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		
		if(arg0.getPropertyName() == "EMPTY_WEAPONS") {
			/*this.emptyWeapons = true;
			System.out.println("PROPERTY CHANGE BROOOOO!");*/
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
	//private Boolean emptyWeapons;
	//Variabili di lavoro
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
			//this.controller = controller;
			this.initButtons();
			this.pack();
			//input = RobotStates.IDLE;
		}
		
		public void showAction(Sprite sprite) {
			//this.setLocation(sprite.getLocation().x+71, sprite.getLocation().y+30);
			this.setLocationRelativeTo(sprite);
			//this.setLocationRelativeTo(sprite);	
			
			/*if (emptyWeapons) {
				this.remove(takeWeapon);
			}*/
			
			this.setVisible(true);		
		}
		
		private void initButtons() {
			moveButton = new JButton("Muovi");
			this.add(moveButton);
			moveButton.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					robotInput = RobotStates.MOVING;
					setVisible(false);
				}
			});
			
			attackButton = new JButton("Usa arma");
			this.add(attackButton);
			attackButton.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					robotInput = RobotStates.ATTACKING;
					setVisible(false);
				}
			});
			
			if (this.controller == null) {
				System.out.println("E' VUOTO");
			}
			
			takeWeapon = new JButton("Prendi arma");
			this.add(takeWeapon);
			takeWeapon.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					robotInput = RobotStates.TAKE_WEAPON;
					setVisible(false);
				}
			});
			
			destroyButton = new JButton("Distruggi ostacolo");
			this.add(destroyButton);
			destroyButton.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					robotInput = RobotStates.DESTROY_OBSTACLE;
					setVisible(false);
				}
			});
			
			rechargeButton = new JButton("Ricaricati");
			this.add(rechargeButton);
			rechargeButton.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					robotInput = RobotStates.RECHARGE;
					setVisible(false);
				}
			});
			
			doNothingButton = new JButton("Non fare nulla");
			this.add(doNothingButton);
			doNothingButton.addMouseListener(new MouseAdapter() {
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


//In futuro passare lo state del world come parametro di questo metodo
/*
* Clicco un bottone sul dialog e setto lo stato del robot di conseguenza. Prendo l'indice del robot e lo tengo come attivo.
* Al click successivo parte l'else qui sotto. Switcho in base allo stato del robot e faccio qualcosa.
* Potenzialmente potrei settare uno stato anche nel GameWorld per fare sapere a tutti gli altri controllori che c'è
* un robot che deve selezionare la meta o un bersaglio in modo tale da non far apparire altri dialogs se clicca su dei robot
*/


/*Robot r = getReference().hasActiveRobot();
if (r != null) {
Boolean t = true;
for (AttackRobot a : this.robots) {
if (!a.equals(r)) 
	t = false;
}
if(t == true) {
return;
}
if (r != activeRobot) {
return;
}
}*/

/*if (!checkRobot())
if (activeRobot != null)
return;*/



/*i = 0;
trovato = false;
if (this.currentInput != null && activeRobot == null && !checkRobot()) {
while (!trovato && i < robots.size()) {
AttackRobot robot = robots.get(i);
if (robot.getCoords().equals(currentInput) && robot.getState() == RobotStates.IDLE){
	trovato = activateRobot(robot);
}
i++;
}
}*/