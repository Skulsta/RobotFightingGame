package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import no.uib.info233.v2017.vap003.oblig4.game.GameMaster;
import no.uib.info233.v2017.vap003.oblig4.player.HumanPlayer;
import no.uib.info233.v2017.vap003.oblig4.player.Player;

public class GameLayout extends JPanel implements ActionListener{

	
	// private JPanel mainPanel = new JPanel();
	
	// For the startup screen
	private JLabel welcome = new JLabel ("What's the player's name?");
	private JTextField input = new JTextField();
	private JButton submit = new JButton("Confirm");
	private Player player;
	private Player enemy;
	private JPanel welcomePanel = new JPanel();
	private boolean gameStarted;
	
	// For the console
	private  String message = "The Almighty Console \n \n";
	private  JTextArea textArea = new JTextArea(message);
	public final static String newLine = "\n";
	
	// For the gameplay
	private JPanel gamePanel = new JPanel();
	private GameMaster gameMaster = new GameMaster();
	private int roundNumber = 1;
	
	
	
	public GameLayout () {
		
		defineWelcomeElements();
		console();
		createGUI();
		
	}
	
	// Adding two panels to the main panel
	public void createGUI () {
		
		setLayout (new BorderLayout());
		
		add (welcomePanel, BorderLayout.CENTER);
		// add(label, BorderLayout.NORTH);
		add(textArea, BorderLayout.EAST);
		
	}
	
	
	// Setting up the start screen
	
	public void defineWelcomeElements() {
		
		welcome.setAlignmentX(CENTER_ALIGNMENT);
		
		input.setMaximumSize(new Dimension (200, 25));
		input.addActionListener(this);
		
		submit.setAlignmentX(CENTER_ALIGNMENT);
		submit.addActionListener(this);
		submit.setAlignmentY(CENTER_ALIGNMENT);
		
		
		welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
		
		welcomePanel.add(welcome);
		welcomePanel.add(input);
		welcomePanel.add(submit);
		
		welcomePanel.setMinimumSize(new Dimension(500, 500));
		welcomePanel.setPreferredSize(new Dimension(500, 500));
		
	}
	
	public int getMove () {
		int move = Integer.parseInt(input.getText());
		if (move < 0 || move > player.getEnergy()) {
			throw new IllegalArgumentException("Input must be between 0 and " + player.getEnergy());
		}
		else {
			sendToConsole("Something is kinda right." + move);
			return move;
		}
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (!gameStarted) {
		player = new HumanPlayer (input.getText());
		
		// Temp
		Player player1 = new Player("Bill");
		gameMaster.setPlayers(player1, null);
		player1.registerGameMaster(gameMaster);
		
		input.setText("");
		sendToConsole("Player " + player.getName() + " is added to the game.");
		if (gameMaster.getPlayer1() == null) {
			gameMaster.setPlayers(player, null);
			sendToConsole("You are player1. Waiting for player 2...");
		}
		else if (gameMaster.getPlayer2() == null) {
			gameMaster.setPlayers(gameMaster.getPlayer1(), player);
			enemy = player1;
			sendToConsole("You are player2. You're fighting against " + player1.getName());
		}
		else sendToConsole("ERROR: Both spots are somehow taken");
		
		player.registerGameMaster(gameMaster);
		createGameScreen();
		gameStarted = true;
		}
		else {
			enemy.makeNextMove(gameMaster.getPosition(), enemy.getEnergy(), player.getEnergy());
			gameMaster.listenToPlayerMove(player, getMove());
			input.setText("");
		}		
	}
	
	// ??
	/** {
			int move = Integer.parseInt(input.getText());
			if (move < 0 || move > player.getEnergy()) {
				throw new IllegalArgumentException("Input must be between 0 and " + player.getEnergy());
			}
			else {
				gameMaster.listenToPlayerMove(player, move);
			}
		}
	*/
	
	
	// Setting up the console
	
	public void console() {
		textArea.setEditable(false);
		textArea.setPreferredSize(new Dimension (500, 100));
		textArea.setBackground(Color.LIGHT_GRAY);
	}
	
	
	public void sendToConsole(String message) {
		textArea.append(message + newLine);
	}
	
	
	// Getting into the gaming screen
	
	public void createGameScreen () {
		
		gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout());
		
		
		// Adds round number, your energy level and opponent's energy level to the top.
		
		JLabel roundMessage = new JLabel (" Round: " + roundNumber + " ");
		roundMessage.setOpaque(true);
		roundMessage.setBackground(Color.BLACK);
		roundMessage.setForeground(Color.WHITE);
		
		JLabel yourEnergy = new JLabel ("Your energy: " + String.valueOf(player.getEnergy()) + " - ");
		yourEnergy.setOpaque(true);
		// yourEnergy.setBackground(Color.BLUE);
		
		// TODO Make sure this is actually opponent energy level
		JLabel opponentEnergy = new JLabel (" - Opponent's energy: " + String.valueOf(player.getEnergy()));
		yourEnergy.setOpaque(true);
		// yourEnergy.setBackground(Color.BLUE);
		
		topPanel.add(yourEnergy);
		topPanel.add(roundMessage);
		topPanel.add(opponentEnergy);
		
		
		gamePanel.add(topPanel);
		
		createArenaLabel("The current position is: " + gameMaster.getPosition());
		
		createArenaLabel("Choose your next move");
		
		actionBar();
		
		swapPanel (welcomePanel, gamePanel);
	}
	
	
	public void swapPanel (Component removed, Component added) {
		remove(removed);
		add (added, BorderLayout.CENTER);
		revalidate();
		repaint();
		
	}
	
	public void createArenaLabel(String message) {
		
		JPanel arenaPanel = new JPanel();
		arenaPanel.setLayout(new FlowLayout());

		JLabel m = new JLabel (message);
		arenaPanel.add(m);
		
		gamePanel.add(arenaPanel);
	}
	
	
	
	public void createActionPanel() {
		
		JPanel actionPanel = new JPanel();
		actionPanel.setLayout(new GridBagLayout());
		
		JLabel actionMessage = new JLabel (message);
		
		
		input.setMaximumSize(new Dimension (50, 25));
		input.setPreferredSize(new Dimension (50, 25));
		
		submit.addActionListener(this);

		
		actionPanel.add(actionMessage);
		actionPanel.add(input);
		actionPanel.add(submit);
		
		gamePanel.add(actionPanel);
	}
	
	
	public void actionBar() {
		
		JPanel actionPanel = new JPanel();
		actionPanel.setLayout(new FlowLayout());
		
		input.setMaximumSize(new Dimension (50, 25));
		input.setPreferredSize(new Dimension (50, 25));
		
		actionPanel.add(input);
		
		gamePanel.add(actionPanel);
		
	}

}
