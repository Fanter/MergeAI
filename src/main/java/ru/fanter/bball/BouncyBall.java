package ru.fanter.bball;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ru.fanter.bball.entities.Borders;
import ru.fanter.bball.entities.PlayerSphere;

public class BouncyBall extends JFrame implements KeyListener {
	// number of updates per seconds 
    public static final int UPDATE_RATE = 35; 
    // time for one update in milliseconds
    public static final int UPDATE_PERIOD = 1000 / UPDATE_RATE; 
	public static final int WINDOW_WIDTH = 900;
	public static final int WINDOW_HEIGHT = 600;
	public static GameWorld gameWorld;
	private CollisionHandler collisionHandler;
	private GamePanel gamePanel;
	private int step;
	
	public void start() {
		createWorldObjects();
		createGui();
		startGameLoop();
	}
	
	private void createWorldObjects() {
		gameWorld = new GameWorld();
		gameWorld.createWorld();
		collisionHandler = new CollisionHandler();
		gameWorld.addCollisionListener(collisionHandler);
	}
	
	private void createGui() {
		initGuiObjects();
		confGuiSettings();
	}
	
	private void initGuiObjects() {
		gamePanel = new GamePanel();
	}
	
	private void confGuiSettings() {
		gamePanel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		this.setContentPane(gamePanel);
		this.addKeyListener(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	private void startGameLoop() {
		Thread gameThread = new Thread() {
			@Override
			public void run() {
				gameLoop();
			}
		};
		gameThread.start();
	}
	
	private void gameLoop() {
        long startTime;
        long elapsedTime;
        long timeToSleep;

        while (true) {
            startTime = System.currentTimeMillis();
            gameWorld.step();
            collisionHandler.handleCollisions();
            repaint();
            elapsedTime = System.currentTimeMillis() - startTime;
            timeToSleep = UPDATE_PERIOD - elapsedTime;
            try {
                if (timeToSleep < 10) {
                    timeToSleep = 10;
                }
                Thread.sleep(timeToSleep);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
	}
	
	public class GamePanel extends JPanel {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
		    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		    
		    g.setColor(Color.GRAY);
		    g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
			gameWorld.draw(g);
			
			g.setColor(Color.BLACK);
			g.drawString("" + step, 10, 15);
			step += 1;
		}
	}
	
	@Override
	public void keyPressed(KeyEvent ev) {
		gameWorld.keyPressed(ev);
	}
	
	@Override
	public void keyReleased(KeyEvent ev) {
		gameWorld.keyReleased(ev);
	}
	
	@Override
	public void keyTyped(KeyEvent ev) {
	}
}
