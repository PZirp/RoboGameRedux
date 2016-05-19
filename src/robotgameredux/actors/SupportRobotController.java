package robotgameredux.actors;

import java.util.ArrayList;

import javax.swing.JFrame;

import robotgameredux.core.GameWorld;
import robotgameredux.core.MovementSystem;
import robotgameredux.core.Vector2;
import robotgameredux.input.Faction;
import robotgameredux.input.RobotActionDialog;
import robotgameredux.input.RobotStates;
import robotgameredux.input.SupportDialog;
import robotgameredux.tools.ToolsDialog;
import robotgameredux.tools.UsableTool;

public class SupportRobotController extends RobotController{

	private MovementSystem ms;
	
	public SupportRobotController(GameWorld gameWorld, MovementSystem ms) {
		super(gameWorld);
		this.activeRobot = null;
		this.robots = new ArrayList<SupportRobot>();
		this.actionSelector = new SupportDialog((JFrame)gameWorld.getParent(), false);
		this.toolSelector = new ToolsDialog((JFrame) gameWorld.getParent(), false);
		currentInput = null;
		this.ms = ms;
	}

	public Robot createRobot(Vector2 position) {
		SupportRobot robot = new SupportRobot(gameWorld, position, ms);
		robot.setFaction(Faction.FRIEND);
		robots.add(robot);
		return robot;
	}

	public Boolean isFriendly(Vector2 target) {
		for (Robot r : robots) {
			if (r.getCoords().x == target.x && r.getCoords().y == target.y && r.getFaction() == Faction.FRIEND) { 
				this.target = r;
				return true;
			}
		}
		return false;	
		
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
		}else if (activeRobot != null && robotInput != null) {
			if (robotInput == RobotStates.DO_NOTHING) {
				doNothing();
			} else if (robotInput == RobotStates.USE_OBJECT) {
				ArrayList<UsableTool> tools = activeRobot.getTools();
				if (!tools.isEmpty()) {
					//show dialog
					toolSelector.showTools(tools);
					activeRobot.setActiveTool(toolSelector.getSelected());
					activeRobot.setState(RobotStates.USE_OBJECT);
					activeRobot = null;
				}
			}
			//try catch qui per InsufficientEnergyException?
			else if (currentInput != null) {
				if (robotInput == RobotStates.MOVING) {
					//Possibile TileOccupiedException? Catch qui
					moveRobot();
				} else if (robotInput == RobotStates.GIVE_OBJECT) {
					/*if(gameWorld.isFriendly(currentInput)) {
						ArrayList<UsableTool> tools = activeRobot.getTools();
						if (!tools.isEmpty()) {
							toolSelector.showTools(tools);
							activeRobot.setActiveTool(toolSelector.getSelected());
							activeRobot.setTarget(currentInput);
							activeRobot.setState(RobotStates.GIVE_OBJECT);
						}
						activeRobot = null;
					}*/
				}
			} //Da rimuovere
			else {
				System.out.println("Aspetto il secondo input");
			}
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
		if (activeRobot.getCoords().dst(currentInput) == 0) {
			System.out.println("Sei già sulla tile scelta");
			activeRobot.setState(RobotStates.INACTIVE);
			activeRobot = null;
		} else if (gameWorld.isTileFree(currentInput)) {
			activeRobot.setDest(currentInput);
			actionSelector.resetInput();
			activeRobot.setState(RobotStates.MOVING);
			activeRobot = null;
		} else {
			System.out.println("Tile scelta occupata!");
			activeRobot.setState(RobotStates.INACTIVE);
			activeRobot = null;
		}
	}
	
	public void addRobot(SupportRobot robot) {
		robots.add(robot);
	}
	
	private ArrayList<SupportRobot> robots;
	private SupportRobot activeRobot;
	private Robot target;
	//Variabili di lavoro
	private RobotStates robotInput;
	private int i = 0;
	private Boolean trovato = false;
	private SupportDialog actionSelector;
	private ToolsDialog toolSelector;

}
