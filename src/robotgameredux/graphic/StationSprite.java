package robotgameredux.graphic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import robotgameredux.gameobjects.Station;

public class StationSprite extends Sprite {

	private static final long serialVersionUID = 3592184420029474507L;

	public StationSprite(Station station) {
		// this.setSize(100, 100);
		this.station = station;
		try {
			spriteDefault = ImageIO.read(getClass().getResource("/robotgameredux/images/stazione.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Aggiorna l'immagine analizzando lo stato corrente della stazione
	 */

	@Override
	public void update() {
		this.setBounds(station.getCoords().getX() * 64, station.getCoords().getY() * 64, 64, 64);
	}

	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(spriteDefault, 0, 0, null);
	}

	transient private BufferedImage spriteDefault; 
	private Station station;

}
