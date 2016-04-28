
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.awt.geom.Rectangle2D;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.io.*;
import java.lang.*;


public class GameEngine implements KeyListener{
	private GamePanel gp;		
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private ArrayList<Heal> heals = new ArrayList<Heal>();		
	private SpaceShip v;	
	private Timer timer;
	private String name;
	private String reader;
	private String topname;
	private int readscore;
	private int topscore;
	private LifePoint life;
	private int level = 1;
	private double healitem= 0.003;
	private double generate = 0.1;
	private double biggenerate = 0.01;
	private double addhealitem= 0.001;
	private double addgenerate  = 0.02;
	private double addbiggenerate = 0.002;
	private long score = 0;
	private int generatecount = 0;
	private long diff = 0;
	private boolean triker = false;


	
	public GameEngine(GamePanel gp, SpaceShip v) {
		this.gp = gp;
		this.v = v;		
		
		gp.sprites.add(v);

		life = new LifePoint(10, 10, 100, 20);
		gp.sprites.add(life);
		
		timer = new Timer(50, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				process();
			}
		});
		timer.setRepeats(true);
		
	}
	
	public void start(){
		name = JOptionPane.showInputDialog("Enter Your Name");
		timer.start();
	}
	

	private void generateEnemy(){
		Enemy e = new Enemy((int)(Math.random()*390), 30, 5, 10);
		gp.sprites.add(e);
		enemies.add(e);

	}

	private void generateHeal(){
		Heal h = new Heal((int)(Math.random()*390), 30, 10, 10);
		gp.sprites.add(h);
		heals.add(h);

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
		if(Math.random() < healitem){
			generateHeal();
		}

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
						healitem = healitem + addhealitem;
						triker = false;
						level++;
					}
				}	
					
				if(e.width == 30 || e.width == 50){
					score += 10;
					if( ( timer.getDelay() >= 10 ) && ( triker == true ) ){
						timer.setDelay(timer.getDelay() - 5);
						generate = generate + addgenerate;
						biggenerate = biggenerate + addbiggenerate;
						healitem = healitem + addhealitem;
						triker = false;
						level++;
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
					life.hited(10);
					e.setClashable(false);
				}
				else if (e.getClashable() == true && ( (e.width == 30) ||  (e.width == 50) )){
					life.hited(40);
					e.setClashable(false);
				}
					
				if (life.getLifepoint() < 1) 
					die();
				
			}
		}	
		
		Iterator<Heal> h_iter = heals.iterator();
		while(h_iter.hasNext()){
			Heal h = h_iter.next();
			h.proceed();
			if(!h.isAlive()){
					gp.sprites.remove(h);
					h_iter.remove();
			}
			
			er = h.getRectangle();
			if (er.intersects(vr)){
				gp.sprites.remove(h);
				h_iter.remove();
				if(life.getLifepoint() < 100)
					life.setLifepoint(20);
			}

		}	
				gp.updateGameUI(this);
	}

	public void die(){
		gp.updateGameUI(this);


		try {
     		FileWriter writer = new FileWriter("topscore.txt",true);
     		BufferedWriter out = new BufferedWriter(writer);
      		out.write(name + ":" + score + "\n");
      		out.close();
   		}catch (Exception e) {
      		System.out.println("Error1: " + e.getMessage());
   		}

   		try{
			Scanner sc = new Scanner(new File("topscore.txt"));
			int index;
			while(sc.hasNextLine()){
				reader = sc.nextLine();
				index = reader.indexOf(":");
				readscore = Integer.parseInt(reader.substring(index + 1));
				if(readscore > topscore)
					topscore = readscore;
			}
		}catch (Exception e){
			System.out.println("Error2: " + e.getMessage());
		}

		if(score >= topscore){
			JOptionPane.showMessageDialog( null, "***" + name + "***" + "\n" + " YOU GOT TOPSOCRE " + score +" SCORE!" + "\nDODGE " + generatecount +
			" GENERATE!" + "\nAT LEVEL " + level);
		}else
			JOptionPane.showMessageDialog( null, name + "\n" + "GOT " + score +" SCORE!" + "\nDODGE " + generatecount + " GENERATE!" + "\nAT LEVEL " + level);


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
	
	public int getLevel(){
		return level;
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