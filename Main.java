
import java.awt.*;
import javax.swing.JFrame;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args){
		JFrame frame = new JFrame("TEST");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 600);
		frame.getContentPane().setLayout(new BorderLayout());

		SpaceShip s = new SpaceShip(180, 500, 20, 20);
		GamePanel g = new GamePanel();
		g.sprites.add(s);
		g.updateGameUI();

		frame.getContentPane().add(g, BorderLayout.CENTER);
		frame.setVisible(true);
		
	
		
		
	}
}
