package todo;

public class TimeHandler extends Thread {
	private long targetTime;
	private long dt;
	private SharedData sd;

	public TimeHandler(SharedData sd) {
		super();
		this.sd = sd;
	}

	public void run() {
		targetTime = System.currentTimeMillis();
		while (true) {
			targetTime += 1000;
			try {
				sleep(1000 - dt);
				dt = (System.currentTimeMillis() - targetTime);
				sd.incrementClock();
			} catch (InterruptedException e) {
				return;
			}
		}

	}
}