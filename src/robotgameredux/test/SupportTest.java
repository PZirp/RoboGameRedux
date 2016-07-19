package robotgameredux.test;

import java.util.ArrayList;

import robotgameredux.Commands.RobotSupportCommand;
import robotgameredux.TargetImplementations.RobotTarget;
import robotgameredux.core.Coordinates;
import robotgameredux.core.IActorManager;
import robotgameredux.enums.Faction;
import robotgameredux.enums.RobotStates;
import robotgameredux.exceptions.CriticalStatusException;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;
import robotgameredux.gameobjects.Actor;
import robotgameredux.gameobjects.SupportRobot;
import robotgameredux.systemsImplementations.StandardSupportSystem;
import robotgameredux.tools.HealthPack;

public class SupportTest implements IActorManager {

	public static void main(String[] args) {
		System.out.println(">>>INIZIO TEST<<<");
		SupportTest tester = new SupportTest();
		SupportRobot testRobotFriend = new SupportRobot(new Coordinates(1, 1), null, new StandardSupportSystem(tester),
				null);
		testRobotFriend.setFaction(Faction.FRIEND);
		testRobotFriend.addTool(new HealthPack());
		testRobotFriend.addTool(new HealthPack());
		testRobotFriend.addTool(new HealthPack());
		SupportRobot testRobotEnemy = new SupportRobot(new Coordinates(2, 1), null, new StandardSupportSystem(tester),
				null);
		testRobotEnemy.setFaction(Faction.ENEMY);
		testRobotEnemy.damage(50);
		SupportRobot testRobotFriend2 = new SupportRobot(new Coordinates(1, 2), null, new StandardSupportSystem(tester),
				null);
		testRobotFriend2.setFaction(Faction.FRIEND);
		testRobotFriend2.damage(50);
		tester.addRobot(testRobotFriend);
		tester.addRobot(testRobotFriend2);
		tester.addRobot(testRobotEnemy);
		RobotSupportCommand ac = null;
		tester.show();
		System.out.println(">>>Stato alla partenza<<<");
		System.out.println("Primo robot (amico): " + testRobotFriend.toString());
		System.out.println("Secondo robot (nemico): " + testRobotEnemy.toString());
		System.out.println("Terzo robot (amico): " + testRobotFriend2.toString());
		System.out.println("Guarisco il robot nemico");
		testRobotFriend.setState(RobotStates.ATTACKING);
		tester.support(testRobotFriend, ac, new Coordinates(2, 1));
		System.out.println("Primo robot (amico): " + testRobotFriend.toString());
		System.out.println("Secondo robot (nemico): " + testRobotEnemy.toString());
		System.out.println("Terzo robot (amico): " + testRobotFriend2.toString());
		System.out.println("Guarisco il robot amico");
		testRobotFriend.setState(RobotStates.ATTACKING);
		tester.support(testRobotFriend, ac, new Coordinates(1, 2));
		System.out.println("Primo robot (amico): " + testRobotFriend.toString());
		System.out.println("Secondo robot (nemico): " + testRobotEnemy.toString());
		System.out.println("Terzo robot (amico): " + testRobotFriend2.toString());
		System.out.println("Gurisco una coordinata vuota");
		testRobotFriend.setState(RobotStates.ATTACKING);
		tester.support(testRobotFriend, ac, new Coordinates(3, 1));
		System.out.println("Primo robot (amico): " + testRobotFriend.toString());
		System.out.println("Secondo robot (nemico): " + testRobotEnemy.toString());
		System.out.println("Terzo robot (amico): " + testRobotFriend2.toString());
		System.out.println("Guarisco una coordinata non valida (diagonale)");
		testRobotFriend.setState(RobotStates.ATTACKING);
		tester.support(testRobotFriend, ac, new Coordinates(2, 2));
		System.out.println("Primo robot (amico): " + testRobotFriend.toString());
		System.out.println("Secondo robot (nemico): " + testRobotEnemy.toString());
		System.out.println("Terzo robot (amico): " + testRobotFriend2.toString());
		System.out.println(">>>FINE TEST<<<");
	}

	public SupportTest() {
		this.robots = new ArrayList<>();
	}

	public void support(SupportRobot testRobot, RobotSupportCommand ac, Coordinates target) {
		ac = new RobotSupportCommand(0, target, testRobot);
		testRobot.setCommand(ac);
		try {
			while (testRobot.getState() != RobotStates.TURN_OVER) {
				testRobot.update();
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

	@Override
	public RobotTarget getTarget(Coordinates target) {
		for (Actor r : robots) {
			if (r.getCoords().equals(target)) {
				return new RobotTarget(r);
			}
		}
		return null;
	}

	private void show() {
		for (Actor r : robots) {
		}
	}

	private void addRobot(Actor robot) {
		robots.add(robot);
	}

	private ArrayList<Actor> robots;

}