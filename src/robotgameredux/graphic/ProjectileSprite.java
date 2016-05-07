package robotgameredux.graphic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import robotgameredux.weapons.Projectile;

public class ProjectileSprite extends JComponent {
	
	public ProjectileSprite(Projectile projectile) {
		this.setSize(25, 25);
		this.projectile = projectile;
	}
	
	public void update() {
		int x = (int) projectile.getTarget().x - (int) projectile.getCurrent().x;
		int y = (int) projectile.getTarget().y - (int) projectile.getCurrent().y;
		this.setBounds(x, x, 16, 16);
		this.repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		g2.fillOval(0, 0, 16, 16);
	}
	
	
	Color color = Color.BLACK;
	Projectile projectile;
}
