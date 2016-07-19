package robotgameredux.graphic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

import robotgameredux.enums.RobotStates;
import robotgameredux.gameobjects.Actor;

public class ESupportRobotSprite extends Sprite {
	/**
	 *  
	 */

	// Creare classe unica per le sprite che estende JComponent e poi estendere
	// quella per le sprite dei vari gameobjects
	private static final long serialVersionUID = -1343989742137472214L;

	public ESupportRobotSprite(Actor robot) {
		this.robot = robot;
		try {
			spriteDefault = ImageIO.read(getClass().getResource("/robotgameredux/images/e_support_idle.png"));
			spriteTurnOver = ImageIO.read(getClass().getResource("/robotgameredux/images/e_support_turn_over.png"));
			spriteInactive = ImageIO.read(getClass().getResource("/robotgameredux/images/disabled.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		HP = new JLabel();
		energy = new JLabel();
		defense = new JLabel();
		HP.setBounds(1, 36, 64, 16);
		energy.setBounds(1, 49, 64, 16);
		defense.setBounds(36, 36, 64, 16);
		HP.setForeground(Color.WHITE);
		energy.setForeground(Color.WHITE);
		defense.setForeground(Color.WHITE);
		this.add(HP);
		this.add(energy);
		this.add(defense);
	}

	@Override
	public void update() {
		this.HP.setText("S: " + robot.getHealth());
		this.energy.setText("E: " + robot.getEnergy());
		this.defense.setText("D:" + robot.getDefense());
		this.setBounds(robot.getCoords().getX() * 64, robot.getCoords().getY() * 64, 64, 64);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (robot.getState() == RobotStates.TURN_OVER) {
			g.drawImage(spriteTurnOver, 0, -16, null);
		} else if (robot.getState() == RobotStates.INACTIVE) {
			g.drawImage(spriteInactive, 0, -16, null);
		} else {
			g.drawImage(spriteDefault, 0, -16, null);
		}
	}

	Dimension preferredSize;
	Actor robot;
	transient private BufferedImage spriteDefault;
	transient private BufferedImage spriteTurnOver;
	transient private BufferedImage spriteInactive;
	transient private JLabel HP;
	transient private JLabel energy;
	transient private JLabel defense;
}
