package robotgameredux.core;

import java.util.ArrayList;

import robotgameredux.actors.Obstacle;
import robotgameredux.tools.UsableTool;
import robotgameredux.weapons.Weapon;

public interface IGameWorld {

	void releaseTile(Coordinates tile);
	void occupyTile(Coordinates tile);
	boolean isTileFree(Coordinates tile);
	ArrayList<Coordinates> pathfind(Coordinates origin, int range);
	void disablePath(ArrayList<Coordinates> path);
	public void highlightPath(Coordinates origin, int range);
	
	public void randomMap();
	public void createObstacle(Coordinates position);
	public void createStation(Coordinates position);
	
	public Obstacle isObstacle(Coordinates target);
	public Boolean destroyObstacle(Coordinates target, int robotStrenght);
	public Boolean pushObstacle(Coordinates target, int robotStrenght, Coordinates coords);
	public boolean isStation(Coordinates position);
	public Weapon getWeapon();
	public UsableTool getTool();
	public Integer recharge();
	
}
