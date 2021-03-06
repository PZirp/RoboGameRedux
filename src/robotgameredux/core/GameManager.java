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

import robotgameredux.enums.Faction;
import robotgameredux.enums.RobotStates;
import robotgameredux.exceptions.CriticalStatusException;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;
import robotgameredux.gameobjects.Actor;
import robotgameredux.graphic.Sprite;
import robotgameredux.input.AttackRobotController;
import robotgameredux.input.SupportRobotController;
import robotgameredux.players.CPUController;
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
		// initGame();
		// randomGeneration();
	}

	/**
	 * Inizializza l'ambiente. Crea un gameWorld che mantiene l'ambiente, un
	 * ActorManager che gestisce gli attori, e inizializza il player interattivo
	 * e quello gestito dalla CPU.
	 */

	private void gameSetUp() {
		gameWorld = new GameWorld(this);
		attackRobotController = new AttackRobotController(this);
		supportRobotController = new SupportRobotController(this);
		player = new Player();
		player.addPropertyChangeListener(this);
		ai = new CPUController(gameWorld, robotFactory);
		ai.addPropertyChangeListener(this);

		robotFactory = new ActorManager(this, gameWorld, attackRobotController, supportRobotController, player, ai);

		ai.setRF(robotFactory);
		gameWorld.addPropertyChangeListener(attackRobotController);
		gameWorld.addPropertyChangeListener(supportRobotController);
	}

	/**
	 * Inizializza i vari attori standard all'inizio di una partita. Crea sia
	 * gli attori controllati interattivamente, sia quelli controllati dalla
	 * CPU.
	 */

	private void actorsSetUp() {
		robotFactory.createStandardAttack(Faction.FRIEND, new Coordinates(1, 3));
		robotFactory.createStandardAttack(Faction.FRIEND, new Coordinates(1, 5));
		robotFactory.createStandardAttack(Faction.ENEMY, new Coordinates(18, 2));
		robotFactory.createStandardAttack(Faction.ENEMY, new Coordinates(18, 6));
		robotFactory.createSupport(Faction.FRIEND, new Coordinates(1, 4));
		robotFactory.createSupport(Faction.ENEMY, new Coordinates(18, 4));
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

		gameWorld.createObstacle(new Coordinates(5, 8));
		gameWorld.createObstacle(new Coordinates(15, 8));
		gameWorld.createObstacle(new Coordinates(5, 2));
		gameWorld.createObstacle(new Coordinates(15, 2));
		gameWorld.createObstacle(new Coordinates(10, 4));
		gameWorld.createObstacle(new Coordinates(11, 4));
		gameWorld.createObstacle(new Coordinates(11, 6));
		gameWorld.createObstacle(new Coordinates(9, 6));
		gameWorld.createObstacle(new Coordinates(9, 4));
		gameWorld.createObstacle(new Coordinates(10, 6));
		gameWorld.createObstacle(new Coordinates(9, 5));
		gameWorld.createObstacle(new Coordinates(11, 5));
		gameWorld.createStation(new Coordinates(10, 5));

		runGameLoop();
	}

	/**
	 * Metodo di servizio per la creazione del secondo livello
	 */

	public void secondLevel() {
		gameSetUp();
		actorsSetUp();

		gameWorld.createObstacle(new Coordinates(5, 8));
		gameWorld.createObstacle(new Coordinates(15, 8));
		gameWorld.createObstacle(new Coordinates(5, 2));
		gameWorld.createObstacle(new Coordinates(5, 5));
		gameWorld.createObstacle(new Coordinates(15, 5));
		
		gameWorld.createObstacle(new Coordinates(10, 2));
		gameWorld.createObstacle(new Coordinates(15, 2));
		gameWorld.createObstacle(new Coordinates(10, 8));
		gameWorld.createStation(new Coordinates(10, 5));

		runGameLoop();
	}
	
	/**
	 * Rimuove il robot specificato dal gioco
	 * 
	 * @param robot
	 *            da rimuovere
	 */

	public void removeRobot(Actor robot) {
		gameWorld.releaseTile(robot.getCoords());
		pane.remove(robot.getSprite());
	}

	/**
	 * Rimuove la sprite indicata dallo schermo
	 * 
	 * @param sprite
	 *            da rimuovere
	 */

	/**
	 * Metodo usato per aggiungere sprite allo schermo
	 * 
	 * @param sprite
	 *            la sprite da aggiungere
	 * @param layer
	 *            il layer del JLayeredPane in cui aggiungere la sprite
	 */

	public void addToScreen(Sprite sprite, int layer) {
		this.pane.add(sprite, layer);
	}


	/**
	 * Metodo usato per rimuovere una sprite allo schermo
	 * 
	 * @param sprite
	 *            la sprite da aggiungere
	 */

	
	public void removeFromScreen(Sprite sprite) {
		pane.remove(sprite);
	}

	/**
	 * Metodo usato per decidere l'ordine di movimento in un nuovo turno. Se
	 * nessuno dei due giocatori � attivo, ed entrambi hanno mosso, inizia un
	 * nuovo turno, quindi tutti gli attori vengono resettati, insieme ai
	 * controllori. Viene scelto casualmente un controllore che muover� per
	 * primo. Se uno dei due controllori ha mosso, viene attivato l'altro.
	 */

	private void turn() {
		if (!player.isActve() && !ai.isActve()) {
			if (player.hasMoved() && ai.hasMoved()) {
				robotFactory.resetRobots();
				player.resetMoved();
				ai.resetMoved();
				random = new Random();
				if (random.nextBoolean() == true) {
					JOptionPane.showMessageDialog(null, "Turno del giocatore");
					player.setActive(true);
				} else {
					JOptionPane.showMessageDialog(null, "Turno della CPU");
					ai.setActive(true);
				}
			} else if (!player.hasMoved()) {
				player.setActive(true);
				JOptionPane.showMessageDialog(null, "Turno del giocatore");
			} else if (!ai.hasMoved()) {
				JOptionPane.showMessageDialog(null, "Turno della CPU");
				ai.setActive(true);
			}
		}
	}

	/**
	 * Metodo che contiene il loop principale del programma. Ad ogni iterazione,
	 * se il controllore interattivo � attivo, esamina l'input, se il
	 * controllore gestito dalla CPU � attivo, questo sceglie una mossa. In
	 * segito alla scelta e al controllo dell'input, viene aggiornato il robot
	 * attivo, e viene effettuato il painting dell'ambiente aggiornato. Il
	 * metodo inoltre si occupa di catturare e gestire le eccezioni lanciate dai
	 * robot.
	 */
	private void gameLoop() {

		while (isRunning) {
			turn();
			try {
				if (player.isActve()) {
					attackRobotController.parseInput(); // Processa l'input
					supportRobotController.parseInput(); // Processa l'input
				} else if (ai.isActve() && this.robotFactory.hasActiveRobot() == null) {
					ai.update();
				}
				robotFactory.updateActiveRobot();
				robotFactory.render();
				gameWorld.render();

				Thread.sleep(300);

			} catch (InvalidTargetException e) {
				JOptionPane.showMessageDialog(pane, "Mossa non valida");
				e.getCommand().setState(RobotStates.IDLE);
			} catch (InsufficientEnergyException e) {
				JOptionPane.showMessageDialog(pane,
						"Il robot non ha abbastanza energia per compiere quest'azione!\n" + "Energia necessaria: "
								+ e.getRequiredEnergy() + " Energia residua: " + e.getResidualEnergy());
				e.getCommand().setState(RobotStates.IDLE);
			} catch (CriticalStatusException e) {
				if (e.getFaction() == Faction.FRIEND) {
					JOptionPane.showMessageDialog(pane,
							"Un tuo robot � in stato critico!\n" + "Salute residua: " + e.getResidualHealth());
				}
				robotFactory.setTurnOver();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Evidenzia i percorsi percorribili da un robot (tramite il metodo di
	 * gameWorld)
	 * @param r
	 * 		l'attore di cui si deve evidenziare il percorso
	 */

	public void highlight(Actor r) {
		gameWorld.highlightPath(r.getCoords(), r.getRange());
	}

	/**
	 * Controlla se c'� un robot attualmente attivo;
	 * 
	 * @return il robot attivo se presente, null altrimenti
	 */

	public Actor hasActiveRobot() {
		Actor r = robotFactory.hasActiveRobot();
		if (r != null) {
			return r;
		}
		return null;
	}

	/**
	 * Termina il turno quando entrambi i player (interattivo e non) hanno
	 * effettuato la loro mossa
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
	 * In seguito alla deserializzazione, esegue i metodi post-deserializzazione
	 * di ActorManager e GameWorld, installa un nuovo mouselistener e fa
	 * ripartire il loop
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
			@Override
			public void mousePressed(MouseEvent e) {
				if (endTurnButton.isEnabled()) {
					endTurn();
					endTurnButton.setEnabled(false);
				}
			}
		});
		pane.getParent().add(endTurnButton, BorderLayout.SOUTH);
		if (player.isActve() && player.hasMoved()) {
			endTurnButton.setEnabled(true);
		}
		endTurnButton.setEnabled(false);
	}

	public JLayeredPane getPane() {
		return pane;
	}

	/**
	 * Crea il thread su cui gira la logica e lo fa partire
	 */

	private void runGameLoop() {
		Thread loop = new Thread() {
			@Override
			public void run() {
				gameLoop();
			}
		};
		loop.start();
	}
	
	/**
	 * Gestisce i PropertyChangeEvent
	 * @param arg0
	 * 			l'evento da gestire
	 */

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
	private CPUController ai;
	private Player player;
	private Boolean isRunning = true;
	private AttackRobotController attackRobotController;
	private SupportRobotController supportRobotController;
	private Random random;

	class InputHandler2 extends MouseAdapter {

		public InputHandler2() {
		}

		/**
		 * Converte le coordinate del click in coordinate usabili dalla logica
		 * del programma
		 */

		@Override
		public void mousePressed(MouseEvent e) {
			// Passa l'input ai controller
			Coordinates click = new Coordinates(e.getX() / 64, e.getY() / 64);
			if (player.isActve()) {
				attackRobotController.setInput(click);
				supportRobotController.setInput(click);
			}
		}
	}

}
