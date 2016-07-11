package robotgameredux.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import robotgameredux.actors.*;
import robotgameredux.graphic.ObstacleSprite;
import robotgameredux.graphic.Sprite;
import robotgameredux.graphic.StationSprite;
import robotgameredux.graphic.TileSprite;
import robotgameredux.graphic.WallSprite;
import robotgameredux.tools.EnergyPack;
import robotgameredux.tools.HealthPack;
import robotgameredux.tools.ToolsDialog;
import robotgameredux.tools.UsableTool;
import robotgameredux.weapons.Pistol;
import robotgameredux.weapons.Weapon;

public class GameWorld implements Serializable, PropertyChangeListener, IGameWorld {


	/**
	 * 
	 */
	private static final long serialVersionUID = -2734801533533490919L;
	
	public final static int GRID_LENGHT = 20;
	public final static int GRID_HEIGHT = 11;
	
	public GameWorld(GameManager reference) {
		this.reference = reference;
		tileSet = new Tile[GRID_LENGHT][GRID_HEIGHT];
		obstacles = new ArrayList<Obstacle>();
		this.initWorld();
		this.dialog = new ToolsDialog(null, true);		
		this.propertyChange = new PropertyChangeSupport(this);
	}
	
	public void render() {
		for (int i = 0; i < GRID_LENGHT; i++) {
			for (int j = 0; j < GRID_HEIGHT; j++) {
				tileSet[i][j].render();
			}
		}	
		
		for (Obstacle o : obstacles) {
			o.render();
		}
		
		station.render();
	}
	
	/*
	 * Metodi di setup e costruzione della mappa
	 */
	
	private void initWorld() {
		for (int i = 0; i < GRID_LENGHT; i++) {
			for (int j = 0; j < GRID_HEIGHT; j++) {
				tileSet[i][j] = new Tile();				 
				if (i == GRID_LENGHT-1 || i == 0 || j == GRID_HEIGHT-1 || j == 0) {
					WallSprite s = new WallSprite(tileSet[i][j], i, j);
					this.reference.addToScreen(s, 0);
					tileSet[i][j].setSprite(s);
					tileSet[i][j].setCalpestabile(false);
				} else {
					TileSprite s = new TileSprite(tileSet[i][j], i, j);
					this.reference.addToScreen(s, 0);
					tileSet[i][j].setSprite(s);
				}
			}
		}		
		
		
	}
	
	public void randomMap() {
		createStation(new Coordinates(10,5));
		Random rand = new Random();
		for (int i = 0; i < GRID_LENGHT-1; i++) {
			for (int j = 0; j < GRID_HEIGHT-1; j++) {
				if (tileSet[i][j].isCalpestabile() == true) {
					if ((rand.nextInt(10)) < 2) {
						createObstacle(new Coordinates(i,j));
					}
				}
			}
		}
	}
	
	public void createObstacle(Coordinates position) {
		Obstacle o = new Obstacle(position); 
		ObstacleSprite op = new ObstacleSprite(o);
		o.setSprite(op);
		occupyTile(position);
		obstacles.add(o);		
		this.reference.addToScreen(op, 1);
	}
	
	public void createStation(Coordinates position) {
		station = new Station(position);
		StationSprite sp = new StationSprite(station);
		station.setSprite(sp);		
		occupyTile(position);
		station.addPropertyChangeListener(this);		
		this.reference.addToScreen(sp, 1);
	}
		
	/*
	 * Metodi per le tiles
	 */
	
	public boolean isTileFree(Coordinates tile) {
		
		if (tile.getX() >= GRID_LENGHT-1 || tile.getY() >= GRID_HEIGHT-1 || tile.getX() < 1 || tile.getY() < 1) {
			return false;
		}
		
		if (tileSet[tile.getX()][tile.getY()].isCalpestabile() == true)
			return true;
		else 
			return false;
	}
	
	public void releaseTile(Coordinates tile) {
		tileSet[tile.getX()][tile.getY()].setCalpestabile(true);
	}
	
	public void occupyTile(Coordinates tile) {
		tileSet[tile.getX()][tile.getY()].setCalpestabile(false);
	}
	
	/*
	 * Metodi relativi agli ostacoli
	 */
	
	public Obstacle isObstacle(Coordinates target) {
		for (Obstacle obs : obstacles) {
			if (target.equals(obs.getCoords())) {
				return obs;
			}
		}
		return null;
	}
	
	public Boolean destroyObstacle(Coordinates target, int robotStrenght) {
		Obstacle o = isObstacle(target);
		if (o != null && robotStrenght > o.getResistence()) {
			//System.out.println("FACCIO PULIZIA");
			reference.removeFromScreen(o.getSprite());
			releaseTile(o.getCoords());
			obstacles.remove(o);
			if (obstacles.isEmpty()) {
				propertyChange.firePropertyChange("NO_MORE_OBSTACLES", null, null);
			}
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
	
	public Boolean pushObstacle(Coordinates target, int robotStrenght, Coordinates coords) {
		Obstacle o = isObstacle(target);
		if (o != null && robotStrenght > o.getWeight()) {
			// Direzione in cui si muoverà l'ostacolo dopo essere stato colpito dal robot
			Coordinates direction = o.getCoords().sub(coords);/*Coordinates.sub(o.getCoords(), coords);*/
			//System.out.println(direction.toString() + "DIREZIONE");
			Coordinates newPosition = direction.add(o.getCoords());
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

	/*
	 * Metodi relativi alla stazione di rifornimento
	 */
	
	
	public boolean isStation(Coordinates position) {
		if (position.equals(station.getCoords())) {
			return true;
		}
		return false;
	}
	
	public Weapon getWeapon() {
		
		if (station.getWeapons() != null) {
			this.dialog.showWeapons(station.getWeapons());
			JOptionPane.showMessageDialog(null, dialog.getSelected());
			int selection = dialog.getSelected();
			if (selection > -1) {
				Weapon weapon = station.getWeapon(selection);
				station.removeWeapon(selection);
				return weapon;
			}
		}
		return null;
	}
	
	public UsableTool getTool() {
		if (station.getTools() != null) {
			this.dialog.showTools(station.getTools());
			int selection = dialog.getSelected();
			if (selection > -1) {
				UsableTool tool = station.getTool(selection);
				station.removeTool(dialog.getSelected());
				return tool;
			}
			
		}
		return null;
	}
	
	public Integer recharge() {
		return station.recharge();
	}
	
	/*
	 * Metodi di pathfinding
	 */
	
	public ArrayList<Coordinates> pathfind(Coordinates origin, int range) {
		ArrayList<Coordinates> path = new ArrayList<Coordinates>();

		for (int i = 0; i < range; i++) {
			if (origin.getY() + i < GRID_HEIGHT-1) {
				path.add(new Coordinates(origin.getX(), origin.getY() + i));
			}
			if (origin.getY() - i >= 1) {
				path.add(new Coordinates(origin.getX(), origin.getY() - i));
			}
			if (origin.getX() + i < GRID_LENGHT-1) {
				path.add(new Coordinates(origin.getX() + i, origin.getY()));
			}
			if (origin.getX() - i >= 1) {
				path.add(new Coordinates(origin.getX() - i, origin.getY()));
			}
		}
		
		return path;
	}
	
	public void highlightPath(Coordinates origin, int range) {
		ArrayList<Coordinates> path = pathfind(origin, range);
		for (Coordinates v : path) {
			tileSet[v.getX()][v.getY()].setActive(true);
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

	public void disablePath(ArrayList<Coordinates> path) {
		for (Coordinates t : path) {
			tileSet[t.getX()][t.getY()].setActive(false);
		}
	}
	
	/*
	 * Metodi relativi alla serializzazione
	 */
	
	private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
		inputStream.defaultReadObject();
		
		for (int i = 0; i < GRID_LENGHT; i++) {
			for (int j = 0; j < GRID_HEIGHT; j++) {
				tileSet[i][j] = new Tile();				 
				if (i == GRID_LENGHT-1 || i == 0 || j == GRID_HEIGHT-1 || j == 0) {
					WallSprite s = new WallSprite(tileSet[i][j], i, j);
					tileSet[i][j].setSprite(s);
					tileSet[i][j].setCalpestabile(false);
				} else {
					TileSprite s = new TileSprite(tileSet[i][j], i, j);
					tileSet[i][j].setSprite(s);
				}
			}
		}
		for (Obstacle s : obstacles) {
			ObstacleSprite os = new ObstacleSprite(s);
			s.setSprite(os);
		}
		StationSprite ss = new StationSprite(station);
		station.setSprite(ss);
	}
		
	/*public void postSerialization() {
		for (int i = 0; i < GRID_LENGHT; i++) {
			for (int j = 0; j < GRID_HEIGHT; j++) {
				tileSet[i][j] = new Tile();				 
				if (i == GRID_LENGHT-1 || i == 0 || j == GRID_HEIGHT-1 || j == 0) {
					WallSprite s = new WallSprite(tileSet[i][j], i, j);
					tileSet[i][j].setSprite(s);
					tileSet[i][j].setCalpestabile(false);
				} else {
					TileSprite s = new TileSprite(tileSet[i][j], i, j);
					tileSet[i][j].setSprite(s);
				}
			}
		}
		for (Obstacle s : obstacles) {
			ObstacleSprite os = new ObstacleSprite(s);
			s.setSprite(os);
		}
		StationSprite ss = new StationSprite(station);
		station.setSprite(ss);
	}*/
	
	public void postDeserialization() {
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 11; j++) {
				reference.addToScreen(tileSet[i][j].getSprite(), 0);
			}
		}
		for (Obstacle o : obstacles) {
			reference.addToScreen(o.getSprite(), 1);
		}		
		reference.addToScreen(station.getSprite(), 1); 
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChange.addPropertyChangeListener(listener);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if (arg0.getPropertyName() == "EMPTY_TOOLS") {
			propertyChange.firePropertyChange("EMPTY_TOOLS", null, null);
		}
		if (arg0.getPropertyName() == "EMPTY_WEAPONS") {
			propertyChange.firePropertyChange("EMPTY_WEAPONS", null, null);
		}
		if (arg0.getPropertyName() == "OUT_OF_ENERGY") {
			propertyChange.firePropertyChange("OUT_OF_ENERGY", null, null);
		}
	}
	
	private PropertyChangeSupport propertyChange;
	private ArrayList<Obstacle> obstacles;
	private Tile[][] tileSet;
	private GameManager reference;
	private Station station;
	private ToolsDialog dialog;


}
