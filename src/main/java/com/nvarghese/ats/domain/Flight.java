package com.nvarghese.ats.domain;

import com.nvarghese.ats.params.AirCraftType;
import com.nvarghese.ats.type.Time;

public class Flight {

	private int uniqueId;
	private String airliner;
	private AirCraftType airCraftType;
	private String destination;
	private String origin;
	private Time arrivalTime;
	private Time departureTime;
	
	public Flight() {
		super();
	}

	public int getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(int uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getAirliner() {
		return airliner;
	}

	public void setAirliner(String airliner) {
		this.airliner = airliner;
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

	public void setArrivalTime(Time arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public Time getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Time departureTime) {
		this.departureTime = departureTime;
	}

}
