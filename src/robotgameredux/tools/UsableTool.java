package robotgameredux.tools;

import robotgameredux.TargetInterfaces.TargetInterface;

public interface UsableTool {

	public <T> void use(TargetInterface<T> robot);

	public String getName();

	public int getCost();

	public UsableTool clone();
}
