package ru.fanter.merge.entities;

import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import ru.fanter.merge.GameWorld;
import ru.fanter.merge.event.EntityEvent;
import ru.fanter.merge.event.EntityListener;
import ru.fanter.merge.event.EntityEvent.EventType;
import ru.fanter.merge.util.B2Util;

public class LifeSphere extends Entity {
	private EntityType type = EntityType.LIFE_SPHERE;
	private Body body;
	private final int radius = 7;
	private int ticksToLive = 600;
	
	public LifeSphere(int x, int y) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(B2Util.toMeterX(x), B2Util.toMeterY(y));
		
		CircleShape cs = new CircleShape();
		cs.m_radius = B2Util.toMeterScale(radius);
		
		FixtureDef fixtDef = new FixtureDef();
		fixtDef.shape = cs;
		fixtDef.isSensor = true;
		
		body = GameWorld.world.createBody(bodyDef);
		body.createFixture(fixtDef);
		body.setUserData(this);
	}
	
	public int getTicksLeft() {
		return ticksToLive;
	}

	@Override
	public EntityType getType() {
		return type;
	}
	
	@Override
	public Body getBody() {
		return body;
	}
	
	@Override 
	public void addEntityListener(EntityListener dl) {
		this.entityListener = dl;
	}
	
	@Override
	public void draw(Graphics g) {
		Vec2 position = body.getPosition();
		int posX = B2Util.toPixelX(position.x);
		int posY = B2Util.toPixelY(position.y);
		
		g.setColor(Color.BLACK);
		g.fillOval(posX - radius, posY - radius, radius*2, radius*2);
	}
	
	@Override
	public void update() {
		ticksToLive -= 1;
		if (ticksToLive == 0) {
			remove();
		}
	}
}
