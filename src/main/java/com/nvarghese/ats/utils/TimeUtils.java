package com.nvarghese.ats.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.ats.type.Time;

public class TimeUtils {

	static Logger logger = LoggerFactory.getLogger(TimeUtils.class);

	public static Time addTime(Time time, int mins) {

		int resolvedMins = time.getResolvedMins() + mins;

		int newHour = resolvedMins / 60;
		int newMins = resolvedMins % 60;

		Time addedTime = new Time(newHour, newMins);

		return addedTime;
	}
	
	public static Time subtractTime(Time time, int mins) {
		
		int resolvedMins = time.getResolvedMins() - mins;

		int newHour = resolvedMins / 60;
		int newMins = resolvedMins % 60;

		Time subTime = new Time(newHour, newMins);

		return subTime;
	}

	public static Time copy(Time time) {

		Time newTime = new Time(time.getHour(), time.getMins());
		return newTime;
	}

	public static Time parseTime(String timeStr) {

		String[] splits = timeStr.split(":");
		try {
			if (splits.length == 2) {
				return new Time(Integer.parseInt(splits[0].trim()), Integer.parseInt(splits[1].trim()));
			}
		} catch (Exception e) {
			logger.error("Failed to parse given time string");
		}

		return null;

	}
}
