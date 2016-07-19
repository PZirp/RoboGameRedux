package robotgameredux.systemsImplementations;

import java.io.Serializable;

import robotgameredux.CommandsInterfaces.SupportCommandInterface;
import robotgameredux.TargetInterfaces.TargetInterface;
import robotgameredux.core.Coordinates;
import robotgameredux.core.IActorManager;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;
import robotgameredux.systemInterfaces.SupportSystem;
import robotgameredux.tools.UsableTool;

public class StandardSupportSystem implements SupportSystem, Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 730914482322382664L;

	public StandardSupportSystem(IActorManager rf) {
		this.actorsManager = rf;
	}

	@Override
	public <T> Boolean execute(SupportCommandInterface<T> command)
			throws InvalidTargetException, InsufficientEnergyException {
		Coordinates target = command.getTarget();
		UsableTool tool = command.getActiveTool();

		if (command.getEnergy() < tool.getCost()) {
			throw new InsufficientEnergyException(command, tool.getCost());
		}

		TargetInterface<?> targeted = actorsManager.getTarget(target);
		if (targeted == null || targeted.getFaction() != command.getFaction()) {
			throw new InvalidTargetException(command);
		}

		tool.use(targeted);
		command.removeUsedTool(tool);
		command.removeEnergy(tool.getCost());
		return true;
	}

	@Override
	public String toString() {
		return getClass().getName() + "[ActorManager: " + actorsManager.toString() + "]";
	}

	@Override
	public StandardSupportSystem clone() {
		try {
			StandardSupportSystem clone = (StandardSupportSystem) super.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	@Override
	public boolean equals(Object otherObject) {
		if (otherObject == null)
			return false;
		if (getClass() != otherObject.getClass())
			return false;
		StandardSupportSystem other = (StandardSupportSystem) otherObject;
		return actorsManager.equals(other.actorsManager);
	}

	private IActorManager actorsManager;

}
