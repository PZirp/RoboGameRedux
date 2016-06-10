package robotgameredux.weapons;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class WeaponDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8242534944108292198L;
	private Integer selected;

	public WeaponDialog(JFrame owner, boolean modal) {
		super(owner, true);
		this.setLayout(new GridLayout(3, 3));
		this.setUndecorated(false);
		this.selected = null;
	}
	
	public void showWeapons(ArrayList<Weapon> weapons) {
		for (int i = 0; i < weapons.size(); i++) {
			System.out.println(weapons.get(i).getName() + "NOME DI UN'ARMA");
			wepButton but = new wepButton(weapons.get(i).getName(), i);
			this.add(but);
			but.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					selected = but.getSelectionIndex();
					setVisible(false);
					getContentPane().removeAll(); //Rimuove i pulsanti dal dialog
				}
			});	
		}

		System.out.println("Eccomi qui");
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
	
}

class wepButton extends JButton {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3034536393470085565L;

	wepButton(String name, int i) {
		super(name);
		this.index = i;		
	}
	
	public int getSelectionIndex() {
		return this.index;
	}
	
	private int index;
}