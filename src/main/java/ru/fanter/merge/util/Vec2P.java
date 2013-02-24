package ru.fanter.merge.util;

import org.jbox2d.common.Settings;

public class Vec2P {
	public float x;
	public float y;
	
	public Vec2P(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vec2P sub(Vec2P anotherVec) {
		float newX = x - anotherVec.x;
		float newY = y - anotherVec.y;
		return new Vec2P(newX, newY);
	}
	
	public float length() {
		return (float) Math.sqrt((double)(x*x + y*y));
	}
	
	public float normalize() {
		float length = length();
  		if (length < Settings.EPSILON) {
			return 0f;
    	}

    	float invLength = 1.0f / length;
    	x *= invLength;
    	y *= invLength;
    	return length;
	}
	
	public Vec2P mul(float a) {
		return new Vec2P(x * a, y * a);
	}
}
