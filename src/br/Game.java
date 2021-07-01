package br;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import br.display.Display;
import br.states.StateManager;

public class Game implements Runnable {
	
	private Display display;
	private Thread thread;
	private boolean running = false;
	
	public static final int width = 532 , heigth = 532;
	
	private StateManager sm;
	
	public Game() {
		display = new Display("Snake", width, heigth);
		sm = new StateManager();
		display.setKeyListener(sm);
		StateManager.setStade(StateManager.MENU);
	}
	
	@Override
	public void run() {
		init();
		int FPS = 60;
		double timePerTick = 1000000000 / FPS;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		
		while(running) {
			now = System.nanoTime();
			delta += ( now - lastTime ) / timePerTick;
			lastTime = now;
			
			
			if (delta >= 1) {
				update();
				render();
				
				// Não mecha
				delta--;
			}
		}
		stop();
	}
	
	private void render() {
		BufferStrategy bs = display.getBufferStrategy();
		if (bs == null) {
			display.createBufferStrategy();
			bs = display.getBufferStrategy();
		}
		
		Graphics g = bs.getDrawGraphics();
		g.clearRect(0, 0, width, heigth);
		
		if(StateManager.getState() != null){ sm.render(g); }
		
		g.dispose();
		bs.show();
	}

	private void update() {
		if(StateManager.getState() == null) { return; }
		sm.update();
	}

	private void init() {
		
		
	}

	public synchronized void start() {
		if (thread != null) { return; }
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void stop() {
		if(thread == null) { return; }
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
