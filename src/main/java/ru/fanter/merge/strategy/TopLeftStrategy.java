package ru.fanter.merge.strategy;

import ru.fanter.merge.model.Move;
import ru.fanter.merge.model.SphereModel;
import ru.fanter.merge.model.WorldData;

public class TopLeftStrategy implements Strategy {
	
	@Override
	public void move(SphereModel sphere, WorldData world, Move move) {
		//move.fireParticles(Math.toRadians(30), 1);
	}
}
