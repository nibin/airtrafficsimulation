package com.nvarghese.ats.factory;

import java.util.Random;

import com.nvarghese.ats.domain.Flight;
import com.nvarghese.ats.params.AirCraftType;
import com.nvarghese.ats.params.AirLine;
import com.nvarghese.ats.params.Location;
import com.nvarghese.ats.type.Time;

public class FlightFactory {

	private static int flightNumberTracker = 1;

	public static Flight createFlight(Location origin, Location destination, Time departureTime, Time arrivalTime,
			boolean departing, boolean arriving) {

		Flight flight = new Flight();

		flight.setUniqueId(flightNumberTracker);
		flight.setOrigin(origin);
		flight.setDestination(destination);
		flight.setDepartureTime(departureTime);
		flight.setArrivalTime(arrivalTime);
		flight.setArriving(arriving);
		flight.setDeparting(departing);
		flight.setAirCraftType(getRandomAircraftType());
		flight.setAirline(getRandomAirLine());

		flightNumberTracker++;

		return flight;
	}

	private static AirCraftType getRandomAircraftType() {

		Random random = new Random();
		int size = AirCraftType.values().length;

		return AirCraftType.values()[random.nextInt(size)];

	}

	private static AirLine getRandomAirLine() {

		Random random = new Random();
		int size = AirLine.values().length;

		return AirLine.values()[random.nextInt(size)];
	}

}
