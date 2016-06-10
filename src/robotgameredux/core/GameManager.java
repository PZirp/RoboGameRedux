package robotgameredux.core;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

import robotgameredux.actors.GameObject;
import robotgameredux.actors.Obstacle;
import robotgameredux.actors.Robot;
import robotgameredux.actors.RobotType;
import robotgameredux.actors.Station;
import robotgameredux.input.AttackRobotController;
import robotgameredux.input.Faction;
import robotgameredux.input.SupportRobotController;
import robotgameredux.systems.AttackInteractionSystem;
import robotgameredux.systems.StandardAttackInteractionSystem;
import robotgameredux.systems.BattleSystem;
import robotgameredux.systems.InteractSystem;
import robotgameredux.systems.MovementSystem;
import robotgameredux.systems.StandardBattleSystem;
import robotgameredux.systems.StandardMovementSystem;
import robotgameredux.systems.StandardSupportSystem;
import robotgameredux.systems.SupportInteractionSystem;
import robotgameredux.systems.SupportSystem;
import robotgameredux.systems.SupportWorldInteractionSystem;

public class GameManager extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9095022656022659550L;
	
	public GameManager() {
		this.setSize(1280, 720);
		this.setLayout(null);
		initGame();
	}
	
	private StandardBattleSystem battleSystem;
	private MovementSystem movementSystem;
	private StandardSupportSystem supportSystem;
	private AttackInteractionSystem attackInteractSystem;
	private SupportInteractionSystem iss;
	private GameWorld gameWorld;
	private RobotFactory robotFactory;
	private Station station;
	
	private void initGame() {
		
		actors = new ArrayList<Robot>();
		obstacles = new ArrayList<Obstacle>();
		gameWorld = new GameWorld(this);
		attackRobotController = new AttackRobotController(this);
		supportRobotController = new SupportRobotController(this);
		movementSystem = new StandardMovementSystem(gameWorld);
		supportSystem = new StandardSupportSystem();
		//interactSystem = new STD_WorldInteractionSystem(gameWorld);
		attackInteractSystem = new StandardAttackInteractionSystem(gameWorld);
		iss = new SupportWorldInteractionSystem(gameWorld);
		battleSystem = new StandardBattleSystem();
		robotFactory = new RobotFactory(this, attackRobotController, supportRobotController, battleSystem, movementSystem, supportSystem, attackInteractSystem, iss);
		battleSystem.setActorManager(robotFactory);
		battleSystem.setGameWorld(gameWorld);
		supportSystem.setActorManager(robotFactory);
		this.addMouseListener(new InputHandler2());
		Robot r;
		r = robotFactory.createRobot(Faction.FRIEND, new Vector2(1,0), RobotType.ATTACK);
		actors.add(r);
		this.add(r.getSprite());
		r = robotFactory.createRobot(Faction.FRIEND, new Vector2(6,7), RobotType.SUPPORT);
		actors.add(r);
		this.add(r.getSprite());
		r = robotFactory.createRobot(Faction.FRIEND, new Vector2(10,5), RobotType.ATTACK);
		actors.add(r);
		this.add(r.getSprite());
		Obstacle o = gameWorld.createObstacle(new Vector2(5, 5));
		this.add(o.getSprite());
		Station s = gameWorld.createStation(new Vector2(7,7));
		this.add(s.getSprite());
		station = s;
		
		//actors.add(robotFactory.createRobot(Faction.FRIEND, new Vector2(1,0), RobotType.ATTACK));
		//actors.add(robotFactory.createRobot(Faction.FRIEND, new Vector2(10,5), RobotType.ATTACK));
		//actors.add(robotFactory.createRobot(Faction.FRIEND, new Vector2(6,7), RobotType.SUPPORT));
		obstacles = gameWorld.getObstacles();
		runGameLoop();
	}
	
	public void setPaused(Boolean paused) {
		this.paused = paused;
	}
	
	public Boolean getPaused() {
		return this.paused;
	}
	
	private void runGameLoop() {
		
		Thread loop = new Thread()
			{ 
				public void run() {
					gameLoop();
			}
		};
		loop.start();
	}
	
	private void gameLoop() {
		
		while(isRunning) {
			System.out.println(paused);
			if(!paused){
				
				/*
				 * GameManager vede i vari attori solo come GameObjects, non gli interessa se sono robot attaccanti, di supporto, stazioni o ostacoli. Utilizza i metodi
				 * update() e render() di GameObject per causarne l'aggiornamento e il render a prescindere dal tipo tramite polimorfismo.
				 * "Utilizza" i vari controller per far processare l'input ai componenti interattivi 
				 */
				
				attackRobotController.parseInput(); //Processa l'input
				supportRobotController.parseInput(); //Processa l'input
				//Aggiorna gli stati dei vari componenti
				for(int i = 0; i < actors.size(); i++) {
					actors.get(i).update();
				}
				/*for(int i = 0; i < obstacles.size(); i++) {
					obstacles.get(i).update();
				}*/
				//Disegna i vari componenti
				for(int i = 0; i < actors.size(); i++) 
					actors.get(i).render();
				for(int i = 0; i < obstacles.size(); i++) 
					obstacles.get(i).render();
				station.render();
				this.repaint(); //fare in maniera migliore
				try {
					Thread.sleep(500);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}



	private Boolean paused = false;
	private Boolean isRunning = true;
	private ArrayList<Robot> actors;
	private ArrayList<Obstacle> obstacles;
	private AttackRobotController attackRobotController;
	private SupportRobotController supportRobotController;

	class InputHandler2 extends MouseAdapter{
		
		public InputHandler2() {
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			/* Passa l'input ai controller */
			Vector2 click = new Vector2(e.getX()/64, e.getY()/64);
			System.out.println(click.toString());
			attackRobotController.setInput(click);
			supportRobotController.setInput(click);
		}
	}
}