import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

public class Bonusscore extends Sprite{
	public static final int Y_TO_FADE = 400;
	public static final int Y_TO_DIE = 600;
	private int step = 22;
	private boolean alive = true;
	private boolean clashable = true;

	
	public Bonusscore(int position_x, int position_y, int size_x, int size_y, String type) {
		super(position_x, position_y, size_x, size_y, type);
		
	}

	@Override
	public void draw(Graphics2D g) {
		if(y < Y_TO_FADE)
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		else{
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 
					(float)(Y_TO_DIE - y)/(Y_TO_DIE - Y_TO_FADE)));
		}
		g.setColor(Color.ORANGE);
		g.fillRect(x, y, width, height);
		
	}

	public void proceed(){
		y += step;
		if(y > Y_TO_DIE){
			alive = false;
		}
	}
	
	public boolean isAlive(){
		return alive;
	}

	public boolean getClashable(){
		return clashable;
	}

	public void setClashable(boolean set){
		clashable = set;
	}

}