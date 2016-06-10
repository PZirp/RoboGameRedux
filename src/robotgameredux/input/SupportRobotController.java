package robotgameredux.input;

import java.util.ArrayList;

import javax.swing.JFrame;

import robotgameredux.actors.Robot;
import robotgameredux.actors.SupportRobot;
import robotgameredux.core.GameManager;
import robotgameredux.core.GameWorld;
import robotgameredux.core.Vector2;
import robotgameredux.systems.MovementSystem;
import robotgameredux.tools.ToolsDialog;
import robotgameredux.tools.UsableTool;

public class SupportRobotController extends RobotController{

	
	public SupportRobotController(GameManager gameManager) {
		super(gameManager);
		this.activeRobot = null;
		this.robots = new ArrayList<SupportRobot>();
		this.actionSelector = new SupportDialog((JFrame)gameManager.getParent(), false);
		this.toolSelector = new ToolsDialog((JFrame) gameManager.getParent(), false);
		currentInput = null;
		this.target = null;

	}

	@Override
	public void parseInput() {
		robotInput = actionSelector.getInput();
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
					activeRobot.setState(RobotStates.RECHARGE);
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
					/*activeRobot.setTarget(currentInput);
					activeRobot.setState(RobotStates.PUSH_OBSTACLE);*/
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
		actionSelector.resetInput();
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
	
	private ArrayList<SupportRobot> robots;
	private SupportRobot activeRobot;
	private Vector2 target;
	//Variabili di lavoro
	private RobotStates robotInput;
	private int i = 0;
	private Boolean trovato = false;
	private SupportDialog actionSelector;
	private ToolsDialog toolSelector;

}


/*OLD

if (robotInput == RobotStates.DO_NOTHING) {
	doNothing();
} else if (robotInput == RobotStates.USE_OBJECT) {
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
}
//try catch qui per InsufficientEnergyException?
else if (currentInput != null) {
	if (robotInput == RobotStates.MOVING) {
		//Possibile TileOccupiedException? Catch qui
		System.out.println(currentInput.toString() + "00000000000000000000000000000000000000000000000000000000000000000");

		moveRobot();
	} 
} //Da rimuovere
else {
	System.out.println("Aspetto il secondo input");
}
}*/