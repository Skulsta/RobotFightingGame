package no.uib.info233.v2017.vap003.oblig4.player;

import java.util.Random;

public class SmallJoe extends Player{

	public SmallJoe(String name) {
		super(name);
	}

	/**
	 * Override method in order to make this subclass more aggressive than the standard class.
	 * He will use between all and half of his remaining energy points when
	 * fighting in the middle circles.
	 */
	@Override
	public void makeNextMove (int currentPosition, int yourEnergy, int opponentEnergy) {
		
		int result = yourEnergy;
		
		// If result is an invalid value, set it to 0.
		if (result > getEnergy() || result < 0)
			result = 0;
		
		// If the player has no energy left, skip all of this and call listenToPlayerMove on the bottom.
		if (getEnergy() != 0) {
			
		Random rand = new Random();
		
		// If you have more energy than your opponent. Don't overkill.
		if (yourEnergy > opponentEnergy)
			result = opponentEnergy + 1;
		
		// Fighting on the middle circles of the field. That is; circle 1, 2 and 3.
			if (currentPosition < 4 && currentPosition > 0) {
					result = rand.nextInt(result + 1) + ((result + 1)/2);	// Be unpredictable. Spend between 0 and half of your remaining energy points.
					if (opponentEnergy < 2)	// If your opponent is out of energy or has just one point left, spend one energy point per round.
						result = 1;
			}
		}
		
		gameMaster.listenToPlayerMove(this, result);
	}

}

