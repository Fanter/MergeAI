package ru.fanter.merge.model;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import ru.fanter.merge.MergeAI;
import ru.fanter.merge.entities.PlayerSphere;
import ru.fanter.merge.util.B2Util;

public class SphereModel {
	private float x;
	private float y;
	private float radius;
	private float velocityX;
	private float velocityY;
	private int particlesToSplit;
	private int particles;
	
	public SphereModel(PlayerSphere ps) {
		Body body = ps.getBody();
		CircleShape cs = (CircleShape) body.getFixtureList().getShape();
		Vec2 point = body.getPosition();
		Vec2 velocity = body.getLinearVelocity();
		
		x = B2Util.toPixelX(point.x);
		y = B2Util.toPixelY(point.y);
		velocityX = B2Util.toPixelScale(velocity.x);
		//minus because box2d y axis  has opposite direction
		velocityY = -B2Util.toPixelScale(velocity.y);
		radius = B2Util.toPixelScale(cs.m_radius);
		particles = ps.getParticles();
		particlesToSplit = ps.getNumOfParticlesToSplit();
	}
	
	public int getParticles() {
		return particles;
	}
	
	public int getParticlesToSplit() {
		return particlesToSplit;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getRadius() {
		return radius;
	}
	
	public float getVelocityX() {
		return velocityX;
	}
	
	public float getVelocityY() {
		return velocityY;
	}
	
	public boolean isTeammate() {
		return false;
	}
}
