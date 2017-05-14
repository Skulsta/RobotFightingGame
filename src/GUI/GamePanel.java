package GUI;

import javax.swing.JLabel;
import javax.swing.JPanel;

import no.uib.info233.v2017.vap003.oblig4.player.Player;

public class GamePanel extends JPanel {
	
	private int roundNumber = 1;
	private JLabel roundMessage = new JLabel ("Round: " + roundNumber);
	private Player player1;
	private Player player2;
	
	
	public GamePanel() {
		
		roundMessage.setAlignmentX(CENTER_ALIGNMENT);
		createGUI();
	}
	
	public void createGUI() {
		
	}
	
	

}
