package todo;

import done.*;
import se.lth.cs.realtime.semaphore.*;

public class ButtonHandler extends Thread {

	private SharedData sd;
	private ClockInput input;
	private Semaphore signal;
	private int previousChoice;

	public ButtonHandler(SharedData sd, ClockInput input, Semaphore signal) {
		this.sd = sd;
		this.input = input;
		this.signal = input.getSemaphoreInstance();
		previousChoice = 0;

	}

	public void doAction() {
		signal.take();
		int choice = input.getChoice();
		sd.setAlarmFlag(input.getAlarmFlag());
		if (choice != previousChoice) {
			if (previousChoice == 1) {
				sd.setAlarmTime(input.getValue(), input.getAlarmFlag());
			}
			if (previousChoice == 2) {
				sd.x(input.getValue());

			}
		}
		previousChoice = choice;
	}

	public void run() {
		try {
			while (true) {
				doAction();
			}
		} catch (Throwable t) {
			return;
		}
	}
}