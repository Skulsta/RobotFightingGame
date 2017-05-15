package GUI;

import javax.swing.JFrame;

public class GameFrame {

	public static void main(String[] args) {
		
		JFrame frame = new JFrame();
		
		GameLayout panel = new GameLayout();
			
		frame.setContentPane(panel);
		frame.setTitle("Danger Zone");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
