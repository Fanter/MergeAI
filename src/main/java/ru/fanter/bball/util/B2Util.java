package ru.fanter.bball.util;

import ru.fanter.bball.BouncyBall;

public class B2Util {
	//1 meter = 100 pixels;
	private static final int SCALE = 100;

	public static int toPixelX(float x) {
		return (int) (x * SCALE);
	}
	
	public static int toPixelY(float y) {
		return BouncyBall.WINDOW_HEIGHT - (int) (y * SCALE);
	}
	
	public static float toMeterX(int x) {
		return (float) x/ (float) SCALE;
	}
	
	public static float toMeterY(int y) {
		return (float) (BouncyBall.WINDOW_HEIGHT - y) / (float) SCALE;
	}
	
	public static int toPixelScale(float size) {
		return (int) (size * SCALE);
	}
	
	public static float toMeterScale(int size) {
		return (float) size / (float) SCALE;
	}
}
