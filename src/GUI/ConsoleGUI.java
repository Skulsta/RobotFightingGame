package GUI;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JTextArea;

public class ConsoleGUI {
	
	private static String message = "The Almighty Console \n \n";
	private static JTextArea textArea = new JTextArea(message);
	public final static String newLine = "\n";
	
	
	public ConsoleGUI () {
		
		textArea.setEditable(false);
		textArea.setPreferredSize(new Dimension (500, 100));
		textArea.setBackground(Color.LIGHT_GRAY);
		
	}
	
	
	public static void sendToConsole(String message) {
		textArea.append(message + newLine);
	}
	
	
	public JTextArea getConsole() {
		return textArea;
	}
	
	
	public JTextArea getTextArea () {
		return textArea;
	}
	
	
	public String getMessage() {
		return message;
	}

}
