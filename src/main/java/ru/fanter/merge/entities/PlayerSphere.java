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
import ru.fanter.merge.event.EntityEvent;
import ru.fanter.merge.event.EntityListener;
import ru.fanter.merge.event.EntityEvent.EventType;
import ru.fanter.merge.model.Move;
import ru.fanter.merge.model.SphereModel;
import ru.fanter.merge.model.WorldData;
import ru.fanter.merge.strategy.Strategy;
import ru.fanter.merge.util.B2Util;

public class PlayerSphere extends Entity implements Consumable {
	private final int particlesToSplit = 600;
	private int particles = particlesToSplit - 200;
	private int impulseOffset;
	private float radius;
	private Body body;
	private EntityType type = EntityType.PLAYER_SPHERE;
	private Strategy strategy;
	private Move move;
	private Color color;
	private float fireAngle;
	
	//pixels per second
	private float maxVelocity = 60.0f;
	
	//for keyboard manipulation
	private boolean moveUp;
	private boolean moveDown;
	private boolean moveRight;
	private boolean moveLeft;
	
	public PlayerSphere(Strategy strategy, Color color) {
		this.strategy = strategy;
		this.color = color;
		this.radius = calculateRadius();
	}
	
	private float calculateRadius() {
		return 10 + (float) Math.sqrt((double) particles) * 0.5f;
	}
	
	public void createSphere (int x, int y) {
		float density = 8.5f;
		float friction = 0.0f;
		float restitution = 1.0f;
		
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
			body.setLinearDamping(0.1f);
			body.setUserData(this);
		} else {
			throw new NullPointerException("world.createBody() is locked");
		}
	}
	
	public void move(SphereModel sm, WorldData wd, Move move) {
		this.move = move;
		strategy.move(sm, wd, move);
		
		fireAngle = this.move.getFireAngle();
	}
	
	@Override
	public void update() {
		float impulseCoeff = 0.01f;
		CircleShape cs = (CircleShape) body.getFixtureList().getShape();
		float impulse = move.getParticles() * impulseCoeff;
		float impulseX = - (float) Math.cos(move.getFireAngle()) * impulse;
		float impulseY = - (float) Math.sin(move.getFireAngle()) * impulse;
		
		if (particles <= 0) {
			return;
		}
		
		if (move.shouldSplit()) {
			split();
			return;
		}
		
		if (moveUp) {
			applyLinearImpulse(0, impulseCoeff);
			particles -= 1;
		}
		if (moveDown) {
			applyLinearImpulse(0, -impulseCoeff);
			particles -= 1;
		}
		if (moveRight) {
			applyLinearImpulse(impulseCoeff, 0);
			particles -= 1;
		}
		if (moveLeft) { 
			applyLinearImpulse(-impulseCoeff, 0);
			particles -= 1;
		}
		
		//Sphere velocity has a limit
		if (body.getLinearVelocity().length() > B2Util.toMeterScale(maxVelocity)) {
			float coeff = B2Util.toMeterScale(maxVelocity) / body.getLinearVelocity().length();
			float oldVelocityX = body.getLinearVelocity().x;
			float oldVelocityY = body.getLinearVelocity().y;
			float newVelocityX = oldVelocityX * coeff;
			float newVelocityY = oldVelocityY * coeff;
			body.setLinearVelocity(new Vec2(newVelocityX, newVelocityY));
		}

		applyLinearImpulse(impulseX, impulseY);
		particles -= move.getParticles();
		radius = calculateRadius();
		cs.m_radius = B2Util.toMeterScale(radius);
		body.resetMassData();
	}
	
	private void split() {
		createDividedSphere();
		createDividedSphere();
		remove();
	}
	
	private void createDividedSphere() {
		PlayerSphere sphere = new PlayerSphere(strategy, color);
		sphere.createSphere(getX(), getY());
		sphere.addEntityListener(entityListener);
		sphere.particles = this.particles / 2;
		Vec2 velocity = body.getLinearVelocity();
		sphere.body.setLinearVelocity(velocity);
		
		EntityEvent ee = new EntityEvent(sphere, EventType.SPLIT);
		entityListener.update(ee);
	}
	
	public boolean isTeammate(PlayerSphere another) {
		if (this.color == another.color) {
			return true;
		}
		return false;
	}
	
	public void consume(Consumable another) {
		particles += another.getParticles();
		radius = calculateRadius();
		body.resetMassData();
	}
	
	private void applyLinearImpulse(float x, float y) {
		Vec2 point = body.getPosition();
		body.applyLinearImpulse(new Vec2(x, y), point);
	}
	
	@Override
	public void draw(Graphics g) {
		Vec2 velocity = body.getLinearVelocity();
		Vec2 position = body.getPosition();
		int velX = (int) B2Util.toPixelScale(velocity.x);
		int velY = (int) B2Util.toPixelScale(velocity.y);
		int posX = (int) B2Util.toPixelX(position.x);
		int posY = (int) B2Util.toPixelY(position.y);
		int radius = (int) this.radius;
		int impulseX = -(int) (Math.cos((double) fireAngle) * radius);
		int impulseY = -(int) (Math.sin((double) fireAngle) * radius);
		
		//draw Circle
		g.setColor(this.color);
		g.fillOval(posX - radius, posY - radius, radius*2, radius*2);
		g.setColor(Color.BLACK);
		g.drawOval(posX - radius, posY - radius, radius*2, radius*2);
		
		//draw inverse velocity vector
		g.drawLine(posX, posY, posX - velX, posY + velY);
		
		//draw strategy impulse vector
		g.setColor(Color.RED);
		g.drawLine(posX, posY, posX - impulseX, posY + impulseY); 
		impulseOffset++;
		
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
	
	public Strategy getStrategy() {
		return strategy;
	}
	
	public int getX() {
		Vec2 point = body.getPosition();
		return (int) B2Util.toPixelX(point.x);
	}
	
	public int getY() {
		Vec2 point = body.getPosition();
		return (int) B2Util.toPixelY(point.y);
	}
	
	public float getAngle() {
		return body.getAngle();
	}
	
	public float getMeterRadius() {
		CircleShape cs = (CircleShape) body.getFixtureList().getShape();
		return cs.m_radius;
	}
	
	@Override
	public int getParticles() {
		return particles;
	}
	
	public int getNumOfParticlesToSplit() {
		return particlesToSplit;
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
