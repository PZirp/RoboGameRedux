package robotgameredux.weapons;

import java.io.Serializable;

import robotgameredux.TargetInterfaces.TargetInterface;

public class ShieldBullet implements Serializable, IBullet {

	public ShieldBullet(int defense) {
		this.defense = defense;
	}
	
	@Override
	public <T> void hit(TargetInterface<T> target) {
		target.setDefense(this.defense);
	}
	
	private int defense;

}
