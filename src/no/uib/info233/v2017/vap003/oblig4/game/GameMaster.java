package no.uib.info233.v2017.vap003.oblig4.game;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

import GUI.ConsoleGUI;
import GUI.GameLayout;
import no.uib.info233.v2017.vap003.oblig4.database.DatabaseScoreboard;
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

	// Keeps track of how many rounds the players has played in a game.
	private int round = 1;

	// The game id. Used by the database.
	private SecureRandom random = new SecureRandom();
	private String gameid;
	
	// Access the database.
	DatabaseScoreboard database;


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
		gameid = new BigInteger(130, random).toString(32);
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


		// Assigning the right move to the right player. Throw exception if not a valid player.
		if (player == player1) {
			playerOneMove = move;
		} else if (player == player2) {
			playerTwoMove = move;
		} else throw new IllegalArgumentException("Input must be 'player1' or 'player2'.");

		// If both players have called makeNextMove method, proceed.
		if ((playerOneMove != null) && (playerTwoMove != null))
			evaluateTurn();
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
			gameLayout.removeInputField();
			updateRanking();
		}

		else if ((player1.getEnergy() == 0) && (player2.getEnergy() == 0)) {

			System.out.println("------------");
			player1.gameOver(score + (positionPoints * position));
			player2.gameOver(score - (positionPoints * position));
			gameLayout.removeInputField();
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
	public void getLoadedGame(String gameid, String player1, String player2, Integer position,
			Integer playerOneEnergy, Integer playerTwoEnergy) {
		
		this.gameid = gameid;
		this.player1 = new Player(player1, playerOneEnergy);
		this.player2 = new Player(player2, playerTwoEnergy);
		this.position = position;
		this.round = 1;
		
		ConsoleGUI.sendToConsole("\n------------------------\nYou selected a game with gameid: " + gameid +
				"\n" + player1 + " vs " + player2 + " - Position: " + position +
				"\nGet your opponent to position 3");
		gameLayout.showLoadedGame();
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