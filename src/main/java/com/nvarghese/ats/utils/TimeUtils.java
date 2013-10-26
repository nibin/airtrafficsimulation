package com.nvarghese.ats.utils;

import com.nvarghese.ats.type.Time;

public class TimeUtils {

	public static Time addTime(Time time, int mins) {

		int resolvedMins = time.getResolvedMins() + mins;

		int newHour = resolvedMins / 60;
		int newMins = resolvedMins % 60;

		Time addedTime = new Time(newHour, newMins);

		return addedTime;
	}

	public static Time copy(Time time) {

		Time newTime = new Time(time.getHour(), time.getMins());
		return newTime;
	}

}
