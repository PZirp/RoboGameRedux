package robotgameredux.core;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.io.File;
import java.io.Serializable;

import javax.imageio.ImageIO;

import robotgameredux.graphic.Sprite;
import robotgameredux.graphic.TileSprite;

//Logic tile
public class Tile implements Serializable{
		
		/**
	 * 
	 */
	private static final long serialVersionUID = -427813845990404269L;
		public Tile() {
			this.sprite = null;
			calpestabile = true;
			this.active = false;
			/*try {
				img = ImageIO.read(new File("C:\\Utenti\\Paolo\\Desktop\\tile.png"));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}*/
			
		}
	
		public void setSprite(Sprite sprite) {
			this.sprite = sprite;
		}
		
		public Sprite getSprite(){
			return sprite;
		}
		
		public Boolean isCalpestabile() {
			return calpestabile;
		}
		public void setCalpestabile(Boolean calpestabile) {
			this.calpestabile = calpestabile;
		}
			
		public void render() {
			sprite.update();
		}
		
		public Boolean isActive() {
			return active;
		}
		
		public void setActive(Boolean active) {
			this.active = active;
		}
		
		// private int baseID; Individua la texture da usare per questa tile
		private Boolean calpestabile;
		private BufferedImage img = null;
		transient private Sprite sprite;
		private Boolean active;
}
