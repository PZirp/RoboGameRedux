package robotgameredux.gameobjects;

import java.util.ArrayList;

import robotgameredux.core.Coordinates;
import robotgameredux.systemInterfaces.InteractionSystem;
import robotgameredux.systemInterfaces.BattleSystem;
import robotgameredux.systemInterfaces.MovementSystem;
import robotgameredux.systemsImplementations.StandardBattleSystem;
import robotgameredux.weapons.Weapon;

public class AttackRobot extends Actor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 649485930721818531L;

	public AttackRobot(Coordinates coords, StandardBattleSystem bs, MovementSystem ms, InteractionSystem is) {
		super(coords, ms);
		this.weapons = new ArrayList<Weapon>();
		this.battleSystem = bs;
		this.interactionSystem = is;

	}

	/**
	 * Aggiunge l'arma specificata come parametro al robot. Se l'arma è già
	 * presente nell'array del robot, vengono aggiunti i proiettili all'arma.
	 * 
	 * @param w2
	 *            l'arma da aggiungere
	 */

	public void addWeapon(Weapon w2) {

		for (Weapon w : weapons) {
			if (w.isSameWeapon(w2)) {
				w.addBullets(w2.getBulletCount());
				return;
			}
		}

		this.weapons.add(w2);
	}

	/**
	 * Ritorna l'arma specificata dall'indice del parametro
	 * 
	 * @param i
	 *            l'indice dell'arma nell'array
	 * @return l'arma scelta
	 */

	public Weapon getActiveWeapon(Integer i) {
		return weapons.get(i);
	}

	/**
	 * Ritorna una copia dell'array delle armi del robot
	 * 
	 * @return l'array delle armi (clone)
	 */

	public ArrayList<Weapon> getWeapons() {
		ArrayList<Weapon> clone = new ArrayList<>();
		for (Weapon w : weapons) {
			clone.add(w.clone());
		}
		return clone;
	}

	/**
	 * Ritorna un riferimento al sistema di combattimento usato dall'istanza del
	 * robot di attacco
	 * 
	 * @return il sistema di combattimento usato
	 */

	public BattleSystem getBattleSystem() {
		return battleSystem;
	}

	/**
	 * Ritorna un riferimento al sistema di interazione usato dall'istanza del
	 * robot di attacco
	 * 
	 * @return il sistema di interazione usato
	 */

	public InteractionSystem getInteractionSystem() {
		return interactionSystem;
	}

	@Override
	public String toString() {
		return super.toString() + " [Weapon = " + weapons.toString() + "]";
	}

	@Override
	public boolean equals(Object otherObject) {
		if (!super.equals(otherObject))
			return false;
		AttackRobot other = (AttackRobot) otherObject;
		// Anche qui, aggiungere gli equals per i sistemi
		return weapons.equals(other.weapons) && battleSystem == other.battleSystem
				&& interactionSystem == other.interactionSystem;
	}

	@Override
	public AttackRobot clone() {
		AttackRobot clone = (AttackRobot) super.clone();
		ArrayList<Weapon> cloneW = new ArrayList<>();
		for (Weapon w : weapons) {
			cloneW.add(w.clone());
		}
		clone.weapons = cloneW;
		clone.interactionSystem = interactionSystem;
		clone.battleSystem = battleSystem;
		return clone;
	}

	private InteractionSystem interactionSystem;
	private BattleSystem battleSystem;
	private ArrayList<Weapon> weapons;

}
