package ru.fanter.merge;

import ru.fanter.merge.model.Move;
import ru.fanter.merge.model.SphereModel;
import ru.fanter.merge.model.WorldData;

public class PlayerStrategy {
	public void move(SphereModel sphere, WorldData world, Move move) {
		move.fireParticles(Math.toRadians(30), 1);
	}
}
