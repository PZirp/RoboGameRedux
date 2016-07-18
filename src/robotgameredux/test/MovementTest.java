package robotgameredux.test;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import Exceptions.CriticalStatusException;
import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.Commands.RobotMovementCommand;
import robotgameredux.actors.AttackRobot;
import robotgameredux.actors.Faction;
import robotgameredux.actors.Obstacle;
import robotgameredux.core.IGameWorld;
import robotgameredux.core.Tile;
import robotgameredux.core.Coordinates;
import robotgameredux.graphic.TileSprite;
import robotgameredux.graphic.WallSprite;
import robotgameredux.input.RobotStates;
import robotgameredux.systemsImplementations.StandardMovementSystem;
import robotgameredux.tools.UsableTool;
import robotgameredux.weapons.Weapon;

public class MovementTest implements IGameWorld {

	private Tile[][] testSet;
	
	public MovementTest() {
		testSet = new Tile[10][10]; 
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				testSet[i][j] = new Tile();				 
				if (i == 10-1 || i == 0 || j == 10-1 || j == 0) {
					WallSprite s = new WallSprite(testSet[i][j], i, j);
					testSet[i][j].setSprite(s);
					testSet[i][j].setOccupied(false);
				} else {
					TileSprite s = new TileSprite(testSet[i][j], i, j);
					testSet[i][j].setSprite(s);
				}
			}
		}
	}
	
	public void movement(AttackRobot testRobot, RobotMovementCommand mc, Coordinates dest) {
		mc = new  RobotMovementCommand(testRobot, dest);
		testRobot.setCommand(mc);
		try {
			while(testRobot.getState() != RobotStates.TURN_OVER && testRobot.getState() != RobotStates.INACTIVE) {
				testRobot.update();
				System.out.println(testRobot.toString());
			}
		} catch (InvalidTargetException e) {
			System.out.println("Mossa non valida");
			System.out.println(e.getMessage());
			e.getCommand().setState(RobotStates.IDLE);
			//activeRobot = null;
		}
		catch (InsufficientEnergyException e) {
			System.out.println(e.getMessage());
			System.out.println("Il robot non ha abbastanza energia per compiere quest'azione!");
			e.getCommand().setState(RobotStates.IDLE);
			//activeRobot = null;
		}
		catch (CriticalStatusException e) {
			System.out.println(e.getMessage());
			System.out.println("Un tuo robot è in stato critico!");
			testRobot.setState(RobotStates.TURN_OVER);
		} finally {
			testRobot.setState(RobotStates.IDLE);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(">>>INIZIO TEST<<<");
		MovementTest tester = new MovementTest();
		AttackRobot testRobot = new AttackRobot(new Coordinates(1,1), null, new StandardMovementSystem(tester), null);
		testRobot.setFaction(Faction.FRIEND);
		RobotMovementCommand mc = null;
		tester.occupyTile(new Coordinates(4,3));
		testRobot.removeEnergy(90);
		System.out.println(">>>Stato alla partenza<<<");
		System.out.println(testRobot.toString());
		System.out.println(">>>Movimento su una tile libera e nel range<<<");
		tester.movement(testRobot, mc, new Coordinates(4,1));
		System.out.println(">>>Movimento sulla stessa tile su cui si trova ora il robot<<<");
		tester.movement(testRobot, mc, new Coordinates(4,1));
		System.out.println(">>>Movimento con ostacolo<<<");
		tester.movement(testRobot, mc, new Coordinates(4,4));
		System.out.println(">>>Movimento su una tile fuori range<<<");
		tester.movement(testRobot, mc, new Coordinates(8,2));
		System.out.println(">>>Movimento normale<<<");
		tester.movement(testRobot, mc, new Coordinates(8,1));
		System.out.println(">>>Movimento su tile occupata<<<");
		tester.movement(testRobot, mc, new Coordinates(9,1));
		System.out.println(">>>Movimento ad esaurimento energia<<<");
		tester.movement(testRobot, mc, new Coordinates(8,4));		
		System.out.println(">>>Movimento senza energia<<<");
		tester.movement(testRobot, mc, new Coordinates(8,6));
		System.out.println(">>>FINE TEST<<<");
	}
	
	@Override
	public void releaseTile(Coordinates tile) {
		testSet[tile.getX()][tile.getY()].setOccupied(true);		
	}

	@Override
	public void occupyTile(Coordinates tile) {
		testSet[tile.getX()][tile.getY()].setOccupied(false);		
	}

	@Override
	public boolean isTileFree(Coordinates tile) {
		if (tile.getX() >= 10-1 || tile.getY() >= 10-1 || tile.getX() < 1 || tile.getY() < 1) {
			return false;
		}
		
		if (testSet[tile.getX()][tile.getY()].isOccupied() == true)
			return true;
		else 
			return false;
	}

	@Override
	public ArrayList<Coordinates> pathfind(Coordinates origin, int range) {
		ArrayList<Coordinates> path = new ArrayList<Coordinates>();

		for (int i = 0; i < range; i++) {
			if (origin.getY() + i < 10-1) {
				path.add(new Coordinates(origin.getX(), origin.getY() + i));
			}
			if (origin.getY() - i >= 1) {
				path.add(new Coordinates(origin.getX(), origin.getY() - i));
			}
			if (origin.getX() + i < 10-1) {
				path.add(new Coordinates(origin.getX() + i, origin.getY()));
			}
			if (origin.getX() - i >= 1) {
				path.add(new Coordinates(origin.getX() - i, origin.getY()));
			}
		}
		
		return path;
	}

	@Override
	public void disablePath(ArrayList<Coordinates> path) {
		;
	}

	@Override
	public void highlightPath(Coordinates origin, int range) {
		;
	}

	@Override
	public void randomMap() {
		;
	}

	@Override
	public void createObstacle(Coordinates position) {
		;
		
	}

	@Override
	public void createStation(Coordinates position) {
		;
		
	}

	@Override
	public Boolean isObstacle(Coordinates target) {
		return null;
	}

	@Override
	public Boolean destroyObstacle(Coordinates target, int robotStrenght) {
		return null;
	}

	@Override
	public Boolean pushObstacle(Coordinates target, int robotStrenght, Coordinates coords) {
		return null;
	}

	@Override
	public boolean isStation(Coordinates position) {
		return false;
	}

	@Override
	public Weapon getWeapon() {
		return null;
	}

	@Override
	public UsableTool getTool() {
		return null;
	}

	@Override
	public Integer recharge() {
		return null;
	}

}
