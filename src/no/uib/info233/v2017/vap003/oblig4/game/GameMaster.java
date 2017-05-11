package no.uib.info233.v2017.vap003.oblig4.game;
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
	
	// Create an object of GameMaster
	private static GameMaster gameMaster = new GameMaster();
	
	//Current position on the arena.
	private int position = 0;
	
	// Made static so the database class can access their 
	// names for searching through the ranking table
	private Player player1;
	private Player player2;
	
	// Variable for storing the player's move in a round.
	private Integer playerOneMove = null;
	private Integer playerTwoMove = null;
	
	// Keeps track of how many rounds the players has played in a game.
	private int round = 1;
	
	
	// Private constructor so that the class cannot be instantiated.
	private GameMaster() {
		
	}
	
	
	// Get the only object available
	public static GameMaster getGameMaster() {
		return gameMaster;
	}
	
	
	// Assign the players that are going to fight each other
	public void setPlayers (Player player1, Player player2) {
		this.player1 = player1;
		this.player2 = player2;
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
		
		//  if the game is over; both players are out of energy or a player has been defeated.
		if ((player1.getEnergy() == 0) && (player2.getEnergy() == 0)) {
			
			float positionPoints = 0.25f;
			player1.gameOver(score + (positionPoints * position));
			player2.gameOver(score - (positionPoints * position));
			updateRanking();
		}
		
		
		else if ((position == -3 || position == 3)) {
				player1.gameOver(score + (0.5f * position));
				player2.gameOver(score - (0.5f * position));
				updateRanking();
		}
		
		
		else if (position < -3 || position > 3) 
			System.out.println("The robots are somehow outside of the arena.");

		
		
		// If the game is still not over
		
		else {
			
			System.out.println("Round: " + round);
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
		
		
		// PlayerMove is set to null so the listenToPlayerMove() method is ready to
		// listen to the players' next moves in the next round.
		playerOneMove = null;
		playerTwoMove = null;
		
		// startGame is called to start a new round.
		startGame();
		
		}	
	}
	
	
	/** Update the player rankings in the ranking table. Stored in
	 *  the remote mySQL database. Using the table "ranking" and columns
	 *  "player" and "score"
	 *  Calls a separate class that handles the database logic.
	 */
	public void updateRanking() {
		
		System.out.println("---------");
		System.out.println("Game over");
		System.out.println("---------");
		
		DatabaseScoreboard database = new DatabaseScoreboard();
		
		System.out.println("Old Scoreboard: ");
		database.displayScoreboard();
		System.out.println();
		
		database.updateDatabseRanking(player1);
		database.updateDatabseRanking(player2);
		
		System.out.println("The score is now: ");
		database.displayScoreboard();
		
	}
	
	
	// Get method for player. Throws an exception if the player does not exist.
	public Player getPlayer(Player player) {
		if (player1.equals(player)) {
			return player1;
		} else if (player2.equals(player)) {
			return player2;
		}
		else throw new IllegalArgumentException("Not a valid input");
	}
}