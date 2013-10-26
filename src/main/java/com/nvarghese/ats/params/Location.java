package com.nvarghese.ats.params;

public enum Location {
	
	SAN_FRANCISCO ("San Francisco"),
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

	public String getName() {
		return name;
	}

	public static Location getLocation(String value) {
		for (Location loc : Location.values()) {
			if (loc.getName().equalsIgnoreCase(value)) {
				return loc;
			}
		}

		return NONE;
	}

}
