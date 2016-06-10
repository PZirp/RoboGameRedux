package robotgameredux.input;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import robotgameredux.graphic.Sprite;

public class SupportDialog extends JDialog{
	private RobotStates input;
	private JButton moveButton;
	private JButton takeObjectButton;
	private JButton doNothingButton;
	private JButton useObjectButton;
	private JButton pushButton;
	private JButton rechargeButton;
	
	public SupportDialog(JFrame owner, boolean modal) {
		super(owner, true);
		//this.setLayout(new BorderLayout());
		this.setLayout(new GridLayout(0, 1));
		this.setUndecorated(true);
		this.initButtons();
		this.pack();
		input = null;

	}
	
	public void showAction(Sprite sprite) {
		this.setLocationRelativeTo(sprite);		
		this.setVisible(true);		
	}
	
	public RobotStates getInput() {
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
		this.add(moveButton);
		moveButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				input = RobotStates.MOVING;
				hideAction();
			}
		});
		
		takeObjectButton = new JButton("Prendi oggetto");
		this.add(takeObjectButton);
		takeObjectButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				input = RobotStates.TAKE_OBJECT;
				hideAction();
			}
		});
		
		pushButton = new JButton("Spingi ostacolo");
		this.add(pushButton);
		pushButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				input = RobotStates.PUSH_OBSTACLE;
				setVisible(false);
			}
		});
		
		
		doNothingButton = new JButton("Non fare nulla");
		this.add(doNothingButton);
		doNothingButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				input = RobotStates.DO_NOTHING;
				hideAction();
			}
		});
		
		useObjectButton = new JButton("Usa oggetto");
		this.add(useObjectButton);
		useObjectButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				input = RobotStates.USE_OBJECT;
				hideAction();
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
		
		
	}
}
