package todo;
import done.*;
import se.lth.cs.realtime.semaphore.Semaphore;

/**
 * Main class of alarm-clock application.
 * Constructor providing access to IO.
 * Method start corresponding to main,
 * with closing down done in terminate.
 */

/**
 * Main class of alarm-clock application.
 * Constructor providing access to IO.
 * Method start corresponding to main,
 * with closing down done in terminate.
 */
public class AlarmClock {

	private ClockInput	input;
	private ClockOutput	output;
	private Semaphore	signal; 
	private SharedData sharedData;
	private ButtonHandler buttonHandler;
	private TimeHandler timeHandler;
	// Declare thread objects here..

	/**
	 * Create main application and bind attributes to device drivers.
	 * @param i The input from simulator/emulator/hardware.
	 * @param o Dito for output.
	 */
	public AlarmClock(ClockInput i, ClockOutput o) {
		input = i;
		output = o;
		signal = input.getSemaphoreInstance();
		this.sharedData = new SharedData(o);
		timeHandler = new TimeHandler(sharedData);
		buttonHandler = new ButtonHandler(sharedData,input, input.getSemaphoreInstance());
	}

	/**
	 * Tell threads to terminate and wait until they are dead.
	 */
	public void terminate() {
		// Do something more clever here...
		timeHandler.interrupt();
		buttonHandler.interrupt();
		output.console("AlarmClock exit.");
	}
	
	/**
	 * Create thread objects, and start threads
	 */
	public void start() {
		buttonHandler.start();
		timeHandler.start();
	}
	
//	class InputOutputTest implements Runnable {
//		public void run() {
//			long curr; int time, mode; boolean flag;
//			output.console("Click on GUI to obtain key presses!");
//			while (!Thread.currentThread().isInterrupted()) {
//				curr = System.currentTimeMillis();
//			time = input.getValue();
//				flag = input.getAlarmFlag();
//			mode = input.getChoice();
//			output.doAlarm();
//			output.console(curr, time, flag, mode);
//			if (time == 120000) break; // Swe: Bryter f�r middag
//			signal.take();
//		}
//	output.console("IO-test terminated #");
//	}

//	}
	
}