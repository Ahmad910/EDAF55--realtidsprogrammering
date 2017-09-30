package lift;

public class LiftThread extends Thread {

	private DataMonitor1 dataMonitor;
	private int currentFloor;
	private int nextFloor;
	private LiftView liftView;

	public LiftThread(DataMonitor1 dataMonitor, LiftView lv) {
		this.dataMonitor = dataMonitor;
		this.liftView = lv;
		currentFloor = 0;
		nextFloor = 1;

	}

	public void run() {
		while (true) {
			dataMonitor.action();
			dataMonitor.move(nextFloor);
			moveElevator();
		}
	}

	public void moveElevator() {
		if (currentFloor == 6 || currentFloor > nextFloor) {
			if (currentFloor == 6) {
				nextFloor = 5;
			}
			liftView.moveLift(currentFloor, nextFloor);
			currentFloor--;
			nextFloor--;
			if (currentFloor == 0) {
				nextFloor = 1;
			}
		} else {
			liftView.moveLift(currentFloor, nextFloor);
			currentFloor++;
			nextFloor++;
		}
		if (nextFloor == 7) {
			nextFloor = 5;
		}
	}
}