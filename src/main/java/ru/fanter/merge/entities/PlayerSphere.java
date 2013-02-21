package ru.fanter.merge.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import ru.fanter.merge.GameWorld;
import ru.fanter.merge.event.EntityListener;
import ru.fanter.merge.model.Move;
import ru.fanter.merge.model.SphereModel;
import ru.fanter.merge.model.WorldData;
import ru.fanter.merge.strategy.Strategy;
import ru.fanter.merge.util.B2Util;

public class PlayerSphere extends Entity {
	private final float density = 8.5f;
	private final float friction = 0.0f;
	private final float restitution = 1.0f;
	private final int minRadius = 9;
	private int impulseOffset = 0;
	private Body body;
	private int radius = 15;
	private EntityType type = EntityType.PLAYER_SPHERE;
	private Strategy strategy;
	private Move move;
	private Color color;
	private boolean moveUp;
	private boolean moveDown;
	private boolean moveRight;
	private boolean moveLeft;
	
	public PlayerSphere(Strategy strategy, Color color) {
		this.strategy = strategy;
		this.color = color;
	}
	
	public void createSphere (int x, int y) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(B2Util.toMeterX(x), B2Util.toMeterY(y));
		bodyDef.type = BodyType.DYNAMIC;
		
		body = GameWorld.world.createBody(bodyDef);
		
		CircleShape circleShape = new CircleShape();
		circleShape.m_radius = B2Util.toMeterScale(radius);
		circleShape.m_p.set(0, 0);
		
		FixtureDef fixtDef = new FixtureDef();
		fixtDef.shape = circleShape;
		fixtDef.density = density;
		fixtDef.friction = friction;
		fixtDef.restitution = restitution;
		
		if (body != null) {
			body.createFixture(fixtDef);
			body.setLinearDamping(0.3f);
			body.setUserData(this);
		} else {
			throw new NullPointerException("world.createBody() is locked");
		}
	}
	
	public Strategy getStrategy() {
		return strategy;
	}
	
	public void move(SphereModel sm, WorldData wd, Move move) {
		this.move = move;
		strategy.move(sm, wd, move);
	}
	
	@Override
	public void update() {
		CircleShape cs = (CircleShape) body.getFixtureList().getShape();
		float impulse = move.getParticles() * 0.1f;
		float impulseX = (float) Math.cos(move.getFireAngle()) * impulse;
		float impulseY = (float) Math.sin(move.getFireAngle()) * impulse;
		
		if (B2Util.toPixelScale(cs.m_radius) <= minRadius) {
			return;
		}
		
		applyLinearImpulse(impulseX, impulseY);
		cs.m_radius -= 0.001 * move.getParticles();
		radius = B2Util.toPixelScale(cs.m_radius);
		
		if (moveUp) {
			applyLinearImpulse(0, 0.1f);
			cs.m_radius -= 0.001;
			radius = B2Util.toPixelScale(cs.m_radius);
		}
		if (moveDown) {
			applyLinearImpulse(0, -0.1f);
			cs.m_radius -= 0.001;
			radius = B2Util.toPixelScale(cs.m_radius);
		}
		if (moveRight) {
			applyLinearImpulse(0.1f, 0);
			cs.m_radius -= 0.001;
			radius = B2Util.toPixelScale(cs.m_radius);
		}
		if (moveLeft) { 
			applyLinearImpulse(-0.1f, 0);
			cs.m_radius -= 0.001;
			radius = B2Util.toPixelScale(cs.m_radius);
		}
	}
//	public void split() {
//		Vec2 point = body.getPosition();
//		int x = B2Util.toPixelX(point.x);
//		int y = B2Util.toPixelY(point.y);
//		CircleShape cs = (CircleShape) body.getFixtureList().getShape();
//		float radius = cs.m_radius;
//		Vec2 velocity = body.getLinearVelocity();
//		
//		if (this.radius <= minRadius * 2) {
//			return;
//		}
//		
//		remove();
//		
//		for (int i = 0; i < 2; i++) {
//			int halfRadius = B2Util.toPixelScale(radius/2.0f);
//			PlayerSphere sphere = new PlayerSphere(playerStrategy);
//			sphere.setRadius(halfRadius);
//			sphere.createSphere(x, y);
//			sphere.setVelocity(velocity);
//			MergeAI.gameWorld.addEntity(sphere);
//		}
//	}
	
	private void setRadius(int radius) {
		this.radius = radius;
	}
	
	private void setVelocity(Vec2 velocity) {
		body.setLinearVelocity(velocity);
	}
	
	public void consume(Entity another) {
		CircleShape thisSphere = (CircleShape) this.body.getFixtureList().getShape();
		CircleShape anotherSphere = (CircleShape) another.getBody().getFixtureList().getShape();
		double thisRadius = thisSphere.m_radius;
		double anotherRadius = anotherSphere.m_radius;
		double newRadius = Math.sqrt(Math.pow(thisRadius, 2) + Math.pow(anotherRadius, 2));
		
		thisSphere.m_radius = (float) newRadius;
		body.resetMassData();
		radius = B2Util.toPixelScale(thisSphere.m_radius);
	}
	
	private void applyLinearImpulse(float x, float y) {
		Vec2 point = body.getPosition();
		body.applyLinearImpulse(new Vec2(x, y), point);
	}
	
	public int getX() {
		Vec2 point = body.getPosition();
		return B2Util.toPixelX(point.x);
	}
	
	public int getY() {
		Vec2 point = body.getPosition();
		return B2Util.toPixelY(point.y);
	}
	
	public float getAngle() {
		return body.getAngle();
	}
	
	public float getMeterRadius() {
		CircleShape cs = (CircleShape) body.getFixtureList().getShape();
		return cs.m_radius;
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
		g.setColor(this.color);
		g.fillOval(posX - radius, posY - radius, radius*2, radius*2);
		g.setColor(Color.BLACK);
		g.drawOval(posX - radius, posY - radius, radius*2, radius*2);
		
		//draw inverse velocity vector
		g.drawLine(posX, posY, posX - velX, posY + velY);
		
		//draw impulse vector
		g.setColor(Color.RED);
		if (moveUp) {
			g.drawLine(posX, posY, posX, posY + radius + impulseOffset); 
			impulseOffset++;
		}
		if (moveRight) {
			g.drawLine(posX, posY, posX - radius - impulseOffset, posY); 
			impulseOffset++;
		}
		if (moveLeft) {
			g.drawLine(posX, posY, posX + radius + impulseOffset, posY); 
			impulseOffset++;
		}
		if (moveDown) {
			g.drawLine(posX, posY, posX, posY - radius - impulseOffset); 
			impulseOffset++;
		}
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
	
	public void keyPressed(KeyEvent ev) {
		switch(ev.getKeyCode()) {
			case KeyEvent.VK_UP:
				moveUp = true;
				break;
			case KeyEvent.VK_DOWN:
				moveDown = true;
				break;
			case KeyEvent.VK_RIGHT:
				moveRight = true;
				break;
			case KeyEvent.VK_LEFT:
				moveLeft = true;
				break;
		}
	}
	
	public void keyReleased(KeyEvent ev) {
		switch(ev.getKeyCode()) {
		case KeyEvent.VK_UP:
			moveUp = false;
			impulseOffset = 0;
			break;
		case KeyEvent.VK_DOWN:
			moveDown = false;
			impulseOffset = 0;
			break;
		case KeyEvent.VK_RIGHT:
			moveRight = false;
			impulseOffset = 0;
			break;
		case KeyEvent.VK_LEFT:
			moveLeft = false;
			impulseOffset = 0;
			break;
	}
	}
}
