package ru.fanter.merge.util;

import ru.fanter.merge.MergeAI;

public class B2Util {
	//1 meter = 100 pixels;
	private static final int SCALE = 100;
	
	public static int toPixelX(float x) {
		return (int) (x * SCALE);
	}
	
	public static float toScaledX(float x) {
		return x * SCALE;
	}
	
	public static int toPixelY(float y) {
		return MergeAI.WINDOW_HEIGHT - (int) (y * SCALE);
	}
	
	public static float toScaledY(float y) {
		return MergeAI.WINDOW_HEIGHT - (y * SCALE);
	}
	
	public static float toMeterX(int x) {
		return (float) x/ (float) SCALE;
	}
	
	public static float toMeterY(int y) {
		return (float) (MergeAI.WINDOW_HEIGHT - y) / (float) SCALE;
	}
	
	public static int toPixelScale(float size) {
		return (int) (size * SCALE);
	}
	
	public static float toMeterScale(int size) {
		return (float) size / (float) SCALE;
	}
	
	public static float toScaled(float size) {
		return size * SCALE;
	}
}
