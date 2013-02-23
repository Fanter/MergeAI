package ru.fanter.merge.model;

public class Move {
	private double fireAngle;
	private int particles;
	private boolean split = false;
	
	/**
	 * 
	 * @param fireAngle - angle to fire particles of sphere in radians
	 * @param numOfParticles - number of particles fires in one tick
	 */
	public void fireParticles(double fireAngle, int numOfParticles) {
		this.fireAngle = fireAngle;
		if (numOfParticles < 1) {
			particles = 1;
		} else if (numOfParticles > 3) {
			particles = 3;
		} else {
			particles = numOfParticles;
		}
	}
	
	public void split(boolean split) {
		this.split = split;
	}
	
	public double getFireAngle() {
		return fireAngle;
	}
	
	public int getParticles() {
		return particles;
	}
}
