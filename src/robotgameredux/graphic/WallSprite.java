package robotgameredux.graphic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import robotgameredux.core.Tile;
import robotgameredux.input.RobotStates;
import java.awt.Color;

public class WallSprite extends Sprite {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8929261000973748441L;
	private BufferedImage spriteDefault;
	private Tile tile;
	//private int x,y;
	
	public WallSprite(Tile tile, int x, int y) {
		this.tile = tile;
		//this.setPreferredSize(new Dimension(64,64));
		//this.x = x;
		//this.y = y;
		this.setBounds(x*64, y*64, 64, 64);
		try {
			spriteDefault = ImageIO.read(getClass().getResource("/robotgameredux/images/wall.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void update() {
		this.setColor();		
		this.repaint();
		
	}
	
	public Dimension getPreferredSize() {
		if (spriteDefault == null) {
			return new Dimension(64, 64);
		}
		return new Dimension(spriteDefault.getWidth(null), spriteDefault.getHeight(null));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		/*Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		g2.fillRect(0, 0, 64, 64);*/
		g.drawImage(spriteDefault, 0, 0, null);
	}
	
	public void setColor() {
		
		
		/*if (tile.isActive()) this.color = new Color(255, 255, 0);
		else this.color = new Color(150, 75, 0);*/
	}
	
}
