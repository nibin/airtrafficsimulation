package com.nvarghese.ats.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NavigableMap;
import java.util.Set;

import com.nvarghese.ats.domain.Flight;
import com.nvarghese.ats.ds.DataStore;
import com.nvarghese.ats.type.Time;
import com.nvarghese.ats.utils.TimeUtils;

public class FlightDAO {

	public static void save(Flight flight) {

		DataStore.getInstance().getFlightMap().put(flight.getUniqueId(), flight);

		if (flight.isDeparting()) {

			boolean isKeyPresent = DataStore.getInstance().getDepartureTreeMap()
					.containsKey(flight.getDepartureTime().getResolvedMins());
			if (isKeyPresent) {
				DataStore.getInstance().getDepartureTreeMap().get(flight.getDepartureTime().getResolvedMins())
						.add(flight.getUniqueId());
			} else {
				HashSet<Integer> flightIds = new HashSet<Integer>();
				flightIds.add(flight.getUniqueId());
				DataStore.getInstance().getDepartureTreeMap()
						.put(flight.getDepartureTime().getResolvedMins(), flightIds);
			}

			isKeyPresent = DataStore.getInstance().getDepartureDestinationMap()
					.containsKey(flight.getDestination().getSimpleName());
			if (isKeyPresent) {
				DataStore.getInstance().getDepartureDestinationMap().get(flight.getDestination().getSimpleName())
						.add(flight.getUniqueId());
			} else {
				HashSet<Integer> flightIds = new HashSet<Integer>();
				flightIds.add(flight.getUniqueId());
				DataStore.getInstance().getDepartureDestinationMap()
						.put(flight.getDestination().getSimpleName(), flightIds);
			}
		}
	}

	public static void removeDepatureTimeMapping(Flight flight) {

		DataStore.getInstance().getDepartureTreeMap().remove(flight.getDepartureTime().getResolvedMins());
	}

	public static Flight getFlight(int flightId) {

		return DataStore.getInstance().getFlightMap().get(flightId);

	}

	public static List<Flight> getAllFlights() {

		return new ArrayList<Flight>(DataStore.getInstance().getFlightMap().values());

	}

	public static List<Flight> getAllFlightsReadyForDeparture(Time startTime, Time endTime) {

		NavigableMap<Integer, HashSet<Integer>> selectedFlights = DataStore.getInstance().getDepartureTreeMap()
				.subMap(startTime.getResolvedMins(), true, endTime.getResolvedMins(), true);

		HashSet<Integer> flightIds = new HashSet<Integer>();
		for (HashSet<Integer> s : selectedFlights.values()) {
			flightIds.addAll(s);
		}

		List<Flight> flights = getFlights(flightIds);
		return flights;

	}
	
	public static List<Flight> getAllFlightsReadyForDeparture(Time startTime, Time endTime, boolean tailInclusive) {
		
		NavigableMap<Integer, HashSet<Integer>> selectedFlights = DataStore.getInstance().getDepartureTreeMap()
				.subMap(startTime.getResolvedMins(), true, endTime.getResolvedMins(), tailInclusive);

		HashSet<Integer> flightIds = new HashSet<Integer>();
		for (HashSet<Integer> s : selectedFlights.values()) {
			flightIds.addAll(s);
		}

		List<Flight> flights = getFlights(flightIds);
		return flights;
	}

	public static List<Flight> getAllFlightsReadyForDeparture(String location) {

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

	public static Time getProbableTerminalSlotFreeTime(Time startTime) {

		boolean found = false;
		Time tempStart = startTime;
		while (!found) {
			int len = DataStore.getInstance().getDepartureTreeMap()
					.subMap(tempStart.getResolvedMins(), true, TimeUtils.addTime(tempStart, 5).getResolvedMins(), true)
					.values().size();
			found = (len > 0) ? true : false;
			if (!found && tempStart.getResolvedMins() < Time.airportShutdownTime().getResolvedMins()) {
				tempStart = TimeUtils.addTime(tempStart, 5);
			} else {
				tempStart = startTime;
				break;
			}
		}

		return TimeUtils.addTime(tempStart, 5);

	}

	public static List<Flight> getAllFlightsReadyForDeparture(Time startTime, Time endTime, String location) {

		NavigableMap<Integer, HashSet<Integer>> flightsTimeBased = DataStore.getInstance().getDepartureTreeMap()
				.subMap(startTime.getResolvedMins(), true, endTime.getResolvedMins(), true);

		HashSet<Integer> flightLocBasedSet = DataStore.getInstance().getDepartureDestinationMap().get(location);

		HashSet<Integer> flightTimeBasedSet = new HashSet<Integer>();
		for (HashSet<Integer> s : flightsTimeBased.values()) {
			flightTimeBasedSet.addAll(s);
		}

		HashSet<Integer> targetSet = new HashSet<Integer>();
		if (flightsTimeBased.size() > 0 && flightLocBasedSet != null) {
			targetSet.addAll(flightTimeBasedSet);
			targetSet.retainAll(flightLocBasedSet);
		}

		List<Flight> flights = getFlights(targetSet);

		return flights;

	}

	public static void addToUnassignedArrivalQueue(Flight flight) {

		if (!isFlightInUnassignedQueue(flight.getArrivalTime())) {
			DataStore.getInstance().getUnassignedArrivalMap().put(flight.getArrivalTime().getResolvedMins(), flight);
		} else {
			boolean found = true;
			Time nTime = flight.getArrivalTime();
			while (found) {
				nTime = TimeUtils.addTime(nTime, 5);
				found = isFlightInUnassignedQueue(nTime);
			}
			flight.setArrivalTime(nTime);
			DataStore.getInstance().getUnassignedArrivalMap().put(flight.getArrivalTime().getResolvedMins(), flight);

		}

	}

	public static boolean isFlightInUnassignedQueue(Time time) {

		int len = DataStore.getInstance().getUnassignedArrivalMap()
				.subMap(time.getResolvedMins(), true, TimeUtils.addTime(time, 5).getResolvedMins(), false).values()
				.size();

		if (len > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static void delete(int flightUniqueId) {

		Flight flight = DataStore.getInstance().getFlightMap().remove(flightUniqueId);
		DataStore.getInstance().getDepartureTreeMap().remove(flight.getDepartureTime().getResolvedMins());
		DataStore.getInstance().getDepartureDestinationMap().get(flight.getDestination().getSimpleName())
				.remove(flight.getUniqueId());

	}

}
