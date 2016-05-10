package robotgameredux.core;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JPanel;

import robotgameredux.actors.*;
import robotgameredux.weapons.Projectile;
import robotgameredux.weapons.Weapon;

public class GameWorld extends JPanel{

	private static final long serialVersionUID = 7321125104091891404L;

	private final static int GRID_LENGHT = 1280/64; //20
	private final static int GRID_HEIGHT = 720/64; //11
	
	
	public GameWorld() {
		this.setSize(1280, 720);
		this.setLayout(null);
		this.initWorld();
	}
	
	private void initWorld() {
		actors = new ArrayList<GameObject>();
		obstacles = new ArrayList<Obstacle>();
		tileSet = new Tile[GRID_LENGHT][GRID_HEIGHT];
		robotController = new AttackRobotController(this);

		for (int i = 0; i < GRID_LENGHT; i++) {
			for (int j = 0; j < GRID_HEIGHT; j++) {
				 tileSet[i][j] = new Tile();
			}
		}
		
		this.addMouseListener(new InputHandler2());
		
		obstacles.add(new Obstacle(this, new Vector2(5,5)));
		actors.add(robotController.createRobot(new Vector2(1,0)));
		actors.add(robotController.createRobot(new Vector2(10, 5)));
		tileSet[1][0].setCalpestabile(false);
		tileSet[5][5].setCalpestabile(false);
		tileSet[10][5].setCalpestabile(false);	
		runGameLoop();
	}
	

	public boolean isTileFree(Vector2 tile) {
		
		if (tile.x >= GRID_LENGHT || tile.y >= GRID_HEIGHT || tile.x < 0 || tile.y < 0) {
			return false;
		}
		
		if (tileSet[(int) tile.x][(int) tile.y].isCalpestabile() == true)
			return true;
		else 
			return false;
	}
	
	public boolean isObstacle(Vector2 obstacle) {
		for (Obstacle obs : obstacles) {
			if (obs.getCoords().x == obstacle.x && obs.getCoords().y == obstacle.y) {
				return true;
			}
		}
		return false;
	}
	
	public void removeFromWorld(GameObject go) {
		Boolean trovato = false;
		int i = 0;
		while(!trovato && i < obstacles.size()) {
				if (obstacles.get(i) == go){
					System.out.println("FACCIO PULIZIA");
					releaseTile(go.getCoords());
					obstacles.remove(i);
					//this.repaint(); //fare in maniera migliore
					trovato = true;
				}
				i++;
		}
	}
	
	
	/*public Boolean isEnemeyAt(Vector2 target) {
		//Da ripetere per ogni controllore
		if (robotController.isRobot(target)) {
			System.out.println("BAAH");
			//robotController.deliverAttack(wpn);
			return true;
		}
		return false;
	
	}*/
	
	public void addProjectile(Projectile projectile) {
		actors.add(projectile);
		//this.add(projectile.getSprite());
	}
	
	public void destroyObstacle(ActionObject obj) {
		//Obstacle target;
		Boolean trovato = false;
		int i = 0;
		while (!trovato && i < obstacles.size()) {
			if (obstacles.get(i).getCoords().x == obj.getTarget().x && obstacles.get(i).getCoords().y == obj.getTarget(). y) {
				/*obstacles.get(i).setAction(obj);
				obstacles.get(i).setState(ObstacleState.BEING_ATTACKED);*/
				obstacles.get(i).destroy(obj);
				trovato = true;
			}
		}
		
	}
	
	public void pushObstacle(ActionObject obj) {
		//Obstacle target;
		Boolean trovato = false;
		int i = 0;
		while (!trovato && i < obstacles.size()) {
			if (obstacles.get(i).getCoords().x == obj.getTarget().x && obstacles.get(i).getCoords().y == obj.getTarget(). y) {
				/*obstacles.get(i).setAction(obj);
				obstacles.get(i).setState(ObstacleState.BEING_PUSHED);*/
				obstacles.get(i).push(obj);
				trovato = true;
			}
		}
		
	}
	
	public void deliverAttack(Projectile projectile) {
		//this.remove(projectile.getSprite());
		//Check targeted flag in various controller and deliver the attack to them
	}

	/*public Boolean isEnemeyAt(Vector2 target, Weapon wpn) {
		//Da ripetere per ogni controllore
		if (robotController.isRobot(target)) {

			System.out.println("BAAH");
			robotController.deliverAttack(wpn);
			return true;
		}
		return false;
	
	}*/
	
	public Boolean getPaused() {
		return this.paused;
	}
	
	public void releaseTile(Vector2 tile) {
		tileSet[(int) tile.x][(int) tile.y].setCalpestabile(true);
	}
	
	public void occupyTile(Vector2 tile) {
		tileSet[(int) tile.x][(int) tile.y].setCalpestabile(false);
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
				for(int i = 0; i < obstacles.size(); i++) {
					obstacles.get(i).update();
				}
				//Disegna i vari componenti
				for(int i = 0; i < actors.size(); i++) {
					actors.get(i).render();
				}
				for(int i = 0; i < obstacles.size(); i++) {
					obstacles.get(i).render();
				}
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
	private ArrayList<GameObject> actors;
	private ArrayList<Obstacle> obstacles;
	private Tile[][] tileSet;
	private RobotController robotController;
	
	class InputHandler2 extends MouseAdapter{
		
		public InputHandler2() {
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