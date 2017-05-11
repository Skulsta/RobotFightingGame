package no.uib.info233.v2017.vap003.oblig4.player;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestPlayer {
	
	private Player p1 = new Player("Bruce");
	private Player joe = new SmallJoe("Joey");

	@Before
	public void setUp() {
		
	}

	// Making sure the player name gets set when creating a new player.
	@Test
	public void testGetName() {
		
		assertEquals (p1.getName(), "Bruce");
		
		assertFalse (joe.getName() == null);
	}
}
