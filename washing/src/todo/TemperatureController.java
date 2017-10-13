package todo;


import se.lth.cs.realtime.*;
import se.lth.cs.realtime.event.RTEvent;
import done.AbstractWashingMachine;


public class TemperatureController extends PeriodicThread {
	// TODO: add suitable attributes
	private AbstractWashingMachine mach;

	private boolean tempBoolean;
	private double desiredTemp;
	private TemperatureEvent tempEvent;
	private boolean forFirstTimeAck;
	public TemperatureController(AbstractWashingMachine mach, double speed) {
		super((long) (1000/speed)); // TODO: replace with suitable period
		this.mach = mach;
		tempBoolean = false;
		desiredTemp = 0;
		tempEvent = null; 
		forFirstTimeAck = false;
	}

	public void perform() {
		// TODO: implement this method
		RTEvent e = mailbox.tryFetch();
		if(e != null){
			tempEvent = (TemperatureEvent) e;
			int mode = tempEvent.getMode();
			if(mode == TemperatureEvent.TEMP_IDLE){
				mach.setHeating(false);
				tempBoolean = false;
				desiredTemp = 0;
			}else{
				tempBoolean = true;
				forFirstTimeAck = true;
				desiredTemp = tempEvent.getTemperature();
				if(mach.getTemperature() < desiredTemp && mach.getWaterLevel() > 0){
					mach.setHeating(true);
				}
			}
		}else if (tempBoolean && (mach.getTemperature() + 2) < desiredTemp && mach.getWaterLevel() > 0){
			mach.setHeating(true);
		}else if(tempBoolean && mach.getTemperature() >= desiredTemp){
			mach.setHeating(false);
			if(forFirstTimeAck){
				forFirstTimeAck = false;
				((WashingProgram) tempEvent.getSource()).putEvent(new AckEvent(this));
			}
		}
	}
}
