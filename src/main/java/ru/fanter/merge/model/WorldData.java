package ru.fanter.merge.model;

import java.util.List;

import ru.fanter.merge.MergeAI;

public class WorldData {
	private List<SphereModel> sphereModels;
	private List<LifeModel> lifeModels;
	private int width = MergeAI.WINDOW_WIDTH;
	private int height = MergeAI.WINDOW_HEIGHT;
	
	public WorldData(List<SphereModel> sm, List<LifeModel> lm) {
		this.sphereModels = sm;
		this.lifeModels = lm;
	}

	public List<SphereModel> getPlayerSpheres() {
		return sphereModels;
	}
	
	public List<LifeModel> getLifeSpheres() {
		return lifeModels;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
