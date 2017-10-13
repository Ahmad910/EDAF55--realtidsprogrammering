package todo;

import done.AbstractWashingMachine;

public class WashingProgram1 extends WashingProgram {

	protected WashingProgram1(AbstractWashingMachine mach, double speed, TemperatureController tempController,
			WaterController waterController, SpinController spinController) {
		super(mach, speed, tempController, waterController, spinController);
	}

	/*
	 * @see todo.WashingProgram#wash() Color wash: Lock the hatch, let water
	 * into the machine, heat to 60◦C, keep the temperature for 30 minutes,
	 * drain, rinse 5 times 2 minutes in cold water, centrifuge for 5 minutes
	 * and unlock the hatch
	 */
	@Override
	protected void wash() throws InterruptedException {
		myMachine.setLock(true);
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, 0.9));
		mailbox.doFetch(); // wait for Ack
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_SET, 60));
		mailbox.doFetch(); // wait for Ack
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_SLOW));
		sleep((long) (1800000/(mySpeed) - 6000)); 
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_OFF));
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_IDLE, 0));
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, 0));
		mailbox.doFetch();
		for (int i = 0; i < 5; i++) {
			myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, 0.9));
			mailbox.doFetch();
			mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_SLOW));
			sleep((long) ((60000 / mySpeed) + 1800)); // sleep 3 seconds
			mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_OFF));
			myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, 0));
			mailbox.doFetch();
		}
	
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_FAST));
		sleep((long) ((150000 / mySpeed) + 2000)); // sleep 5 seconds
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_OFF));
		myMachine.setLock(false);
	}
	
	
}
