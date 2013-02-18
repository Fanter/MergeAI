package ru.fanter.mergel.entities;

import java.awt.Graphics;

import org.jbox2d.dynamics.Body;

import ru.fanter.merge.event.EntityEvent;
import ru.fanter.merge.event.EntityListener;
import ru.fanter.merge.event.EntityEvent.EventType;

public abstract class Entity {
	public EntityListener entityListener;
	
	public abstract EntityType getType();
	public void update() {}
	public void draw(Graphics g) {}
	public abstract Body getBody();
	public abstract void addDeathListener(EntityListener dl);
	
	public void remove() {
		EntityEvent ee = new EntityEvent(this, EventType.DELETE);
		entityListener.update(ee);
	}
}
