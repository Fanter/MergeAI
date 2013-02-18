package ru.fanter.merge;

import javax.swing.*;

public class LaunchBB {
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MergeAI().start();
			}
		});
	}
}
