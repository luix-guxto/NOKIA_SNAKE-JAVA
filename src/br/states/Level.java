package br.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Random;

import br.Game;
import br.audio.AudioPlayer;

public class Level implements State{
	private AudioPlayer comida;
	Random rm = new Random();
	private boolean coletou = true;
	private double timer;
	private long now, lastTime = System.nanoTime();
	private String[] scores = {"Body: ", "Score: ", "Vitoria: "};
	private Font font = new Font(Font.DIALOG, Font.BOLD, 20);
	private final int maxX = Game.width/14+1, maxY = Game.heigth/14+1;
	private Integer[] planoX = new Integer[maxX];
	private Integer[] planoY = new Integer[maxY];
	private boolean up, left, rigth, down, teclado = false;
	private int maxBody = (maxX-1) * (maxY-1);
	private int body = 3, wins = 0, posX, posY, score;
	private Rectangle[] corpo = new Rectangle[maxBody];
	private boolean[] corpicho = new boolean[maxBody];
	private boolean[] andando = new boolean[maxBody];
	private Integer[] anteriorX = new Integer[maxBody], anteriorY = new Integer[maxBody];
	private Integer[] corpoX = new Integer[maxBody], corpoY = new Integer[maxBody];
	private String[] pontuacao = new String[3];
	private Rectangle 
			maca 	= new Rectangle( 0, 0, 16, 16), 
			cabeca 	= new Rectangle( Game.width/2-8, Game.heigth/2-8, 16, 16);
	
	public Level() {
		comida = new AudioPlayer("/audio/pong.mp3");
	}

	@Override
	public void init() {
		body=3;
		coletou=true;
		teclado=false;
		for(int x = 0; x<maxX ; x++) {
			planoX[x] = 14*x;
		}
		for(int x = 0; x<maxY; x++) {
			planoY[x] = 14*x;
		}
		posX = maxX/2;
		posY = maxY/2;
		cabeca.x=planoX[posX];
		cabeca.y=planoY[posY];
		for(int x = 0; x<maxBody; x++) {
			corpo[x]=new Rectangle(cabeca.x, cabeca.y, 16, 16);
			corpoX[x] = posX;
			corpoY[x] = posY+1;
			anteriorX[x]=corpoX[x];
			anteriorY[x]=corpoY[x];
			corpicho[x]=false;
			andando[x]=false;
		}
		for(int x = 0; x< body; x++) {
			corpicho[x]=true;
		}
		up = left = rigth = down = false;
		
		
	}

	@Override
	public void update() {
		if(body >= maxBody) {
			wins++;
			init();
		}
		limits();
		now=System.nanoTime();
		timer+=now-lastTime;
		lastTime=now;
		if(timer>=(1-0.80)*1000000000) {
			if(teclado) {corpo();}
			if(up) {
				posY--;
			}
			else if(left) {
				posX--;
			}
			else if(rigth) {
				posX++;
			}
			else if(down){
				posY++;
			}
			cabeca.x=planoX[posX];
			cabeca.y=planoY[posY];
			
			timer = 0;
		}
		
		if(coletou) {
			maca.x= planoX[rm.nextInt(maxX-3)+1];
			maca.y= planoY[rm.nextInt(maxX-3)+1];
			score+=10;
			comida.start();
			coletou = false;
		}
		
	}


	private void corpo() {
		for(int x = 0; x< body; x++) {
			corpicho[x]=true;
		}
		anteriorX[0]=corpoX[0];
		anteriorY[0]=corpoY[0];
		corpoX[0]=posX;
		corpoY[0]=posY;
		corpo[0].x=planoX[corpoX[0]];
		corpo[0].y=planoY[corpoY[0]];
		if(corpoX[0] != anteriorX[0] || corpoY[0] != anteriorY[0]) {
		andando[0]=true;
		}else {
			corpoX[0]=anteriorX[0];
			corpoY[0]=anteriorY[0];
		}
		for(int x=1; x<maxBody;x++) {
			if(andando[x-1]) {
				anteriorX[x]=corpoX[x];
				anteriorY[x]=corpoY[x];
				corpoX[x]=anteriorX[x-1];
				corpoY[x]=anteriorY[x-1];
				corpo[x].x=planoX[corpoX[x]];
				corpo[x].y=planoY[corpoY[x]];
				if(corpoX[x] != anteriorX[x] || corpoY[x] != anteriorY[x]){
					andando[x]=true;
				}
				
			}
		}
	}

	private void limits() {
		if(maca.x == cabeca.x && maca.y == cabeca.y) {
			coletou=true;
			body++;
		}
		if(	cabeca.x == planoX[0] 		|| 
			cabeca.x == planoX[maxX-2] 	|| 
			cabeca.x == planoX[0] 		|| 
			cabeca.x == planoX[maxX-2] 	){
				gameOver();
		}
		for(int x = 0; x<maxBody;x++) {
			if(corpicho[x]) {
				if(posX == corpoX[x] &&
						posY==corpoY[x] &&
						teclado &&
						andando[x]) {
					gameOver();	
				}
			}
		}
	}

	private void gameOver() {
		GameOver.setPontuacao(body, score, wins);
		wins=0;
		score=0;
		body=3;
		comida.start();
		StateManager.setStade(StateManager.GAME_OVER);
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(planoX[0], planoY[0], planoX[maxX-1], planoY[maxY-1]);
		
		g.setColor(Color.white);
		g.fillRect(planoX[1]-8, planoY[1]-8, planoX[maxX-3]+16, planoY[maxX-3]+16);
		
		//Corpo
		
		for(int x = maxBody-1; x>=0;x--) {
			if(corpicho[x]) {
				g.setColor(new Color( 0, 50, 0));
				g.fillOval(planoX[corpoX[x]]-2, planoY[corpoY[x]]-2, corpo[x].width+4, corpo[x].height+4);
				g.setColor(new Color(0,150,0));
				g.fillOval(planoX[corpoX[x]], planoY[corpoY[x]], corpo[x].width, corpo[x].height);
			}
		}
		
		// Maca
		g.setColor(new Color( 0, 100, 0));
		g.fillOval(maca.x+maca.width/2, maca.y-3, 5, 7);
		g.setColor(Color.RED);
		g.fillOval(maca.x, maca.y, maca.width, maca.height);
		
		
		// Cabeca
		g.setColor(Color.BLACK);
		g.fillOval(cabeca.x-2, cabeca.y-2, cabeca.width+4, cabeca.height+4);
		g.setColor(new Color( 0, 50, 0));
	 	g.fillOval(cabeca.x, cabeca.y, cabeca.width, cabeca.height);
	 	
	 	// Score
	 	g.setColor(Color.ORANGE);
	 	g.setFont(font);
	 	pontuacao[0]=""+body;
	 	pontuacao[1]=""+score;
	 	pontuacao[2]=""+wins;
	 	for(int x = 0; x<scores.length;x++) {
	 		
	 		g.drawString(scores[x]+pontuacao[x], planoX[1]-8, (planoY[1]-8+g.getFontMetrics(font).getHeight())+(g.getFontMetrics(font).getHeight()*x)+5+(5*x));
	 	}
	}

	@Override
	public void KeyPress(int cod) {
		if(cod == KeyEvent.VK_UP) {
			if(!down) {
				teclado = true;
				up 		= true;
				down 	= false;
				left 	= false;
				rigth 	= false;
			}
		}
		
		else if(cod == KeyEvent.VK_DOWN) {
			if(!up) {
				teclado = true;
				up 		= false;
				down 	= true;
				left 	= false;
				rigth 	= false;
			}
		}
		
		if(cod == KeyEvent.VK_LEFT) {
			if(!rigth) {
				teclado = true;
				up 		= false;
				down 	= false;
				left 	= true;
				rigth 	= false;
			}
		}
		
		else if(cod == KeyEvent.VK_RIGHT) {
			if(!left) {
				teclado = true;
				up 		= false;
				down 	= false;
				left 	= false;
				rigth 	= true;
			}
		}
	}

	@Override
	public void KeyReleased(int cod) {
		if(cod == KeyEvent.VK_V) {
			body = maxBody;
		}
	}

}
