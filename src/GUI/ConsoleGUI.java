package GUI;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JTextArea;

public class ConsoleGUI {
	
	private String message = "The Almighty Console \n \n";
	private JTextArea textArea = new JTextArea(message);
	private final static String newLine = "\n";
	
	
	public ConsoleGUI () {
		
		textArea.setEditable(false);
		textArea.setPreferredSize(new Dimension (500, 100));
		textArea.setBackground(Color.LIGHT_GRAY);
		
	}
	
	
	public void sendToConsole(String message) {
		textArea.append(message + newLine);
	}
	
	
	public JTextArea getConsole() {
		return textArea;
	}

}
