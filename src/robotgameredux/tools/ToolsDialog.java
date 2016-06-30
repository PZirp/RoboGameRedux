package robotgameredux.tools;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import robotgameredux.weapons.Weapon;

public class ToolsDialog extends JDialog{
	
	public ToolsDialog(JFrame owner, boolean modal) {
		super(owner, true);
		this.setLayout(new GridLayout(3, 3));
		this.setUndecorated(true);
		this.selected = null;
	}
	
	//In questo modo ogni volta che apro il dialog tutti i pulsanti vengono creati nuovamente
	//Potrebbe essere meglio tenere una lista di pulsanti e rimuoverli una volta usati? Per� come faccio per creare nuovi pulsanti?
	
	public void showTools(ArrayList<UsableTool> tools) {
		
		for (int i = 0; i < tools.size(); i++) {
			System.out.println(tools.get(i).getName() + "NOME DI UN TOOL");
			toolButton but = new toolButton(tools.get(i).getName() +" (Costo: " + tools.get(i).getCost() + ")" , i);
			this.add(but);
			but.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					selected = but.getSelectionIndex();
					setVisible(false);
					getContentPane().removeAll(); //Rimuove i pulsanti dal dialog
				}
			});	
		}
		
		JButton annullaBtn = new JButton("Annulla");
		annullaBtn.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				selected = -1;
				setVisible(false);
				getContentPane().removeAll(); //Rimuove i pulsanti dal dialog
			}
		});
		this.add(annullaBtn);
		this.setLocationRelativeTo(null);
		this.pack();
		this.setVisible(true);
	}

	public Integer getSelected() {
		return this.selected;
	}
	
	public void resetSelected() {
		this.selected = null;
	}
	

	public void showWeapons(ArrayList<Weapon> weapons) {
		for (int i = 0; i < weapons.size(); i++) {
			System.out.println(weapons.get(i).getName() + "NOME DI UN'ARMA");
			if (weapons.get(i).hasBullets()) {
				toolButton but = new toolButton(weapons.get(i).getName() + "("+weapons.get(i).getBulletCount()+")", i);
				this.add(but);
				but.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						selected = but.getSelectionIndex();
						setVisible(false);
						getContentPane().removeAll(); //Rimuove i pulsanti dal dialog
					}
				});	
			}
		}
		
		JButton annullaBtn = new JButton("Annulla");
		annullaBtn.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				selected = -1;
				setVisible(false);
				getContentPane().removeAll(); //Rimuove i pulsanti dal dialog
			}
		});
		this.add(annullaBtn);
		
		System.out.println("Eccomi qui");
		this.setLocationRelativeTo(null);
		this.pack();
		this.setVisible(true);			
	}

	private Integer selected;

	
}

class toolButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 692968446768906471L;
	
	toolButton(String name, int i) {
		super(name);
		this.index = i;		
	}
	
	public int getSelectionIndex() {
		return this.index;
	}
	
	private int index;
}