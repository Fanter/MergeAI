package ru.fanter.merge.model;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import ru.fanter.merge.MergeAI;
import ru.fanter.merge.entities.LifeSphere;
import ru.fanter.merge.util.B2Util;

public class LifeModel {
	private float x;
	private float y;
	private int ticksToLive;
	
	public LifeModel(LifeSphere ls) {
		Body body = ls.getBody();
		Vec2 point = body.getPosition();
		
		x = B2Util.SCALE * point.x;
		y = MergeAI.WINDOW_HEIGHT - B2Util.SCALE * point.y;
		ticksToLive = ls.getTicksLeft();
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public int getTicksToLive() {
		return ticksToLive;
	}
}
