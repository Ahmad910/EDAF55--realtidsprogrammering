package todo;

import se.lth.cs.realtime.*;
import se.lth.cs.realtime.event.RTEvent;
import done.AbstractWashingMachine;

public class SpinController extends PeriodicThread {
	// TODO: add suitable attributes
	private AbstractWashingMachine mach;

	private SpinEvent spinEvent;
	private int counter;
	private int currentDir;

	public SpinController(AbstractWashingMachine mach, double speed) {
		super((long) (1000 / speed)); // TODO: replace with suitable period
		this.mach = mach;
		spinEvent = null;
		counter = 0;
		currentDir = 0;
	}

	public void perform() {
		// TODO: implement this method
		RTEvent e = mailbox.tryFetch();
		if (e != null) {
			spinEvent = (SpinEvent) e;
			int mode = spinEvent.getMode();
			if (mode == SpinEvent.SPIN_OFF) {
				currentDir = AbstractWashingMachine.SPIN_OFF;
				mach.setSpin(currentDir);
				counter = 0;
			} else if (mode == SpinEvent.SPIN_SLOW) {
				currentDir = AbstractWashingMachine.SPIN_LEFT;
				mach.setSpin(currentDir);
				counter += 1;
			} else {
				currentDir = AbstractWashingMachine.SPIN_FAST;
				mach.setSpin(currentDir);
			}
		} else if (currentDir == AbstractWashingMachine.SPIN_LEFT || currentDir == AbstractWashingMachine.SPIN_RIGHT) {
			if (counter == 600) {
				if (currentDir == AbstractWashingMachine.SPIN_LEFT) {
					currentDir = AbstractWashingMachine.SPIN_RIGHT;
					mach.setSpin(currentDir);
					counter = 0;
				} else if (currentDir == AbstractWashingMachine.SPIN_RIGHT) {
					currentDir = AbstractWashingMachine.SPIN_LEFT;
					mach.setSpin(currentDir);
					counter = 0;
				}
			}else{
				counter += 1;
			}
		} else if (currentDir == AbstractWashingMachine.SPIN_FAST) {
			mach.setSpin(currentDir);
		} else {
			mach.setSpin(currentDir);
		}
	}
}
