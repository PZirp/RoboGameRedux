package robotgameredux.core;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import robotgameredux.actors.*;
import robotgameredux.graphic.ObstacleSprite;
import robotgameredux.graphic.StationSprite;
import robotgameredux.graphic.TileSprite;
import robotgameredux.graphic.WallSprite;
import robotgameredux.tools.HealthPack;
import robotgameredux.tools.ToolsDialog;
import robotgameredux.tools.UsableTool;
import robotgameredux.weapons.Weapon;

public class GameWorld implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -2734801533533490919L;
	
	public final static int GRID_LENGHT = 1280/64; //20
	public final static int GRID_HEIGHT = 720/64; //11
	
	public GameWorld(GameManager reference) {
		this.reference = reference;
		tileSet = new Tile[GRID_LENGHT][GRID_HEIGHT];
		obstacles = new ArrayList<Obstacle>();
		this.initWorld();
		this.dialog = new ToolsDialog(null, true);
		
	}
	
	private void initWorld() {
		
		int a = 0;
		for (int j = 0; j < GRID_HEIGHT; j++) {
				 tileSet[a][j] = new Tile();
				 WallSprite s = new WallSprite(tileSet[a][j], a, j);
				 reference.add(s, 0);
				 tileSet[a][j].setSprite(s);
				 tileSet[a][j].setCalpestabile(false);
		}		
		for (int j = 0; j < GRID_LENGHT; j++) {
			 tileSet[j][a] = new Tile();
			 WallSprite s = new WallSprite(tileSet[j][a], j, a);
			 reference.add(s, 0);
			 tileSet[j][a].setSprite(s);
			 tileSet[j][a].setCalpestabile(false);
		}		
		for (int i = 1; i < GRID_LENGHT; i++) {
			for (int j = 1; j < GRID_HEIGHT; j++) {
				 tileSet[i][j] = new Tile();				 
				 if (i == GRID_LENGHT-1) {
					 WallSprite s = new WallSprite(tileSet[i][j], i, j);
					 reference.add(s, 0);
					 tileSet[i][j].setSprite(s);
					 tileSet[i][j].setCalpestabile(false);
				 } else if (j == GRID_HEIGHT-1) {
					 WallSprite s = new WallSprite(tileSet[i][j], i, j);
					 reference.add(s, 0);
					 tileSet[i][j].setSprite(s);
					 tileSet[i][j].setCalpestabile(false);
				 } else {
				 TileSprite s = new TileSprite(tileSet[i][j], i, j);
				 reference.add(s, 0);
				 tileSet[i][j].setSprite(s);
				 }
			}
		}
		
	}
	
	public void render() {

		for (int i = 0; i < GRID_LENGHT; i++) {
			for (int j = 0; j < GRID_HEIGHT; j++) {
				tileSet[i][j].render();
			}
		}
		
	}
	
	public Tile[][] getTileSet(){
		return tileSet;
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
		if (position.equals(station.getCoords())) {
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
		
		if (tile.getX() >= GRID_LENGHT-1 || tile.getY() >= GRID_HEIGHT-1 || tile.getX() < 1 || tile.getY() < 1) {
			return false;
		}
		
		if (tileSet[tile.getX()][tile.getY()].isCalpestabile() == true)
			return true;
		else 
			return false;
	}
	
	public void releaseTile(Vector2 tile) {
		tileSet[tile.getX()][tile.getY()].setCalpestabile(true);
	}
	
	public void occupyTile(Vector2 tile) {
		tileSet[tile.getX()][tile.getY()].setCalpestabile(false);
	}
	
	public Obstacle isObstacle(Vector2 target) {
		for (Obstacle obs : obstacles) {
			if (target.equals(obs.getCoords())) {
				return obs;
			}
		}
		return null;
	}
	
	public Boolean destroyObstacle(Vector2 target, int robotStrenght) {
		Obstacle o = isObstacle(target);
		if (o != null && robotStrenght > o.getResistence()) {
			System.out.println("FACCIO PULIZIA");
			reference.remove(o.getSprite());
			releaseTile(o.getCoords());
			obstacles.remove(o);
			return true;
		}
		return false;
	}
	
	/* Cosa fa: Calcolo la direzione in cui devo muovermi (direction) e la sommo alla posizone attuale. 
	 * Se la nuova posizitione è libera, sposto l'ostacolo.
	 * In questo caso basta calcolare la direzione in cui viene spinto, dato che la posizione del robo che spinge non è importante
	 * e l'ostacolo spinto si muove solo di una casella, posso quindi direttamente sommare il risultato per ottenere la nuova posizione.
	 * E avessi voluto, per esempio, orientare l'ostacolo verso il robot che spinge, avrei dovuto calcolare la posizione relativa del robot
	 * rispetto all'ostacolo (sottraendo la posizione del robot alla posizione dell'ostacolo).
	 */
	
	public Boolean pushObstacle(Vector2 target, int robotStrenght, Vector2 coords) {
		Obstacle o = isObstacle(target);
		if (o != null && robotStrenght > o.getWeight()) {
			// Direzione in cui si muoverà l'ostacolo dopo essere stato colpito dal robot
			Vector2 direction = Vector2.sub(o.getCoords(), coords);
			System.out.println(direction.toString() + "DIREZIONE");
			Vector2 newPosition = direction.add(o.getCoords());
			if (isTileFree(newPosition)) {
				releaseTile(o.getCoords());
				o.setCoords(newPosition);
				occupyTile(newPosition);
				return true;
			}
			return false;
		}
		return false;
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
	
	public ArrayList<Vector2> pathfind(Vector2 origin, int range) {
		ArrayList<Vector2> path = new ArrayList<Vector2>();

		for (int i = 0; i < range; i++) {
			if (origin.getY() + i < GRID_HEIGHT-1) {
				path.add(new Vector2(origin.getX(), origin.getY() + i));
			}
			if (origin.getY() - i >= 1) {
				path.add(new Vector2(origin.getX(), origin.getY() - i));
			}
			if (origin.getX() + i < GRID_LENGHT-1) {
				path.add(new Vector2(origin.getX() + i, origin.getY()));
			}
			if (origin.getX() - i >= 1) {
				path.add(new Vector2(origin.getX() - i, origin.getY()));
			}
		}
		
		return path;
	}
	
	public void highlightPath(Vector2 origin, int range, Boolean b) {
		ArrayList<Vector2> path = pathfind(origin, range);
		for (Vector2 v : path) {
			tileSet[v.getX()][v.getY()].setActive(b);
		}
		
		/*tileSet[origin.getX()][origin.getY()].setActive(b);
		for (int i = 0; i < range; i++) {
			if (origin.getY()+i < GRID_HEIGHT) {
				tileSet[origin.getX()][origin.getY()+i].setActive(b);
			}
			if (origin.getY()-i >= 0) {
				tileSet[origin.getX()][origin.getY()-i].setActive(b);
			}
			if (origin.getX()+i < GRID_LENGHT) {
				tileSet[origin.getX()+i][origin.getY()].setActive(b);
			}
			if (origin.getX()-i >= 0) {
				tileSet[origin.getX()-i][origin.getY()].setActive(b);
			}
		}*/
		//reference.repaint();
	}

	public void highlightPath() {
		for (int i = 1; i < GRID_LENGHT-1; i++) {
			for (int j = 1; j < GRID_HEIGHT-1; j++) {
				tileSet[i][j].setActive(false);;
			}
		}
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
