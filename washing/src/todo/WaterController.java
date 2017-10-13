package todo;

import se.lth.cs.realtime.*;
import se.lth.cs.realtime.event.RTEvent;
import done.AbstractWashingMachine;

public class WaterController extends PeriodicThread {
	// TODO: add suitable attributes
	private AbstractWashingMachine mach;
	private boolean fill;
	private double desiredWaterLevel;
	private WaterEvent waterEvent;
	private boolean forFirstTimeAck;

	public WaterController(AbstractWashingMachine mach, double speed) {
		super((long) (1000 / speed)); // TODO: replace with suitable period
		this.mach = mach;
		fill = false;
		desiredWaterLevel = 0;
		waterEvent = null;
		forFirstTimeAck = false;
	}

	public void perform() {
		// TODO: implement this method
		RTEvent e = mailbox.tryFetch();
		if (e != null) {
			waterEvent = (WaterEvent) e;
			int mode = waterEvent.getMode();
			if (mode == WaterEvent.WATER_IDLE) {
				mach.setFill(false);
				mach.setDrain(false);
				fill = false;
			} else if (mode == WaterEvent.WATER_FILL) {
				fill = true;
				forFirstTimeAck = true;
				desiredWaterLevel = waterEvent.getLevel();
				
				if (mach.getWaterLevel() < desiredWaterLevel) {
					mach.setFill(true);
				}
			} else {
				fill = false;
				forFirstTimeAck = true;
				mach.setFill(false);
				mach.setDrain(true);
				desiredWaterLevel = 0;
			}
		} else if (fill) {
			if (mach.getWaterLevel() < desiredWaterLevel) {
				mach.setFill(true);
			} else {
				fill = false;
				mach.setFill(false);
				if (forFirstTimeAck) {
					forFirstTimeAck = false;
					((WashingProgram) waterEvent.getSource()).putEvent(new AckEvent(this));
				}
			}
		} else if (!fill) {
			if (mach.getWaterLevel() <= desiredWaterLevel) {
				mach.setDrain(false);
				if (forFirstTimeAck) {
					forFirstTimeAck = false;
					((WashingProgram) (waterEvent.getSource())).putEvent(new AckEvent(
							this));
				}
			}
		}
	}
}
