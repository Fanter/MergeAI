package ru.fanter.merge.util;

import ru.fanter.merge.MergeAI;

public class B2Util {
	//1 meter = 100 pixels;
	public static final int SCALE = 100;
	
	public static float toPixelX(float x) {
		return x * SCALE;
	}
	
	public static float toPixelY(float y) {
		return MergeAI.WINDOW_HEIGHT - y * SCALE;
	}
	
	public static float toMeterX(int x) {
		return (float) x / (float) SCALE;
	}
	
	public static float toMeterY(int y) {
		return (float) (MergeAI.WINDOW_HEIGHT - y) / (float) SCALE;
	}
	
	public static float toPixelScale(float size) {
		return size * SCALE;
	}
	
	public static float toMeterScale(int size) {
		return (float) size / (float) SCALE;
	}
	
	public static float toMeterScale(float size) {
		return size / (float) SCALE;
	}
}
