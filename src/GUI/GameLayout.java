package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import no.uib.info233.v2017.vap003.oblig4.game.GameMaster;
import no.uib.info233.v2017.vap003.oblig4.player.HumanPlayer;
import no.uib.info233.v2017.vap003.oblig4.player.Player;

public class GameLayout extends JPanel implements ActionListener{


	// private JPanel mainPanel = new JPanel();

	// For the start screen
	private JLabel welcome = new JLabel ("What's the player's name?");
	private JTextField input = new JTextField();
	private JButton submit = new JButton("Confirm");
	private Player player;
	private Player enemy;
	private JPanel welcomePanel = new JPanel();
	private boolean gameStarted;

	// For the console
	public final static String newLine = "\n";
	private static ConsoleGUI console = new ConsoleGUI();

	// For the gameplay
	private JPanel gamePanel = new JPanel();
	private GameMaster gameMaster = new GameMaster();
	
	private JLabel roundMessage;
	private JLabel yourEnergy;
	private JLabel opponentEnergy;
	
	
	// For the menu bar
	private JMenuBar menubar;
	private JMenu menu;
	private JMenuItem local;
	
	
	// For the real start screen
	private JPanel startscreen;
	private JLabel welcomeText = new JLabel ("Welcome! Select an option from the menu.");
	



	public GameLayout () {

		createMenuBar();
		defineWelcomeElements();
		createStartPanel();
		createGUI();

	}

	// Adding two panels to the main panel
	public void createGUI () {

		setLayout (new BorderLayout());

		add (welcomeText, BorderLayout.CENTER);
		// add(label, BorderLayout.NORTH);
		add(console, BorderLayout.EAST);

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
			move = 0;
			ConsoleGUI.sendToConsole("Pay attention to your energy level! You spent 0 points.");
		}
		input.setText("");
		return move;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (!gameStarted) {
			player = new HumanPlayer (input.getText());

			// Temp
			Player player1 = new Player("Bill");
			gameMaster.setPlayers(player1, null);
			player1.registerGameMaster(gameMaster);
			gameMaster.setInterface(this);

			input.setText("");
			ConsoleGUI.sendToConsole("Player " + player.getName() + " is added to the game." + newLine);
			if (gameMaster.getPlayer1() == null) {
				gameMaster.setPlayers(player, null);
				ConsoleGUI.sendToConsole("You are player1. Waiting for player 2...");
			}
			else if (gameMaster.getPlayer2() == null) {
				gameMaster.setPlayers(gameMaster.getPlayer1(), player);
				enemy = player1;
				ConsoleGUI.sendToConsole("Welcome " + player.getName() + "! \n" + "You are player 2. You're fighting against " +
				enemy.getName() + newLine + "Get your opponent to arena '-3' to win." + newLine +
				"If you get pushed back to arena 3, you lose." + newLine);
			}
			else ConsoleGUI.sendToConsole("ERROR: Both spots are somehow taken");

			player.registerGameMaster(gameMaster);
			createGameScreen();
			gameStarted = true;
		}
		else {
			try {
				int move = Integer.parseInt(input.getText());
				if (move >= 0 || move <= player.getEnergy()) {
					enemy.makeNextMove(gameMaster.getPosition(), enemy.getEnergy(), player.getEnergy());
					gameMaster.listenToPlayerMove(player, getMove());
				}
				else {
					throw new NumberFormatException();
				}
			}
			catch (NumberFormatException f) {
				input.setText("");
				ConsoleGUI.sendToConsole("Enter a valid number.");
			}
		}
	}



		// Getting into the gaming screen

		public void createGameScreen () {

			gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));

			JPanel topPanel = new JPanel();
			topPanel.setLayout(new FlowLayout());


			// Adds round number, your energy level and opponent's energy level to the top.

			roundMessage = new JLabel (" Round: " + gameMaster.getRound() + " ");
			roundMessage.setOpaque(true);
			roundMessage.setBackground(Color.BLACK);
			roundMessage.setForeground(Color.WHITE);

			yourEnergy = new JLabel ("Your energy: " + String.valueOf(player.getEnergy()) + " - ");
			yourEnergy.setOpaque(true);
			// yourEnergy.setBackground(Color.BLUE);

			// TODO Make sure this is actually opponent energy level
			opponentEnergy = new JLabel (" - Opponent's energy: " + String.valueOf(enemy.getEnergy()));
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
		
		public void updateGameScreen() {
			gamePanel.removeAll();
			createGameScreen();
			gamePanel.revalidate();
			gamePanel.repaint();
			input.requestFocus();
			
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

			JLabel actionMessage = new JLabel ("Hm.");


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
			
			JButton submitMove = new JButton("Submit");
			submitMove.addActionListener(this);

			actionPanel.add(input);
			actionPanel.add(submitMove);

			gamePanel.add(actionPanel);
		}
		
		
		// The menu bar.
		
		public void createMenuBar() {
			menubar = new JMenuBar();
			menu = new JMenu("Local");
			menubar.add(menu);
			local = new JMenuItem("New Game");
			local.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					swapPanel(welcomeText, welcomePanel);
					input.requestFocus();
				}
			});
			menu.add(local);
			local = new JMenuItem("Load Game");
			menu.add(local);
			
			menu = new JMenu("Online");
			menubar.add(menu);
			local = new JMenuItem("Host Game");
			menu.add(local);
			local = new JMenuItem("Join Game");
			menu.add(local);
			
			menubar.setVisible(true);
			
			
			
		}
		
		public JMenuBar getMenuBar() {
			return menubar;
		}
		
		
		public void createStartPanel() {
			/**
			startscreen = new JPanel ();
			startscreen.setLayout(new BorderLayout());
			startscreen.setMinimumSize(new Dimension(500, 500));
			startscreen.setPreferredSize(new Dimension(500, 500));
			
			JPanel startBox = new JPanel();
			startBox.setLayout(new BoxLayout(startBox, BoxLayout.Y_AXIS));
			*/
			

			// welcomeText.setEditable(false);
			welcomeText.setHorizontalAlignment(SwingConstants.CENTER);
			welcomeText.setPreferredSize(new Dimension(500, 500));
			
			
			// startscreen.add(welcomeText, BorderLayout.CENTER);
		}
	}
