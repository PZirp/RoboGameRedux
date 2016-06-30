package robotgameredux.graphic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import robotgameredux.actors.Robot;
import robotgameredux.core.Vector2;
import robotgameredux.input.RobotStates;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class Visual extends Sprite{
	
	private static final long serialVersionUID = 441178591544695129L;
	transient private BufferedImage spriteDefault;
	transient private BufferedImage spriteTurnOver;
	transient private BufferedImage spriteInactive;
	public Visual(Robot robot) {
		//this.setSize(100, 100);
		this.robot = robot;
		this.setPreferredSize(new Dimension(64,64));
		try {
			spriteDefault = ImageIO.read(new File("C:\\Users\\Paolo\\Desktop\\prova.png"));
			spriteTurnOver = ImageIO.read(new File("C:\\Users\\Paolo\\Desktop\\turn_over.png"));
			spriteInactive = ImageIO.read(new File("C:\\Users\\Paolo\\Desktop\\disabled.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
	
	 	
	public void update() {
		this.setColor();
		this.setBounds(robot.getCoords().getX()*64, robot.getCoords().getY()*64, 64, 64);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		/*Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		g2.fillRect(0, 0, 64, 64);*/
		if(robot.getState() == RobotStates.TURN_OVER) {
			g.drawImage(spriteTurnOver, 0, 0, null);
		} else if (robot.getState() == RobotStates.INACTIVE) {
			g.drawImage(spriteInactive, 0, 0, null);
		} else {
		g.drawImage(spriteDefault, 0, 0, null);
		}
	}
	
	public void setColor() {
		if (robot.getState() == RobotStates.ACTIVE) {this.color = Color.RED;}
		else if (robot.getState() == RobotStates.IDLE ){this.color = Color.GREEN;}
		else if (robot.getState() == RobotStates.INACTIVE){this.color = Color.GRAY;}
		else if (robot.getState() == RobotStates.DO_NOTHING ){this.color = Color.GREEN;}
		else if (robot.getState() == RobotStates.TURN_OVER ){this.color = Color.CYAN;}
		if (robot.getHealth() <= 10) {this.color = Color.BLUE;}
		if (robot.getHealth() > 100) {this.color = Color.ORANGE;}
		if (robot.getHealth() > 200) {this.color = Color.MAGENTA;}
		if (robot.getHealth() > 300) {this.color = Color.PINK;}
	}
	
	Dimension preferredSize;
	Robot robot;
	Color color;
}
 