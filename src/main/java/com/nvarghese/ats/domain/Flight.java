package com.nvarghese.ats.domain;

import com.nvarghese.ats.params.AirCraftType;
import com.nvarghese.ats.params.AirLine;
import com.nvarghese.ats.params.Location;
import com.nvarghese.ats.type.Time;

public class Flight {

	private int uniqueId;
	private AirLine airline;
	private AirCraftType airCraftType;
	private Location destination;
	private Location origin;
	private Time arrivalTime;
	private Time departureTime;
	private int terminalSlotUniqueId;
	private boolean departing;
	private boolean arriving;

	public Flight() {

		super();
	}

	public int getUniqueId() {

		return uniqueId;
	}

	public void setUniqueId(int uniqueId) {

		this.uniqueId = uniqueId;
	}

	public Location getDestination() {

		return destination;
	}

	public void setDestination(Location destination) {

		this.destination = destination;
	}

	public Location getOrigin() {

		return origin;
	}

	public void setOrigin(Location origin) {

		this.origin = origin;
	}

	public AirLine getAirline() {

		return airline;
	}

	public void setAirline(AirLine airline) {

		this.airline = airline;
	}

	public AirCraftType getAirCraftType() {

		return airCraftType;
	}

	public void setAirCraftType(AirCraftType airCraftType) {

		this.airCraftType = airCraftType;
	}

	public Time getArrivalTime() {

		return arrivalTime;
	}

	public int getTerminalSlotUniqueId() {

		return terminalSlotUniqueId;
	}

	public void setTerminalSlotUniqueId(int terminalSlotUniqueId) {

		this.terminalSlotUniqueId = terminalSlotUniqueId;
	}

	public void setArrivalTime(Time arrivalTime) {

		this.arrivalTime = arrivalTime;
	}

	public Time getDepartureTime() {

		return departureTime;
	}

	public void setDepartureTime(Time departureTime) {

		this.departureTime = departureTime;
	}

	public boolean isDeparting() {

		return departing;
	}

	public boolean isArriving() {

		return arriving;
	}

	public void setDeparting(boolean departing) {

		this.departing = departing;
	}

	public void setArriving(boolean arriving) {

		this.arriving = arriving;
	}

}
