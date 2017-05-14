package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import no.uib.info233.v2017.vap003.oblig4.player.HumanPlayer;
import no.uib.info233.v2017.vap003.oblig4.player.Player;

public class GameLayout extends JPanel implements ActionListener{

	// TODO remove soon
	private ConsoleGUI console = new ConsoleGUI();
	// private WelcomeGUI welcomePanel = new WelcomeGUI();
	private GamePanel gamePanel = new GamePanel();
	
	// For the startup screen
	private JLabel welcome = new JLabel ("What's the player's name?");
	private JTextField insertName = new JTextField();
	private JButton submitName = new JButton("Confirm");
	private Player player;
	private JPanel welcomePanel = new JPanel();
	
	// For the console
	private  String message = "The Almighty Console \n \n";
	private  JTextArea textArea = new JTextArea(message);
	public final static String newLine = "\n";
	
	
	public GameLayout () {
		
		defineWelcomeElements();
		console();
		
		
	}
	
	// Adding two panels to the main panel
	public void createGUI () {
		
		setLayout (new BorderLayout());
		
		add(welcomePanel, BorderLayout.CENTER);
		// add(label, BorderLayout.NORTH);
		add(textArea, BorderLayout.EAST);
		
	}
	
	
	// Setting up the start screen
	
	public void defineWelcomeElements() {
		
		welcome.setAlignmentX(CENTER_ALIGNMENT);
		
		insertName.setMaximumSize(new Dimension (200, 25));
		insertName.addActionListener(this);
		
		submitName.setAlignmentX(CENTER_ALIGNMENT);
		submitName.addActionListener(this);
		submitName.setAlignmentY(CENTER_ALIGNMENT);
		
		
		welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
		
		welcomePanel.add(welcome);
		welcomePanel.add(insertName);
		welcomePanel.add(submitName);
		
		welcomePanel.setMinimumSize(new Dimension(500, 500));
		welcomePanel.setPreferredSize(new Dimension(500, 500));
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		player = new HumanPlayer (insertName.getText());
		insertName.setText("");
		sendToConsole("Player " + player.getName() + " added to the game.");
	}
	
	
	
	
	
	// Setting up the console
	
	public void console() {
		textArea.setEditable(false);
		textArea.setPreferredSize(new Dimension (500, 100));
		textArea.setBackground(Color.LIGHT_GRAY);
	}
	
	
	public void sendToConsole(String message) {
		textArea.append(message + newLine);
	}
	
	
}
