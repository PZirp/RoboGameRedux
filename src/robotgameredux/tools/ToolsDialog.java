package robotgameredux.tools;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class ToolsDialog extends JDialog{
	
	public ToolsDialog(JFrame owner, boolean modal) {
		super(owner, true);
		this.setLayout(new GridLayout(3, 3));
		this.setUndecorated(false);
		this.selected = -1;
	}
	
	public void showTools(ArrayList<UsableTool> tools) {
		for (int i = 0; i < tools.size(); i++) {
			System.out.println(tools.get(i).getName() + "NOME DI UN TOOL");
			toolButton but = new toolButton(tools.get(i).getName(), i);
			this.add(but);
			but.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					selected = but.getSelectionIndex();
					setVisible(false);
				}
			});	
		}
		System.out.println("Eccomi qui");
		this.setLocationRelativeTo(null);
		this.pack();
		this.setVisible(true);
	}

	public int getSelected() {
		return this.selected;
	}
	
	private int selected;

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