package br.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import br.Game;
import br.audio.AudioPlayer;

public class MenuState implements State {
	
	private AudioPlayer select;
	private String[] options = { "JOGAR", "SAIR" };
	private Font font1;
	private Font font2;
	private int choice = 0;
	
	public MenuState() {
		select = new AudioPlayer("/audio/pong.mp3");
	}
	
	@Override
	public void init() {
		font1= new Font("Dialog", Font.BOLD, Game.width * 1/8);
		font2= new Font("Dialog", Font.PLAIN, Game.width * 1/16);
	}
	
	

	@Override
	public void update() {
		
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(0, 0, Game.width, Game.heigth);
		
		g.setColor(Color.WHITE);
		g.fillRect(10, 10, Game.width-20, Game.heigth-20);
		
		g.setColor(new Color(0, 50, 0));
		g.setFont(font1);
		g.drawString("SNAKE", 
				Game.width/2 - g.getFontMetrics().stringWidth("SNAKE")/2, 
				Game.heigth * 3/7
				);
		g.setFont(font2);
		for(int i = 0; i < options.length; i++) {
			g.setColor(Color.BLACK);
			if(i==choice) {
				g.setColor(Color.BLUE);
			}
			g.drawString(options[i], 
					Game.width/2 - g.getFontMetrics().stringWidth(options[i])/2, 
					Game.heigth* 4/7+g.getFontMetrics(font2).getHeight()*i);
			
		}
		
	}

	@Override
	public void KeyPress(int cod) {
		// TODO Auto-generated method stub

	}

	@Override
	public void KeyReleased(int cod) {
		if(cod == KeyEvent.VK_W || cod == KeyEvent.VK_UP) {
			choice--;
			select.start();
			if(choice<0) {
				choice= options.length-1;
			}
		}if(cod == KeyEvent.VK_S || cod == KeyEvent.VK_DOWN) {
			choice++;
			select.start();
			if(choice>options.length-1) {
				choice = 0;
			}
		}if(cod == KeyEvent.VK_ENTER) {
			select.start();
			select();
			
		}if(cod == KeyEvent.VK_ESCAPE) {
			select.start();
			System.exit(0);
			
		}
		
	}

	private void select() {
		switch (choice) {
		
		case 0:
			StateManager.setStade(StateManager.LEVEL1);
			break;
			
		case 1:
			System.exit(0);
			break;
					
		default:
			break;
		}
	}

}
