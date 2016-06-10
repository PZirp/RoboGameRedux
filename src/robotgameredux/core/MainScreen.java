package robotgameredux.core;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

public class MainScreen extends JFrame {
	
	GameManager gm;
	JButton button;
	
	public static void main(String[] args) {
		MainScreen ms = new MainScreen();
	}

	public MainScreen() {
		this.initScreen();
		//this.gw = new GameWorld();
		//this.add(gw, BorderLayout.CENTER);
		this.gm = new GameManager();
		this.add(gm, BorderLayout.CENTER);
		button = new JButton("Pausa");
		//button.addMouseListener(new PauseButtonListener());
		this.add(button, BorderLayout.SOUTH);
		
	
	}
	
	private void initScreen() {
		this.setTitle("RobotGame Redux");
		this.setSize(1280, 770);
		this.setUndecorated(false);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	/*class PauseButtonListener extends MouseAdapter {
		
		public void mousePressed(MouseEvent e) {
			if (gw.getPaused() == false) {
				gw.setPaused(true);
				button.setText("Riprendi");
			}
			else if (gw.getPaused() == true) {
				gw.setPaused(false);
				button.setText("Pausa");;
			}
			
		}
	}*/
}
