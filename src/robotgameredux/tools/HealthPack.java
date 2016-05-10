package robotgameredux.tools;

public class HealthPack implements UsableTool {

	public HealthPack() {
		this.health = 50;
	};
	
	@Override
	public void use() {
		

	}
	
	private final int health;

}
