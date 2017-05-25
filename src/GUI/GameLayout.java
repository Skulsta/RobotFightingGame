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
	
	
	// For loading game, could have reused "input" and "submit"
	private JTextField loadField;
	private JButton loadButton;
	
	
	private BorderLayout layout;


	// For the real start screen
	private JLabel welcomeText = new JLabel ("Welcome! Select an option from the menu.");


	public GameLayout () {

		gameMaster.setInterface(this);
		input.setMaximumSize(new Dimension (200, 25));
		input.addActionListener(this);

		submit.setAlignmentX(CENTER_ALIGNMENT);
		submit.addActionListener(this);
		submit.setAlignmentY(CENTER_ALIGNMENT);
		
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
		add(console, BorderLayout.EAST);

	}


	// Setting up the start screen

	public void defineWelcomeElements() {

		welcome.setAlignmentX(CENTER_ALIGNMENT);


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
			createNewPlayer();
		}
		
		else {
			makeNextMove();
		}
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

		gameStarted = true;
		createGameScreen();
	}
	
	
	public void makeNextMove() {
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

		opponentEnergy = new JLabel (" - Opponent's energy: " + String.valueOf(enemy.getEnergy()));
		yourEnergy.setOpaque(true);

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
				gameMaster.listSavedGames();
				swapPanel(loadField());
				loadGameScreen();
			}
		});
		menu.add(loadGame);

		menu = new JMenu("Online");
		menubar.add(menu);
		JMenuItem hostGame = new JMenuItem("Host Game");
		menu.add(hostGame);
		JMenuItem joinGame = new JMenuItem("Join Game");
		joinGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gameMaster.listOpenGames();
				swapPanel(loadField());
				// loadGameScreen();
			}
		});
		menu.add(joinGame);

		menubar.setVisible(true);

	}
	
	public JPanel loadField() {
		JPanel loadPanel = new JPanel();
		loadPanel.setLayout(new BorderLayout());
		loadPanel.setPreferredSize(new Dimension(500, 500));
		
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
		
		
		JLabel loadInstructions = new JLabel("Copy your gameid from the console and paste it here");
		loadInstructions.setPreferredSize(new Dimension(500, 200));
		loadInstructions.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		// Create an input text field.
		Dimension textDimension = new Dimension(200, 25);
		loadField = new JTextField(20);
		loadField.setPreferredSize(textDimension);
		loadField.setMinimumSize(textDimension);
		loadField.setMaximumSize(textDimension);
		
		JPanel textfieldPanel = new JPanel();
		textfieldPanel.add(loadInstructions);
		textfieldPanel.add(loadField);

		loadButton = new JButton ("Submit");
		loadButton.setAlignmentX(CENTER_ALIGNMENT);
		
		textfieldPanel.add(loadButton);
		
		inputPanel.add(textfieldPanel);
		
		loadPanel.add(inputPanel,  BorderLayout.CENTER);

		return loadPanel;
		}
	
	
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
	
	
	/** 
	public void loadOpenGame() {
		loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gameMaster.openGames(loadField.getText());
				loadField.setText("");
			}
		});
		
		loadField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gameMaster.openGames(loadField.getText());
				loadField.setText("");
			}
		});
	}
	*/
	


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
	
	
	public void showLoadedGame() {
		gamePanel.removeAll();
		createNewPlayer();
	}
}
