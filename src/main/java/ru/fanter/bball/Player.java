package ru.fanter.bball;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import ru.fanter.bball.entities.Entity;
import ru.fanter.bball.entities.PlayerSphere;

public class Player {
	private ArrayList<PlayerSphere> sphereList = new ArrayList<PlayerSphere>();
	
	public Player(DeathListener dl) {
		for (int i = 1; i < 10; i++) {
			Random random = new Random();
			int radius = (random.nextInt(5) + 2)*5 ;
			PlayerSphere sphere = new PlayerSphere(radius);
			sphere.createSphere(70*i, 200);
			sphere.addDeathListener(dl);
			sphereList.add(sphere);
		}
	}
	
	public void destroy(Entity entity) {
		sphereList.remove(entity);
	}
	
	public void removeEntities(Set<Entity> entities) {
		Iterator<PlayerSphere> it = sphereList.iterator();
		while (it.hasNext()) {
			Entity entity = it.next();
			if (entities.contains(entity)) {
				GameWorld.world.destroyBody(entity.getBody());
				it.remove();
				entities.remove(entity);
			}
		}
	}
	
	public void update() {
		for (Entity entity : sphereList) {
			entity.update();
		}
	}
	
	public void draw(Graphics g) {
		for (Entity entity : sphereList) {
			entity.draw(g);
		}		
	}
	
	public void keyPressed(KeyEvent ev) {
		for (PlayerSphere sphere : sphereList) {
			sphere.keyPressed(ev);
		}
	}
	
	public void keyReleased(KeyEvent ev) {
		for (PlayerSphere sphere : sphereList) {
			sphere.keyReleased(ev);
		}
	}
}
