package robotgameredux.weapons;

import robotgameredux.TargetInterfaces.TargetInterface;

public interface IBullet {

	public <T> void hit(TargetInterface<T> target);

}
