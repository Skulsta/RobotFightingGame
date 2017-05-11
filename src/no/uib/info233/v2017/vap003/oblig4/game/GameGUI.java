package no.uib.info233.v2017.vap003.oblig4.game;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class GameGUI extends JFrame{
	

	public GameGUI() {
		
		initUI();
	}
	
	
	private void initUI() {
		
		JButton quitButton = new JButton ("Quit");
		
		quitButton.addActionListener((ActionEvent event) -> {
			System.exit(0);
		});
		
		createLayout (quitButton);
		
		setTitle("Quit button");
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
}
