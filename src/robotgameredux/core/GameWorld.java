package robotgameredux.core;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JPanel;
import robotgameredux.actors.*;

public class GameWorld extends JPanel{

	private static final long serialVersionUID = 7321125104091891404L;

	private final static int GRID_HEIGHT = 1280/64;
	private final static int GRID_LENGHT = 720/64;
	
	
	public GameWorld() {
		this.setSize(1280, 720);
		this.setLayout(null);
		this.initWorld();
	}
	
	private void initWorld() {
		actors = new ArrayList<GameObject>();
		tileSet = new Tile[GRID_HEIGHT][GRID_LENGHT];
		robotController = new RobotController(this);

		for (int i = 0; i < GRID_HEIGHT; i++) {
			for (int j = 0; j < GRID_LENGHT; j++) {
				 tileSet[i][j] = new Tile();
			}
		}
		
		this.addMouseListener(new InputHandler2());
		
		actors.add(robotController.createRobot(new Vector2(1,0)));
		actors.add(robotController.createRobot(new Vector2(10, 5)));
		tileSet[1][0].setCalpestabile(false);
		tileSet[10][5].setCalpestabile(false);
		
		runGameLoop();
	}
	

	public boolean isTileFree(Vector2 tile) {
		if (tileSet[(int) tile.x][(int) tile.y].isCalpestabile() == true)
			return true;
		else 
			return false;
	}
	
	public void releaseTile(Vector2 tile) {
		tileSet[(int) tile.x][(int) tile.y].setCalpestabile(true);
	}
	
	public void occupyTile(Vector2 tile) {
		tileSet[(int) tile.x][(int) tile.y].setCalpestabile(false);
	}
	
	public Boolean getPaused() {
		return this.paused;
	}
	
	public void setPaused(Boolean paused) {
		this.paused = paused;
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
				 * GameWorld vede i vari attori solo come GameObjects, non gli interessa se sono robot attaccanti, di supporto, stazioni o ostacoli. Utilizza i metodi
				 * update() e render() di GameObject per causarne l'aggiornamento e il render a prescindere dal tipo tramite polimorfismo.
				 * "Utilizza" i vari controller per far processare l'input ai componenti interattivi 
				 */
				
				robotController.update(); //Processa l'input
				//Aggiorna gli stati dei vari componenti
				for(int i = 0; i < actors.size(); i++) {
					actors.get(i).update();
				}
				//Disegna i vari componenti
				for(int i = 0; i < actors.size(); i++) {
					actors.get(i).render();
				}
				try {
					Thread.sleep(16);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private Boolean paused = false;
	private Boolean isRunning = true;
	private ArrayList<GameObject> actors;
	private Tile[][] tileSet;
	private RobotController robotController;
	
	class InputHandler2 extends MouseAdapter{
		
		public InputHandler2() {
			
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			System.out.println("ENTRATO");
			Boolean trovato = false;
			int i = 0;
			while (!trovato) {
				
				Robot robot = (Robot) actors.get(i);
				if (robot.getCoords().x == e.getX()/64 && robot.getCoords().y == e.getY()/64){
					System.out.println("Il robot è alla posizione: " + robot.getCoords().toString());
					trovato = true;
				}
				trovato = true;
			}
			
		}
		@Override
		public void mousePressed(MouseEvent e) {
			/* Passa l'input ai controller */
			Vector2 click = new Vector2(e.getX()/64, e.getY()/64);
			System.out.println(click.toString());
			robotController.setInput(click);
		}
	}
}








