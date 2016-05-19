package robotgameredux.weapons;

import robotgameredux.actors.Robot;

public class Bullet{
	
	public Bullet(int damage) {
		this.dmg = damage;
	}
		
	public void hit(Robot robot) {
		robot.damage(dmg);
	}
	
	private int dmg;
}
