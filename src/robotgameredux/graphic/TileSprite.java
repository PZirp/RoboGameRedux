package robotgameredux.graphic;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import robotgameredux.core.Tile;

public class TileSprite extends Sprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = -909002863011955286L;
	transient private BufferedImage spriteDefault;
	transient private BufferedImage spriteAttiva;
	private Tile tile;

	public TileSprite(Tile tile, int x, int y) {
		this.tile = tile;
		this.setBounds(x * 64, y * 64, 64, 64);
		try {
			spriteDefault = ImageIO.read(getClass().getResource("/robotgameredux/images/tile.png"));
			spriteAttiva = ImageIO.read(getClass().getResource("/robotgameredux/images/tile_attiva.png"));
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
		if (!tile.isActive())
			g.drawImage(spriteDefault, 0, 0, null);
		if (tile.isActive())
			g.drawImage(spriteAttiva, 0, 0, null);
	}


}
