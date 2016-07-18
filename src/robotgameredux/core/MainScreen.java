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
	
	private GameManager gm;
	private File openFile;
	private JFrame frame;
	private JFileChooser fileChooser;
	
	public static void main(String[] args) {
		MainScreen ms = new MainScreen();
	}

	public MainScreen() {
		fileChooser = new JFileChooser();

		frame = new JFrame();
		frame.setTitle("RobotGame Redux");
		frame.setSize(1280, 770);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		makeMenu();
		frame.setVisible(true);	
	}
	
	private void chooseFile() {
		int returnValue = fileChooser.showOpenDialog(frame);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			this.openFile = fileChooser.getSelectedFile(); 
		}
	}
			
	private void makeMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Partita");
		menuBar.add(menu);
		JMenu nuovaSubMenuItem = new JMenu("Nuova partita");
		JMenuItem salvaMenuItem = new JMenuItem("Salva partita");
		JMenuItem caricaMenuItem = new JMenuItem("Carica partita");
				
		JMenuItem liv1 = new JMenuItem("Primo livello");
		JMenuItem liv2 = new JMenuItem("Secondo livello");
		JMenuItem ran = new JMenuItem("Genera");
		
		nuovaSubMenuItem.add(liv1);
		nuovaSubMenuItem.add(liv2);
		nuovaSubMenuItem.add(ran);
		
		liv1.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadFirstLevel();
			}
		});
		
		ran.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				generateRandomLevel();
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
		menu.add(nuovaSubMenuItem);
		menu.add(salvaMenuItem);
		menu.add(caricaMenuItem);
		
		frame.setJMenuBar(menuBar);
	}
	
	public void loadFirstLevel() {
		if (gm != null) {
			frame.getContentPane().removeAll();
			gm = null;
		}
		this.gm = new GameManager();
		this.gm.firstLevel();
		frame.add(gm.getPane(), BorderLayout.CENTER);
		gm.createEndTurnButton();
		frame.revalidate();
	}	

	public void generateRandomLevel() {
		if (gm != null) {
			frame.getContentPane().removeAll();
			//frame.remove(gm.getPane());
			gm = null;
		}
		this.gm = new GameManager();
		this.gm.randomGeneration();
		frame.add(gm.getPane(), BorderLayout.CENTER);
		gm.createEndTurnButton();
		frame.revalidate();
	}	
	
	public void save() {
		try {
			int returnValue = fileChooser.showSaveDialog(frame);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				FileOutputStream fileOut = new FileOutputStream(fileChooser.getSelectedFile());
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(gm);
				out.close();
				fileOut.close();
			}
		}catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void load() {
		try {
			if (openFile != null) {FileInputStream fileIn = new FileInputStream(openFile);
				if (gm != null) { 
					frame.getContentPane().removeAll();
					gm = null;
				}
				ObjectInputStream in = new ObjectInputStream(fileIn);
				GameManager e = (GameManager) in.readObject();
				in.close();
				fileIn.close();
				this.gm = e; 
				frame.add(gm.getPane(), BorderLayout.CENTER);
				gm.createEndTurnButton();
				gm.checkEndTurnButton();
				frame.revalidate();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
