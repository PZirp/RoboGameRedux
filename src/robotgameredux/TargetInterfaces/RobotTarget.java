package robotgameredux.TargetInterfaces;

import robotgameredux.enums.Faction;
import robotgameredux.gameobjects.Actor;

/**
 * Classe che rappresenta una attore come obiettivo per un'azione
 * @author Paolo Zirpoli
 *
 */

public class RobotTarget implements TargetInterface<Actor> {

	public RobotTarget(Actor robot) {
		this.robot = robot;
	}

	/**
	 * Ritorna la salute del robot bersaglio
	 * @return health
	 * 			la salute
	 */
	
	@Override
	public int getHealth() {
		return robot.getHealth();
	}


	/**
	 * Ritorna la difesa del robot bersaglio
	 * @return difesa
	 * 			la difesa
	 */
	
	@Override
	public int getDefense() {
		return robot.getDefense();
	}
	

	/**
	 * Aggiorna la difesa del robot bersaglio
	 * @param defense 
	 * 		la nuova difesa
	 */

	@Override
	public void setDefense(int defense) {
		robot.setDefense(defense);
	}


	/**
	 * Applica il danno al robot
	 * @param damage 
	 * 		il danno 
	 */
	
	@Override
	public void applyDamage(int damage) {
		robot.damage(damage);
	}


	/**
	 * Ritorna la fazione di appartenenza del bersaglio
	 * @return Faction 
	 * 			la fazione
	 */
	
	@Override
	public Faction getFaction() {
		return robot.getFaction();
	}

	/**
	 * Ritorna l'energia del robot bersaglio
	 * @return energy
	 * 			l'energia
	 */
	
	@Override
	public int getEnergy() {
		return robot.getEnergy();
	}

	/**
	 * Aggiunge la quantità di energia indicata dal parametro all'attore
	 * @param energy 
	 * 			l'energia da aggiungere
	 */
	
	@Override
	public void addEnergy(int energy) {
		robot.addEnergy(energy);
	}
	
	/**
	 * Aggiunge la quantità di salute indicata dal parametro all'attore
	 * @param health
	 * 			la salute da aggiungere
	 */
	
	@Override
	public void heal(int health) {
		robot.heal(health);
	}

	Actor robot;

}
