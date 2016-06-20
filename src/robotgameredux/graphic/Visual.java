package robotgameredux.graphic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import robotgameredux.actors.Robot;
import robotgameredux.core.Vector2;
import robotgameredux.input.RobotStates;

import javax.swing.JComponent;

public class Visual extends Sprite{
	
	private static final long serialVersionUID = 441178591544695129L;
	
	public Visual(Robot robot) {
		//this.setSize(100, 100);
		this.robot = robot;
		this.setPreferredSize(new Dimension(64,64));
		current = null;
		dest = null;
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
		/*int newY = 0;
		int newX = 0;
		if (robot.getState() == RobotStates.MOVING) {
			if (dest == null) {
				dest = robot.getDest();
				current = robot.getCoords();
				System.out.println(dest.y + dest.x);
			}
			System.out.println("POSIZIONE SPRITE" + this.getX() +" - " +this.getY());
			System.out.println("DESTINAZIONE" + dest.x*64 +" - " + dest.y*64);
			if (this.getY()/64 < dest.y)
				newY = this.getY()+1;
			if (this.getX()/64 < dest.x)
				newX = this.getX()+1;
			this.setBounds(newX, newY, 64, 64);
			if (this.getY()/64 == dest.y && this.getX()/64 == dest.x) 
				robot.setState(RobotStates.INACTIVE);
				dest = null;
		}
		else 
			this.setBounds((int) robot.getCoords().x*64, (int) robot.getCoords().y*64, 64, 64);
		*/
		this.setBounds(robot.getCoords().getX()*64, robot.getCoords().getY()*64, 64, 64);

		//this.repaint();
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
		if (robot.getHealth() <= 10) {this.color = Color.BLUE;}
		if (robot.getHealth() > 100) {this.color = Color.ORANGE;}
		if (robot.getHealth() > 200) {this.color = Color.MAGENTA;}
		if (robot.getHealth() > 300) {this.color = Color.PINK;}
	}
	
	Dimension preferredSize;
	Robot robot;
	Color color;
	Vector2 current;
	Vector2 dest;
}
 