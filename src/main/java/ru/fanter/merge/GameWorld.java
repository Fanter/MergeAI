package ru.fanter.merge;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.jbox2d.common.Settings;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import ru.fanter.merge.entities.Borders;
import ru.fanter.merge.entities.Entity;
import ru.fanter.merge.entities.EntityType;
import ru.fanter.merge.entities.LifeSphere;
import ru.fanter.merge.entities.PlayerSphere;
import ru.fanter.merge.event.EntityEvent;
import ru.fanter.merge.event.EntityListener;
import ru.fanter.merge.model.LifeModel;
import ru.fanter.merge.model.Move;
import ru.fanter.merge.model.SphereModel;
import ru.fanter.merge.model.WorldData;

public class GameWorld implements EntityListener {
	public static World world;
	private float timeStep = 1.0f/35.0f;
	private int velocityIterations = 8;
	private int positionIterations = 4;
	private ArrayList<Entity> entityList = new ArrayList<Entity>();
	private Set<Entity> entitiesToRemove = new HashSet<Entity>();
	
	public GameWorld() {
		world = new World(new Vec2(), true);
		Settings.velocityThreshold = 0.0f;
	}
	
	public void createWorld() {
		entityList.add(new Borders());
		
		for (int i = 1; i < 10; i++) {
			Random random = new Random();
			int radius = (random.nextInt(5) + 2)*5 ;
			PlayerSphere sphere = new PlayerSphere(new PlayerStrategy());
			sphere.createSphere(70*i, 200);
			sphere.addEntityListener(this);
			entityList.add(sphere);
		}
	}
	
	public void step() {			
		WorldData worldData = createWorldData();
		for (Entity entity : entityList) {
			if (entity.getType() == EntityType.PLAYER_SPHERE) {
				PlayerSphere playerSphere = (PlayerSphere) entity;
				SphereModel sphereModel = new SphereModel(playerSphere);
				Move move = new Move();
				playerSphere.move(sphereModel, worldData, move);
				playerSphere.update();
			} else {
				entity.update();
			}
		}
		
		generateLifeSphere();
		world.step(timeStep, velocityIterations, positionIterations);
	}
	
	private WorldData createWorldData() {
		List<SphereModel> sphereModels = new ArrayList<SphereModel>();
		List<LifeModel> lifeModels = new ArrayList<LifeModel>();
		
		for (Entity entity : entityList) {
			if (entity.getType() == EntityType.PLAYER_SPHERE) {
				PlayerSphere ps = (PlayerSphere) entity;
				sphereModels.add(new SphereModel(ps));
			} else if (entity.getType() == EntityType.LIFE_SPHERE) {
				LifeSphere ls = (LifeSphere) entity;
				lifeModels.add(new LifeModel(ls));
			}
		}
		return new WorldData(sphereModels, lifeModels);
	}
	
	public void removeDeadEntities() {
		Iterator<Entity> it = entityList.iterator();
		while (it.hasNext()) {
			Entity entity = it.next();
			if (entitiesToRemove.contains(entity)) {
				world.destroyBody(entity.getBody());
				it.remove();
				entitiesToRemove.remove(entity);
			}
		}//while
	}
	
	private void generateLifeSphere() {
		Random rnd = new Random();
		int random = rnd.nextInt(30);
		
		if (random == 0) {
			int x = rnd.nextInt(750) + 20;
			int y = rnd.nextInt(550) + 20;
			LifeSphere lifeSphere = new LifeSphere(x, y);
			lifeSphere.addEntityListener(this);
			entityList.add(lifeSphere);
		}
	}
	
	public void addEntity(Entity entity) {
		entityList.add(entity);
	}
	
	public void draw(Graphics g) {    
	    for (Entity entity : entityList) {
	    	entity.draw(g);
	    }
	}
	
	public void addCollisionListener(CollisionHandler handler) {
		world.setContactListener(handler);
	}
	
	@Override 
	public void update(EntityEvent event) {
		switch(event.getEventType()) {
		//TODO move inside PlayerSphere?
		case DELETE:
			entitiesToRemove.add(event.getEntity());
			break;
		//TODO implement
		case SPLIT:
			break;
		}
	}
	
	public void keyPressed (KeyEvent ev) {
		for (Entity entity : entityList) {
			if (entity.getType() == EntityType.PLAYER_SPHERE) {
				((PlayerSphere)entity).keyPressed(ev);	
			}
		}
	}
	
	public void keyReleased(KeyEvent ev) {
		for (Entity entity : entityList) {
			if (entity.getType() == EntityType.PLAYER_SPHERE) {
				((PlayerSphere)entity).keyReleased(ev);	
			}
		}
	}
}
