package robotgameredux.input;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import robotgameredux.graphic.Sprite;
import robotgameredux.graphic.Visual;

public class RobotActionDialog extends JDialog {

	private RobotStates input;
	private JButton moveButton;
	private JButton attackButton;
	private JButton doNothingButton;
	private JButton pushButton;
	private JButton destroyButton;
	private JButton rechargeButton;
	public RobotActionDialog(JFrame owner, boolean modal) {
		super(owner, true);
		this.setLayout(new GridLayout(0, 1));
		this.setUndecorated(true);
		this.initButtons();
		this.pack();
		input = RobotStates.IDLE;

	}
	
	public void showAction(Sprite sprite) {
		this.setLocationRelativeTo(sprite);		
		this.setVisible(true);		
	}
	
	public RobotStates getInput() {
		return input;
	}
	
	public void resetInput() {
		input = RobotStates.IDLE;;
	}
	

	private void initButtons() {
		moveButton = new JButton("Muovi");
		this.add(moveButton);
		moveButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				input = RobotStates.MOVING;
				setVisible(false);
			}
		});
		
		attackButton = new JButton("Attacca");
		this.add(attackButton);
		attackButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				input = RobotStates.ATTACKING;
				setVisible(false);
			}
		});
		
		doNothingButton = new JButton("Non fare nulla");
		this.add(doNothingButton);
		doNothingButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				input = RobotStates.DO_NOTHING;
				setVisible(false);
			}
		});
		
		destroyButton = new JButton("Distruggi ostacolo");
		this.add(destroyButton);
		destroyButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				input = RobotStates.DESTROY_OBSTACLE;
				setVisible(false);
			}
		});
		
		rechargeButton = new JButton("Ricaricati");
		this.add(rechargeButton);
		rechargeButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				input = RobotStates.RECHARGE;
				setVisible(false);
			}
		});
		
		
		/*pushButton = new JButton("Spingi ostacolo");
		this.add(pushButton);
		pushButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				input = RobotStates.PUSH_OBSTACLE;
				setVisible(false);
			}
		});*/
		
	
	}
}
