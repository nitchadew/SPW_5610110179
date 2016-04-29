
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
	private ArrayList<Bonusscore> bonuss = new ArrayList<Bonusscore>();		
	private SpaceShip v;	
	private Timer timer;
	private String name;
	private String reader;
	private String topname;
	private int readscore;
	private int topscore;
	private LifePoint life;
	private LifePoint heightlife1;
	private LifePoint widthlife1;
	private LifePoint widthlife2;
	private LifePoint heightlife2;
	private int level = 1;
	private double healitem= 0.003;
	private double bonusitem = 0.003;
	private double generate = 0.1;	
	private double biggenerate = 0.01;
	private double addhealitem= 0.001;
	private double addgenerate  = 0.02;
	private double addbiggenerate = 0.002;
	private long score = 0;
	private int generatecount = 0;
	private long diff = 0;
	private boolean trigger = false;
	


	
	public GameEngine(GamePanel gp, SpaceShip v) {
		this.gp = gp;
		this.v = v;		
		
		gp.sprites.add(v);


		life = new LifePoint(12, 12, 100, 15, "l");
		heightlife1 = new LifePoint(8, 10, 1, 20, "l");
		widthlife1 = new LifePoint(8, 9, 108, 1, "l");
		widthlife2 = new LifePoint(8, 29, 108, 1, "l");
		heightlife2 = new LifePoint(115, 10, 1, 20, "l");
		gp.sprites.add(life);
		gp.sprites.add(heightlife1);
		gp.sprites.add(widthlife1);
		gp.sprites.add(widthlife2);
		gp.sprites.add(heightlife2);
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
		Enemy e = new Enemy((int)(Math.random()*390), 30, 5, 10,"e");
		gp.sprites.add(e);
		enemies.add(e);

	}

	private void generateHeal(int times){
		for(int i = 0; i < times; i++){	
			Heal h = new Heal((int)(Math.random()*390), 30, 10, 10, "h");
			gp.sprites.add(h);
			heals.add(h);
		}
	}

	private void generateBonus(){
		Bonusscore b = new Bonusscore((int)(Math.random()*390), 30, 10, 10,"b");
		gp.sprites.add(b);
		bonuss.add(b);

	}


	private void bigGenerateEnemy(){
		Enemy e;
		if(Math.random() > 0.5)
			e = new Enemy((int)(Math.random()*390), 30, 30, 50, "e");
		else
			e = new Enemy((int)(Math.random()*390), 30, 50, 30, "e");
		gp.sprites.add(e);
		enemies.add(e);

	}


	private void process(){
		if(Math.random() < healitem){
			generateHeal(1);
		}

		if(Math.random() < bonusitem){
			generateBonus();
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
				trigger = true;

			diff = score % 300;

			if(!e.isAlive()){
				generatecount++;
				if(e.width == 5){
					score += 1;
					if(trigger == true){
						if(timer.getDelay() > 25)
							timer.setDelay(timer.getDelay() - 5);
						generateHeal(13);
						generate = generate + addgenerate;
						biggenerate = biggenerate + addbiggenerate;
						healitem = healitem + addhealitem;
						trigger = false;
						level++;
					}
				}	
					
				if(e.width == 30 || e.width == 50){
					score += 10;
					if(trigger == true){
						if(timer.getDelay() > 25)
							timer.setDelay(timer.getDelay() - 5);
						generateHeal(13);
						generate = generate + addgenerate;
						biggenerate = biggenerate + addbiggenerate;
						healitem = healitem + addhealitem;
						trigger = false;
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


		Iterator<Bonusscore> b_iter = bonuss.iterator();
		while(b_iter.hasNext()){
			Bonusscore b = b_iter.next();
			b.proceed();
			if(!b.isAlive()){
					gp.sprites.remove(b);
					b_iter.remove();
			}
			
			er = b.getRectangle();
			if (er.intersects(vr)){
				gp.sprites.remove(b);
				b_iter.remove();
				score += 200;
			}

		}	


				gp.updateGameUI(this);
	}

	public void die(){
		gp.updateGameUI(this);


		try {
     		FileWriter writer = new FileWriter("topscore.txt",true);
     		BufferedWriter out = new BufferedWriter(writer);
      		out.write("[" + "Lvl-" + level + "]" + name + ":" + score + "\n");
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