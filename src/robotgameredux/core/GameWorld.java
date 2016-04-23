package robotgameredux.core;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JFrame;

import robotgameredux.actors.*;
import robotgameredux.input.InputHandler;

public class GameWorld extends JFrame{

	private static final long serialVersionUID = 7321125104091891404L;

	private final static int HEIGHT = 1280/64;
	private final static int LENGHT = 720/64;
	
	
	public static void main(String[] args) {
		GameWorld gw = new GameWorld();
		gw.initWorld();
		gw.initScreen();
	}

	private void initScreen() {
		this.setTitle("RobotGame Redux");
		this.setSize(1280, 720);
		this.setUndecorated(true);
		this.setLayout(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	private void initWorld() {
		actors = new Vector<GameObject>();
		tileSet = new Tile[HEIGHT][LENGHT];


		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < LENGHT; j++) {
				 tileSet[i][j] = new Tile();
			}
		}
		
		actors.add(new Robot(this));
		actors.get(0).setCoords(new Vector2(1,0));
		tileSet[1][0].setCalpestabile(false);
		actors.add(new Robot(this));
		actors.get(1).setCoords(new Vector2(10,5));
		tileSet[10][5].setCalpestabile(false);
		this.addMouseListener(new InputHandler2());
		runGameLoop();
		//Prova
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
	
	private void runGameLoop() {
		
		Thread loop = new Thread()
			{ public void run() {
				gameLoop();
			}
		};
		loop.start();
	}
	
	private void gameLoop() {
		
		while(isRunning) {
			
			//processInput();
			for(int i = 0; i < actors.size(); i++) {
				actors.get(i).update();
			}
			for(int i = 0; i < actors.size(); i++) {
				actors.get(i).render();
			}			
			try {
				Thread.sleep(8);
			}
			catch (Exception e) {
				e.printStackTrace();
			}			
		}
	}

	private Boolean isRunning = true;
	private Vector<GameObject> actors;
	private Tile[][] tileSet;
	
	class InputHandler2 implements MouseListener{
		
		public InputHandler2() {
			
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {
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
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
			Vector2 click = new Vector2(e.getX()/64, e.getY()/64);
			System.out.println(click.toString());
			if (active == null) {
				for (int i = 0; i < actors.size(); i++) {
					Robot robot = (Robot) actors.get(i);
					if (robot.getCoords().x == click.x && robot.getCoords().y == click.y){
						System.out.println("Il robot è alla posizione: " + robot.getCoords().toString());
						active = robot;
						System.out.println("Il robot è attivo prima del click? " + robot.getActive().toString());
						robot.setActive(true);
						System.out.println("Il robot è attivo dopo il click? " + robot.getActive().toString());
					}
				}
			}
			else {
				if (click.x != e.getX() || click.y != e.getY()) {
					active.setDest(click);
					active.setState(1);
					active = null;
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}	
		
		Robot active;
	}

}




