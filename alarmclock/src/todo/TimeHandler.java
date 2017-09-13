package todo;



public class TimeHandler extends Thread {
	private long dt;
	private SharedData sharedData;
	
	public TimeHandler(SharedData sharedData){
		dt = 0;
		this.sharedData = sharedData;
	}
	
	public void run(){
		while(true){
			long targetTime = System.currentTimeMillis();
			targetTime += 1000;
			try {
				sleep(1000 - dt);
				dt = System.currentTimeMillis() - targetTime;
				//sharedData.incrementTime();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
