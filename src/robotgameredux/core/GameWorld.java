package robotgameredux.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import robotgameredux.gameobjects.Obstacle;
import robotgameredux.gameobjects.Station;
import robotgameredux.graphic.ObstacleSprite;
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

	/**
	 * Inizializza la mappa. La mappa è mantenuta in un array bidimensionale di
	 * oggetti Tile. Gli indici dell'array fungono da coordinate. Le tile
	 * internet sono settate libere, ed ad ogni tile è assegnata una sprite.
	 * Ogni tile sul bordo della mappa è settata "occupata", e gli viene
	 * assegnata una sprite diversa.
	 */

	private void initWorld() {
		for (int i = 0; i < GRID_LENGHT; i++) {
			for (int j = 0; j < GRID_HEIGHT; j++) {
				tileSet[i][j] = new Tile();
				if (i == GRID_LENGHT - 1 || i == 0 || j == GRID_HEIGHT - 1 || j == 0) {
					WallSprite s = new WallSprite(tileSet[i][j], i, j);
					this.reference.addToScreen(s, 0);
					tileSet[i][j].setSprite(s);
					tileSet[i][j].setOccupied(false);
				} else {
					TileSprite s = new TileSprite(tileSet[i][j], i, j);
					this.reference.addToScreen(s, 0);
					tileSet[i][j].setSprite(s);
				}
			}
		}
	}

	/**
	 * Metodo molto semplice per generare casualmente una mappa. Scorre l'array
	 * di tutte le tile e in maniera pseudo-casuale crea ostacoli sul terreno.
	 */

	@Override
	public void randomMap() {
		createStation(new Coordinates(10, 5));
		Random rand = new Random();
		for (int i = 0; i < GRID_LENGHT - 1; i++) {
			for (int j = 0; j < GRID_HEIGHT - 1; j++) {
				if (tileSet[i][j].isOccupied() == true) {
					if ((rand.nextInt(10)) < 2) {
						createObstacle(new Coordinates(i, j));
					}
				}
			}
		}
	}

	/**
	 * Crea un ostacolo alla posizione specificata. La tile su cui viene creato
	 * diventa occupata, in più aggiunge la sprite dell'ostacolo allo schermo.
	 * 
	 * @param le
	 *            coordinate dell'ostacolo
	 */

	@Override
	public void createObstacle(Coordinates position) {
		Obstacle o = new Obstacle(position);
		ObstacleSprite op = new ObstacleSprite(o);
		o.setSprite(op);
		occupyTile(position);
		obstacles.add(o);
		this.reference.addToScreen(op, 1);
	}

	/**
	 * Crea una stazione alla posizione specificata. La tile su cui viene creata
	 * diventa occupata, in più aggiunge la sprite della stazione allo schermo e
	 * inizializza la stazione con alcuni UsableTools e Weapon standard.
	 * 
	 * @param le
	 *            coordinate della stazione
	 */

	@Override
	public void createStation(Coordinates position) {
		station = new Station(position);
		StationSprite sp = new StationSprite(station);
		station.setSprite(sp);
		occupyTile(position);
		station.addPropertyChangeListener(this);
		this.reference.addToScreen(sp, 1);
		station.addObject(new HealthPack());
		station.addObject(new HealthPack());
		station.addObject(new HealthPack());
		station.addObject(new EnergyPack());
		station.addObject(new EnergyPack());
		station.addObject(new EnergyPack());
		station.addWeapon(new Pistol());
		station.addWeapon(new Pistol());
		station.addWeapon(new Pistol());
	}

	/*
	 * Metodi per le tiles
	 */

	/**
	 * Controlla se la tile selezionata è occupata. Nel caso sia una tile di
	 * delimitazione (quindi si trova sul confine della mappa), è
	 * automaticamente occupata.
	 * 
	 * @param le
	 *            coordinate della tile da controllare
	 * @return true se la tile è libera, false altrimenti
	 */

	@Override
	public boolean isTileFree(Coordinates tile) {

		if (tile.getX() >= GRID_LENGHT - 1 || tile.getY() >= GRID_HEIGHT - 1 || tile.getX() < 1 || tile.getY() < 1) {
			return false;
		}

		if (tileSet[tile.getX()][tile.getY()].isOccupied() == true)
			return true;
		else
			return false;
	}

	/**
	 * Libera la tile selezionata
	 * 
	 * @param Le
	 *            coordinate della tile da liberare
	 */

	@Override
	public void releaseTile(Coordinates tile) {
		tileSet[tile.getX()][tile.getY()].setOccupied(true);
	}

	/**
	 * Occupa la tile selezionata
	 * 
	 * @param Le
	 *            coordinate della tile da occupare
	 */

	@Override
	public void occupyTile(Coordinates tile) {
		tileSet[tile.getX()][tile.getY()].setOccupied(false);
	}

	/*
	 * Metodi relativi agli ostacoli
	 */

	/**
	 * Controlla se il target selezionato è un ostacolo o meno.
	 * 
	 * @param Le
	 *            coordinate da controllare
	 * @return true se è un ostacolo, false altrimenti
	 */

	@Override
	public Boolean isObstacle(Coordinates target) {
		for (Obstacle obs : obstacles) {
			if (target.equals(obs.getCoords())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Distrugge l'ostacolo indicato. Se le coordinate indicate puntano ad un
	 * ostacolo e la forza del robot è sufficiente (forza > resistenza),
	 * l'ostacolo è distrutto, altrimenti l'azione fallisce. La tile su cui si
	 * trovava l'ostacolo viene liberata. Se viene distrutto l'ultimo ostacolo
	 * presente sulla mappa, viene generato un PropertyChangeEvent.
	 * 
	 * @param le
	 *            coordinate scelte
	 * @param la
	 *            forza del robot che sta attacando
	 * @return true se l'ostacolo viene distrutto, falso altrimenti
	 */

	@Override
	public Boolean destroyObstacle(Coordinates target, int robotStrenght) {
		for (Obstacle obs : obstacles) {
			if (target.equals(obs.getCoords()) && robotStrenght > obs.getResistence()) {
				reference.removeFromScreen(obs.getSprite());
				releaseTile(obs.getCoords());
				obstacles.remove(obs);
				if (obstacles.isEmpty()) {
					propertyChange.firePropertyChange("NO_MORE_OBSTACLES", null, null);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Sposta l'ostacolo indicato. Se le coordinate indicate puntano ad un
	 * ostacolo e la forza del robot è sufficiente (forza > resistenza), si
	 * calcola la direzione in cui l'ostacolo deve muoversi. (La formula per
	 * calcolare la direzione è coordinate dell'ostacolo - coordinate del robot
	 * che spinge. La nuova posizione si trova sommando il risultato della
	 * sottrazione alla vecchia posizione dell'ostacolo). La tile precedente
	 * viene liberata, e la nuova occupata. Se l'ostacolo viene spinto verso una
	 * tile occupata, l'azione fallisce.
	 * 
	 * @param le
	 *            coordinate dell'ostacolo
	 * @param la
	 *            forza del robot che sta attacando
	 * @param le
	 *            coordinate in cui si trova il robot
	 * @return true se l'ostacolo viene spostato, falso altrimenti
	 */

	/*
	 * Cosa fa: Calcolo la direzione in cui devo muovermi (direction) e la sommo
	 * alla posizone attuale. Se la nuova posizitione è libera, sposto
	 * l'ostacolo. In questo caso basta calcolare la direzione in cui viene
	 * spinto, dato che la posizione del robo che spinge non è importante e
	 * l'ostacolo spinto si muove solo di una casella, posso quindi direttamente
	 * sommare il risultato per ottenere la nuova posizione. E avessi voluto,
	 * per esempio, orientare l'ostacolo verso il robot che spinge, avrei dovuto
	 * calcolare la posizione relativa del robot rispetto all'ostacolo
	 * (sottraendo la posizione del robot alla posizione dell'ostacolo).
	 */

	@Override
	public Boolean pushObstacle(Coordinates target, int robotStrenght, Coordinates coords) {
		for (Obstacle obs : obstacles) {
			if (target.equals(obs.getCoords()) && robotStrenght > obs.getWeight()) {
				Coordinates direction = obs.getCoords().sub(coords); // Direzione
																		// in
																		// cui
																		// si
																		// muoverà
																		// l'ostacolo
																		// dopo
																		// essere
																		// stato
																		// colpito
																		// dal
																		// robot
				Coordinates newPosition = direction.add(obs.getCoords()); // La
																			// nuova
																			// posizione
																			// dell'ostacolo
				if (isTileFree(newPosition)) {
					releaseTile(obs.getCoords());
					obs.setCoords(newPosition);
					occupyTile(newPosition);
					return true;
				}
				return false;
			}
		}
		return false;

	}

	/*
	 * Metodi relativi alla stazione di rifornimento
	 */

	/**
	 * Controlla se le coordinate scelte corrispondono ad una stazione
	 * 
	 * @param le
	 *            coordinate
	 * @return true se è una stazione, false altrimenti
	 */
	@Override
	public boolean isStation(Coordinates position) {
		if (position.equals(station.getCoords())) {
			return true;
		}
		return false;
	}

	/**
	 * Metodo utilizzato dagli attori per interagire con l'ambiente. Aggiunge
	 * l'arma selezionata dalla stazione all'attore che vi interagisce. L'arma
	 * viene rimossa dalla stazione, ed aggiunta a quelle disponibili per
	 * l'attore. Monstra un dialogo per la selezione dell'arma.
	 * 
	 * @return l'arma scelta, null altrimenti
	 */
	@Override
	public Weapon getWeapon() {
		if (station.getWeapons() != null) {
			this.dialog.showWeapons(station.getWeapons());
			int selection = dialog.getSelected();
			if (selection > -1) {
				Weapon weapon = station.getWeapon(selection);
				station.removeWeapon(selection);
				return weapon;
			}
		}
		return null;
	}

	/**
	 * Metodo utilizzato dagli attori per interagire con l'ambiente. Aggiunge
	 * l'oggetto selezionato dalla stazione all'attore che vi interagisce.
	 * L'oggetto viene rimosso dalla stazione, ed aggiunto a quelli disponibili
	 * per l'attore. Monstra un dialogo per la selezione dell'oggetto.
	 * 
	 * @return l'oggetto scelto, null altrimenti
	 */

	@Override
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

	/**
	 * Metodo utilizzato dagli attori per interagire con l'ambiente. Ricarica
	 * l'energia dell'attore che vi interagisce.
	 * 
	 * @return la quantità di energia da ricaricare
	 */

	@Override
	public Integer recharge() {
		return station.recharge();
	}

	/*
	 * Metodi di pathfinding
	 */

	/**
	 * Metodo per trovare le tile raggiungibili da un attore. Calcola i path
	 * possibili su tutte e quattro le direzioni di movimento. Si ferma quando
	 * raggiunge il bordo dell'ambiente o quando è al massimo del range del
	 * robot
	 * 
	 * @param le
	 *            coordinate di partenza dell'attore
	 * @param range
	 *            il range di movimento massimo dell'attore
	 * @return arraylist di tile raggiungibili
	 */

	@Override
	public ArrayList<Coordinates> pathfind(Coordinates origin, int range) {
		ArrayList<Coordinates> path = new ArrayList<Coordinates>();
		for (int i = 0; i < range; i++) {
			if (origin.getY() + i < GRID_HEIGHT - 1) {
				path.add(new Coordinates(origin.getX(), origin.getY() + i));
			}
			if (origin.getY() - i >= 1) {
				path.add(new Coordinates(origin.getX(), origin.getY() - i));
			}
			if (origin.getX() + i < GRID_LENGHT - 1) {
				path.add(new Coordinates(origin.getX() + i, origin.getY()));
			}
			if (origin.getX() - i >= 1) {
				path.add(new Coordinates(origin.getX() - i, origin.getY()));
			}
		}
		return path;
	}

	/**
	 * Evidenzia i possibili path disponbili per un attore
	 * 
	 * @param le
	 *            coordinate dell'attore
	 * @param il
	 *            range massimo di movimento
	 */

	@Override
	public void highlightPath(Coordinates origin, int range) {
		ArrayList<Coordinates> path = pathfind(origin, range);
		for (Coordinates v : path) {
			tileSet[v.getX()][v.getY()].setActive(true);
		}

		/*
		 * tileSet[origin.getX()][origin.getY()].setActive(b); for (int i = 0; i
		 * < range; i++) { if (origin.getY()+i < GRID_HEIGHT) {
		 * tileSet[origin.getX()][origin.getY()+i].setActive(b); } if
		 * (origin.getY()-i >= 0) {
		 * tileSet[origin.getX()][origin.getY()-i].setActive(b); } if
		 * (origin.getX()+i < GRID_LENGHT) {
		 * tileSet[origin.getX()+i][origin.getY()].setActive(b); } if
		 * (origin.getX()-i >= 0) {
		 * tileSet[origin.getX()-i][origin.getY()].setActive(b); } }
		 */
		// reference.repaint();
	}

	/**
	 * Disattiva le caselle evidenziate
	 * 
	 * @param ArrayList
	 *            di caselle da disattivare
	 */

	@Override
	public void disablePath(ArrayList<Coordinates> path) {
		for (Coordinates t : path) {
			tileSet[t.getX()][t.getY()].setActive(false);
		}
	}

	/*
	 * Metodi relativi alla serializzazione
	 */

	/**
	 * Oltre a deserializzare l'oggeto, il metodo associa una nuova sprite ad
	 * ogni elemento letto.
	 * 
	 * @param inputStream
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */

	private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
		inputStream.defaultReadObject();

		for (int i = 0; i < GRID_LENGHT; i++) {
			for (int j = 0; j < GRID_HEIGHT; j++) {
				if (i == GRID_LENGHT - 1 || i == 0 || j == GRID_HEIGHT - 1 || j == 0) {
					WallSprite s = new WallSprite(tileSet[i][j], i, j);
					tileSet[i][j].setSprite(s);
					tileSet[i][j].setOccupied(false);
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

	/**
	 * Dopo che la deserializzazione è completa, questo metodo aggiunge
	 * nuovamente le sprite allo schermo
	 */

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
