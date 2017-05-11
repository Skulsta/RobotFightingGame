package no.uib.info233.v2017.vap003.oblig4.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import no.uib.info233.v2017.vap003.oblig4.player.HumanPlayer;
import no.uib.info233.v2017.vap003.oblig4.player.Player;

public class GameGUI extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	
	private String message = "The Almighty Console \n \n";
	protected static JButton inputButton = new JButton("Confirm");
	protected JTextField textField = new JTextField("Type a name here:");
	protected JTextArea textArea = new JTextArea(message);
	private final static String newLine = "\n";


	public GameGUI() {
		
		initUI();
		
	}
		
		public void addComponentsToPane (Container pane) {
		//	Output text area
		textArea.setEditable(false);
		
		
		//	Input text area
		textField.setBackground(Color.DARK_GRAY);
		textField.setForeground(Color.CYAN);
		textField.addActionListener(this);
		
		
		//	Set content pane
		Container container = getContentPane();
		
		
		//	Add components to content pane
		textArea.setPreferredSize(new Dimension (500, 100));
		container.add(textArea, BorderLayout.EAST);
		container.add(textField, BorderLayout.SOUTH);
		container.add(inputButton, BorderLayout.CENTER);
		
		
		GameGUI.inputButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Player player = new HumanPlayer (textField.getText());
				textField.setText("");
				message = "Player " + player.getName() + " added to game.";
				sendMessage();
			}
		});
	}
	
	
	public void sendMessage() {
		textArea.append(message + newLine);
	}
        
	
	public static void main (String[] args) {
		EventQueue.invokeLater(() -> {
			GameGUI gui = new GameGUI();
			gui.setVisible(true);
		});
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		message = textField.getText();
		textField.setText("");
		textArea.append(message + newLine);
		
	}
	
	
	public void initUI() {
		//	create and set up the window
		JFrame frame = new JFrame ("Epic Robot Fighting Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//	Set up the content pane
		addComponentsToPane (frame.getContentPane());
		
		//	Display the window
		frame.pack();
		frame.setVisible(true);
	}


}
