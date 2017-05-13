package GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import no.uib.info233.v2017.vap003.oblig4.game.WelcomeGUI;

public class GameLayout extends JPanel implements ActionListener {
	
	private JTextArea inputText = new JTextArea ("Type a name...");
	private ConsoleGUI console = new ConsoleGUI();
	private WelcomeGUI welcomePanel = new WelcomeGUI();
	
	private JLabel label = new JLabel ("Welcome to the Danger Zone!");
	
	public GameLayout () {
		
		this.createGUI();
		
	}
	
	
	private void createGUI () {
		setLayout (new BorderLayout());
		
		add(welcomePanel.getPanel(), BorderLayout.CENTER);
		add(label, BorderLayout.NORTH);
		add(console.getConsole(), BorderLayout.EAST);
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	

}
