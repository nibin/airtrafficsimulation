package com.nvarghese.ats.type;

public class Time {

	private final int hour;
	private final int mins;
	private final int resolvedMins;

	public Time(int hour, int mins) {

		super();
		this.hour = hour;
		this.mins = mins;
		this.resolvedMins = (hour * 60) + mins;
	}

	public int getHour() {

		return hour;
	}

	public int getMins() {

		return mins;
	}

	public String getFormattedTime() {

		String formattedTime = String.format("%02d:%02d", this.hour, this.mins);
		return formattedTime;
	}

	public int getResolvedMins() {

		return resolvedMins;
	}

	public static Time airportStartTime() {

		Time time = new Time(5, 0);
		return time;
	}

	public static Time airportShutdownTime() {

		Time time = new Time(23, 0);
		return time;
	}

}
