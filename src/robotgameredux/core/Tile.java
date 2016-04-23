package robotgameredux.core;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.io.File;

import javax.imageio.ImageIO;

//Logic tile
public class Tile {
		
		public Tile() {
			calpestabile = true;
			/*try {
				img = ImageIO.read(new File("C:\\Utenti\\Paolo\\Desktop\\tile.png"));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}*/
			
		}
	
		public Boolean isCalpestabile() {
			return calpestabile;
		}
		public void setCalpestabile(Boolean calpestabile) {
			this.calpestabile = calpestabile;
		}
			
		public void render() {

		}
		
		// private int baseID; Individua la texture da usare per questa tile
		private  Boolean calpestabile;
		private BufferedImage img = null;
}
