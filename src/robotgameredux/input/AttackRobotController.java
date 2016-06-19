package robotgameredux.input;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import robotgameredux.actors.AttackRobot;
import robotgameredux.core.GameManager;
import robotgameredux.core.GameWorld;
import robotgameredux.core.Vector2;
import robotgameredux.graphic.Sprite;
import robotgameredux.weapons.Weapon;
import robotgameredux.weapons.WeaponDialog;

public class AttackRobotController extends RobotController implements PropertyChangeListener {

	public AttackRobotController (GameManager gameManager) {
		super(gameManager);
		this.activeRobot = null;
		this.robots = new ArrayList<AttackRobot>();
		this.actionSelector = new RobotActionDialog3((JFrame)gameManager.getParent(), false);
		this.weaponSelector = new WeaponDialog((JFrame) gameManager.getParent(), false);
		currentInput = null;
		emptyWeapons = false;
	}

	/*
	 * Se c'è già un robot attivo, il Dialog non va visualizzato nuovamente.
	 */
	
	public void parseInput() {
				
		//In futuro passare lo state del world come parametro di questo metodo
		/*
		* Clicco un bottone sul dialog e setto lo stato del robot di conseguenza. Prendo l'indice del robot e lo tengo come attivo.
		* Al click successivo parte l'else qui sotto. Switcho in base allo stato del robot e faccio qualcosa.
		* Potenzialmente potrei settare uno stato anche nel GameWorld per fare sapere a tutti gli altri controllori che c'è
		* un robot che deve selezionare la meta o un bersaglio in modo tale da non far apparire altri dialogs se clicca su dei robot
		*/

		//robotInput = actionSelector.getInput();
		i = 0;
		trovato = false;
		//System.out.println(robotInput.toString() + "AAAAAAAAAAAAAAAAAAAAAAA");
		//Quando implementerò la scelta casuale del robot attivo, questo primo if dove controllo se è stato cliccato un robot e lo cerco deve essere eliminato
		if (this.currentInput != null && activeRobot == null) {
			while (!trovato && i < robots.size()) {
				AttackRobot robot = robots.get(i);
				if (robot.getCoords().x == currentInput.x && robot.getCoords().y == currentInput.y){
					trovato = activateRobot(robot);
				}
				i++;
			}
		}		
		else if (activeRobot != null) { 
			System.out.println(activeRobot.getState() + "QUIIIIIII");
			if (robotInput == null) {
				actionSelector.showAction(activeRobot.getSprite());
				currentInput = null; 
			}
			switch(robotInput) {
			case IDLE: 
				break;
			case DO_NOTHING: 
				doNothing();
				break;
			case MOVING: 
				if(currentInput != null)
					moveRobot();
				break;
			case ATTACKING: 
				ArrayList<Weapon> weapons = activeRobot.getWeapons();
				if(weaponSelector.getSelected() == null) {
					weaponSelector.showWeapons(weapons);
				}				
				//Questo è solo perchè per adesso per attivare il robot devo cliccarci sopra, quando il robot si attiva in base alla scelta casuale devo cambiarlo
				//O forse no
				if (weaponSelector.getSelected() == -1) {
					activeRobot.setState(RobotStates.INACTIVE);
					weaponSelector.resetSelected();
					activeRobot = null;
					robotInput = null;
				}				
				else if (weaponSelector.getSelected() != null && currentInput != null) {
					this.target = currentInput;
				}
				if (target != null) {
					Command c = new AttackCommand(weaponSelector.getSelected(), target, activeRobot);
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
					Command c = new AtkInteractCommand(activeRobot, currentInput);
					activeRobot.setCommand(c);
					activeRobot.setState(RobotStates.DESTROY_OBSTACLE);
					/*activeRobot.setTarget(currentInput);
					activeRobot.setState(RobotStates.DESTROY_OBSTACLE);*/
					activeRobot = null;
					//robotInput = null;
				}
				break;
			case RECHARGE: 
				if (currentInput != null) {
					Command c = new AtkInteractCommand(activeRobot, currentInput);
					activeRobot.setCommand(c);
					activeRobot.setState(RobotStates.RECHARGE);
					activeRobot = null;
					//robotInput = null;
				}
				break;
			case TAKE_WEAPON: 
				if (currentInput != null) {
					Command c = new AtkInteractCommand(activeRobot, currentInput);
					activeRobot.setCommand(c);
					activeRobot.setState(RobotStates.TAKE_WEAPON);
					activeRobot = null;
					//robotInput = null;
				}
				break;
			}
			currentInput = null;
	}
	}
	
	private void moveRobot() { 
		MovementCommand mc = new  MovementCommand(activeRobot, currentInput);
		activeRobot.setCommand(mc);
		//move.setInput(currentInput);
		//move.execute(activeRobot);
		activeRobot.setState(RobotStates.MOVING);
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
		activeRobot.setState(RobotStates.DO_NOTHING);
		//actionSelector.resetInput();
		activeRobot = null;
		//robotInput = null;
	}

	public void addRobot(AttackRobot robot) {
		robots.add(robot);
	}
	
	public void setEmptyWeapons(Boolean b) {
		this.emptyWeapons = b;
	}
	
	public Boolean getEmptyWeapon() {
		return this.emptyWeapons;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		
		if(arg0.getPropertyName() == "EMPTY_WEAPONS") {
			this.emptyWeapons = true;
			System.out.println("PROPERTY CHANGE BROOOOO!");
		}
		if(arg0.getPropertyName() == "ACTIVE") {
			this.activeRobot = (AttackRobot) arg0.getNewValue();
		}
	}
	
	//ActionObject currentAction;
	private ArrayList<AttackRobot> robots;
	private AttackRobot activeRobot;	
	private RobotActionDialog3 actionSelector;
	private Vector2 target;		
	private WeaponDialog weaponSelector;
	private Boolean emptyWeapons;
	//Variabili di lavoro
	private RobotStates robotInput;
	private int i = 0;
	private Boolean trovato = false;
	
	
	private class RobotActionDialog3 extends JDialog {

		private JButton moveButton;
		private JButton attackButton;
		private JButton doNothingButton;
		private JButton destroyButton;
		private JButton rechargeButton;
		private JButton takeWeapon;
		private AttackRobotController controller;
		
		public RobotActionDialog3(JFrame owner, boolean modal) {
			super(owner, true);
			this.setLayout(new GridLayout(0, 1));
			this.setUndecorated(true);
			//this.controller = controller;
			this.initButtons();
			this.pack();
			//input = RobotStates.IDLE;
		}
		
		public void showAction(Sprite sprite) {
			this.setLocation(sprite.getLocation().x+71, sprite.getLocation().y+30);
			//this.setLocationRelativeTo(sprite);	
			
			if (emptyWeapons) {
				this.remove(takeWeapon);
			}
			
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
			
			attackButton = new JButton("Attacca");
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
	}	
}