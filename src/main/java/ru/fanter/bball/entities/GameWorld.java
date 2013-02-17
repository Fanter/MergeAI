package ru.fanter.bball.entities;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.jbox2d.common.Settings;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class GameWorld {
	public static World world;
	private float timeStep = 1.0f/35.0f;
	private int velocityIterations = 8;
	private int positionIterations = 4;
	private ArrayList<Player> playerList = new ArrayList<Player>();
	private ArrayList<Entity> entityList = new ArrayList<Entity>();
	private ArrayList<Entity> entitiesToRemove = new ArrayList<Entity>();
	
	public GameWorld() {
		world = new World(new Vec2(), true);
		Settings.velocityThreshold = 0.0f;
	}
	
	public void createWorld() {
		entityList.add(new Borders());
		playerList.add(new Player());
	}
	
	public void step() {
		//delete dead entities
		Iterator<Entity> it = entityList.iterator();
		while (it.hasNext()) {
			Entity entity = it.next();
			entity.update();
			if (entity.getType() == EntityType.LIFE_SPHERE) {
				if (((LifeSphere)entity).isDead) {
					world.destroyBody(entity.getBody());
					it.remove();
				}
			}
		}
		
		for (Player player : playerList) {
			player.update();
		}
		generateLifeSphere();
		world.step(timeStep, velocityIterations, positionIterations);
	}
	
	private void generateLifeSphere() {
		Random rnd = new Random();
		int random = rnd.nextInt(30);
		
		if (random == 0) {
			int x = rnd.nextInt(750) + 20;
			int y = rnd.nextInt(550) + 20;
			entityList.add(new LifeSphere(x, y));
		}
	}
	
	public void destroyEntity(Entity entity) {
		world.destroyBody(entity.getBody());
		entityList.remove(entity);
	}
	
	public void addEntity(Entity entity) {
		entityList.add(entity);
	}
	
	public void draw(Graphics g) {    
	    for (Entity entity : entityList) {
	    	entity.draw(g);
	    }
	    
	    for (Player player : playerList) {
	    	player.draw(g);
	    }
	}
	
	public void addCollisionListener(CollisionHandler handler) {
		world.setContactListener(handler);
	}
	
	public void keyPressed (KeyEvent ev) {
		for (Entity entity : entityList) {
			if (entity.getType() == EntityType.PLAYER_SPHERE) {
				((PlayerSphere) entity).keyPressed(ev);	
			}
		}
		
		switch (ev.getKeyCode()) {
			case 87://w
				try {
					PlayerSphere sphere = new PlayerSphere(10);
					sphere.createSphere(200, 200);
					entityList.add(sphere);
				} catch (NullPointerException ex) {
					ex.printStackTrace();
				}
				break;
			case 82://r
				for (Entity entity : entityList) {
					world.destroyBody(entity.getBody());
				}
				entityList.clear();
				createWorld();
				break;
			case 83://s
				for (Entity entity : entityList) {
					if (entity.getType() == EntityType.PLAYER_SPHERE) {
						((PlayerSphere)entity).split();
					}
				}
				break;
		}//switch
	}
	
	public void keyReleased(KeyEvent ev) {
		for (Entity entity : entityList) {
			if (entity.getType() == EntityType.PLAYER_SPHERE) {
				((PlayerSphere) entity).keyReleased(ev);	
			}
		}
	}
}
