package robotgameredux.graphic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import robotgameredux.actors.Robot;
import robotgameredux.input.RobotStates;

import javax.swing.JComponent;

public class Visual extends JComponent{
	
	private static final long serialVersionUID = 441178591544695129L;
	
	public Visual(Robot robot) {
		this.setSize(100, 100);
		this.robot = robot;
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
		this.setBounds((int) robot.getCoords().x*64, (int) robot.getCoords().y*64, 64, 64);
		this.repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		g2.fillRect(0, 0, 64, 64);
	}
	
	public void setColor() {
		if (robot.getState() == RobotStates.ACTIVE) {this.color = Color.RED;}
		else if (robot.getState() == RobotStates.INACTIVE ){this.color = Color.GREEN;}
		else if (robot.getState() == RobotStates.DO_NOTHING ){this.color = Color.GREEN;}
		if (robot.getHealth() == 10) {this.color = Color.BLUE;}
	}
	
	Dimension preferredSize;
	Robot robot;
	Color color;
}
