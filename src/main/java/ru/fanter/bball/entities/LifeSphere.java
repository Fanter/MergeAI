package ru.fanter.bball.entities;

import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import ru.fanter.bball.BouncyBall;

public class LifeSphere implements Entity {
	public boolean isDead = false;
	private EntityType type = EntityType.LIFE_SPHERE;
	private Body body;
	private final int radius = 7;
	private int stepsToLive = 600;
	
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

	@Override
	public EntityType getType() {
		return type;
	}
	
	@Override
	public Body getBody() {
		return body;
	}
	
	@Override
	public void draw(Graphics g) {
		Vec2 velocity = body.getLinearVelocity();
		Vec2 position = body.getPosition();
		int velX = B2Util.toPixelScale(velocity.x);
		int velY = B2Util.toPixelScale(velocity.y);
		int posX = B2Util.toPixelX(position.x);
		int posY = B2Util.toPixelY(position.y);
		
		//draw Circle
		g.setColor(Color.BLACK);
		g.fillOval(posX - radius, posY - radius, radius*2, radius*2);
	}
	
	@Override
	public void update() {
		stepsToLive -= 1;
		if (stepsToLive == 0) {
			isDead = true;
		}
	}
}
