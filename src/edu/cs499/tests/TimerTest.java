/* 
 * Project: Life Sim for CS 499
 * Team 2: Adam, Alicia, Matt, Paul
 */
package edu.cs499.tests;

import edu.cs499.Timer;
import edu.cs499.Timer.TimerSpeed;

/**
 * Tests the functionality of the (custom) Timer class.
 * @author Adam Armbrester
 */
public class TimerTest implements Runnable {

	private Timer timer;
	
	private int counter;
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		TimerTest test = new TimerTest();
		test.runTests();
	}
	
	private void pause(int ms) {
		try {
			java.lang.Thread.sleep(ms);
		} catch (java.lang.InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
	
	public void runTests() {
		timer = new Timer(this);
		
		timer.start();
		
		pause(3500);
		
		System.out.println("Pausing for a bit…");
		timer.suspend();
		pause(2000);
		System.out.println("Resuming…");
		timer.start();
		
		pause(3000);
		System.out.println("Changing speed…");
		timer.setSpeed(TimerSpeed.X10);
	}
	
	@Override
	public void run() {
		System.out.printf("Multiplier: x%d; iteration: %d\n",
			timer.getSpeed().getMultiplier(),
			counter++);
		
		if (counter == 20)
		{
			timer.terminate();
		}
	}
	
}
