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
import robotgameredux.weapons.Weapon;

public class GameManager implements PropertyChangeListener, Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1384150637987144235L;

	public GameManager() {
		pane = new JLayeredPane();
		pane.setSize(1280, 720);
		pane.setLayout(null);
		initGame();
	}
		
	private void initGame() {
		player = new Player();
		player.addPropertyChangeListener(this);
		
		actors = new ArrayList<Robot>();
		obstacles = new ArrayList<Obstacle>();
		gameWorld = new GameWorld(this);
		attackRobotController = new AttackRobotController(this);
		supportRobotController = new SupportRobotController(this);
		AIattackRobotController = new AttackRobotController(this);

		robotFactory = new RobotFactory (this, gameWorld, attackRobotController, supportRobotController, AIattackRobotController);
		pane.addMouseListener(new InputHandler2());
		pane.addMouseMotionListener(new InputHandler2());
		
		AttackRobot r;
		r = robotFactory.createStandardAttack(Faction.FRIEND, new Vector2(1,1));
		gameWorld.occupyTile(new Vector2(1,0));
		player.addRobot(r);
		actors.add(r);
		pane.add(r.getSprite(), 1);
		r.addPropertyChangeListener(attackRobotController);
		r.addPropertyChangeListener(this);
		r.addPropertyChangeListener(robotFactory);
		r.addPropertyChangeListener(player);
		System.out.println(r.toString());
	
		
		r = robotFactory.createStandardAttack(Faction.FRIEND, new Vector2(10,5));
		gameWorld.occupyTile(new Vector2(10,5));
		player.addRobot(r);
		actors.add(r);
		pane.add(r.getSprite(), 1);
		gameWorld.occupyTile(r.getCoords());
		r.addPropertyChangeListener(attackRobotController);
		r.addPropertyChangeListener(this);
		r.addPropertyChangeListener(robotFactory);
		r.addPropertyChangeListener(player);
		System.out.println(r.toString());
	
		ai = new AI(gameWorld, robotFactory);
		ai.addPropertyChangeListener(this);
		r = robotFactory.createStandardAttack(Faction.ENEMY, new Vector2(3,8));
		gameWorld.occupyTile(new Vector2(3,8));
		actors.add(r);
		pane.add(r.getSprite(), 1);
		gameWorld.occupyTile(r.getCoords());
		r.addPropertyChangeListener(attackRobotController);
		r.addPropertyChangeListener(this);
		r.addPropertyChangeListener(robotFactory);
		r.addPropertyChangeListener(ai);
		System.out.println(r.toString());
		
		
		
		SupportRobot sr = robotFactory.createSupport(Faction.FRIEND, new Vector2(6,7));
		gameWorld.occupyTile(new Vector2(6,7));
		player.addRobot(sr);
		actors.add(sr);
		pane.add(sr.getSprite(), 1);
		gameWorld.occupyTile(sr.getCoords());
		sr.addPropertyChangeListener(supportRobotController);
		sr.addPropertyChangeListener(this);
		sr.addPropertyChangeListener(robotFactory);
		sr.addPropertyChangeListener(player);
		System.out.println(sr.toString());

		sr = robotFactory.createSupport(Faction.FRIEND, new Vector2(6,9));
		gameWorld.occupyTile(new Vector2(6,7));
		player.addRobot(sr);
		actors.add(sr);
		pane.add(sr.getSprite(), 1);
		gameWorld.occupyTile(sr.getCoords());
		sr.addPropertyChangeListener(supportRobotController);
		sr.addPropertyChangeListener(this);
		sr.addPropertyChangeListener(robotFactory);
		sr.addPropertyChangeListener(player);
		System.out.println(sr.toString());
		
		
		Obstacle o = gameWorld.createObstacle(new Vector2(5, 5));
		pane.add(o.getSprite(), 1);
		gameWorld.occupyTile(r.getCoords());
		obstacles.add(o);
	
		Station s = gameWorld.createStation(new Vector2(7,7));
		pane.add(s.getSprite(), 1);
		station = s;
		s.addPropertyChangeListener(attackRobotController);
		s.addPropertyChangeListener(supportRobotController);
		//obstacles = gameWorld.getObstacles();
		
		
		
		runGameLoop();
	}
		
	private void removeRobot(Robot robot) {
		gameWorld.releaseTile(robot.getCoords());
		pane.remove(robot.getSprite());
		actors.remove(robot);
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
				player.setMoved(false);
				ai.setMoved(false);
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
				} else if (ai.isActve()) {
					AIattackRobotController.parseInput();
				}
				//Aggiorna gli stati dei vari componenti
				for(int i = 0; i < actors.size(); i++) {
					actors.get(i).update();
				}
				
				//Disegna i vari componenti
				for(int i = 0; i < obstacles.size(); i++) 
					obstacles.get(i).render();
				for(int i = 0; i < actors.size(); i++) 
					actors.get(i).render();
				station.render();
				gameWorld.render();
				
				Thread.sleep(300);

				//this.repaint(); //fare in maniera migliore
				} 
				catch (InvalidTargetException e) {
					JOptionPane.showMessageDialog(pane, "Mossa non valida");
					System.out.println(e.getMessage());
					e.getCommand().setState(RobotStates.IDLE);
					activeRobot = null;
				}
				catch (InsufficientEnergyException e) {
					System.out.println(e.getMessage());
					JOptionPane.showMessageDialog(pane, "Il robot non ha abbastanza energia per compiere quest'azione!");
					e.getCommand().setState(RobotStates.IDLE);
					activeRobot = null;
				}
				catch (CriticalStatusException e) {
					System.out.println(e.getMessage());
					JOptionPane.showMessageDialog(pane, "Un tuo robot è in stato critico!");
					activeRobot.setState(RobotStates.TURN_OVER);
					activeRobot = null;
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
		if (activeRobot != null) return activeRobot;
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
		gameWorld.addAgain();
		for (Robot r : actors) {
			pane.add(r.getSprite(), 1);
		}
		pane.repaint();
		for (Robot r : actors) {
			System.out.println(r.toString());
		}
		pane.addMouseListener(new InputHandler2());
		JOptionPane.showMessageDialog(null, "Eccomi");
		runGameLoop();
	}
	
	public void checkEndTurnButton() {
		if (ai.isActve() && ai.hasMoved()) {
			endTurnButton.setEnabled(true);
		}
		if (player.isActve() && player.hasMoved()) {
			endTurnButton.setEnabled(true);
		}
	}
	
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
		endTurnButton.setEnabled(false);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if (arg0.getPropertyName() == "DESTROYED") {
			Robot r = (Robot) arg0.getOldValue();
			pane.remove(r.getSprite());
			removeRobot(r);
		}
		//Non serve più
		/*if (arg0.getPropertyName() == "MOVING") {
			Robot r = (Robot) arg0.getOldValue();
			gameWorld.highlightPath(r.getCoords(), r.getRange(), false);
		}*/
		if (arg0.getPropertyName() == "ACTIVE") {
			//JOptionPane.showMessageDialog(null, "Attivato");
			activeRobot = (Robot) arg0.getOldValue();
		}
		if (arg0.getPropertyName() == "TURN_OVER") {
			activeRobot = null;
		}
		if (arg0.getPropertyName() == "ALL_MOVED") {
			this.endTurnButton.setEnabled(true);
		}
		
	}
	
	
	private GameWorld gameWorld;
	private RobotFactory robotFactory;
	private Station station;
	transient private JLayeredPane pane;
	transient private JButton endTurnButton;
	private AI ai;
	private Player player;
	private Robot activeRobot;
	private Boolean isRunning = true;
	private ArrayList<Robot> actors;
	private ArrayList<Obstacle> obstacles;
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
			Vector2 click = new Vector2(e.getX()/64, e.getY()/64);
			System.out.println(click.toString() + "NUOVOOOOOOOOOOOO CLICK");
			if (player.isActve()) {
			attackRobotController.setInput(click);
			supportRobotController.setInput(click);
			}
			AIattackRobotController.setInput(click);
			
			
		}
		
		public void mouseMoved(MouseEvent e) {
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
		}
	}

	

}
