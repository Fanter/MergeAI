package ru.fanter.merge;

import java.awt.Color;
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
import ru.fanter.merge.strategy.BottomLeftStrategy;
import ru.fanter.merge.strategy.BottomRightStrategy;
import ru.fanter.merge.strategy.Strategy;
import ru.fanter.merge.strategy.TopLeftStrategy;
import ru.fanter.merge.strategy.TopRightStrategy;

public class GameWorld implements EntityListener {
	public static World world;
	private float timeStep = 1.0f/35.0f;
	private int velocityIterations = 8;
	private int positionIterations = 4;
	private CollisionHandler collisionHandler = new CollisionHandler();
	private Set<Entity> entities = new HashSet<Entity>();
	private Set<Entity> entitiesToRemove = new HashSet<Entity>();
	private Set<Entity> entitiesToAdd = new HashSet<Entity>();
	
	public GameWorld() {
		world = new World(new Vec2(), true);
		Settings.velocityThreshold = 0.0f;
		addCollisionListener(collisionHandler);
	}
	
	public void createWorld() {
		int width = MergeAI.WINDOW_WIDTH;
		int height = MergeAI.WINDOW_HEIGHT;
		
		createSphere(new TopLeftStrategy(), Color.BLUE, width/5, height/4);
		createSphere(new TopRightStrategy(), Color.RED, width - width/5, height/4);
		createSphere(new BottomLeftStrategy(), Color.GREEN, width/5, height - height/4);
		createSphere(new BottomRightStrategy(), Color.ORANGE, width - width/5, height - height/4);
		entities.add(new Borders());
	}
	
	private void createSphere(Strategy strategy, Color color, int x, int y) {
		PlayerSphere sphere = new PlayerSphere(strategy, color);
		sphere.createSphere(x, y);
		sphere.addEntityListener(this);
		entities.add(sphere);
	}
	
	public void step() {			
		updateEntities();
		generateLifeSphere();
		world.step(timeStep, velocityIterations, positionIterations);
        collisionHandler.handleCollisions();
        removeDeadEntities();
        addNewEntities();
	}
	
	private void updateEntities() {
		WorldData worldData = createWorldData();
		
		for (Entity entity : entities) {
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
	}
	
	private WorldData createWorldData() {
		List<SphereModel> sphereModels = new ArrayList<SphereModel>();
		List<LifeModel> lifeModels = new ArrayList<LifeModel>();
		
		for (Entity entity : entities) {
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
	
	private void removeDeadEntities() {
		Iterator<Entity> it = entitiesToRemove.iterator();
		while (it.hasNext()) {
			Entity entity = it.next();
			if (entities.contains(entity)) {
				world.destroyBody(entity.getBody());
				it.remove();
				entities.remove(entity);
			}
		}
	}
	
	private void addNewEntities() {
		Iterator<Entity> it = entitiesToAdd.iterator();
		while (it.hasNext()) {
			Entity entity = it.next();
			entities.add(entity);
		}
		entitiesToAdd.clear();
	}
	
	private void generateLifeSphere() {
		Random rnd = new Random();
		int random = rnd.nextInt(30);
		
		if (random == 0) {
			int x = rnd.nextInt(MergeAI.WINDOW_WIDTH - 50) + 20;
			int y = rnd.nextInt(MergeAI.WINDOW_HEIGHT - 50) + 20;
			LifeSphere lifeSphere = new LifeSphere(x, y);
			lifeSphere.addEntityListener(this);
			entities.add(lifeSphere);
		}
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	public void draw(Graphics g) {    
	    for (Entity entity : entities) {
	    	entity.draw(g);
	    }
	}
	
	private void addCollisionListener(CollisionHandler handler) {
		world.setContactListener(handler);
	}
	
	@Override 
	public void update(EntityEvent event) {
		switch(event.getEventType()) {
		case DELETE:
			entitiesToRemove.add(event.getEntity());
			break;
		case SPLIT:
			entitiesToAdd.add(event.getEntity());
			break;
		}
	}
	
	public void keyPressed (KeyEvent ev) {
		for (Entity entity : entities) {
			if (entity.getType() != EntityType.PLAYER_SPHERE) {
				continue;
			}
			if (((PlayerSphere)entity).getStrategy() instanceof BottomRightStrategy) {
				((PlayerSphere)entity).keyPressed(ev);
			}
		}
	}
	
	public void keyReleased(KeyEvent ev) {
		for (Entity entity : entities) {
			if (entity.getType() == EntityType.PLAYER_SPHERE) {
				((PlayerSphere)entity).keyReleased(ev);	
			}
		}
	}
}
