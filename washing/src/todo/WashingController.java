package todo;

import done.*;

public class WashingController implements ButtonListener {	
	// TODO: add suitable attributes
	private AbstractWashingMachine theMachine;
	private double theSpeed;
	private WashingProgram washingProgram;
	private TemperatureController temperatureController;
	private WaterController waterController;
	private SpinController spinController;
    public WashingController(AbstractWashingMachine theMachine, double theSpeed) {
		// TODO: implement this constructor
    	this.theMachine = theMachine;
    	this.theSpeed = theSpeed;
    	createControllerThreads();
    }
    private void createControllerThreads(){
    	temperatureController = new TemperatureController(theMachine, theSpeed);
    	waterController = new WaterController(theMachine, theSpeed);
    	spinController = new SpinController(theMachine, theSpeed);
    	temperatureController.start();
    	waterController.start();
    	spinController.start();
    }

    public void processButton(int theButton) {
		// TODO: implement this method
    	if(theButton == 0){
    		if(washingProgram != null && washingProgram.isAlive()){
        		washingProgram.interrupt();
    		}
    	}else if(theButton == 1){
    		if(washingProgram == null || !washingProgram.isAlive()){
    			washingProgram = new WashingProgram1(theMachine, theSpeed, temperatureController, waterController, spinController);
    			washingProgram.start();
    		}
    	}else if(theButton == 2){
    		if(washingProgram == null || !washingProgram.isAlive()){
    			washingProgram = new WashingProgram2(theMachine, theSpeed, temperatureController, waterController, spinController);
    			washingProgram.start();
    		}
    	}else{
    		if(washingProgram == null || !washingProgram.isAlive()){
    			washingProgram = new WashingProgram3(theMachine, theSpeed, temperatureController, waterController, spinController);
    			washingProgram.start();
    		}
    	}
    }
}
