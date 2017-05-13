package no.uib.info233.v2017.vap003.oblig4.game;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WelcomeGUI extends JPanel implements ActionListener {
	
	private JTextField welcome;
	private JTextField insertName = new JTextField();
	private JButton submitName = new JButton("Confirm");
	private JPanel welcomePanel;
	
	public WelcomeGUI () {
		
		welcome = new JTextField ("What's the player name?");
		welcome.setBounds(80, 40, 225, 20);
		welcome.setEditable(false);
		
		createGUI();
		
	}
	
	
	public void addComponentsToPane (Container pane) {
		
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		
		pane.add(welcome);
		pane.add(insertName);
		pane.add(submitName);
	}
	
	
	private void createGUI() {
		
		welcomePanel = new JPanel();
		addComponentsToPane (welcomePanel);
	}
	
	public JPanel getPanel() {
		return welcomePanel;
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	

}
