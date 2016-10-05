package robotgameredux.gameobjects;

import java.util.ArrayList;

import robotgameredux.core.Coordinates;
import robotgameredux.systemInterfaces.InteractionSystem;
import robotgameredux.systemInterfaces.MovementSystem;
import robotgameredux.systemInterfaces.SupportSystem;
import robotgameredux.tools.UsableTool;

public class SupportRobot extends Actor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6334439551075353024L;

	public SupportRobot(Coordinates coords, MovementSystem ms, SupportSystem sm, InteractionSystem is) {
		super(coords, ms);
		this.tools = new ArrayList<UsableTool>();
		this.supportSystem = sm;
		this.interactionSystem = is;
	}


	/**
	 * Aggiunge il tool specificato come parametro al robot.
	 * 
	 * @param tool
	 *            il tool da aggiungere
	 */

	public void addTool(UsableTool tool) {
		tools.add(tool); // fare add(tool.clone())
	}

	/**
	 * Ritorna una copia dell'array dei tool del robot
	 * 
	 * @return l'array dei tool (clone)
	 */

	public ArrayList<UsableTool> getTools() {
		return tools; // ritornare un clone
	}

	/**
	 * Ritorna il tool specificato dall'indice del parametro
	 * 
	 * @param i
	 *            l'indice del tool nell'array
	 * @return il tool scelto
	 */

	public UsableTool getActiveTool(Integer i) {
		return tools.get(i);
	}

	/**
	 * Rimuove il tool usato specificato
	 * 
	 * @param tool
	 *            da rimuovere
	 */

	public void removeUsedTool(UsableTool tool) {
		this.tools.remove(tool);
	}

	/**
	 * Ritorna un riferimento al sistema di supporto di una istanza del robot di
	 * supporto
	 * 
	 * @return il sistema di supporto
	 */

	public SupportSystem getSupportSystem() {
		return supportSystem;
	}

	/**
	 * Ritorna un riferimento al sistema di interazione usato dall'istanza del
	 * robot di supporto
	 * 
	 * @return il sistema di interazione usato
	 */

	public InteractionSystem getInteractionSystem() {
		return interactionSystem;
	}

	@Override
	public String toString() {
		return super.toString() + " [Tools = " + tools.toString() + "]";
	}

	@Override
	public boolean equals(Object otherObject) {
		if (!super.equals(otherObject))
			return false;
		SupportRobot other = (SupportRobot) otherObject;
		return tools.equals(other.tools) && supportSystem == other.supportSystem
				&& interactionSystem == other.interactionSystem;
	}

	@Override
	public SupportRobot clone() {
		SupportRobot clone = (SupportRobot) super.clone();
		ArrayList<UsableTool> cloneT = new ArrayList<>();
		for (UsableTool t : tools) {
			cloneT.add(t.clone());
		}
		clone.tools = cloneT;
		clone.supportSystem = supportSystem;
		clone.interactionSystem = interactionSystem;
		return clone;
	}

	private ArrayList<UsableTool> tools;
	private SupportSystem supportSystem;
	private InteractionSystem interactionSystem;

}
