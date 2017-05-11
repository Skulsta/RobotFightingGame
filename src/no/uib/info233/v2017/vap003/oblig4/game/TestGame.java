package no.uib.info233.v2017.vap003.oblig4.game;

import org.junit.Before;
import org.junit.Test;

import no.uib.info233.v2017.vap003.oblig3.Player;

public class TestGame {
	
	// Creating two equal leveled players
	private Player mike = new Player("Big Smash");
	private Player steve = new Player("Wha");
	
	// Assigning the GameMaster instance to a variable.
	GameMaster game = GameMaster.getGameMaster();

	
	// Sets the game and registers the players so everything is setup to start a game.
	@Before
	public void setUp() {
		game.setPlayers(mike, steve);
		mike.registerGameMaster(game);
		steve.registerGameMaster(game);
	}

	// Tests a whole game between two equally strong players.
	@Test
	public void testStartGame() {
		game.startGame();
	}
}