package robotgameredux.core;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MainScreen extends JFrame {
	
	GameManager gm;
	JButton button;
	
	public static void main(String[] args) {
		MainScreen ms = new MainScreen();
		ms.load();
		/*try {
			FileOutputStream fileOut = new FileOutputStream("C:\\Users\\Paolo\\Desktop\\prova3.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(ms.getGM());
			out.close();
			fileOut.close();
		}catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	public GameManager getGM() {
		return gm;
	}
	
	public MainScreen() {
		this.initScreen();
		//this.gm = //new GameManager();
		//this.add(gm, BorderLayout.CENTER);
		//gm.initGame();
		//gm.createEndTurnButton();
//		button = new JButton("Fine turno");
	//	button.addMouseListener(new TurnButtonListener());
		//this.add(button, BorderLayout.SOUTH);
	}

	/*public void enableEndTurnButton(Boolean b) {
		button.setEnabled(b);
	}*/
	
	private void initScreen() {
		this.setTitle("RobotGame Redux");
		this.setSize(1280, 770);
		//this.setUndecorated(false);
	//	this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	/*class TurnButtonListener extends MouseAdapter {
		
		public void mousePressed(MouseEvent e) {
			gm.endTurn();			
		}
	}*/

	
	public void load() {
		/*try {
			FileInputStream fileIn = new FileInputStream("C:\\Users\\Paolo\\Desktop\\prova3.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			GameManager e = (GameManager) in.readObject();
			in.close();
			fileIn.close();
			System.out.println(e.a);*/
			this.gm = new GameManager();
			this.add(gm, BorderLayout.CENTER);
			gm.createEndTurnButton();
			this.setVisible(true);

			
		/*} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}*/	
	}
	

}
