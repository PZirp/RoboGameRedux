package robotgameredux.actors;

import java.util.ArrayList;

import robotgameredux.core.Vector2;
import robotgameredux.graphic.StationSprite;
import robotgameredux.tools.UsableTool;
import robotgameredux.weapons.Weapon;

public class Station extends GameObject{

	public Station(Vector2 coords) {
		super(coords);
		tools = new ArrayList<UsableTool>();
		weapons = new ArrayList<Weapon>();
		energyReserve = 10000;
	}

	public void addObject(UsableTool o) { 
		tools.add(o);
	}
	
	public UsableTool getObject(int i) {
		return tools.get(i);
	}
	
	public void setSprite(StationSprite sprite) {
		this.sprite = sprite;
	}	

	public void render() {
		System.out.println("Posizione della stazione: " + this.getCoords().toString());
		sprite.update();
	}
	
	public StationSprite getSprite() {
		return sprite;
	}
	
	public Integer recharge() {
		if (energyReserve > 0) {
			energyReserve = energyReserve - 100;
			return 100;
		}
		return null;
	}
	
	public ArrayList<UsableTool> getObjects() {
		return tools;
	}
	
	public Weapon getWeapon(int i) {
		return weapons.get(i);
	}
	
	public ArrayList<Weapon> getWeapons() {
		return weapons;
	}
	
	private int energyReserve;
	private ArrayList<UsableTool> tools;
	private ArrayList<Weapon> weapons;

	private StationSprite sprite;

}
