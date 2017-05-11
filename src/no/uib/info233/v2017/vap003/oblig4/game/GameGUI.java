package no.uib.info233.v2017.vap003.oblig4.game;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class GameGUI extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private GameMaster gameMaster;


	public GameGUI() {
		initUI();
	}


	private void initUI() {

		
		// Exit button
		JButton quitButton = new JButton ("Quit");

		quitButton.addActionListener((ActionEvent event) -> {
			System.exit(0);
		});
		
		
		// Start Game
		JButton startGame = new JButton ("Start Game");
		
		startGame.addActionListener((ActionEvent event) -> {
			gameMaster.startGame();
		});

		createLayout (quitButton, startGame);

		setTitle("Epic Robot Fighting Game");
		setSize(800, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void createLayout (JComponent... arg) {

		Container pane = getContentPane();
		GroupLayout gl = new GroupLayout(pane);
		pane.setLayout(gl);

		gl.setAutoCreateContainerGaps(true);

		gl.setHorizontalGroup(gl.createSequentialGroup().addComponent(arg[0])
				);

		gl.setVerticalGroup(gl.createSequentialGroup().addComponent(arg[0])
				);
	}

	
	public static void main (String[] args) {
		EventQueue.invokeLater(() -> {
			GameGUI gui = new GameGUI();
			gui.setVisible(true);
		});
	}
}
