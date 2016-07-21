package robotgameredux.test;

import java.util.ArrayList;

import robotgameredux.Commands.RobotSupportInteractCommand;
import robotgameredux.core.Coordinates;
import robotgameredux.core.IGameWorld;
import robotgameredux.core.Tile;
import robotgameredux.enums.Faction;
import robotgameredux.enums.RobotStates;
import robotgameredux.exceptions.CriticalStatusException;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;
import robotgameredux.gameobjects.Obstacle;
import robotgameredux.gameobjects.Station;
import robotgameredux.gameobjects.SupportRobot;
import robotgameredux.graphic.TileSprite;
import robotgameredux.graphic.WallSprite;
import robotgameredux.systemsImplementations.SupportInteractionSystem;
import robotgameredux.tools.EnergyPack;
import robotgameredux.tools.UsableTool;
import robotgameredux.weapons.Weapon;

public class SupInteractionTest implements IGameWorld {
	public ArrayList<Obstacle> obstacles;
	public Station station;
	private Tile[][] testSet;

	public static void main(String[] args) {
		System.out.println(">>>INIZIO TEST<<<");
		SupInteractionTest tester = new SupInteractionTest();
		SupportRobot testRobot = new SupportRobot(new Coordinates(1, 1), null, null,
				new SupportInteractionSystem(tester));
		testRobot.setFaction(Faction.FRIEND);
		tester.createObstacle(new Coordinates(1, 2));
		tester.createStation(new Coordinates(2, 1));
		RobotSupportInteractCommand ic = null;
		testRobot.removeEnergy(80);
		System.out.println(">>>Stato alla partenza<<<");
		System.out.println(testRobot.toString());
		if (!tester.obstacles.isEmpty()) {
			System.out.println(tester.obstacles.get(0).toString());
		}
		if (tester.station != null) {
			System.out.println(tester.station.toString());
		}
		System.out.println(">>>Spingo un ostacolo<<<");
		testRobot.setState(RobotStates.PUSH_OBSTACLE);
		tester.interact(testRobot, ic, new Coordinates(1, 2));
		System.out.println(">>>Provo a spingere un ostacolo che non esiste<<<");
		testRobot.setState(RobotStates.PUSH_OBSTACLE);
		tester.interact(testRobot, ic, new Coordinates(1, 2));
		System.out.println(">>>Prendo un oggetto<<<");
		testRobot.setState(RobotStates.TAKE_OBJECT);
		tester.interact(testRobot, ic, new Coordinates(2, 1));
		System.out.println(">>>Prendo un'oggetto con stazione vuota<<<");
		tester.station.removeTool(0);
		tester.station.removeTool(0);
		tester.station.removeTool(0);
		tester.station.removeTool(0);
		tester.station.removeTool(0);
		testRobot.setState(RobotStates.TAKE_OBJECT);
		tester.interact(testRobot, ic, new Coordinates(2, 1));
		System.out.println(">>>Ricarico il robot<<<");
		testRobot.setState(RobotStates.RECHARGE);
		tester.interact(testRobot, ic, new Coordinates(2, 1));
		System.out.println(">>>FINE TEST<<<");

	}

	public void interact(SupportRobot testRobot, RobotSupportInteractCommand ic, Coordinates target) {
		ic = new RobotSupportInteractCommand(testRobot, target);
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
			System.out.println("Un tuo robot � in stato critico!");
			testRobot.setState(RobotStates.TURN_OVER);
		} finally {
			testRobot.setState(RobotStates.IDLE);
		}
	}

	public SupInteractionTest() {
		obstacles = new ArrayList<>();
		testSet = new Tile[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				testSet[i][j] = new Tile();
				if (i == 10 - 1 || i == 0 || j == 10 - 1 || j == 0) {
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

	@Override
	public void releaseTile(Coordinates tile) {
	}

	@Override
	public void occupyTile(Coordinates tile) {
	}

	@Override
	public boolean isTileFree(Coordinates tile) {

		if (tile.getX() >= 10 - 1 || tile.getY() >= 10 - 1 || tile.getX() < 1 || tile.getY() < 1) {
			return false;
		}

		if (testSet[tile.getX()][tile.getY()].isOccupied() == true)
			return true;
		else
			return false;
	}

	@Override
	public ArrayList<Coordinates> pathfind(Coordinates origin, int range) {
		return null;
	}

	@Override
	public void disablePath(ArrayList<Coordinates> path) {
	}

	@Override
	public void highlightPath(Coordinates origin, int range) {
	}

	@Override
	public void randomMap() {
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
		station.addObject(new EnergyPack());

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
		return null;
	}

	@Override
	public Boolean pushObstacle(Coordinates target, int robotStrenght, Coordinates coords) {
		for (Obstacle obs : obstacles) {
			if (target.equals(obs.getCoords()) && robotStrenght > obs.getWeight()) {
				Coordinates direction = obs.getCoords().sub(coords); // Direzione
																		// in
																		// cui
																		// si
																		// muover�
																		// l'ostacolo
																		// dopo
																		// essere
																		// stato
																		// colpito
																		// dal
																		// robot
				Coordinates newPosition = direction.add(obs.getCoords()); // La
																			// nuova
																			// posizione
																			// dell'ostacolo
				if (isTileFree(newPosition)) {
					releaseTile(obs.getCoords());
					obs.setCoords(newPosition);
					occupyTile(newPosition);
					return true;
				}
				return false;
			}
		}
		return false;
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
		return null;
	}

	@Override
	public UsableTool getTool() {
		if (station.getTools() != null) {
			UsableTool tool = station.getTool(0);
			station.removeTool(0);
			return tool;

		}
		return null;
	}

	@Override
	public Integer recharge() {
		return station.recharge();
	}
}
