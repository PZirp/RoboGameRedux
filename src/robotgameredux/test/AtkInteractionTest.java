package robotgameredux.test;

import java.util.ArrayList;

import robotgameredux.Commands.RobotAttackInteractCommand;
import robotgameredux.core.Coordinates;
import robotgameredux.core.IGameWorld;
import robotgameredux.core.Tile;
import robotgameredux.enums.Faction;
import robotgameredux.enums.RobotStates;
import robotgameredux.exceptions.CriticalStatusException;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;
import robotgameredux.gameobjects.AttackRobot;
import robotgameredux.gameobjects.Obstacle;
import robotgameredux.gameobjects.Station;
import robotgameredux.systemsImplementations.StandardAttackInteractionSystem;
import robotgameredux.tools.UsableTool;
import robotgameredux.weapons.Weapon;

public class AtkInteractionTest implements IGameWorld {

	public ArrayList<Obstacle> obstacles;
	public Station station;
	private Tile[][] testSet;

	public static void main(String[] args) {
		System.out.println(">>>INIZIO TEST<<<");
		AtkInteractionTest tester = new AtkInteractionTest();
		AttackRobot testRobot = new AttackRobot(new Coordinates(1, 1), null, null,
				new StandardAttackInteractionSystem(tester));
		testRobot.setFaction(Faction.FRIEND);
		tester.createObstacle(new Coordinates(1, 2));
		tester.createStation(new Coordinates(2, 1));
		RobotAttackInteractCommand ic = null;
		testRobot.removeEnergy(80);
		System.out.println(">>>Stato alla partenza<<<");
		System.out.println(testRobot.toString());
		if (!tester.obstacles.isEmpty()) {
			System.out.println(tester.obstacles.get(0).toString());
		}
		if (tester.station != null) {
			System.out.println(tester.station.toString());
		}
		System.out.println(">>>Distruggo un ostacolo<<<");
		testRobot.setState(RobotStates.DESTROY_OBSTACLE);
		tester.interact(testRobot, ic, new Coordinates(1, 2));
		System.out.println(">>>Provo a distruggere un ostacolo che non esiste<<<");
		testRobot.setState(RobotStates.DESTROY_OBSTACLE);
		tester.interact(testRobot, ic, new Coordinates(1, 2));
		System.out.println(">>>Prendo un'arma<<<");
		testRobot.setState(RobotStates.TAKE_WEAPON);
		tester.interact(testRobot, ic, new Coordinates(2, 1));
		System.out.println(">>>Prendo un'arma con stazione vuota<<<");
		tester.station.removeWeapon(0);
		tester.station.removeWeapon(0);
		tester.station.removeWeapon(0);
		testRobot.setState(RobotStates.TAKE_WEAPON);
		tester.interact(testRobot, ic, new Coordinates(2, 1));
		System.out.println(">>>Ricarico il robot<<<");
		testRobot.setState(RobotStates.RECHARGE);
		tester.interact(testRobot, ic, new Coordinates(2, 1));
		System.out.println(">>>FINE TEST<<<");

	}

	public void interact(AttackRobot testRobot, RobotAttackInteractCommand ic, Coordinates target) {
		ic = new RobotAttackInteractCommand(testRobot, target);
		testRobot.setCommand(ic);
		try {
			while (testRobot.getState() != RobotStates.TURN_OVER && testRobot.getState() != RobotStates.INACTIVE) {
				testRobot.update();
				System.out.println(testRobot.toString());
				if (!obstacles.isEmpty()) {
					System.out.println(obstacles.get(0).toString());
				}
				if (station != null) {
					System.out.println(station.toString());
				}
				testRobot.setState(RobotStates.TURN_OVER);

			}
		} catch (InvalidTargetException e) {
			System.out.println("Mossa non valida");
			System.out.println(e.getMessage());
			e.getCommand().setState(RobotStates.IDLE);
		} catch (InsufficientEnergyException e) {
			System.out.println(e.getMessage());
			System.out.println("Il robot non ha abbastanza energia per compiere quest'azione!");
			e.getCommand().setState(RobotStates.IDLE);
		} catch (CriticalStatusException e) {
			System.out.println(e.getMessage());
			System.out.println("Un tuo robot è in stato critico!");
			testRobot.setState(RobotStates.TURN_OVER);
		} finally {
			testRobot.setState(RobotStates.IDLE);
		}
	}

	public AtkInteractionTest() {
		obstacles = new ArrayList<>();
		testSet = new Tile[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				testSet[i][j] = new Tile();
				if (i == 10 - 1 || i == 0 || j == 10 - 1 || j == 0) {
					// WallSprite s = new WallSprite(testSet[i][j], i, j);
					// testSet[i][j].setSprite(s);
					testSet[i][j].setOccupied(false);
				} else {
					// TileSprite s = new TileSprite(testSet[i][j], i, j);
					// testSet[i][j].setSprite(s);
				}
			}
		}
	}

	@Override
	public void releaseTile(Coordinates tile) {
		// TODO Auto-generated method stub

	}

	@Override
	public void occupyTile(Coordinates tile) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isTileFree(Coordinates tile) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Coordinates> pathfind(Coordinates origin, int range) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disablePath(ArrayList<Coordinates> path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void highlightPath(Coordinates origin, int range) {
		// TODO Auto-generated method stub

	}

	@Override
	public void randomMap() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createObstacle(Coordinates position) {
		Obstacle o = new Obstacle(position);
		occupyTile(position);
		obstacles.add(o);
	}

	@Override
	public void createStation(Coordinates position) {
		station = new Station(position);
		occupyTile(position);
	}

	@Override
	public Boolean isObstacle(Coordinates target) {
		for (Obstacle obs : obstacles) {
			if (target.equals(obs.getCoords())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Boolean destroyObstacle(Coordinates target, int robotStrenght) {
		for (Obstacle obs : obstacles) {
			if (target.equals(obs.getCoords()) && robotStrenght > obs.getResistence()) {
				// System.out.println("FACCIO PULIZIA");
				releaseTile(obs.getCoords());
				obstacles.remove(obs);
				return true;
			}
		}
		return false;
	}

	@Override
	public Boolean pushObstacle(Coordinates target, int robotStrenght, Coordinates coords) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isStation(Coordinates position) {
		if (position.equals(station.getCoords())) {
			return true;
		}
		return false;
	}

	@Override
	public Weapon getWeapon() {
		/*
		 * if (station.getWeapons() != null) { Weapon weapon =
		 * station.getWeapon(0); return weapon; } return null;
		 */

		if (station.getWeapons() != null) {
			int selection = 0;
			if (selection > -1) {
				Weapon weapon = station.getWeapon(selection);
				return weapon;
			}
		}
		return null;
	}

	@Override
	public UsableTool getTool() {
		return null;
	}

	@Override
	public Integer recharge() {
		return station.recharge();
	}
}
