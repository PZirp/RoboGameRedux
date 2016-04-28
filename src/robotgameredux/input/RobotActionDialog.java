package robotgameredux.input;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import robotgameredux.actors.Robot;
import robotgameredux.core.Vector2;

public class RobotActionDialog extends JDialog {

	private RobotStates input;
	private JButton moveButton;
	private JButton attackButton;
	private JButton doNothingButton;
	
	public RobotActionDialog(JFrame owner, boolean modal) {
		super(owner, modal);
		this.setLayout(new BorderLayout());
		this.initButtons();
		this.pack();
		input = null;

	}
	
	public void showAction() {
		this.setLocationRelativeTo(null);
		this.setVisible(true);		
	}
	
	public RobotStates getInput() {
		//input = RobotStates.ATTACKING;
		System.out.println("Adesso stampo l'input (dialog):");
		System.out.println(input);
		return input;
		
	}
	
	public void resetInput() {
		input = null;
	}
	
	public void hideAction() {
		this.setVisible(false);
	}
	
	private void initButtons() {
		moveButton = new JButton("Muovi");
		this.add(moveButton, BorderLayout.CENTER);
		moveButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				input = RobotStates.MOVING;
				hideAction();
			}
		});
		
		attackButton = new JButton("Attacca");
		this.add(attackButton, BorderLayout.NORTH);
		attackButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				input = RobotStates.ATTACKING;
				hideAction();
			}
		});
		
		doNothingButton = new JButton("Non fare nulla");
		this.add(doNothingButton, BorderLayout.SOUTH);
		doNothingButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				input = RobotStates.INACTIVE;
				hideAction();
			}
		});
		
	}
}
