
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.geom.Rectangle2D;
import javax.swing.JOptionPane;

import javax.swing.Timer;


public class GameEngine implements KeyListener, GameReporter{
	private GamePanel gp;		
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();	
	private SpaceShip v;	
	private Timer timer;
	private double generate = 0.1;
	private double biggenerate = 0.02;
	private double addgenerate  = 0.02;
	private double addbiggenerate = 0.002;
	private long score = 0;
	private long lifepoint = 10;
	private int generatecount = 0;
	private long diff = 0;
	private boolean triker = false;

	
	public GameEngine(GamePanel gp, SpaceShip v) {
		this.gp = gp;
		this.v = v;		
		
		gp.sprites.add(v);
		
		timer = new Timer(50, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				process();
			}
		});
		timer.setRepeats(true);
		
	}
	
	public void start(){
		timer.start();
	}
	

	private void generateEnemy(){
		Enemy e = new Enemy((int)(Math.random()*390), 30, 5, 10);
		gp.sprites.add(e);
		enemies.add(e);

	}


	private void bigGenerateEnemy(){
		Enemy e;
		if(Math.random() > 0.5)
			e = new Enemy((int)(Math.random()*390), 30, 30, 50);
		else
			e = new Enemy((int)(Math.random()*390), 30, 50, 30);
		gp.sprites.add(e);
		enemies.add(e);

	}

	private void process(){
		if(Math.random() < generate){
			generateEnemy();
		}

		if(Math.random() < biggenerate){
			bigGenerateEnemy();
		}


		Iterator<Enemy> e_iter = enemies.iterator();
		while(e_iter.hasNext()){
			Enemy e = e_iter.next();
			e.proceed();

			if(diff > score % 300)
				triker = true;

			diff = score % 300;


			if(!e.isAlive()){
				generatecount++;
				if(e.width == 5){
					score += 1;
					if( ( timer.getDelay() >= 10 ) && ( triker == true ) ){
						timer.setDelay(timer.getDelay() - 5);
						generate = generate + addgenerate;
						biggenerate = biggenerate + addbiggenerate;
						triker = false;
					}
				}	
					
				if(e.width == 30 || e.width == 50){
					score += 10;
					if( ( timer.getDelay() >= 10 ) && ( triker == true ) ){
						timer.setDelay(timer.getDelay() - 5);
						generate = generate + addgenerate;
						biggenerate = biggenerate + addbiggenerate;
						triker = false;
					}
				}	

				e_iter.remove();
				gp.sprites.remove(e);
					
			}
		}


		Rectangle2D.Double vr = v.getRectangle();
		Rectangle2D.Double er;

		for(Enemy e : enemies){
			er = e.getRectangle();
			if(er.intersects(vr)){
				if (e.getClashable() == true && e.width == 5){
					lifepoint = lifepoint - 1;
					e.setClashable(false);
				}
				else if (e.getClashable() == true && ( (e.width == 30) ||  (e.width == 50) )){
					lifepoint = lifepoint - 4;
					e.setClashable(false);
				}
					
				if (lifepoint < 1) 
					die();
				return;

				
			}
		}	
		gp.updateGameUI(this);
	}

	public void die(){
		gp.updateGameUI(this);
		JOptionPane.showMessageDialog( null, "YOU LOSE WITH " + score +" SCORE!" + "\nYOU DODGE " + generatecount + " GENERATE!");
		timer.stop();
	}
	
	public void controlVehicle(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			v.move(-1);
			break;
		case KeyEvent.VK_RIGHT:
			v.move(1);
			break;
		}
	}

	public long getScore(){
		return score;
	}
	
	public long getLifepoint(){
		if(lifepoint < 0)
			return 0;
		else
			return lifepoint;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		controlVehicle(e);
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//do nothing
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//do nothing		
	}

}