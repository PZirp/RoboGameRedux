package robotgameredux.graphic;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import robotgameredux.core.Tile;

public class WallSprite extends Sprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8929261000973748441L;
	private BufferedImage spriteDefault;
	private Tile tile;

	public WallSprite(Tile tile, int x, int y) {
		this.tile = tile;
		this.setBounds(x * 64, y * 64, 64, 64);
		try {
			spriteDefault = ImageIO.read(getClass().getResource("/robotgameredux/images/wall.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Aggiorna l'immagine della tile
	 */

	@Override
	public void update() {
		this.repaint();

	}
	

	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(spriteDefault, 0, 0, null);
	}



}
