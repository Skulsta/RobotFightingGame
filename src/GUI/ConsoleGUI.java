package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ConsoleGUI extends JPanel {
	
	private static final long serialVersionUID = 1738734736633198221L;
	private static String message = "The Almighty Console \n \n";
	private static JTextArea textArea;
	public final static String newLine = "\n";
	private JScrollPane scroll;
	
	
	public ConsoleGUI () {
		
		setLayout (new BorderLayout());
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBackground(Color.LIGHT_GRAY);
		textArea = new JTextArea(message);
		
		setMinimumSize(new Dimension(500, 100));
		setPreferredSize(new Dimension (500, 100));
		scroll = new JScrollPane(textArea);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(scroll, BorderLayout.CENTER);
		
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
