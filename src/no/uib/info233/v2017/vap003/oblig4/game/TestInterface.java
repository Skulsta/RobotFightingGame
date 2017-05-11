package no.uib.info233.v2017.vap003.oblig4.game;

import java.awt.EventQueue;

import org.junit.Before;
import org.junit.Test;

public class TestInterface {
	
	private GameGUI gui;

	@Before
	public void setUp() {
		this.gui = new GameGUI();
	}

	@Test
	public void testInterfaceBuilder() {
		EventQueue.invokeLater(() -> {
			gui.setVisible(true);
		});
	}
}
