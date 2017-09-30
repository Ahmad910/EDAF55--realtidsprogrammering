package lift;

public class PersonThread extends Thread {
	private int currentFloor;
	private int targetFloor;
	private DataMonitor1 dataMonitor;

	public PersonThread(DataMonitor1 dataMonitor) {
		this.dataMonitor = dataMonitor;
		generateRandoms();
	}
	private void generateRandoms(){
		int x = (int) (Math.random() * 7);
		int y = (int) (Math.random() * 7);
		while(x == y){
			y = (int) (Math.random() * 7);
		}
		currentFloor = x;
		targetFloor = y;
	}

	public void run() {
		while (true) {
			dataMonitor.personWaiting(currentFloor, targetFloor);
			try {
				sleep(1000 * ((int) (Math.random() * 46)));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			generateRandoms();
		}
	}
}