package no.uib.info233.v2017.vap003.oblig4.game;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Circle extends JFrame {

	private static final long serialVersionUID = 1L;

	public Circle() {
        setSize(100, 100);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        Circle circle = new Circle();
    }
}

class CirclePanel extends JComponent {
	private static final long serialVersionUID = 1L;

	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillOval(0, 0, getWidth(), getHeight());  
    }
}