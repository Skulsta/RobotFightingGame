package no.uib.info233.v2017.vap003.oblig4.player;

import GUI.GameLayout;
import no.uib.info233.v2017.vap003.oblig4.game.GameMaster;

public class HumanPlayer extends Player {
	
	private GameMaster gameMaster;
	private GameLayout gameLayout;

	public HumanPlayer(String name) {
		super(name);
		
	}
	
	
	@Override
	public void makeNextMove(int currentPosition, int yourEnergy, int opponentEnergy) {
		
		int result = yourEnergy;
		
		if (yourEnergy > 0)
			result = gameLayout.getMove();
		
		gameMaster.listenToPlayerMove(this, result);
	}
}
