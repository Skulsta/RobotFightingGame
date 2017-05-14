package GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class GameLayout extends JPanel implements ActionListener {
	
	private ConsoleGUI console = new ConsoleGUI();
	private WelcomeGUI welcomePanel = new WelcomeGUI();
	
	private JLabel label = new JLabel ("Welcome to the Danger Zone!");
	
	public GameLayout () {
		
		this.createGUI();
		
	}
	
	
	public void createGUI () {
		
		setLayout (new BorderLayout());
		
		add(welcomePanel.getPanel(), BorderLayout.CENTER);
		// add(label, BorderLayout.NORTH);
		add(console.getConsole(), BorderLayout.EAST);
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	

}
