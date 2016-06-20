package robotgameredux.core;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import robotgameredux.actors.*;
import robotgameredux.graphic.ObstacleSprite;
import robotgameredux.graphic.StationSprite;
import robotgameredux.tools.HealthPack;
import robotgameredux.tools.ToolsDialog;
import robotgameredux.tools.UsableTool;
import robotgameredux.weapons.Weapon;

public class GameWorld {

	private static final long serialVersionUID = 7321125104091891404L;

	private final static int GRID_LENGHT = 1280/64; //20
	private final static int GRID_HEIGHT = 720/64; //11
	
	public GameWorld(GameManager reference) {
		this.reference = reference;
		this.initWorld();
		this.dialog = new ToolsDialog((JFrame) reference.getParent(), true);
	}
	
	private void initWorld() {
		tileSet = new Tile[GRID_LENGHT][GRID_HEIGHT];
		obstacles = new ArrayList<Obstacle>();
		
	
		for (int i = 0; i < GRID_LENGHT; i++) {
			for (int j = 0; j < GRID_HEIGHT; j++) {
				 tileSet[i][j] = new Tile();
			}
		}
	
		//tileSet[1][0].setCalpestabile(false);
		//tileSet[5][5].setCalpestabile(false);

	}
	
	public ArrayList<Obstacle> getObstacles() {
		return this.obstacles;
	}
	
	public Obstacle createObstacle(Vector2 position) {
		Obstacle o = new Obstacle(position); 
		ObstacleSprite sp = new ObstacleSprite(o);
		o.setSprite(sp);
		occupyTile(position);
		obstacles.add(o);		
		return o;
	}
	
	public boolean isStation(Vector2 position) {
		if (position.x == station.getCoords().x && position.y == station.getCoords().y) {
			return true;
		}
		return false;
	}
	
	public Integer recharge() {
		return station.recharge();
	}
	


	public Station createStation(Vector2 position) {
		Station s = new Station(position);
		StationSprite sp = new StationSprite(s);
		s.setSprite(sp);		
		s.addObject(new HealthPack());
		s.addObject(new HealthPack());
		s.addObject(new HealthPack());
		
		occupyTile(position);
		station = s;
		return s;
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
	
	public void releaseTile(Vector2 tile) {
		tileSet[(int) tile.x][(int) tile.y].setCalpestabile(true);
	}
	
	public void occupyTile(Vector2 tile) {
		tileSet[(int) tile.x][(int) tile.y].setCalpestabile(false);
	}
	
	public boolean isObstacle(Vector2 obstacle) {
		for (Obstacle obs : obstacles) {
			if (obs.getCoords().x == obstacle.x && obs.getCoords().y == obstacle.y) {
				return true;
			}
		}
		return false;
	}
	
	public void destroyObstacle(Vector2 target, int robotStrenght) {
		//Passagli un clone! O solo la forza del robot?
		Boolean trovato = false;
		int i = 0;
		while (!trovato && i < obstacles.size()) {
			if (obstacles.get(i).getCoords().x == target.x && obstacles.get(i).getCoords().y == target. y) {
				if (robotStrenght > obstacles.get(i).getResistence())
					System.out.println("FACCIO PULIZIA");
					reference.remove(obstacles.get(i).getSprite());
					releaseTile(obstacles.get(i).getCoords());
					obstacles.remove(i);
				trovato = true;
			}
		}
		
	}
	
	/* Cosa fa: Calcolo la direzione in cui devo muovermi (direction) e la sommo alla posizone attuale. 
	 * Se la nuova posizitione è libera, sposto l'ostacolo.
	 * In questo caso basta calcolare la direzione in cui viene spinto, dato che la posizione del robo che spinge non è importante
	 * e l'ostacolo spinto si muove solo di una casella, posso quindi direttamente sommare il risultato per ottenere la nuova posizione.
	 * E avessi voluto, per esempio, orientare l'ostacolo verso il robot che spinge, avrei dovuto calcolare la posizione relativa del robot
	 * rispetto all'ostacolo (sottraendo la posizione del robot alla posizione dell'ostacolo).
	 */
	
	public void pushObstacle(Vector2 target, int robotStrenght, Vector2 coords) {
		Boolean trovato = false;
		int i = 0;
		while (!trovato && i < obstacles.size()) {
			if (obstacles.get(i).getCoords().x == target.x && obstacles.get(i).getCoords().y == target. y) {
				if (robotStrenght >= obstacles.get(i).getWeight()) {
					// Direzione in cui si muoverà l'ostacolo dopo essere stato colpito dal robot
					Vector2 direction = Vector2.sub(obstacles.get(i).getCoords(), coords);
					System.out.println(direction.toString() + "DIREZIONE");
					Vector2 newPosition = direction.add(obstacles.get(i).getCoords());
					if (isTileFree(newPosition)) {
						releaseTile(obstacles.get(i).getCoords());
						obstacles.get(i).setCoords(newPosition);
						occupyTile(newPosition);
					}
				}				
				trovato = true;
			}
		}	
	}

	public Weapon getWeapon() {
		
		if (station.getWeapons() != null) {
			showWeapons();
			Weapon weapon = station.getWeapon(dialog.getSelected());
			station.removeWeapon(dialog.getSelected());
			return weapon;
		}
	//	reference.emptyWeapons();
		return null;
		
		//return station.getWeapon(dialog.getSelected());
		
		
	}
	
	public UsableTool getTool() {
		if (station.getTools() != null) {
			showTools();
			UsableTool tool = station.getTool(dialog.getSelected());
			station.removeTool(dialog.getSelected());
			return tool;
		}
		return null;
	}
	
	public void showTools() {
		this.dialog.showTools(station.getTools());
	}
	
	public void showWeapons() {
		this.dialog.showWeapons(station.getWeapons());
	}
	
	private ArrayList<Obstacle> obstacles;
	private Tile[][] tileSet;
	private GameManager reference;
	private Station station;
	private ToolsDialog dialog;

}

/*
 * Vedere come fare per far uscire il proiettile sullo schermo, at a later time, quando farò per bene la parte del rendering
 * public void addProjectile(Bullet projectile) {
	actors.add(projectile);
	//this.add(projectile.getSprite());
}*/
