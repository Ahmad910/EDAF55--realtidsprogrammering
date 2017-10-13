package todo;

import done.AbstractWashingMachine;

public class WashingProgram2 extends WashingProgram {

	protected WashingProgram2(AbstractWashingMachine mach, double speed, TemperatureController tempController,
			WaterController waterController, SpinController spinController) {
		super(mach, speed, tempController, waterController, spinController);

	}
	/*
	 * White wash: Like program 1, but with a 15 minute pre-wash in 40◦C. The
	 * main wash should be performed in 90◦C.
	 */

	@Override
	protected void wash() throws InterruptedException {
		myMachine.setLock(true);
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, 0.9));
		mailbox.doFetch();
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_SET, 40));
		mailbox.doFetch();
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_SLOW));
		sleep((long) ((900000 / mySpeed) - 3000)); // sleep 15 seconds
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_OFF));
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, 0));
		mailbox.doFetch();
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, 0.9));
		mailbox.doFetch(); // wait for Ack
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_SET, 90));
		mailbox.doFetch(); // wait for Ack
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_SLOW));
		sleep((long) ((1800000/mySpeed) -6000)); // sleep 30 seconds
		mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_OFF));
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_IDLE, 0));
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, 0));
		mailbox.doFetch();
		for (int i = 0; i < 5; i++) {
			myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, 0.9));
			mailbox.doFetch();
			mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_SLOW));
			sleep((long) ((60000 / mySpeed) + 3800)); // sleep 3 seconds
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
