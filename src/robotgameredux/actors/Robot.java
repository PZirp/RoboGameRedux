package robotgameredux.actors;

import robotgameredux.core.Vector2;
import robotgameredux.graphic.Visual;
import robotgameredux.input.Faction;
import robotgameredux.input.RobotStates;
import robotgameredux.weapons.Projectile;
import robotgameredux.weapons.Weapon;

import java.util.ArrayList;

import robotgameredux.core.GameWorld;

public class Robot extends GameObject {
//Bisogna passare su la sprite appena creata
	public Robot(RobotController reference){
		this.reference = reference;
		sprite = new Visual(this);
		this.reference.addRobotToScreen(sprite); //Passa la sprite al controllore che comunica con il GameWorld (JPanel) e la aggiunge sullo schermo
		this.state = RobotStates.INACTIVE;
		this.weapons = new ArrayList<Weapon>();
	}
	
	public void render() {
		if (this.getCoords() == oldPos) {
			System.out.print("Tile scelta occupata ");
		}		
		else if(energy == 0) {
			System.out.print("Energia insufficiente ");
		}
		System.out.println("Posizione: X = " + this.getCoords().x + " Y = " + this.getCoords().y);
		System.out.println("Salute del robot: " + health);
		sprite.update();
	}
	
	public void update() {
		if (state != RobotStates.INACTIVE) {
			if (state == RobotStates.MOVING) {
				move(this.dest);
			} else if (state == RobotStates.ATTACKING){
				System.out.println("BANZAIIIIIIIIIIIIII");
				if (target != null) {
					attack();
				}
			}
		}
	}
	
	private void movementComplete(Vector2 oldPos) {
		this.dest = null;
		this.state = RobotStates.INACTIVE;
		this.reference.updateMap(oldPos, this.getCoords());
	}
		
	private void move(Vector2 dest) {
		//Oldpos deve diventare variabile interna, non c'è bisogno di tenerla come variabile di istanza (per adesso è di istanza solo per la stampa). Oppure si?
		if (energy != 0) { 
			if(dest.dst(this.getCoords()) < range) {
				oldPos = this.getCoords();
				//Da spostare nel controller?
				this.setCoords(dest);
				energy--;
				movementComplete(oldPos);
			} else {
				System.out.println("Movimento impossibile, supera il range");
			}
			this.setState(RobotStates.INACTIVE);
		}
	}
	
	private void attack() { 
		if (energy != 0) {
			if(activeWeapon.hasBullets()) {
				System.out.println("BUDI");
				Projectile proj = activeWeapon.fire(target);
				reference.addProjectileToWorld(proj);
				this.state = RobotStates.INACTIVE;
				this.target = null;
			}
			
		}
	}
	

	public void addWeapon(Weapon weapon) {
		this.weapons.add(weapon);
	}
	
	public void setActiveWeapon() {
		this.activeWeapon = this.weapons.get(0);
		System.out.println("Yo");
	}
	
	public void setDest(Vector2 dest) {
		this.dest = dest;
	}
	
	public void setState(RobotStates state) {
		this.state = state;
	}
	
	public RobotStates getState() {
		return this.state;
	}
	
	public Visual getSprite(){
		return sprite;
	}
	
	public int getHealth() {
		return this.health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public Faction getFaction() {
		return this.faction;
	}
	
	public void setTarget(Vector2 target) {
		this.target = target;
	}
	
	private Faction faction;
	private RobotController reference;
	private RobotStates state;
	private int health = 100;
	//private int actionPoints = 2;
	private int range = 5;
	private int strenght = 10;
	private int energy = 100;
	private Vector2 oldPos;
	private Vector2 dest;
	private Vector2 target;
	private Visual sprite;
	private ArrayList<Weapon> weapons;
	private Weapon activeWeapon;
}

