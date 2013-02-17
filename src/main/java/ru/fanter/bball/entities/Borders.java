package ru.fanter.bball.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import ru.fanter.bball.BouncyBall;
import ru.fanter.bball.GameWorld;
import ru.fanter.bball.util.B2Util;

public class Borders implements Entity {
	private Body body;
	private EntityType type = EntityType.BORDER;
	private ArrayList<EdgePoints> pointList = new ArrayList<EdgePoints>();
	
	public Borders() {
		int width = BouncyBall.WINDOW_WIDTH;
		int height = BouncyBall.WINDOW_HEIGHT;
		
		pointList.add(new EdgePoints(0, 0, 0, height));//left wall
		pointList.add(new EdgePoints(0, height, width, height));//bottom
		pointList.add(new EdgePoints(0, 0, width, 0));//top
		pointList.add(new EdgePoints(width, 0, width, height));//right wall
		
		for (EdgePoints ep : pointList) {
			createBorder(ep);
		}
	}
	
	private void createBorder(EdgePoints ep) {
		float mx1 = B2Util.toMeterX(ep.x1);
		float my1 = B2Util.toMeterY(ep.y1);
		float mx2 = B2Util.toMeterX(ep.x2);
		float my2 = B2Util.toMeterY(ep.y2); 
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(0, 0);
		bodyDef.type = BodyType.STATIC;
		
		PolygonShape polyShape = new PolygonShape();
		polyShape.setAsEdge(new Vec2(mx1, my1), new Vec2(mx2, my2));
		
		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = polyShape;
		
		body = GameWorld.world.createBody(bodyDef);
		body.createFixture(fixDef);
		body.setUserData(this);
	}
	
	@Override
	public void update() {
		
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		
		for(EdgePoints point : pointList) {
			g.drawLine(point.x1, point.y1, point.x2, point.y2);	
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
	
	public class EdgePoints {
		public int x1;
		public int y1;
		public int x2;
		public int y2;
		
		public EdgePoints(int x1, int y1, int x2, int y2) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}
	}
}
