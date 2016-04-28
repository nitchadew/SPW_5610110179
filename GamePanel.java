

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JPanel;

import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GamePanel extends JPanel {
	
	private BufferedImage bi;	
	Graphics2D big;
	Graphics2D bigfadeenemy;
	Graphics2D bigheal;
	ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	BufferedImage background;

	public GamePanel() {

		bi = new BufferedImage(400, 600, BufferedImage.TYPE_INT_ARGB);
		big = (Graphics2D) bi.getGraphics();
		bigfadeenemy = (Graphics2D) bi.getGraphics();
		bigheal = (Graphics2D) bi.getGraphics();
		//big.setBackground(Color.BLUE);
		try{
			background = ImageIO.read(new File("C:/Users/Administrator/Documents/GitHub/SPW_5610110179/space8bit.jpg"));
		}
		catch(IOException d){

		}
		
	}

	public void updateGameUI(GameEngine reporter){
		big.clearRect(0, 0, 400, 600);
		big.drawImage(background, 0, 0, 400, 600, null);
		big.setColor(Color.RED);
		big.drawString(String.format("%08d", reporter.getScore()), 310, 20);
		big.drawString(String.format("%d", reporter.getLevel()), 290, 20);
		big.drawString("LV",275, 20);

		for(Sprite s : sprites){
			if(s.width == 15 || s.height == 20)
				s.draw(big);
			else if(s.width == 5)
				s.draw(bigheal);
			else
				s.draw(bigfadeenemy);

		}
		
		repaint();
	}



	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(bi, null, 0, 0);
	}

}
