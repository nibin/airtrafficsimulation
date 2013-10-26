package com.nvarghese.ats.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NavigableMap;
import java.util.Set;

import com.nvarghese.ats.domain.Flight;
import com.nvarghese.ats.ds.DataStore;
import com.nvarghese.ats.type.Time;

public class FlightDAO {

	public static void save(Flight flight) {

		DataStore.getInstance().getFlightMap().put(flight.getUniqueId(), flight);

		if (flight.isDeparting()) {
			DataStore.getInstance().getDepartureTreeMap()
					.put(flight.getDepartureTime().getResolvedMins(), flight.getUniqueId());

			boolean isKeyPresent = DataStore.getInstance().getDepartureDestinationMap()
					.containsKey(flight.getDestination().getName());
			if (isKeyPresent) {
				DataStore.getInstance().getDepartureDestinationMap().get(flight.getDestination().getName())
						.add(flight.getUniqueId());
			} else {
				HashSet<Integer> flightIds = new HashSet<Integer>();
				flightIds.add(flight.getUniqueId());
				DataStore.getInstance().getDepartureDestinationMap().put(flight.getDestination().getName(), flightIds);
			}
		}
	}

	public static List<Flight> getAllFlights() {

		return new ArrayList<Flight>(DataStore.getInstance().getFlightMap().values());

	}

	public static List<Flight> getAllFlightReadyForDeparture(Time startTime, Time endTime) {

		NavigableMap<Integer, Integer> selectedFlights = DataStore.getInstance().getDepartureTreeMap()
				.subMap(startTime.getResolvedMins(), true, endTime.getResolvedMins(), true);

		List<Flight> flights = getFlights(selectedFlights.keySet());
		return flights;

	}

	public static List<Flight> getAllFlightReadyForDeparture(String location) {

		List<Flight> flights = new ArrayList<Flight>();

		boolean isKeyPresent = DataStore.getInstance().getDepartureDestinationMap().containsKey(location);
		if (isKeyPresent) {
			HashSet<Integer> flightIds = DataStore.getInstance().getDepartureDestinationMap().get(location);
			flights = getFlights(flightIds);
		}

		return flights;
	}

	public static List<Flight> getFlights(Set<Integer> flightIds) {

		List<Flight> flights = new ArrayList<Flight>();
		for (Integer selectedFlight : flightIds) {
			Flight fl = DataStore.getInstance().getFlightMap().get(selectedFlight);
			flights.add(fl);

		}

		return flights;

	}

	public static List<Flight> getAllFlightReadyForDeparture(Time startTime, Time endTime, String location) {

		NavigableMap<Integer, Integer> flightsTimeBased = DataStore.getInstance().getDepartureTreeMap()
				.subMap(startTime.getResolvedMins(), true, endTime.getResolvedMins(), true);

		HashSet<Integer> flightLocBased = DataStore.getInstance().getDepartureDestinationMap().get(location);

		HashSet<Integer> targetSet = new HashSet<Integer>();
		targetSet.addAll(flightsTimeBased.keySet());

		if (flightLocBased != null) {
			targetSet.addAll(flightLocBased);
		}

		List<Flight> flights = getFlights(targetSet);

		return flights;

	}

}
