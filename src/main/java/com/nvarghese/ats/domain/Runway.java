package com.nvarghese.ats.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import com.nvarghese.ats.dao.FlightDAO;
import com.nvarghese.ats.params.RunwayMode;
import com.nvarghese.ats.type.RunwayDirection;
import com.nvarghese.ats.type.Time;

public class Runway {

	private final int uniqueId;
	private final RunwayDirection direction;

	// private LinkedList<Slot> runwayQueue = new LinkedList<Slot>();
	private TreeMap<Integer, Slot> timeSlotMap = new TreeMap<Integer, Slot>();

	public Runway(int uniqueId, RunwayDirection direction) {

		this.uniqueId = uniqueId;
		this.direction = direction;
	}

	public int getUniqueId() {

		return uniqueId;
	}

	public RunwayDirection getDirection() {

		return direction;
	}

	public void addFlightToDepart(Flight flight) {

		Slot slot = new Slot(Slot.slotCounter);
		slot.departureTime = flight.getDepartureTime();
		slot.runwayMode = RunwayMode.DEPARTURE;
		slot.flightUniqueId = flight.getUniqueId();

		Slot.slotCounter++;

		// runwayQueue.add(slot);
		timeSlotMap.put(slot.departureTime.getResolvedMins(), slot);

	}

	public void addFlightToArrive(Flight flight) {

		Slot slot = new Slot(Slot.slotCounter);
		slot.arrivalTime = flight.getArrivalTime();
		slot.runwayMode = RunwayMode.ARRIVAL;
		slot.flightUniqueId = flight.getUniqueId();

		Slot.slotCounter++;

		// runwayQueue.add(slot);
		timeSlotMap.put(slot.arrivalTime.getResolvedMins(), slot);
	}

	public static class Slot {

		private static int slotCounter = 1;
		private final int uniqueId;
		private RunwayMode runwayMode;
		private int flightUniqueId;
		private Time departureTime;
		private Time arrivalTime;

		public Slot(int uniqueId) {

			this.uniqueId = uniqueId;
		}

		public int getUniqueId() {

			return uniqueId;
		}

		public RunwayMode getRunwayMode() {

			return runwayMode;
		}

		public int getFlightUniqueId() {

			return flightUniqueId;
		}

		public Time getDepartureTime() {

			return departureTime;
		}

		public Time getArrivalTime() {

			return arrivalTime;
		}

		public void setRunwayMode(RunwayMode runwayMode) {

			this.runwayMode = runwayMode;
		}

		public void setFlightUniqueId(int flightUniqueId) {

			this.flightUniqueId = flightUniqueId;
		}

		public void setDepartureTime(Time departureTime) {

			this.departureTime = departureTime;
		}

		public void setArrivalTime(Time arrivalTime) {

			this.arrivalTime = arrivalTime;
		}

	}

	public boolean isSlotAvailable(Time currentTime, Time expectedTime) {

		int slotsBooked = timeSlotMap.subMap(currentTime.getResolvedMins(), true, expectedTime.getResolvedMins(), true)
				.values().size();

		if (slotsBooked > 0) {
			return false;
		} else {
			return true;
		}
	}

	public List<Slot> getSlots(Time startTime, Time endTime) {

		List<Slot> slots = new ArrayList<Slot>(timeSlotMap.subMap(startTime.getResolvedMins(), true,
				endTime.getResolvedMins(), false).values());

		Collections.sort(slots, new Comparator<Slot>() {

			@Override
			public int compare(Slot slot1, Slot slot2) {

				int slot1ResolvedMins = 0;
				int slot2ResolvedMins = 0;

				if (slot1.runwayMode == RunwayMode.DEPARTURE)
					slot1ResolvedMins = slot1.getDepartureTime().getResolvedMins();
				else
					slot1ResolvedMins = slot1.getArrivalTime().getResolvedMins();

				if (slot2.runwayMode == RunwayMode.DEPARTURE)
					slot2ResolvedMins = slot2.getDepartureTime().getResolvedMins();
				else
					slot2ResolvedMins = slot2.getArrivalTime().getResolvedMins();

				if (slot1ResolvedMins < slot2ResolvedMins) {
					return -1;
				} else if (slot1ResolvedMins > slot2ResolvedMins) {
					return 1;
				} else {
					return 0;
				}
			}
		});

		return slots;

	}

	public void process(Slot slot) {

		Flight flight = FlightDAO.getFlight(slot.flightUniqueId);
		if (slot.runwayMode == RunwayMode.DEPARTURE) {
			System.out.println("Flight " + flight.getUniqueId() + " is about to take off to " + flight.getDestination()
					+ " via runway" + this.uniqueId + " at " + flight.getDepartureTime().getFormattedTime());
			flight.setDeparted(true);
			FlightDAO.delete(slot.flightUniqueId);
		} else if (slot.runwayMode == RunwayMode.ARRIVAL) {
			System.out.println("Flight " + flight.getUniqueId() + " is about to arrive from " + flight.getOrigin()
					+ " via runway" + this.uniqueId + " at " + flight.getArrivalTime().getFormattedTime());
			flight.setArrived(true);
			
		}
	}

}
