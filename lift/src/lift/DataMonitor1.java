package lift;

public class DataMonitor1 {
	private int currentFloor, nextFloor, load, numOfFloors;
	private int[] waitEntry, waitExit;
	private LiftView liftView;

	public DataMonitor1(LiftView liftView) {
		this.liftView = liftView;
		currentFloor = 0;
		load = 0;
		nextFloor = 0;
		numOfFloors = 7;
		waitExit = new int[numOfFloors];
		waitEntry = new int[numOfFloors];
	}

	public synchronized void move(int next) {
		while (waitEntry[currentFloor] > 0 && load < 4) {
			try {
				wait();
			} catch (Throwable t) {
			}
		}
		nextFloor = next;
		notifyAll();
	}

	public synchronized void action() {
		try {
			while (currentFloor != nextFloor && waitExit[currentFloor] > 0) {
				wait();
			}
		} catch (Throwable t) {
		}
		while (load > 0 && waitExit[nextFloor] > 0) {
			waitExit[nextFloor] = waitExit[nextFloor] - 1;
			load--;
			liftView.drawLift(nextFloor, load);
		}
		currentFloor = nextFloor;
		notifyAll();
	}

	public synchronized void personWaiting(int initialFloor, int targetFloor) {
		waitEntry[initialFloor]++;
		liftView.drawLevel(initialFloor, waitEntry[initialFloor]);
		notifyAll();
		try {
			while (load == 4 || initialFloor != currentFloor) {
				wait();
			}
		} catch (Throwable t) {
		}
		waitExit[targetFloor]++;
		if (load < 4) {
			load++;
			waitEntry[initialFloor]--;
			liftView.drawLift(initialFloor, load);
			liftView.drawLevel(initialFloor, waitEntry[initialFloor]);
		}
		notifyAll();
	}

}