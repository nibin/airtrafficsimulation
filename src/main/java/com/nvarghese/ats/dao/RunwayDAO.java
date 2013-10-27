package com.nvarghese.ats.dao;

import java.util.ArrayList;
import java.util.List;

import com.nvarghese.ats.domain.Runway;
import com.nvarghese.ats.ds.DataStore;
import com.nvarghese.ats.type.Time;

public class RunwayDAO {

	public static void save(Runway runway) {

		DataStore.getInstance().getRunwayMap().put(runway.getUniqueId(), runway);

	}

	public static Runway getAnyFreeRunway(Time currentTime, Time expectedTime) {

		List<Runway> runways = new ArrayList<Runway>(DataStore.getInstance().getRunwayMap().values());
		for (Runway runway : runways) {

			boolean available = runway.isSlotAvailable(currentTime, expectedTime);
			if (available) {
				return runway;
			} else {
				continue;
			}
		}

		return null;

	}

	public static Runway getRunway(int runwayId) {

		return DataStore.getInstance().getRunwayMap().get(runwayId);
	}

	public static List<Runway> getAllRunways() {

		List<Runway> runways = new ArrayList<Runway>(DataStore.getInstance().getRunwayMap().values());
		return runways;
	}
}
