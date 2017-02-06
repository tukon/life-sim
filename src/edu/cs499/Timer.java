/* 
 * Project: Life Sim for CS 499
 * Team 2: Adam, Alicia, Matt, Paul
 */
package edu.cs499;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Tracks time in the program, and tells it when to update. When creating a
 * Timer, give it a Runnable, and the Timer will call its run() method at
 * regular intervals.
 * @author Adam Armbrester
 */
public class Timer {
	
	/**
	 * Contains the different speeds that the timer can run at.
	 */
	public enum TimerSpeed {
		X1(1000),
		X10(100),
		X50(20),
		X100(10);
		
		/**
		 * The timer’s period, in milliseconds.
		 */
		private final int period;
		
		/**
		 * The timer’s multiplier, such as 1x, 2x, etc.
		 */
		private final int multiplier;
		
		TimerSpeed(int period) {
			this.period = period;
			multiplier = 1000/period;
		}
		
		/**
		 * Retrieves the timer’s period.
		 * @return Time between ticks, in milliseconds.
		 */
		public int getPeriod() {
			return period;
		}
		
		/**
		 * Retrieves the timer’s multipler.
		 * @return 1x, 2x, etc.
		 */
		public int getMultiplier() {
			return multiplier;
		}
	}
	
	/**
	 * The task to be called on each timer tick.
	 */
	private final Runnable task;
	
	/**
	 * The speed at which the timer should run.
	 */
	private TimerSpeed speed;
	
	/**
	 * The object responsible for tracking the elapsed time, and running the
	 * task.
	 */
	private final ScheduledExecutorService scheduler;
	
	/**
	 * Handle for cancelling the scheduled task.
	 */
	private ScheduledFuture<?> handle;
	
	/**
	 * True if the timer is running, false if it is stopped.
	 */
	private boolean running;
	
	/**
	 * Creates a new Timer object. The timer’s default speed is 1x (one tick
	 * per second). It is stopped by default.
	 * @param task The task to be executed on each timer tick.
	 */
	public Timer(Runnable task) {
		this.task = task;
		speed = TimerSpeed.X1;
		scheduler = Executors.newScheduledThreadPool(1);
		running = false;
	}
	
	/**
	 * Starts the timer, if it is not already running. The first update will
	 * occur immediately.
	 */
	public void start() {
		if (!running) {
			handle = scheduler.scheduleAtFixedRate(task, 0,
				speed.getPeriod(), MILLISECONDS);
			running = true;
		}
	}
	
	/**
	 * Suspends the timer, if it is not already suspended.
	 */
	public void suspend() {
		if (running) {
			handle.cancel(true);
			running = false;
		}
	}
	
	/**
	 * Permenently terminates the timer. It cannot be used again after this
	 * is called.
	 * \see suspend
	 */
	public void terminate() {
		if (running) {
			suspend();
		}
		scheduler.shutdown();
	}
	
	/**
	 * Changes the speed of the timer. If it is currently running, it will
	 * be restarted automatically.
	 * @param speed The new speed for the timer to run at.
	 */
	public void setSpeed(TimerSpeed speed)
	{
		this.speed = speed;
		if (running)
		{
			suspend();
			start();
		}
	}
	
	/**
	 * Gets the timer’s speed.
	 * @return Timer’s speed. The period (in milliseconds) can be retrieved
	 *         with the getPeriod() method.
	 */
	public TimerSpeed getSpeed() {
		return speed;
	}
}
