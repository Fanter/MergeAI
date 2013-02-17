package ru.fanter.bball.entities;

import java.awt.Graphics;

import org.jbox2d.dynamics.Body;

import ru.fanter.bball.DeathListener;

public abstract class Entity {
	public DeathListener dl;
	
	public abstract EntityType getType();
	public void update() {}
	public void draw(Graphics g) {}
	public abstract Body getBody();
	public abstract void addDeathListener(DeathListener dl);
	
	public void setDead() {
		dl.addEntityToRemove(this);
	}
}
