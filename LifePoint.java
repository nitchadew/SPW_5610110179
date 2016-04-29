import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

public class LifePoint extends Sprite{
	private boolean alive = true;
	private boolean clashable = true;

	
	public LifePoint(int position_x, int position_y, int size_x, int size_y, String type) {
		super(position_x, position_y, size_x, size_y, type);
		
	}

	@Override
	public void draw(Graphics2D g) {

		g.setColor(Color.RED);
		g.fillRect(x, y, width, height);
		
	}

	public void hited(int damage){
		width = width - damage;
		if(x < 1){
			alive = false;
		}
	}	
	
	public boolean isAlive(){
		return alive;
	}

	public int getLifepoint(){
		return width;
	}
	public void setLifepoint(int set){
		if((width + set) > 100)
			width = 100;
		else
			width = width + set;
	}


}