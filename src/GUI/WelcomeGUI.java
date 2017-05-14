package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import no.uib.info233.v2017.vap003.oblig4.player.HumanPlayer;
import no.uib.info233.v2017.vap003.oblig4.player.Player;

public class WelcomeGUI extends JPanel implements ActionListener {
	
	private JLabel welcome;
	private JTextField insertName = new JTextField();
	private JButton submitName = new JButton("Confirm");
	private JPanel welcomePanel;
	private GamePanel gamePanel = new GamePanel();
	private Player player;
	private GameLayout gameLayout;
	
	public WelcomeGUI () {
		
		welcome = new JLabel ("What's the player's name?");
		welcome.setAlignmentX(CENTER_ALIGNMENT);
		
		insertName.setMaximumSize(new Dimension (200, 25));
		insertName.addActionListener(this);
		
		submitName.setAlignmentX(CENTER_ALIGNMENT);
		submitName.addActionListener(this);
		submitName.setAlignmentY(CENTER_ALIGNMENT);
		
		
		createGUI();
		
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		player = new HumanPlayer (insertName.getText());
		insertName.setText("");
		ConsoleGUI.sendToConsole("Player " + player.getName() + " added to the game.");
		
		//TODO Make new panel
	}
	
	
	
	public void addComponentsToPane (Container pane) {
		
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		
		pane.add(welcome);
		pane.add(insertName);
		pane.add(submitName);
		
		pane.setMinimumSize(new Dimension(500, 500));
		pane.setPreferredSize(new Dimension(500, 500));
	}
	
	
	private void createGUI() {
		
		welcomePanel = new JPanel();
		addComponentsToPane (welcomePanel);

	}
	
	public JPanel getPanel() {
		return welcomePanel;
	}
	

}
