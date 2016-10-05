package robotgameredux.graphic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import robotgameredux.gameobjects.Obstacle;

public class ObstacleSprite extends Sprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3592184420029474507L;

	public ObstacleSprite(Obstacle obstacle) {
		this.obstacle = obstacle;
		try {
			BufferedImage temp = ImageIO.read(getClass().getResource("/robotgameredux/images/obstacle.png"));
			
			AffineTransform at = new AffineTransform();
			at.scale(2.0, 2.0);
			AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			spriteDefault = scaleOp.filter(temp, spriteDefault);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Aggiorna l'immagine analizzando lo stato corrente dell'ostacolo
	 */

	@Override
	public void update() {
		this.setBounds(obstacle.getCoords().getX() * 64, obstacle.getCoords().getY() * 64, 64, 64);
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
	private Obstacle obstacle;

}
