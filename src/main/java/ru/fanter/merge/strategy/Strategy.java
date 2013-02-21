package ru.fanter.merge.strategy;

import ru.fanter.merge.model.Move;
import ru.fanter.merge.model.SphereModel;
import ru.fanter.merge.model.WorldData;

public interface Strategy {
	public void move(SphereModel sphere, WorldData world, Move move);
}
