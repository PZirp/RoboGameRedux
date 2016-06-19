package robotgameredux.input;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import robotgameredux.graphic.Sprite;

public class RobotActionDialog extends JDialog {

	//Classe interna all'AttackRobotController ora, si può eliminare 

	
	/**
	 * 
	 *//*
	private static final long serialVersionUID = -5620050801218829995L;
	private RobotStates input;
	private JButton moveButton;
	private JButton attackButton;
	private JButton doNothingButton;
	private JButton destroyButton;
	private JButton rechargeButton;
	private JButton takeWeapon;
	private AttackRobotController controller;
	
	public RobotActionDialog(JFrame owner, boolean modal, AttackRobotController controller) {
		super(owner, true);
		this.setLayout(new GridLayout(0, 1));
		this.setUndecorated(true);
		this.controller = controller;
		this.initButtons();
		this.pack();
		input = RobotStates.IDLE;
	}
	
	public void showAction(Sprite sprite) {
		this.setLocationRelativeTo(sprite);	
		
		if (controller.getEmptyWeapon()) {
			this.remove(takeWeapon);
		}
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
		
		if (this.controller == null) {
			System.out.println("E' VUOTO");
		}
		
		takeWeapon = new JButton("Prendi arma");
		this.add(takeWeapon);
		takeWeapon.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				input = RobotStates.TAKE_WEAPON;
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
		
		doNothingButton = new JButton("Non fare nulla");
		this.add(doNothingButton);
		doNothingButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				input = RobotStates.DO_NOTHING;
				setVisible(false);
			}
		});
			
	}*/
}
