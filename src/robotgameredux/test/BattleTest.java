package robotgameredux.test;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import Exceptions.CriticalStatusException;
import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.Commands.RobotAttackCommand;
import robotgameredux.Commands.RobotAttackInteractCommand;
import robotgameredux.TargetImplementations.RobotTarget;
import robotgameredux.actors.AttackRobot;
import robotgameredux.actors.Faction;
import robotgameredux.actors.Robot;
import robotgameredux.core.Coordinates;
import robotgameredux.core.IActorManager;
import robotgameredux.input.RobotStates;
import robotgameredux.systemsImplementations.StandardAttackInteractionSystem;
import robotgameredux.systemsImplementations.StandardBattleSystem;
import robotgameredux.weapons.Pistol;

public class BattleTest implements IActorManager {

	public static void main(String[] args) {
		System.out.println(">>>INIZIO TEST<<<");
		BattleTest tester = new BattleTest();
		AttackRobot testRobotFriend = new AttackRobot(new Coordinates(1,1), new StandardBattleSystem(tester), null, null);
		testRobotFriend.setFaction(Faction.FRIEND);
		testRobotFriend.addWeapon(new Pistol());
		AttackRobot testRobotEnemy = new AttackRobot(new Coordinates(1,2), new StandardBattleSystem(tester), null, null);
		testRobotEnemy.setFaction(Faction.ENEMY);
		AttackRobot testRobotFriend2 = new AttackRobot(new Coordinates(2,1), new StandardBattleSystem(tester), null, null);
		testRobotFriend2.setFaction(Faction.FRIEND);
		tester.addRobot(testRobotFriend);
		tester.addRobot(testRobotFriend2);
		tester.addRobot(testRobotEnemy);
		RobotAttackCommand ac = null;
		tester.show();
		System.out.println(">>>Stato alla partenza<<<");
		System.out.println("Primo robot (amico): " + testRobotFriend.toString());
		System.out.println("Secondo robot (nemico): " + testRobotEnemy.toString());
		System.out.println("Terzo robot (amico): " + testRobotFriend2.toString());
		System.out.println("Attacco il robot nemico");
		testRobotFriend.setState(RobotStates.ATTACKING);
		tester.attack(testRobotFriend, ac, new Coordinates(1,2));
		System.out.println("Primo robot (amico): " + testRobotFriend.toString());
		System.out.println("Secondo robot (nemico): " + testRobotEnemy.toString());
		System.out.println("Terzo robot (amico): " + testRobotFriend2.toString());
		System.out.println("Attacco il robot amico");
		testRobotFriend.setState(RobotStates.ATTACKING);
		tester.attack(testRobotFriend, ac, new Coordinates(2,1));
		System.out.println("Primo robot (amico): " + testRobotFriend.toString());
		System.out.println("Secondo robot (nemico): " + testRobotEnemy.toString());
		System.out.println("Terzo robot (amico): " + testRobotFriend2.toString());
		System.out.println("Attacco una coordinata vuota");
		testRobotFriend.setState(RobotStates.ATTACKING);
		tester.attack(testRobotFriend, ac, new Coordinates(3,1));
		System.out.println("Primo robot (amico): " + testRobotFriend.toString());
		System.out.println("Secondo robot (nemico): " + testRobotEnemy.toString());
		System.out.println("Terzo robot (amico): " + testRobotFriend2.toString());
		System.out.println("Attacco una coordinata non valida (diagonale)");
		testRobotFriend.setState(RobotStates.ATTACKING);
		tester.attack(testRobotFriend, ac, new Coordinates(2,2));
		System.out.println("Primo robot (amico): " + testRobotFriend.toString());
		System.out.println("Secondo robot (nemico): " + testRobotEnemy.toString());
		System.out.println("Terzo robot (amico): " + testRobotFriend2.toString());
		System.out.println(">>>FINE TEST<<<");
	}
	
	public BattleTest() {
		this.robots = new ArrayList<>();
	}
	
	public void attack(AttackRobot testRobot, RobotAttackCommand ac, Coordinates target) {
		ac = new RobotAttackCommand(0, target, testRobot);
		testRobot.setCommand(ac);
		try {
			while(testRobot.getState() != RobotStates.TURN_OVER) {
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
		for (Robot r : robots) {
			if (r.getCoords().equals(target)) {
				return new RobotTarget(r);
			}
		}	
		return null;
	}
	
	private void show() {
		for (Robot r : robots) {
		}
	}
	
	private void addRobot(Robot robot) {
		robots.add(robot);
	}
	
	private ArrayList<Robot> robots;

}
