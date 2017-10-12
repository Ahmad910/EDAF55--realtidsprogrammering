package todo;

import done.*;
import se.lth.cs.realtime.semaphore.*;

public class SharedData {
	private int alarmTime;
	private Semaphore mutex;
	private ClockOutput output;
	private boolean alarmOn;
	private int counter;
	private int hhmmss;

	public SharedData(ClockOutput output) {
		mutex = new MutexSem();
		this.output = output;
		hhmmss = 0;
		alarmTime = Integer.MAX_VALUE;
	}

	public void setAlarmTime(int time) {
		mutex.take();
		counter = 20;
		alarmTime = time;
		mutex.give();
	}

	public void setAlarmFlag(boolean alarmOn) {
		mutex.take();
		this.alarmOn = alarmOn;
		mutex.give();
	}

	public void setTime(int time) {
		mutex.take();
		alterClockTime(time);
		mutex.give();
	}

	public void incrementClock() {
		mutex.take();
		alterClockTime(hhmmss + 1);
		mutex.give();
	}

	private void alterClockTime(int time) {
		hhmmss = time;
		checkAlarm();
		checkTime();
	}

	private void checkTime() {
		String s = String.valueOf(hhmmss);
		if (s.endsWith("60")) {
			hhmmss -= 60;
			hhmmss += 100;
		}
		if (s.endsWith("5960")) {
			hhmmss -= 6000;
			hhmmss += 10000;
		}
		if (hhmmss == 240000)
			hhmmss = 0;
		output.showTime(hhmmss);
	}

	private void checkAlarm() {
		if (counter < 20 && counter > 0) {
			alarmTime = hhmmss;
		}
		if (hhmmss == alarmTime) {
			counter--;
			doAlarm20();
		}
	}

	private void doAlarm20() {
		if (counter != 0 && alarmOn) {
			output.doAlarm();
		}
	}
}