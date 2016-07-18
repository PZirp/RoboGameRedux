package robotgameredux.core;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;

import Exceptions.CriticalStatusException;
import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.actors.Faction;
import robotgameredux.actors.Robot;
import robotgameredux.graphic.Sprite;
import robotgameredux.input.AttackRobotController;
import robotgameredux.input.RobotStates;
import robotgameredux.input.SupportRobotController;
import robotgameredux.players.AI;
import robotgameredux.players.Player;

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
	
	/**
	 * Inizializza l'ambiente. Crea un gameWorld che mantiene l'ambiente, un ActorManager che gestisce gli attori, e inizializza il player interattivo e quello gestito dalla CPU.
	 */
	
	private void gameSetUp() {
		gameWorld = new GameWorld(this);
		attackRobotController = new AttackRobotController(this);
		supportRobotController = new SupportRobotController(this);
		AIattackRobotController = new AttackRobotController(this);
		player = new Player();
		player.addPropertyChangeListener(this);
		ai = new AI(gameWorld, robotFactory);
		ai.addPropertyChangeListener(this);

		robotFactory = new ActorManager (this, gameWorld, attackRobotController, supportRobotController, AIattackRobotController, player, ai);		

		ai.setRF(robotFactory);
		gameWorld.addPropertyChangeListener(attackRobotController);
		gameWorld.addPropertyChangeListener(AIattackRobotController);
		gameWorld.addPropertyChangeListener(supportRobotController);
	}
	
	/**
	 * Inizializza i vari attori standard all'inizio di una partita. Crea sia gli attori controllati interattivamente, sia quelli controllati dalla CPU.
	 */
	
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

		robotFactory.createSupport(Faction.ENEMY, new Coordinates(18,4));
		//System.out.println(sr.toString());
	}
	
	/**
	 * Metodo per la creazione di un ambiente pseudo-casuale
	 */
	
	public void randomGeneration() {
		gameSetUp();
		actorsSetUp();		
		gameWorld.randomMap();
		runGameLoop();
	}
	

		
	/**
	 * Metodo di servizio per la creazione del primo livello
	 */
	
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
	
	/**
	 * Rimuove il robot specificato dal gioco
	 * @param robot da rimuovere
	 */
		
	public void removeRobot(Robot robot) {
		gameWorld.releaseTile(robot.getCoords());
		pane.remove(robot.getSprite());
	}
	
	/**
	 * Rimuove la sprite indicata dallo schermo
	 * @param sprite da rimuovere
	 */
	
	/**
	 * Metodo usato per aggiungere sprite allo schermo
	 * @param sprite la sprite da aggiungere
	 * @param layer il layer del JLayeredPane in cui aggiungere la sprite
	 */
	
	public void addToScreen(Sprite sprite, int layer) {
		this.pane.add(sprite, layer);
	}
	
	public void removeFromScreen(Sprite sprite) {
		pane.remove(sprite);
	}
	
	/**
	 * Crea il thread su cui gira la logica e lo fa partire
	 */
	
	private void runGameLoop() {
		Thread loop = new Thread()
			{ 
				public void run() {
					gameLoop();
			}
		};
		loop.start();
	}

	/**
	 * Metodo usato per decidere l'ordine di movimento in un nuovo turno.
	 * Se nessuno dei due giocatori è attivo, ed entrambi hanno mosso, inizia un nuovo turno, quindi tutti gli attori vengono resettati, insieme ai controllori.
	 * Viene scelto casualmente un controllore che muoverà per primo.
	 * Se uno dei due controllori ha mosso, viene attivato l'altro.
	 */
	
	private void turn() {
		if (!player.isActve() && !ai.isActve()) {
			if(player.hasMoved() && ai.hasMoved()) {
				robotFactory.resetRobots();
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
	
	
	/**
	 * Metodo che contiene il loop principale del programma.
	 * Ad ogni iterazione, se il controllore interattivo è attivo, esamina l'input, se il controllore gestito dalla CPU è attivo, questo sceglie una mossa.
	 * In segito alla scelta e al controllo dell'input, viene aggiornato il robot attivo, e viene effettuato il painting dell'ambiente aggiornato.
	 * Il metodo inoltre si occupa di catturare e gestire le eccezioni lanciate dai robot. 
	 */
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
	
	/**
	 * Evidenzia i percorsi percorribili da un robot (tramite il metodo di gameWorld)
	 */
	
	public void highlight(Robot r) {
		gameWorld.highlightPath(r.getCoords(), r.getRange());
	}

	/**
	 * Controlla se c'è un robot attualmente attivo;
	 * @return il robot attivo se presente, null altrimenti
	 */
	
	public Robot hasActiveRobot() {
		Robot r = robotFactory.hasActiveRobot();
		if (r != null)  {
			return r;
		}
		return null;
	}
		
	/**
	 * Termina il turno quando entrambi i player (interattivo e non) hanno effettuato la loro mossa
	 */
	
	private void endTurn() {
		player.setActive(false);
		ai.setActive(false);
	}

	
	private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
		inputStream.defaultReadObject();
		postSerialization();
	}
	
	/**
	 * In seguito alla deserializzazione, esegue i metodi post-deserializzazione di ActorManager e GameWorld, installa un nuovo mouselistener e fa ripartire il loop
	 */
	
	private void postSerialization() {
		this.pane = new JLayeredPane();
		gameWorld.postDeserialization();
		robotFactory.postDeserialization();
		pane.addMouseListener(new InputHandler2());
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
	
	/**
	 * Crea il bottone di fine turno da aggiungere al frame padre;
	 */
	
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

	public JLayeredPane getPane() {
		return pane;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if (arg0.getPropertyName() == "ALL_MOVED") {
			this.endTurnButton.setEnabled(true);
		}
		if (arg0.getPropertyName() == "PLAYER_LOST") {
			JOptionPane.showMessageDialog(null, "HAI PERSO!");
			this.pane.removeAll();
			pane.repaint();
			isRunning = false;
		}
		if (arg0.getPropertyName() == "AI_LOST") {
			JOptionPane.showMessageDialog(null, "HAI VINTO!");
			pane.removeAll();
			pane.repaint();
			isRunning = false;
			
		}
		
	}
	

	
	
	private GameWorld gameWorld;
	private ActorManager robotFactory;
	transient private JLayeredPane pane;
	transient private JButton endTurnButton;
	private AI ai;
	private Player player;
	private Boolean isRunning = true;
	private AttackRobotController attackRobotController;
	private AttackRobotController AIattackRobotController;
	private SupportRobotController supportRobotController;
	private Random random;
	
	class InputHandler2 extends MouseAdapter{	
		
		public InputHandler2() {}			
		
		/**
		 * Converte le coordinate del click in coordinate usabili dalla logica del programma 
		 */
		
		@Override
		public void mousePressed(MouseEvent e) {
			// Passa l'input ai controller 
			System.out.println(e.getX()/64);
			System.out.println(e.getY()/64);
			Coordinates click = new Coordinates(e.getX()/64, e.getY()/64);
			//System.out.println(click.toString() + "NUOVOOOOOOOOOOOO CLICK");
			if (player.isActve()) {
			attackRobotController.setInput(click);
			supportRobotController.setInput(click);
			}
			AIattackRobotController.setInput(click);				
		}
	}

	

}
