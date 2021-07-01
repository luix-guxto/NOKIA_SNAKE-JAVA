package br.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import br.Game;
import br.audio.AudioPlayer;

public class GameOver implements State {
	private AudioPlayer select;
	private static int score, body, wins;
	private Font font1, font2;
	private String[] text = {"Body: ", "Score: ", "Vitorias: "};
	
	public static void setPontuacao(int bodys, int scores, int win) {
		
		score=scores;
		body=bodys;
		wins=win;
	}
	
	@Override
	public void init() {
		select = new AudioPlayer("/audio/pong.mp3");
		text[0] = "Body: "+body;
		text[1] = "Score: "+score;
		text[2] = "Vitorias: "+wins;
		font1= new Font("Dialog", Font.BOLD, Game.width * 1/8);
		font2= new Font("Dialog", Font.PLAIN, Game.width * 1/16);
	}

	@Override
	public void update() {
		
	}

	@Override
	public void render(Graphics g) {
		 g.setColor(Color.WHITE);
	        g.fillRect(0,0, Game.width, Game.heigth);


	        g.setColor(Color.RED);
	        g.setFont(font1);
	        g.drawString("GAME-OVER",Game.width/2-g.getFontMetrics().stringWidth("GAME-OVER")/2, Game.heigth*3/7-g.getFontMetrics(font1).getHeight());

	        g.setColor(Color.black);
	        g.setFont(font2);
	        for(int x = 0; x < text.length; x++){
	            g.drawString(text[x], Game.width/2-g.getFontMetrics().stringWidth(text[x])/2, Game.heigth/2+g.getFontMetrics(font2).getHeight()*x);
	        }
	        
	}

	@Override
	public void KeyPress(int cod) {
	
	}

	@Override
	public void KeyReleased(int cod) {
		if(cod == KeyEvent.VK_ESCAPE) {
			select.start();
			StateManager.setStade(StateManager.MENU);
		}
	}

}
