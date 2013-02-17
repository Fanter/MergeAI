package ru.fanter.bball;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class BouncyBalls {
	public void simulateWorld() {
		Vec2 gravity = new Vec2(5.0f, -10.0f);
		boolean doSleep = true;
		World world = new World(gravity, doSleep);
		
		BodyDef bd = new BodyDef();
		bd.position.set(50, 50);
		bd.type = BodyType.DYNAMIC;
		
		CircleShape cs = new CircleShape();
		cs.m_radius = 0.5f;
		
		FixtureDef fd = new FixtureDef();
		fd.shape = cs;
		fd.density = 0.5f;
		fd.friction = 0.3f;
		fd.restitution = 0.5f;
		
		Body body = world.createBody(bd);
		body.createFixture(fd);
		
		//world simulation
		float timeStep = 1.0f/60.0f;
		int velocityIterations = 6;
		int positionIterations = 2;
		
		for(int i = 0; i < 10; i++) {
			world.step(timeStep, velocityIterations, positionIterations);
			Vec2 position = body.getPosition();
			float angle = body.getAngle();
			System.out.printf("%4.2f %4.2f %4.2f\n", position.x, position.y, angle);
		}
		
		System.out.println("hello world");
	}
}
