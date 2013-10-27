package com.nvarghese.ats.factory;

import com.nvarghese.ats.domain.Runway;
import com.nvarghese.ats.type.RunwayDirection;

public class RunwayFactory {

	private static int runwayNumberTracker = 1;

	public static Runway createRunway(RunwayDirection direction) {

		Runway runway = new Runway(runwayNumberTracker, direction);
		runwayNumberTracker++;

		return runway;
	}

}
