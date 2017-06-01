package no.uib.info233.v2017.vap003.oblig4.game;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import GUI.ConsoleGUI;
import GUI.GameLayout;
import no.uib.info233.v2017.vap003.oblig4.database.DatabaseScoreboard;
import no.uib.info233.v2017.vap003.oblig4.player.HumanPlayer;
import no.uib.info233.v2017.vap003.oblig4.player.Player;

/** The game arena is divided into 7 circles. The circle in the middle is number 2.
 * Each robot start at their own "-1" circle and want to reach their "5" circle, which is also
 * their opponents' "-1" circle. This is done to make game progress and calculating points easier.
 * 

 * @author skulstad
 *
 */

public class GameMaster {

	//Current position on the arena.
	private int position = 0;

	// Made static so the database class can access their 
	// names for searching through the ranking table
	private Player player1;
	private Player player2;

	// Variable for storing the player's move in a round.
	private Integer playerOneMove = null;
	private Integer playerTwoMove = null;

	private GameLayout gameLayout;

	private boolean inOnlineGame = false;
	// Keeps track of how many rounds the players has played in a game.
	private int round = 1;

	// The game id. Used by the database.
	private SecureRandom random = new SecureRandom();
	private String gameid;

	// Access the database.
	DatabaseScoreboard database;
	
	// Saving the player1id if joining an online game
	private String enteredPlayer1id;


	// Private constructor so that the class cannot be instantiated.
	public GameMaster() {
		this.database = new DatabaseScoreboard(this);
	}




	// Assign the players that are going to fight each other
	public void setPlayers (Player player1, Player player2) {
		this.player1 = player1;
		this.player2 = player2;
	}


	public void setInterface(GameLayout gameLayout) {
		this.gameLayout = gameLayout;
		gameid = new BigInteger(25, random).toString(32);
	}


	/** Sends a message to each of the players to come up with their next move.
	 *  Done by running "player." for each player */
	public void startGame() {
		player1.makeNextMove(position, player1.getEnergy(), player2.getEnergy());
		player2.makeNextMove(position, player2.getEnergy(), player1.getEnergy());
	}


	/** Each player use this method to communicate how much energy he
	 *  wants to use in the current turn.
	 * @param player the player that calls this method
	 * @param move the amount of energy he wants to use. Must be more than or
	 * equal to the available energy he has, invalid and set to 0 if not.
	 */
	public void listenToPlayerMove(Player player, int move) {
		
		if (inOnlineGame) {
			ConsoleGUI.sendToConsole("Round: " + round);
			if (player == player1) {
				String updatePlayerMove = "update game_in_progress set player_1_move = " + move + " where game_id = " + gameid;
				database.inOnlineGame(updatePlayerMove);
				playerOneMove = move;
				ConsoleGUI.sendToConsole("You are: " + player.getName() + ". Your move was: " + playerOneMove);
				ConsoleGUI.sendToConsole("\nThe last bit is still missing. Close but no cigar.");
				
				
			} else if (player == player2) {
				String updatePlayerMove = "update game_in_progress set player_2_move = " + move + " where game_id = " + gameid;
				database.inOnlineGame(updatePlayerMove);
				playerTwoMove = move;
				
				ConsoleGUI.sendToConsole("Player 2 has made a move.");
				ConsoleGUI.sendToConsole("You are: " + player.getName() + ". Your move was: " + playerOneMove);
				
			} else if (player != player1 && player != player2)
				ConsoleGUI.sendToConsole("You are none of the players somehow.");
		
		
		/**
		Thread getPlayerMoves = new Thread(new Runnable() {
			public void run() {
				// while ((!(playerOneMove >= 0)) || (!(playerTwoMove >= 0)))
					try {
						ConsoleGUI.sendToConsole("You got 7 seconds to choose your move.");
						Thread.sleep((long) 7000);
						String getPlayerMove = "select player_1_move, player_2_move from game_in_progress where game_id = '" + gameid + "'";
						database.inOnlineGameGetPlayerMove(getPlayerMove);
						
						ConsoleGUI.sendToConsole(player1.getName() + "Used " + playerOneMove + "Energy points.");
						ConsoleGUI.sendToConsole(player2.getName() + "Used " + playerTwoMove + "Energy points.");
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						ConsoleGUI.sendToConsole("");
					}
				}
			});
		
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);
		
		executor.execute(getPlayerMoves);
		
		*/
		}
		
		else {

		// Assigning the right move to the right player. Throw exception if not a valid player.
		if (player == player1) {
			playerOneMove = move;
		} else if (player == player2) {
			playerTwoMove = move;
		} else throw new IllegalArgumentException("Input must be 'player1' or 'player2'.");
		}
		
		
		// If both players have called makeNextMove method, proceed.
		if ((playerOneMove >= 0) && (playerTwoMove >= 0)) {
			evaluateTurn();
			ConsoleGUI.sendToConsole("Both players have made a move.");
		}
	}
	
	
	public void setPlayerMove(int playerOneMove, int playerTwoMove) {
		this.playerOneMove = playerOneMove;
		this.playerTwoMove = playerTwoMove;
	}


	/** Uses the info from .listenToPlayerMove to identify who won and
	 * update the players on the state of the game by running player.makeNextMove
	 * or player.gameOver. If the latter, also runs .updateRanking
	 */
	public void evaluateTurn() {

		float score = 0.5f;
		float positionPoints = 0.25f;



		// If the game is still not over

		// else {

		System.out.println("Round: " + round);
		ConsoleGUI.sendToConsole("\nRound: " + round);
		round++;

		// If player one used more energy than player two, player one takes one step forward, player two the opposite.
		if (playerOneMove > playerTwoMove) {
			position++;
		}

		// If player two used more energy than player one, player two takes one step forward, player two the opposite.
		else if (playerOneMove < playerTwoMove) {
			position--;
		}


		// The players' energy level is decreased by the amount they spent this round.
		player1.setEnergy(player1.getEnergy() - playerOneMove);
		player2.setEnergy(player2.getEnergy() - playerTwoMove);


		// Printing the new status of the game. Player position, energy spent and energy left.
		System.out.println(player1.getName() + " - Energy used: " + playerOneMove +
				" New position: " + position + " Energy left: " + player1.getEnergy());

		System.out.println(player2.getName() + " - Energy used: " + playerTwoMove +
				" New position: " + position + " Energy left: " + player2.getEnergy());	



		ConsoleGUI.sendToConsole(player1.getName() + " - Energy used: " + playerOneMove +
				" - New position: " + position + " Energy left: " + player1.getEnergy());

		ConsoleGUI.sendToConsole(player2.getName() + " - Energy used: " + playerTwoMove +
				" - New position: " + position + " Energy left: " + player2.getEnergy());


		// Updates the game layout with the new round and energy values.
		gameLayout.updateGameScreen();




		// PlayerMove is set to null so the listenToPlayerMove() method is ready to
		// listen to the players' next moves in the next round.
		playerOneMove = null;
		playerTwoMove = null;

		// startGame is called to start a new round.

		//  if the game is over; both players are out of energy or a player has been defeated.
		if ((position == -3 || position == 3)) {

			System.out.println("------------");
			player1.gameOver(score + (score * position));
			player2.gameOver(score - (score * position));
			gameLayout.gameOverScreen();
			updateRanking();
		}

		else if ((player1.getEnergy() == 0) && (player2.getEnergy() == 0)) {

			System.out.println("------------");
			player1.gameOver(score + (positionPoints * position));
			player2.gameOver(score - (positionPoints * position));
			gameLayout.gameOverScreen();
			updateRanking();
		}

		// Making sure nothing weird happens.
		else if (position < -3 || position > 3) 
			System.out.println("The robots are somehow outside of the arena.");
	}	


	/** Update the player rankings in the ranking table. Stored in
	 *  the remote mySQL database. Using the table "ranking" and columns
	 *  "player" and "score"
	 *  Calls a separate class that handles the database logic.
	 */
	public void updateRanking() {


		ConsoleGUI.sendToConsole("---------");
		ConsoleGUI.sendToConsole("Game over");
		ConsoleGUI.sendToConsole("---------");

		ConsoleGUI.sendToConsole(player1.getName() + " recieved: " + player1.getScore() + " points.");
		ConsoleGUI.sendToConsole(player2.getName() + " recieved: " + player2.getScore() + " points. \n");

		database.updateDatabseRanking(player1.getName(), player1.getScore());
		database.updateDatabseRanking(player2.getName(), player2.getScore());


		ConsoleGUI.sendToConsole("Top players: ");
		database.displayScoreboard();

		ConsoleGUI.sendToConsole("");
		if (player1.getScore() > player2.getScore())
			ConsoleGUI.sendToConsole(player1.getName() + " won!");
		else if (player2.getScore() > player1.getScore())
			ConsoleGUI.sendToConsole(player2.getName() + " won!");
		else
			ConsoleGUI.sendToConsole("It's a tie!");
	}

	// Save the game by saving the current state of the game to a database.
	public void saveGame() {
		database.saveGame(this);
	}


	// Load a game by giving a gameid as input.
	public void loadGame(String enteredGameid) {
		database.loadGame(enteredGameid);
	}


	// Assign the values from the database to the gameMaster
	public void getLoadedGame(String gameid, String player1, String player2, int position,
			int playerOneEnergy, int playerTwoEnergy) {

		this.gameid = gameid;
		this.player1 = new HumanPlayer(player1, playerOneEnergy);
		this.player2 = new Player(player2, playerTwoEnergy);
		this.position = position;
		this.round = 1;

		ConsoleGUI.sendToConsole("\n------------------------\nYou selected a game with gameid: " + gameid +
				"\n" + player1 + " vs " + player2 + " - Position: " + position +
				"\nGet your opponent to position 3");

		gameLayout.createNewPlayer();
		gameLayout.createGameScreen();
	}


	public void listOpenGames(){
		database.listOpenGames();
	}

	public void loadOpenGame (String enteredPlayer1id) {
		this.enteredPlayer1id = enteredPlayer1id;
		String addPlayerTwo = "update open_games set player_2 = '" + player2.getName() +
				"', player_2_random = '" + player2.getPlayerRandom() + "' where player_1_random = " + "'"
				+ enteredPlayer1id + "'";
		
		database.loadOpenGame(enteredPlayer1id, addPlayerTwo);
		
		database.startOnlineGame();
		
	}
	
	
	public void getIntoOnlineGame (ResultSet gameInfo) {
		
		try {
			gameid = gameInfo.getString(1);
			String playerOneName = gameInfo.getString(2);
			String playerTwoName = gameInfo.getString(3);
			position = gameInfo.getInt(4);
			int player_1_energy = gameInfo.getInt(5);
			int player_2_energy = gameInfo.getInt(6);
			// playerOneMove = gameInfo.getInt(7);
			// playerTwoMove = gameInfo.getInt(8);
			round = gameInfo.getInt(9);
			
			if (player1 == null)
				player1 = new HumanPlayer(playerOneName, player_1_energy);
			
			if (player2 == null)
				player2 = new HumanPlayer(playerTwoName, player_2_energy);
			
			inOnlineGame = true;
			
			ConsoleGUI.sendToConsole("\n" + player1.getName() + " is player 1.\n" + player2.getName() + 
					" is player2.\n" +
					"\nLet's fight!\n");
			
			gameLayout.setUpOnlineGame();
			
			ConsoleGUI.sendToConsole("Choose your move.");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void inOnlineGame() {
		String updatePlayerOne = "update game_in_progress set player_1_move = '" + playerOneMove;
	}

	
	
	public void hostOnlineGame () {
		ConsoleGUI.sendToConsole("Creating online game...");
		
		String createOpenGame = "insert into open_games values ('" + player1.getName() + "', '" + player1.getPlayerRandom() +
				"', NULL, NULL)";
		
		database.createOpenGame(createOpenGame);
		
	}
	
	
	public String startOnlineGame (String playerTwoId) {
		ConsoleGUI.sendToConsole("Starting the game.");
		
		String gameInProgress = "insert into game_in_progress values ('" + player1.getPlayerRandom() + playerTwoId +
				"', '" + player1.getName() + "', '" + player2.getName() + "', 0, " + player1.getEnergy() + ", " +
				player2.getEnergy() + ", NULL, NULL, 1)";
		
		
		
		return gameInProgress;
	}



		// Get method for player. Throws an exception if the player does not exist.
		public Player getPlayer(Player player) {
			if (player1.equals(player)) {
				return player1;
			} else if (player2.equals(player)) {
				return player2;
			}
			else player = null;
			
			return player;
		}


		public Player getPlayer1 () {
			return player1;
		}

		public Player getPlayer2 () {
			return player2;
		}

		public int getPosition() {
			return position;
		}

		public int getRound() {
			return round;
		}

		public String getGameid() {
			return gameid;
		}

		public void resetGame() {
			player1 = null;
			player2 = null;
			round = 1;
			position = 0;
		}

		public void listSavedGames() {
			database.listSavedGames();
		}
	}