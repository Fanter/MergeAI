package ru.fanter.bball;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import ru.fanter.bball.entities.Entity;
import ru.fanter.bball.entities.PlayerSphere;

public class Player {
	private ArrayList<PlayerSphere> sphereList = new ArrayList<PlayerSphere>();
	
	public Player() {
		for (int i = 1; i < 10; i++) {
			Random random = new Random();
			int radius = (random.nextInt(5) + 2)*5 ;
			PlayerSphere sphere = new PlayerSphere(radius);
			sphere.createSphere(70*i, 200);
			sphereList.add(sphere);
		}
	}
	
	public void update() {
		for (Entity entity : sphereList) {
			entity.update();
		}
	}
	
	public void draw(Graphics g) {
		for (Entity entity : sphereList) {
			entity.draw(g);
		}		
	}
}
