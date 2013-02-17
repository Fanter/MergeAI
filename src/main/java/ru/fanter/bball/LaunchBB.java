package ru.fanter.bball;

import javax.swing.*;

public class LaunchBB {
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new BouncyBall().start();
			}
		});
	}
}
