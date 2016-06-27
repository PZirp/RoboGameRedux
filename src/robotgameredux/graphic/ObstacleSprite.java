package robotgameredux.graphic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import robotgameredux.actors.Obstacle;
import robotgameredux.actors.Robot;
import robotgameredux.input.RobotStates;

public class ObstacleSprite extends JComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3592184420029474507L;
	
	public ObstacleSprite(Obstacle obstacle) {
		//this.setSize(100, 100);
		this.obstacle = obstacle;
		this.setPreferredSize(new Dimension(64,64));
	}

	public void setPreferredSize(Dimension preferredSize) {
		this.preferredSize = preferredSize;
	}
	public Dimension getSize() {
		return preferredSize;
	}
	public Dimension getPreferredSize() {
		return preferredSize;
	}
	
	//Devo usare le coordinate dello schermo non quelle del world per avere un movimento fluido 	
	public void update() {
		this.setColor();
		this.setBounds(obstacle.getCoords().getX()*64, (int) obstacle.getCoords().getY()*64, 64, 64);
		//this.repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		g2.fillRect(0, 0, 64, 64);
	}
	
	public void setColor() {
		this.color = new Color(165, 42, 42);
	}
	
	Dimension preferredSize;
	Obstacle obstacle;
	Color color;
	
}
