package lift;

public class Test {

	public static void main(String[] args) {
		LiftView liftView = new LiftView();
		DataMonitor1 dataMonitor = new DataMonitor1(liftView);
		LiftThread lt = new LiftThread(dataMonitor, liftView);
		for (int i = 0; i < 10; i++) {
			PersonThread pt = new PersonThread(dataMonitor);
			pt.start();
		}
		lt.start();
	}

}