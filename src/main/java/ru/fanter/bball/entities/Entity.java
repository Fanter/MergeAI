package ru.fanter.bball.entities;

import java.awt.Graphics;

import org.jbox2d.dynamics.Body;

public interface Entity {
	
	public EntityType getType();
	public void update();
	public void draw(Graphics g);
	public Body getBody();
}
