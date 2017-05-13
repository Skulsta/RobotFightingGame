package no.uib.info233.v2017.vap003.oblig4.game;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

public class WelcomeGUI extends JPanel implements ActionListener {
	
	private JTextField welcome;
	private JPanel typeNamePanel = new JPanel();
	private JPanel insertNamePanel = new JPanel();
	
	public WelcomeGUI () {
		super (new BorderLayout());
		
		welcome = new JTextField ("Welcome to Epic Robot Fighting Game!");
		welcome.setBounds(80, 40, 225, 20);
		welcome.setEditable(false);
		
	}
	
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
