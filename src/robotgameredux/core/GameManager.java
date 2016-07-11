package robotgameredux.core;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Exceptions.CriticalStatusException;
import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.actors.AttackRobot;
import robotgameredux.actors.Faction;
import robotgameredux.actors.Obstacle;
import robotgameredux.actors.Robot;
import robotgameredux.actors.RobotType;
import robotgameredux.actors.Station;
import robotgameredux.actors.SupportRobot;
import robotgameredux.graphic.InfoDialog;
import robotgameredux.graphic.Sprite;
import robotgameredux.graphic.Visual;
import robotgameredux.input.AttackRobotController;
import robotgameredux.input.RobotStates;
import robotgameredux.input.SupportRobotController;
import robotgameredux.players.AI;
import robotgameredux.players.Player;
import robotgameredux.systemsImplementations.StandardAttackInteractionSystem;
import robotgameredux.systemsImplementations.StandardBattleSystem;
import robotgameredux.systemsImplementations.StandardMovementSystem;
import robotgameredux.weapons.Pistol;

public class GameManager implements PropertyChangeListener, Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1384150637987144235L;

	public GameManager() {
		pane = new JLayeredPane();
		pane.setSize(1280, 720);
		pane.setLayout(null);
		pane.addMouseListener(new InputHandler2());
		//initGame();
		//randomGeneration();
	}
	
	private void gameSetUp() {
		gameWorld = new GameWorld(this);
		attackRobotController = new AttackRobotController(this);
		supportRobotController = new SupportRobotController(this);
		AIattackRobotController = new AttackRobotController(this);
		player = new Player();
		player.addPropertyChangeListener(this);
		ai = new AI(gameWorld, robotFactory);
		ai.addPropertyChangeListener(this);

		robotFactory = new RobotFactory (this, gameWorld, attackRobotController, supportRobotController, AIattackRobotController, player, ai);		

		ai.setRF(robotFactory);
		gameWorld.addPropertyChangeListener(attackRobotController);
		gameWorld.addPropertyChangeListener(AIattackRobotController);
		gameWorld.addPropertyChangeListener(supportRobotController);
	}
	
	private void actorsSetUp() {
		robotFactory.createStandardAttack(Faction.FRIEND, new Coordinates(1,3));
		//System.out.println(r.toString());
		robotFactory.createStandardAttack(Faction.FRIEND, new Coordinates(1,5));
		//System.out.println(r.toString());
		robotFactory.createStandardAttack(Faction.ENEMY, new Coordinates(18,3));
		//System.out.println(r.toString());
		robotFactory.createStandardAttack(Faction.ENEMY, new Coordinates(18,5));
		//System.out.println(r.toString());
		robotFactory.createSupport(Faction.FRIEND, new Coordinates(1,4));
		//System.out.println(sr.toString());
	}
	
	public void randomGeneration() {
		gameSetUp();
		actorsSetUp();		
		gameWorld.randomMap();
		runGameLoop();
	}
	
	public void addToScreen(Sprite sprite, int layer) {
		this.pane.add(sprite, layer);
	}
		
	public void firstLevel() {
		gameSetUp();
		actorsSetUp();
		
		gameWorld.createObstacle(new Coordinates(5,8));
		gameWorld.createObstacle(new Coordinates(15,8));
		
		gameWorld.createObstacle(new Coordinates(5,2));
		gameWorld.createObstacle(new Coordinates(15,2));
		
		gameWorld.createObstacle(new Coordinates(10,4));
		gameWorld.createObstacle(new Coordinates(11,4));
		gameWorld.createObstacle(new Coordinates(11,6));
		gameWorld.createObstacle(new Coordinates(9, 6));
		gameWorld.createObstacle(new Coordinates(9,4));
		gameWorld.createObstacle(new Coordinates(10,6));
		gameWorld.createObstacle(new Coordinates(9,5));
		gameWorld.createObstacle(new Coordinates(11,5));
		gameWorld.createStation(new Coordinates(10,5));
		
		runGameLoop();
	}
		
	public void removeRobot(Robot robot) {
		gameWorld.releaseTile(robot.getCoords());
		pane.remove(robot.getSprite());
	}
	
	public void removeFromScreen(Sprite sprite) {
		pane.remove(sprite);
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

	private void turn() {
		if (!player.isActve() && !ai.isActve()) {
			if(player.hasMoved() && ai.hasMoved()) {
				robotFactory.resetRobots();
				/*player.setMoved(false);
				ai.setMoved(false);*/
				player.resetMoved();
				ai.resetMoved();
				random = new Random();
				if(random.nextBoolean() == true) {
					JOptionPane.showMessageDialog(null, "Player attivo RAND");
					player.setActive(true);
				} else {
					JOptionPane.showMessageDialog(null, "AI attiva RAND");
					ai.setActive(true);
				}
			} else	if (!player.hasMoved()) {
				player.setActive(true);
				JOptionPane.showMessageDialog(null, "Player attivo");
			} else if (!ai.hasMoved()) {
				JOptionPane.showMessageDialog(null, "AI attiva");
				ai.setActive(true);
			}
		}
	}
	
	private void gameLoop() {
		
		while(isRunning) {
				turn();
				
				/*
				 * GameManager vede i vari attori solo come GameObjects, non gli interessa se sono robot attaccanti, di supporto, stazioni o ostacoli. Utilizza i metodi
				 * update() e render() di GameObject per causarne l'aggiornamento e il render a prescindere dal tipo tramite polimorfismo.
				 * "Utilizza" i vari controller per far processare l'input ai componenti interattivi 
				 */
				try {
					if (player.isActve()) {					
						attackRobotController.parseInput(); //Processa l'input
						supportRobotController.parseInput(); //Processa l'input
					} else if (ai.isActve() && this.robotFactory.hasActiveRobot() == null) {
						//JOptionPane.showMessageDialog(null, "Prova");
						//AIattackRobotController.parseInput();
						ai.update();
					}
				robotFactory.updateActiveRobot();
				robotFactory.render();
				gameWorld.render();
				
				Thread.sleep(300);

				} 
				catch (InvalidTargetException e) {
					JOptionPane.showMessageDialog(pane, "Mossa non valida");
					System.out.println(e.getMessage());
					e.getCommand().setState(RobotStates.IDLE);
				}
				catch (InsufficientEnergyException e) {
					System.out.println(e.getMessage());
					JOptionPane.showMessageDialog(pane, "Il robot non ha abbastanza energia per compiere quest'azione!");
					e.getCommand().setState(RobotStates.IDLE);
				}
				catch (CriticalStatusException e) {
					System.out.println(e.getMessage());
					
					JOptionPane.showMessageDialog(pane, "Un tuo robot è in stato critico!");
					robotFactory.setTurnOver();
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
	}
	
	public void highlight(Robot r) {
		gameWorld.highlightPath(r.getCoords(), r.getRange());
	}
	
	public Robot hasActiveRobot() {
		Robot r = robotFactory.hasActiveRobot();
		if (r != null)  {
			return r;
		}
		return null;
	}
		
	public void endTurn() {
		player.setActive(false);
		ai.setActive(false);
	}

	public JLayeredPane getPane() {
		return pane;
	}
	
	private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
		inputStream.defaultReadObject();
		postSerialization();
	}
	
	public void postSerialization() {
		this.pane = new JLayeredPane();
		gameWorld.postDeserialization();
		robotFactory.postDeserialization();
		pane.addMouseListener(new InputHandler2());
//		JOptionPane.showMessageDialog(null, "Eccomi");
		runGameLoop();
	}
	
	/*public void checkEndTurnButton() {
		if (ai.isActve() && ai.hasMoved()) {
			endTurnButton.setEnabled(true);
		}
		if (player.isActve() && player.hasMoved()) {
			endTurnButton.setEnabled(true);
		}
	}*/
	
	public void createEndTurnButton() {
		endTurnButton = new JButton("Termina turno");
		endTurnButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
					if (endTurnButton.isEnabled()) {
						endTurn();
						endTurnButton.setEnabled(false);
					}
				}
		});
		pane.getParent().add(endTurnButton, BorderLayout.SOUTH);
		/*if (ai.isActve() && ai.hasMoved()) {
			endTurnButton.setEnabled(true);
		}*/
		if (player.isActve() && player.hasMoved()) {
			endTurnButton.setEnabled(true);
		}
		endTurnButton.setEnabled(false);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if (arg0.getPropertyName() == "ALL_MOVED") {
			this.endTurnButton.setEnabled(true);
		}
		if (arg0.getPropertyName() == "PLAYER_LOST") {
			JOptionPane.showMessageDialog(null, "HAI PERSO!");
			this.pane.removeAll();
		}
		if (arg0.getPropertyName() == "AI_LOST") {
			JOptionPane.showMessageDialog(null, "HAI VINTO!");
			pane.removeAll();
			pane.repaint();
			
		}
		
	}
	
	
	private GameWorld gameWorld;
	private RobotFactory robotFactory;
	//private Station station;
	transient private JLayeredPane pane;
	transient private JButton endTurnButton;
	private AI ai;
	private Player player;
	//private Robot activeRobot;
	private Boolean isRunning = true;
	//private ArrayList<Robot> actors;
	//private ArrayList<Obstacle> obstacles;
	private AttackRobotController attackRobotController;
	private AttackRobotController AIattackRobotController;
	private SupportRobotController supportRobotController;
	private Random random;
	
	class InputHandler2 extends MouseAdapter{
		
		InfoDialog info;
		
		public InputHandler2() {
			info = new InfoDialog(null, false);
		}
		
		
		
		@Override
		public void mousePressed(MouseEvent e) {
			/* Passa l'input ai controller */
			System.out.println(e.getX()/64);
			System.out.println(e.getY()/64);
			Coordinates click = new Coordinates(e.getX()/64, e.getY()/64);
			System.out.println(click.toString() + "NUOVOOOOOOOOOOOO CLICK");
			if (player.isActve()) {
			attackRobotController.setInput(click);
			supportRobotController.setInput(click);
			}
			AIattackRobotController.setInput(click);
			
			
		}
		
		/*public void mouseMoved(MouseEvent e) {
			Robot r = null;
			Vector2 pos = new Vector2(e.getX()/64, e.getY()/64);
			r = robotFactory.isRobot(pos);
			if (r != null) {
				//JOptionPane.showMessageDialog(null, r.getEnergy());
				info.setHPlabel(r.getHealth());
				info.setEnergyLabel(r.getEnergy());
				info.setVisible(true);
				info.setLocation(e.getPoint());
			}
		}*/
	}

	

}
