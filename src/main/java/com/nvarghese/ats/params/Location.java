package com.nvarghese.ats.params;

import java.util.ArrayList;
import java.util.List;

public enum Location {

		SAN_FRANCISCO("San Francisco"),
		CHICAGO("Chicago"),
		DELHI("Delhi"),
		BANGALORE("Bangalore"),
		CHENNAI("Chennai"),
		NEW_YORK("New York"),
		LOS_ANGELES("Los Angeles"),
		HYDERABAD("Hyderabad"),
		MUMBAI("Mumbai"),
		DUBAI("Dubai"),
		COCHIN("Cochin"),
		SHARJAH("Sharjah"),
		MANAMA("Manama"),
		NONE("");

	String name;

	Location(String name) {

		this.name = name;
	}

	public String getSimpleName() {

		return name;
	}

	public static Location getLocation(String value) {

		for (Location loc : Location.values()) {
			if (loc.getSimpleName().equalsIgnoreCase(value)) {
				return loc;
			}
		}

		return NONE;
	}

	public static Location[] getFilteredLocations(Location excludedLocation) {

		List<Location> locs = new ArrayList<Location>();

		for (Location loc : Location.values()) {
			if (loc != excludedLocation && loc != Location.NONE) {
				locs.add(loc);
			}
		}

		return locs.toArray(new Location[0]);

	}

}
