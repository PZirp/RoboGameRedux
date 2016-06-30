package robotgameredux.graphic;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InfoDialog extends JDialog {
	
	//JPanel panel;
	JLabel HPlabel;
	JLabel energyLabel;
	JLabel typeLabel;
	
	public InfoDialog(JFrame frame, Boolean modal) {
		super(frame, modal);
		this.setSize(100, 50);
		this.setUndecorated(true);
		//panel = new JPanel();
		HPlabel = new JLabel();
		energyLabel = new JLabel();
		typeLabel = new JLabel();
		this.setLayout(new GridLayout(3,0));
	}

	public void setHPlabel(int hp) {
		HPlabel.setText("Salute = " + hp);
		this.add(HPlabel);
	}


	public void setEnergyLabel(int energy) {
		this.energyLabel.setText("Energia = " + energy);
		this.add(energyLabel);
	}


	public void setTypeLabel(String typeLabel) {
		//this.typeLabel = typeLabel;
	}

	
}
