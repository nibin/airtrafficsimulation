package com.nvarghese.ats.type;

public class Time {
	
	private final int hour;
	private final int mins;
	private final int resolvedMins; 
	
	public Time(int hour, int mins) {
		
		//TODO: Boundary check for hour and mins
		super();
		this.hour = hour;
		this.mins = mins;
		this.resolvedMins = (hour*60) + mins;
	}

}
