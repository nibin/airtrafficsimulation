package com.nvarghese.ats.ds;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import com.nvarghese.ats.domain.Flight;
import com.nvarghese.ats.domain.Runway;
import com.nvarghese.ats.domain.Terminal;
import com.nvarghese.ats.domain.TerminalSlot;

public class DataStore {

	private final Map<Integer, Terminal> terminalMap = new HashMap<Integer, Terminal>();
	private final Map<Integer, TerminalSlot> terminalSlotMap = new HashMap<Integer, TerminalSlot>();
	private final Map<Integer, Flight> flightMap = new HashMap<Integer, Flight>();

	private final Map<String, HashSet<Integer>> departureDestinationMap = new HashMap<String, HashSet<Integer>>();
	private final TreeMap<Integer, HashSet<Integer>> departureTreeMap = new TreeMap<Integer, HashSet<Integer>>();

	private final Map<Integer, Runway> runwayMap = new HashMap<Integer, Runway>();

	private final TreeMap<Integer, Flight> unassignedArrivalMap = new TreeMap<Integer, Flight>();

	private static final DataStore instance = new DataStore();

	private DataStore() {

		super();
	}

	public static DataStore getInstance() {

		return instance;
	}

	public Map<Integer, Terminal> getTerminalMap() {

		return terminalMap;
	}

	public Map<Integer, TerminalSlot> getTerminalSlotMap() {

		return terminalSlotMap;
	}

	public Map<Integer, Flight> getFlightMap() {

		return flightMap;
	}

	public TreeMap<Integer, HashSet<Integer>> getDepartureTreeMap() {

		return departureTreeMap;
	}

	public Map<String, HashSet<Integer>> getDepartureDestinationMap() {

		return departureDestinationMap;
	}

	public Map<Integer, Runway> getRunwayMap() {

		return runwayMap;
	}

	
	public TreeMap<Integer, Flight> getUnassignedArrivalMap() {
	
		return unassignedArrivalMap;
	}

}
