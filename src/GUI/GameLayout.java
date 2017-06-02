package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.security.SecureRandom;

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
	private JTextField input = new JTextField();
	private JButton submit = new JButton("Confirm");
	private Player player;
	private Player enemy;
	private JPanel insertNamePanel = new JPanel();
	private boolean gameStarted = false;
	private boolean joinOnlineGame = false;
	private boolean hostOnlineGame = false;
	private boolean inOnlineGame = false;

	// For the console
	public final static String newLine = "\n";
	private static ConsoleGUI console = new ConsoleGUI();

	// For the gameplay
	private JPanel gamePanel = new JPanel();
	private GameMaster gameMaster = new GameMaster();

	private JLabel roundMessage;
	private JLabel yourEnergy;
	private JLabel opponentEnergy;

	// Used for hosting and joining online games.
	private String playerRandom;
	private String enemyRandom;

	// For the menu bar
	private JMenuBar menubar;
	private JMenu menu;


	// For loading game, could have reused "input" and "submit"
	private JTextField loadField;
	private JButton loadButton;
	private JPanel textfieldPanel;


	private BorderLayout layout;


	// For the real start screen
	private JLabel welcomeText = new JLabel ("Welcome! Select an option from the menu.");


	public GameLayout () {

		gameMaster.setInterface(this);
		input.setMaximumSize(new Dimension (200, 25));
		input.addActionListener(this);

		submit.setAlignmentX(CENTER_ALIGNMENT);
		submit.setAlignmentY(CENTER_ALIGNMENT);
		submit.addActionListener(this);

		createMenuBar();
		createStartPanel();
		createGUI();
	}

	// Adding two panels to the main panel
	public void createGUI () {

		layout = new BorderLayout();
		setLayout (layout);

		add (welcomeText, BorderLayout.CENTER);
		add(console, BorderLayout.EAST);

	}


	// Panel for entering player name

	public void createNameInputPanel() {

		JLabel instructions = new JLabel ("Choose a name");

		instructions.setAlignmentX(CENTER_ALIGNMENT);


		insertNamePanel.setLayout(new BoxLayout(insertNamePanel, BoxLayout.Y_AXIS));

		insertNamePanel.add(instructions);
		insertNamePanel.add(input);
		insertNamePanel.add(submit);

		insertNamePanel.setMinimumSize(new Dimension(500, 500));
		insertNamePanel.setPreferredSize(new Dimension(500, 500));
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

	public void createNewPlayer() {

		// Assign player 1.
		if (gameMaster.getPlayer1() != null)
			player = gameMaster.getPlayer1();
		else
			player = new HumanPlayer (input.getText());
		ConsoleGUI.sendToConsole("\nPlayer " + player.getName() + " is added to the game.");

		// Assign player 2.
		if (gameMaster.getPlayer2() != null)
			enemy = gameMaster.getPlayer2();
		else
			enemy = new Player("CPU");
		ConsoleGUI.sendToConsole("Player " + enemy.getName() + " is added to the game." + newLine);

		// The formalities..
		gameMaster.setPlayers(player, enemy);
		player.registerGameMaster(gameMaster);
		enemy.registerGameMaster(gameMaster);

		input.setText("");
	}


	public void makeNextMove() {
		try {
			int move = Integer.parseInt(input.getText());
			if (move >= 0 || move <= player.getEnergy()) {
				// enemy.makeNextMove(gameMaster.getPosition(), enemy.getEnergy(), player.getEnergy());
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



	/** Getting into the gaming screen. Includes info about play energy, round number,
	 * which field the players are currently at, an input field for choosing next move
	 * and a submit button. 
	 * Calls the "swapPanels" method in order to remove the welcome screen and get into the game screen.
	 */

	public void createGameScreen () {
		
		gameStarted = true;

		if (gamePanel != null)
			gamePanel.removeAll();

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

		opponentEnergy = new JLabel (" - Opponent's energy: " + String.valueOf(enemy.getEnergy()));
		yourEnergy.setOpaque(true);

		topPanel.add(yourEnergy);
		topPanel.add(roundMessage);
		topPanel.add(opponentEnergy);


		gamePanel.add(topPanel);

		createArenaLabel("The current position is: " + gameMaster.getPosition());

		createArenaLabel("Choose your next move between 0 and " + player.getEnergy());

		actionBar();

		gameStarted = true;

		swapPanel (gamePanel);
		
		gamePanel.revalidate();
		gamePanel.repaint();

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
	public void gameOverScreen() {
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
		menubar.setVisible(true);
		menu = new JMenu("Local");
		menubar.add(menu);
		
		// New Game
		JMenuItem newGame = new JMenuItem("New Game");
		menu.add(newGame);
		newGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameStarted = false;
				gameMaster.resetGame();
				insertNamePanel.removeAll();
				gamePanel.removeAll();
				createNameInputPanel();
				swapPanel(insertNamePanel);
				input.requestFocus();
			}
		});
		
		// Save Game
		JMenuItem saveGame = new JMenuItem("Save Game");
		menu.add(saveGame);
		saveGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameMaster.saveGame();
			}
		});
		
		// Load Game
		JMenuItem loadGame = new JMenuItem("Load Game");
		menu.add(loadGame);
		loadGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameMaster.listSavedGames();
				gameStarted = false;
				swapPanel(loadField("Copy your gameid from the console and paste it here"));
				loadGameScreen();
			}
		});

		// Online menus
		menu = new JMenu("Online");
		menubar.add(menu);
		
		// Host Game
		JMenuItem hostGame = new JMenuItem("Host Game");
		menu.add(hostGame);
		hostGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameStarted = false;
				hostOnlineGame = true;
				insertNamePanel.removeAll();
				createNameInputPanel();
				swapPanel(insertNamePanel);
				input.requestFocus();
			}
		});
		
		// Join Game
		JMenuItem joinGame = new JMenuItem("Join Game");
		menu.add(joinGame);
		joinGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameStarted = false;
				joinOnlineGame = true;
				insertNamePanel.removeAll();
				createNameInputPanel();
				swapPanel(insertNamePanel);
				input.requestFocus();
			}
		});

	}

	public JPanel loadField(String instructions) {

		// Creating panels to make components get centered.
		JPanel loadPanel = new JPanel();
		loadPanel.setLayout(new BorderLayout());
		loadPanel.setPreferredSize(new Dimension(500, 500));

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));


		JLabel loadInstructions = new JLabel(instructions);
		loadInstructions.setPreferredSize(new Dimension(500, 200));
		loadInstructions.setHorizontalAlignment(SwingConstants.CENTER);


		// Create an input text field.
		Dimension textDimension = new Dimension(200, 25);
		loadField = new JTextField(20);
		loadField.setPreferredSize(textDimension);
		loadField.setMinimumSize(textDimension);
		loadField.setMaximumSize(textDimension);

		textfieldPanel = new JPanel();
		textfieldPanel.add(loadInstructions);
		textfieldPanel.add(loadField);

		loadButton = new JButton ("Submit");
		loadButton.setAlignmentX(CENTER_ALIGNMENT);

		textfieldPanel.add(loadButton);

		inputPanel.add(textfieldPanel);

		loadPanel.add(inputPanel,  BorderLayout.CENTER);

		return loadPanel;
	}


	// Stupid solution part 1 of 2.
	// Load a local game
	// Adds an action to the button and text field in the loadField method.
	// Makes a search for game ID when loading a game.
	public void loadGameScreen() {
		loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gameMaster.loadGame(loadField.getText());
				loadField.setText("");
			}
		});

		loadField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gameMaster.loadGame(loadField.getText());
				loadField.setText("");
			}
		});
	}


	// Stupid solution part 2 of 2.
	// Load a online game
	// Adds an action to the button and text field in the loadField method.
	// Makes a search for player ID when joining an online game.
	public void loadOnlineGameScreen () {
		loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gameMaster.loadOpenGame(loadField.getText());
				loadField.setText("");
			}
		});

		loadField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gameMaster.loadOpenGame(loadField.getText());
				loadField.setText("");
			}
		});
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
	
	
	public void setUpOnlineGame () {
		ConsoleGUI.sendToConsole("Show this shit");
		
		if (joinOnlineGame) {
			enemy = gameMaster.getPlayer1();
			player = gameMaster.getPlayer2();
			ConsoleGUI.sendToConsole("Welcome " + player.getName() + ". Player 1 is: " + enemy.getName());
		}
		
		if (hostOnlineGame) {
			player = gameMaster.getPlayer1();
			enemy = gameMaster.getPlayer2();
			ConsoleGUI.sendToConsole("Welcome " + player.getName() + ". Player 2 is: " + enemy.getName());
		}
		else {
			player = gameMaster.getPlayer1();
			enemy = gameMaster.getPlayer2();
		}
		
		
		createGameScreen();
	}
	
	
	public boolean getGameStarted() {
		return gameStarted;
	}
	
	
	public boolean getInOnlineGame() {
		return inOnlineGame;
	}
	
	public void setPlayer (Player player) {
		this.player = player;
	}
	
	
	public boolean getJoinOnlineGame() {
		return joinOnlineGame;
	}
	
	public boolean getHostOnlineGame() {
		return hostOnlineGame;
	}
	



	// Creates a new player with name from input. Then starts a game
	// If in a game, input is used to define next move.
	@Override
	public void actionPerformed(ActionEvent e) {

		// Making sure an empty input is not passed.
		if (input.getText() == null) {
			ConsoleGUI.sendToConsole("Input cannot be empty.");
		}
		else {

				// If a game has started
			if (gameStarted) 
				makeNextMove();

			// If trying to join an online game, expect player_1_id input.
			else {
				if (inOnlineGame) {
					makeNextMove();
				}
				else if (joinOnlineGame) {
					enemy = new HumanPlayer(input.getText());
					gameMaster.setPlayers(null, enemy);
					swapPanel(loadField("Copy and paste one of the IDs from the console."));
					gameMaster.listOpenGames();
					loadOnlineGameScreen();
				}
				else if (hostOnlineGame) {
					player = new HumanPlayer(input.getText());
					gameMaster.setPlayers(player, null);
					swapPanel(loadField("Want to play with a friend? Give him/her the id below"));
					textfieldPanel.remove(loadButton);
					loadField.setText(gameMaster.getPlayer1().getPlayerRandom());
					loadField.setEditable(false);
					loadField.setHorizontalAlignment(JTextField.CENTER);
					gameMaster.hostOnlineGame();
				}
				
				// If starting a new local game.
				else {
					createNewPlayer();
					createGameScreen();
				}
			}
		}
	}
}
