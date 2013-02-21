package ru.fanter.merge;

import javax.swing.*;

public class LaunchAI {
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MergeAI().start();
			}
		});
	}
}
