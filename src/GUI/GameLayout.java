package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import no.uib.info233.v2017.vap003.oblig4.game.GameMaster;
import no.uib.info233.v2017.vap003.oblig4.player.HumanPlayer;
import no.uib.info233.v2017.vap003.oblig4.player.Player;

public class GameLayout extends JPanel implements ActionListener{

	
	private static final long serialVersionUID = 8945151505299015846L;
	// For the start screen
	private JLabel welcome = new JLabel ("What's the player's name?");
	private JTextField input = new JTextField();
	private JButton submit = new JButton("Confirm");
	private Player player;
	private Player enemy;
	private JPanel welcomePanel = new JPanel();
	private boolean gameStarted = false;

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
	
	
	private BorderLayout layout;


	// For the real start screen
	private JLabel welcomeText = new JLabel ("Welcome! Select an option from the menu.");


	public GameLayout () {

		createMenuBar();
		// defineWelcomeElements();
		createStartPanel();
		createGUI();
	}

	// Adding two panels to the main panel
	public void createGUI () {

		layout = new BorderLayout();
		setLayout (layout);

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


	/**
	 * The action to be performed when "Input" gets any input. This text field is used first when
	 * assigning a name to a new player, after this, the field is used to choose the next move.
	 * Input will then only accept integers. Otherwise, it casts an exception which it catches and
	 * gives the user instructions on what to do.
	 */
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



	/** Getting into the gaming screen. Includes info about play energy, round number,
	 * which field the players are currently at, an input field for choosing next move
	 * and a submit button. 
	 * Calls the "swapPanels" method in order to remove the welcome screen and get into the game screen.
	 */

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

		createArenaLabel("Choose your next move between 0 and " + player.getEnergy());

		actionBar();

		swapPanel (gamePanel);
		
		input.requestFocus();
	}

	
	/** Update the information on the game screen after each round. Updates the player's
	energy levels, round number and which field they are currently at.
	Does so by removing the old components, adding the updated components and calling
	revalidate and repaint. Also calls requestFocus so the input field is ready from the start. */
	public void updateGameScreen() {
		gamePanel.removeAll();
		createGameScreen();
		gamePanel.revalidate();
		gamePanel.repaint();
		input.requestFocus();

	}


	/**
	 * Used when switching from one panel to a new one.
	 * @param removed the component that is to be removed
	 * @param added the component that is added
	 * Swaps only the components on the CENTER position of the main panel. The only other part of
	 * the main panel is the console part at the EAST position, which should always be there.
	 */
	public void swapPanel (Component added) {

		remove(layout.getLayoutComponent(BorderLayout.CENTER));
		add (added, BorderLayout.CENTER);
		revalidate();
		repaint();
	}
	
	// When the game is over. Simply remove the game panel and add a label saying "Game Over". Center it.
	public void removeInputField() {
		JLabel gameOver = new JLabel("Game Over");
		gameOver.setHorizontalAlignment(SwingConstants.CENTER);
		gameOver.setPreferredSize(new Dimension(500, 500));
		swapPanel(gameOver);
	}

	// Used to create different informational messages on the gamePanel
	public void createArenaLabel(String message) {
		JPanel arenaPanel = new JPanel();
		arenaPanel.setLayout(new FlowLayout());

		JLabel m = new JLabel (message);
		arenaPanel.add(m);

		gamePanel.add(arenaPanel);
	}

	// Creates the part for user input for the game screen.
	public void actionBar() {

		JPanel actionPanel = new JPanel();
		actionPanel.setLayout(new FlowLayout());

		input = new JTextField();
		input.setMaximumSize(new Dimension (50, 25));
		input.setPreferredSize(new Dimension (50, 25));

		JButton submitMove = new JButton("Submit");
		submitMove.addActionListener(this);

		actionPanel.add(input);
		actionPanel.add(submitMove);
		
		input.addActionListener(this);
		input.requestFocus();

		gamePanel.add(actionPanel);
	}


	// The menu bar.
	public void createMenuBar() {
		menubar = new JMenuBar();
		menu = new JMenu("Local");
		menubar.add(menu);
		JMenuItem newGame = new JMenuItem("New Game");
		newGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gameStarted = false;
				gameMaster.resetGame();
				welcomePanel.removeAll();
				gamePanel.removeAll();
				defineWelcomeElements();
				swapPanel(welcomePanel);
				input.requestFocus();
			}
		});
		menu.add(newGame);
		
		JMenuItem saveGame = new JMenuItem("Save Game");
		saveGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// swapPanel(gamePanel, welcomePanel);
				gameMaster.saveGame();
			}
		});
		menu.add(saveGame);
		
		JMenuItem loadGame = new JMenuItem("Load Game");
		loadGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// swapPanel(gamePanel, welcomePanel);
				gameMaster.listSavedGames();
			}
		});
		menu.add(loadGame);

		menu = new JMenu("Online");
		menubar.add(menu);
		JMenuItem hostGame = new JMenuItem("Host Game");
		menu.add(hostGame);
		JMenuItem joinGame = new JMenuItem("Join Game");
		menu.add(joinGame);

		menubar.setVisible(true);

	}

	// Get method. Needed to add the menu to the Frame. Used in the GameFrame method.
	public JMenuBar getMenuBar() {
		return menubar;
	}


	// Creates the simple, first screen that the user sees. Tells them to use the menu.
	// Makes the game window have a decent size when opened, and centers the text.
	public void createStartPanel() {
		welcomeText.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeText.setPreferredSize(new Dimension(500, 500));
	}
	
	// Used for Game_ID in database... maybe.
	public long getSerialNumber() {
		return serialVersionUID;
	}
}
