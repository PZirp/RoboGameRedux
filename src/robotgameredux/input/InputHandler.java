package robotgameredux.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import robotgameredux.core.Vector2;
import robotgameredux.actors.GameObject;
import robotgameredux.actors.Robot;

public class InputHandler implements MouseListener{
	
	public InputHandler(Vector<GameObject> robots) {
		this.robots = robots;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		Boolean trovato = false;
		int i = 0;
		while (!trovato) {
			
			Robot robot = (Robot) robots.get(i);
			if (robot.getCoords().x == e.getX()/64 && robot.getCoords().y == e.getY()/64){
				System.out.println("Il robot è alla posizione: " + robot.getCoords().toString());
				trovato = true;
			}
			trovato = true;
		}
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
			Vector2 click = new Vector2(e.getX()/64, e.getY()/64);
		System.out.println(click.toString());
		if (active == null) {
			for (int i = 0; i < robots.size(); i++) {
				Robot robot = (Robot) robots.get(i);
				if (robot.getCoords().x == click.x && robot.getCoords().y == click.y){
					System.out.println("Il robot è alla posizione: " + robot.getCoords().toString());
					active = robot;
					System.out.println("Il robot è attivo prima del click? " + robot.getActive().toString());
					robot.setActive(true);
					System.out.println("Il robot è attivo dopo il click? " + robot.getActive().toString());
				}
			}
		}
		else {
			if (click.x != e.getX() || click.y != e.getY()) {
				active.setDest(click);
				active.setState(1);
				active = null;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}	
	
	Vector<GameObject> robots;
	Robot active;
}


