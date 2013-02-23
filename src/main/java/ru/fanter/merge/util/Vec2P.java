package ru.fanter.merge.util;

public class Vec2P {
	public float x;
	public float y;
	
	public Vec2P(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vec2P sub(Vec2P anotherVec) {
		float newX = x - anotherVec.x;
		float newY = anotherVec.y - y;
		return new Vec2P(newX, newY);
	}
	
	public float length() {
		return (float) Math.sqrt((double)(x*x + y*y));
	}
}
