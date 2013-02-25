package ru.fanter.merge.strategy;

import java.util.Iterator;
import java.util.List;

import ru.fanter.merge.model.LifeModel;
import ru.fanter.merge.model.Move;
import ru.fanter.merge.model.SphereModel;
import ru.fanter.merge.model.WorldData;
import ru.fanter.merge.util.Vec2P;

public class BottomLeftStrategy implements Strategy {
	private SphereModel sphere;
	private WorldData world;
	private Move move;
	
	@Override
	public void move(SphereModel sphere, WorldData world, Move move) {
		this.sphere = sphere;
		this.world = world;
		this.move = move;
		
		moveToNearestLifeSphere();
	}
	
	private void moveToNearestLifeSphere() {
		LifeModel lifeSphere = findClosestLifeSphere();
		
		if (lifeSphere == null) {
			return;
		}
		moveToPoint(lifeSphere.getX(), lifeSphere.getY());
	}
	
	private LifeModel findClosestLifeSphere() {
		List<LifeModel> lifes= world.getLifeSpheres();
		LifeModel closestLifeSphere = null;
		Vec2P minDist = new Vec2P(0, 0);
		
		Iterator<LifeModel> it = lifes.iterator();
		while (it.hasNext()) {
			LifeModel lifeSphere = it.next();
			Vec2P lifeVec = new Vec2P(lifeSphere.getX(), lifeSphere.getY());
			Vec2P sphereVec = new Vec2P(sphere.getX(), sphere.getY());
			Vec2P dist = lifeVec.sub(sphereVec);
			
			if (closestLifeSphere == null) {
				closestLifeSphere = lifeSphere;
				minDist = dist;
			} else if (minDist.length() > dist.length()) {
				closestLifeSphere = lifeSphere;
				minDist = dist;
			}	
		}//while
		return closestLifeSphere;
	}
	
	private void moveToPoint(float x, float y) {
		Vec2P sphereVec = new Vec2P(sphere.getX(), sphere.getY());
		Vec2P pointVec = new Vec2P(x, y);
		Vec2P distVec = pointVec.sub(sphereVec);
		Vec2P velocityVec = new Vec2P(sphere.getVelocityX(), sphere.getVelocityY());
		
		distVec.normalize();
		distVec = distVec.mul(80.0f);
		Vec2P stirrVec = distVec.sub(velocityVec);
		float atan = (float) Math.atan2((double) stirrVec.y, (double) stirrVec.x);
		move.fireParticles((float)(-atan + Math.PI), 1);
	}
}
