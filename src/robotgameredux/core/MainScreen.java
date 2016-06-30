package robotgameredux.core;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class MainScreen {
	
	GameManager gm;
	File openFile;
	JFrame frame;
	
	public static void main(String[] args) {
		MainScreen ms = new MainScreen();
	}

	public MainScreen() {
		frame = new JFrame();
		frame.setTitle("RobotGame Redux");
		frame.setSize(1280, 770);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		makeMenu();
		frame.setVisible(true);	
	}
	
	private void chooseFile() {
		JFileChooser fileChooser = new JFileChooser();
		int returnValue = fileChooser.showOpenDialog(frame);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			this.openFile = fileChooser.getSelectedFile(); 
		}
	}
	
	private void makeMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Partita");
		menuBar.add(menu);
		JMenuItem nuovaMenuItem = new JMenuItem("Nuova partita");
		JMenuItem salvaMenuItem = new JMenuItem("Salva partita");
		JMenuItem caricaMenuItem = new JMenuItem("Carica partita");
		menu.add(nuovaMenuItem);
		menu.add(salvaMenuItem);
		menu.add(caricaMenuItem);
		
		nuovaMenuItem.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				newStage();
			}
		});
		
		salvaMenuItem.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});
		caricaMenuItem.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				chooseFile();
				load();
			}
		});		
		frame.setJMenuBar(menuBar);
	}
	
	public void newStage() {
		if (gm != null) {
			frame.remove(gm.getPane());
			gm = null;
		}
		this.gm = new GameManager();
		frame.add(gm.getPane(), BorderLayout.CENTER);
		gm.createEndTurnButton();
		//gm.initGame();
		frame.revalidate();
	}	
	
	public void save() {
		try {
			FileOutputStream fileOut = new FileOutputStream("C:\\Users\\Paolo\\Desktop\\prova6.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(gm);
			out.close();
			fileOut.close();
		}catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void load() {
		if (gm != null) {
			frame.remove(gm.getPane());
			gm = null;
			}
		try {
			//FileInputStream fileIn = new FileInputStream("C:\\Users\\Paolo\\Desktop\\prova6.ser");
			FileInputStream fileIn = new FileInputStream(openFile);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			GameManager e = (GameManager) in.readObject();
			in.close();
			fileIn.close();
			this.gm = e; //new GameManager();
			frame.add(gm.getPane(), BorderLayout.CENTER);
			gm.createEndTurnButton();
			gm.checkEndTurnButton();
			frame.revalidate();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
