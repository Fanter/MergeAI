package ru.fanter.bball;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.jbox2d.common.Settings;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import ru.fanter.bball.entities.Borders;
import ru.fanter.bball.entities.Entity;
import ru.fanter.bball.entities.EntityType;
import ru.fanter.bball.entities.LifeSphere;
import ru.fanter.bball.entities.PlayerSphere;

public class GameWorld implements DeathListener {
	public static World world;
	private float timeStep = 1.0f/35.0f;
	private int velocityIterations = 8;
	private int positionIterations = 4;
	private ArrayList<Player> playerList = new ArrayList<Player>();
	private ArrayList<Entity> entityList = new ArrayList<Entity>();
	private Set<Entity> entitiesToRemove = new HashSet<Entity>();
	
	public GameWorld() {
		world = new World(new Vec2(), true);
		Settings.velocityThreshold = 0.0f;
	}
	
	public void createWorld() {
		entityList.add(new Borders());
		playerList.add(new Player(this));
	}
	
	public void step() {
		removeDeadEntities();
		
		Iterator<Entity> it2 = entityList.iterator();
		while (it2.hasNext()) {
			Entity entity = it2.next();
			entity.update();
			if (entity.getType() == EntityType.LIFE_SPHERE) {
				if (((LifeSphere)entity).isDead) {
					world.destroyBody(entity.getBody());
					it2.remove();
				}
			}
		}
		
		for (Player player : playerList) {
			player.update();
		}
		generateLifeSphere();
		world.step(timeStep, velocityIterations, positionIterations);
	}
	
	private void removeDeadEntities() {
		Iterator<Entity> it = entityList.iterator();
		while (it.hasNext()) {
			Entity entity = it.next();
			if (entitiesToRemove.contains(entity)) {
				world.destroyBody(entity.getBody());
				it.remove();
				entitiesToRemove.remove(entity);
			}
		}
		
		for (Player player : playerList) {
			player.removeEntities(entitiesToRemove);
		}
	}
	
	private void generateLifeSphere() {
		Random rnd = new Random();
		int random = rnd.nextInt(30);
		
		if (random == 0) {
			int x = rnd.nextInt(750) + 20;
			int y = rnd.nextInt(550) + 20;
			LifeSphere lifeSphere = new LifeSphere(x, y);
			lifeSphere.addDeathListener(this);
			entityList.add(lifeSphere);
		}
	}
	
	public void destroyEntity(Entity entity) {
		world.destroyBody(entity.getBody());
		entityList.remove(entity);
		for (Player player : playerList) {
			player.destroy(entity);
		}
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
	
	@Override 
	public void addEntityToRemove(Entity entity) {
		entitiesToRemove.add(entity);
	}
	
	public void keyPressed (KeyEvent ev) {
		for (Player player : playerList) {
			player.keyPressed(ev);
		}
	}
	
	public void keyReleased(KeyEvent ev) {
		for (Player player : playerList) {
			player.keyReleased(ev);
		}
	}
}
