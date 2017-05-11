package no.uib.info233.v2017.vap003.oblig4.player;

import java.util.Random;

import no.uib.info233.v2017.vap003.oblig4.game.GameMaster;

public class Player {
	
	private String name;
	private float score;
	private int energy = 10;
	protected GameMaster gameMaster;
	
	/**
	 * A constructor that creates a player with the specified name.
	 * The robot gets a default energy level of 10.
	 * @param name the name of the player.
	 */
	public Player (String name) {
		this.name = name;
	}
	
	/** This constructor takes in both name and energy level as parameters.
	 * @param name the name of the player
	 * @param energy the energy level of the robot.
	 */
	public Player (String name, int energy) {
		this.name = name;
		this.energy = energy;
	}
	
	
	// Register the gameMaster
	public void registerGameMaster(GameMaster gameMaster) {
		this.gameMaster = GameMaster.getGameMaster();
	}
	
	
	/** Figure out how much energy the player wants to spend based on the current
	 * state of the game. Call gameMaster.listenToPlayerMove to inform the gameMaster
	 * about the players choice.
	 * The game starts at circle 2 and ends at circle 5 or -1 depending on which players' view.
	 * A total of 7 circles.
	 * Their decision making could be more optimized. There is for example a chance they will spend all
	 * their points in the first round, which is just plain stupid.
	 * @param currentPosition the players current location
	 * @param yourEnergy the available energy
	 * @param opponentEnergy the opponents' available energy
	 */
	public void makeNextMove (int currentPosition, int yourEnergy, int opponentEnergy) {

		int result = yourEnergy;
		
		// If result is an invalid value, set it to 0.
		if (result > energy || result < 0)
			result = 0;
		
		// If the player has no energy left, skip all of this and call listenToPlayerMove on the bottom.
		if (energy != 0) {
			
		Random rand = new Random();
		
		// If you have more energy than your opponent. Don't overkill.
		if (yourEnergy > opponentEnergy)
			result = opponentEnergy + 1;
		
		// Fighting on the middle circles of the field. That is; circle -1, 0, 1.
			if (currentPosition < 2 && currentPosition > -2) {
					result = rand.nextInt(result + 1) + (0);	// Be unpredictable. Spend between 0 and all your points.
					if (opponentEnergy < 2)	// If your opponent is out of energy or has just one point left, spend one energy point per round.
						result = 1;
			}
		}
		
		gameMaster.listenToPlayerMove(this, result);
	}
	
	
	/** Informs the player that the game has come to an end and how many points
	 * he earned in this game.
	 * @param earnedPoints the amount of points earned in the game
	 * @return 
	 */
	public void gameOver (float earnedPoints) {
		
		score = earnedPoints;
		
		System.out.println(this.getName() + " recieved: " + earnedPoints + " points!");
	}


	// The method reduces the robots' energy level at the end of each round. Used by the GameMaster class.
	public void setEnergy(int energy) {
		this.energy = energy;
	}
	
	
	// The rest of the code is just three getters.
	
	public String getName() {
		return name;
	}
	
	public int getEnergy() {
		return energy;
	}
	
	
	public float getScore() {
		return score;
	}
}
