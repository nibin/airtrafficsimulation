package com.nvarghese.ats.factory;

import com.nvarghese.ats.domain.Flight;

public class FlightFactory {
	
	private static int flightNumberTracker = 1;
	
	public static Flight createFlight() {
		
		Flight flight = new Flight();
		
		flight.setUniqueId(flightNumberTracker);
		flightNumberTracker++;
		
		return flight;
	}

}
