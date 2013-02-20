package ru.fanter.merge.model;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import ru.fanter.merge.entities.LifeSphere;
import ru.fanter.merge.util.B2Util;

public class LifeModel {
	private double x;
	private double y;
	private int ticksToLive;
	
	public LifeModel(LifeSphere ls) {
		Body body = ls.getBody();
		Vec2 point = body.getPosition();
		
		x = B2Util.toScaledX(point.x);
		y = B2Util.toScaledY(point.y);
		ticksToLive = ls.getTicksLeft();
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public int getTicksToLive() {
		return ticksToLive;
	}
}
