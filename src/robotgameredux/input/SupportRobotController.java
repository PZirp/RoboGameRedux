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

import robotgameredux.actors.SupportRobot;
import robotgameredux.core.GameManager;
import robotgameredux.core.Vector2;
import robotgameredux.graphic.Sprite;
import robotgameredux.tools.ToolsDialog;
import robotgameredux.tools.UsableTool;

public class SupportRobotController extends RobotController implements PropertyChangeListener {

	
	public SupportRobotController(GameManager gameManager) {
		super(gameManager);
		this.activeRobot = null;
		this.robots = new ArrayList<SupportRobot>();
		this.actionSelector = new SupportDialog3((JFrame)gameManager.getParent(), false);
		this.toolSelector = new ToolsDialog((JFrame) gameManager.getParent(), false);
		currentInput = null;
		this.target = null;
		emptyTools = false;

	}

	@Override
	public void parseInput() {
		//robotInput = actionSelector.getInput();
		i = 0;
		trovato = false;	
		
		if (this.currentInput != null && activeRobot == null) {
			while (!trovato && i < robots.size()) {
				SupportRobot robot = robots.get(i);
				if (robot.getCoords().x == currentInput.x && robot.getCoords().y == currentInput.y){
					trovato = activateRobot(robot);
				}
				i++;
			}
		} 
		else if (activeRobot != null) 			
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
			case USE_OBJECT:
				ArrayList<UsableTool> tools = activeRobot.getTools();
				if (!tools.isEmpty()) {
					//show dialog
					if (toolSelector.getSelected() == null){
						toolSelector.showTools(tools);
					} else if (toolSelector.getSelected() != null && currentInput != null) {
							this.target = currentInput;
							System.out.println(currentInput.toString() + "NELL'IFFFFFFFFFFFFFFF");
					}
					if (target != null){
						Command c = new SupportCommand(toolSelector.getSelected(), target, activeRobot);
						activeRobot.setCommand(c);
						activeRobot = null;
						toolSelector.resetSelected();
						target = null;
					}
				}
				break;
			case TAKE_OBJECT:
				if (currentInput != null) {
					Command c = new SupInteractCommand(activeRobot, currentInput);
					activeRobot.setCommand(c);
					activeRobot.setState(RobotStates.TAKE_OBJECT);
					activeRobot = null;
				}
				break;
			case RECHARGE: 
				if (currentInput != null) {
					Command c = new SupInteractCommand(activeRobot, currentInput);
					activeRobot.setCommand(c);
					activeRobot.setState(RobotStates.RECHARGE);
					activeRobot = null;
				}
				break;
			case PUSH_OBSTACLE:
				if(currentInput != null) {
					Command c = new SupInteractCommand(activeRobot, currentInput);
					activeRobot.setState(RobotStates.PUSH_OBSTACLE);
					activeRobot.setCommand(c);
					activeRobot = null;
				}
				break;
			}
		
		currentInput = null;	
	}

	//Metodi che probabilmente sposterò nel controller generico

	private Boolean activateRobot(SupportRobot robot) {
		actionSelector.showAction(robot.getSprite());
		activeRobot = robot; 
		robot.setState(RobotStates.ACTIVE);
		currentInput = null;
		return true;
	}
		
	private void doNothing() {
		activeRobot.setState(RobotStates.DO_NOTHING);
//		actionSelector.resetInput();
		activeRobot = null;
	}
	
	private void moveRobot() {
		MovementCommand mc = new  MovementCommand(activeRobot, currentInput);
		activeRobot.setCommand(mc);
		activeRobot = null;
	}
	
	public void addRobot(SupportRobot robot) {
		robots.add(robot);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		
		if(arg0.getPropertyName() == "EMPTY_TOOLS") {
			this.emptyTools = true;
		}
		
	}
	
	private ArrayList<SupportRobot> robots;
	private SupportRobot activeRobot;
	private Vector2 target;
	private Boolean emptyTools;
	//Variabili di lavoro
	private RobotStates robotInput;
	private int i = 0;
	private Boolean trovato = false;
	private SupportDialog3 actionSelector;
	private ToolsDialog toolSelector;

	private class SupportDialog3 extends JDialog{
		//private RobotStates input;
		private JButton moveButton;
		private JButton takeObjectButton;
		private JButton doNothingButton;
		private JButton useObjectButton;
		private JButton pushButton;
		private JButton rechargeButton;
		
		public SupportDialog3(JFrame owner, boolean modal) {
			super(owner, true);
			//this.setLayout(new BorderLayout());
			this.setLayout(new GridLayout(0, 1));
			this.setUndecorated(true);
			this.initButtons();
			this.pack();
			//input = null;

		}
		
		public void showAction(Sprite sprite) {
			this.setLocationRelativeTo(sprite);	
			
			if(emptyTools) {
				remove(takeObjectButton);
			}
			
			this.setVisible(true);		
		}
		
		public void hideAction() {
			this.setVisible(false);
		}
		
		private void initButtons() {
			moveButton = new JButton("Muovi");
			this.add(moveButton);
			moveButton.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					robotInput = RobotStates.MOVING;
					hideAction();
				}
			});
			
			takeObjectButton = new JButton("Prendi oggetto");
			this.add(takeObjectButton);
			takeObjectButton.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					robotInput = RobotStates.TAKE_OBJECT;
					hideAction();
				}
			});
			
			pushButton = new JButton("Spingi ostacolo");
			this.add(pushButton);
			pushButton.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					robotInput = RobotStates.PUSH_OBSTACLE;
					setVisible(false);
				}
			});
			
			
			doNothingButton = new JButton("Non fare nulla");
			this.add(doNothingButton);
			doNothingButton.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					robotInput = RobotStates.DO_NOTHING;
					hideAction();
				}
			});
			
			useObjectButton = new JButton("Usa oggetto");
			this.add(useObjectButton);
			useObjectButton.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					robotInput = RobotStates.USE_OBJECT;
					hideAction();
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
			
			
		}
	}
	
}