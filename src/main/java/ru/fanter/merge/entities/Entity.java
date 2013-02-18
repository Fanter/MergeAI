package ru.fanter.merge.entities;

import java.awt.Graphics;

import org.jbox2d.dynamics.Body;

import ru.fanter.merge.event.EntityEvent;
import ru.fanter.merge.event.EntityListener;
import ru.fanter.merge.event.EntityEvent.EventType;

public abstract class Entity {
	public EntityListener entityListener;
	
	public abstract EntityType getType();
	public abstract void update();
	public abstract void draw(Graphics g);
	public abstract Body getBody();
	public abstract void addEntityListener(EntityListener dl);
	
	public void remove() {
		EntityEvent ee = new EntityEvent(this, EventType.DELETE);
		entityListener.update(ee);
	}
}
