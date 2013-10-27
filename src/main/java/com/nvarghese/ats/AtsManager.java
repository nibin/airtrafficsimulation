package com.nvarghese.ats;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.ats.command.DisplayCommand;
import com.nvarghese.ats.dao.FlightDAO;
import com.nvarghese.ats.dao.RunwayDAO;
import com.nvarghese.ats.dao.TerminalDAO;
import com.nvarghese.ats.dao.TerminalSlotDAO;
import com.nvarghese.ats.domain.Flight;
import com.nvarghese.ats.domain.Runway;
import com.nvarghese.ats.domain.Runway.Slot;
import com.nvarghese.ats.domain.Terminal;
import com.nvarghese.ats.domain.TerminalSlot;
import com.nvarghese.ats.factory.FlightFactory;
import com.nvarghese.ats.factory.RunwayFactory;
import com.nvarghese.ats.factory.TerminalFactory;
import com.nvarghese.ats.factory.TerminalSlotFactory;
import com.nvarghese.ats.params.Location;
import com.nvarghese.ats.type.RunwayDirection;
import com.nvarghese.ats.type.Time;
import com.nvarghese.ats.utils.TimeUtils;

public class AtsManager {

	private Time currentTime;

	private static AtsManager instance = new AtsManager();

	static Logger logger = LoggerFactory.getLogger(AtsManager.class);

	private AtsManager() {

		currentTime = Time.airportStartTime();
	}

	public static AtsManager getInstance() {

		return instance;
	}

	public void initializeAirport() {

		// create terminal1 with 20 slots
		Terminal terminal1 = TerminalFactory.createTerminal();
		TerminalDAO.save(terminal1);
		List<TerminalSlot> terminal1Slots = TerminalSlotFactory.createTerminalSlots(terminal1.getTerminalId(), 20);
		TerminalSlotDAO.saveAll(terminal1Slots);

		// create terminal2 with 20 slots
		Terminal terminal2 = TerminalFactory.createTerminal();
		TerminalDAO.save(terminal2);
		List<TerminalSlot> terminal2Slots = TerminalSlotFactory.createTerminalSlots(terminal2.getTerminalId(), 20);
		TerminalSlotDAO.saveAll(terminal2Slots);

		initializeRunways();

		initializeFlights(40);

		displayAllFlights();

	}

	public void processRunways() {

		Time nextSlabTime = TimeUtils.addTime(currentTime, 5);
		List<Runway> runways = RunwayDAO.getAllRunways();
		for (Runway runway : runways) {

			List<Slot> slots = runway.getSlots(currentTime, nextSlabTime);
			for (Slot slot : slots) {
				runway.process(slot);
			}
		}

		this.currentTime = nextSlabTime;
	}

	private void initializeRunways() {

		Runway runwayWest1 = RunwayFactory.createRunway(RunwayDirection.WEST);
		RunwayDAO.save(runwayWest1);

		// Runway runwayWest2 =
		// RunwayFactory.createRunway(RunwayDirection.WEST);
		// RunwayDAO.save(runwayWest2);

		Runway runwayEast1 = RunwayFactory.createRunway(RunwayDirection.EAST);
		RunwayDAO.save(runwayEast1);

	}

	private void displayAllFlights() {

		DisplayCommand.displayFlights(FlightDAO.getAllFlights());

	}

	private void initializeFlights(int count) {

		Random random = new Random();

		Location[] locs = Location.getFilteredLocations(AtsMain.BASE_AIRPORT_LOCATION);
		Time startTime = Time.airportStartTime();
		Time allottedDepartureTime = TimeUtils.copy(startTime);

		for (int i = 0; i < count; i++) {

			Flight flight = FlightFactory.createFlight(AtsMain.BASE_AIRPORT_LOCATION,
					locs[random.nextInt(locs.length)], allottedDepartureTime, new Time(21, 02), false, false);

			TerminalSlot slot = TerminalSlotDAO.getFreeTerminalSlot();
			if (slot != null) {

				slot.setFlightUniqueId(flight.getUniqueId());
				flight.setTerminalSlotUniqueId(slot.getSlotUniqueId());

				flight.setDeparting(true);
				FlightDAO.save(flight);
				allottedDepartureTime = TimeUtils.addTime(allottedDepartureTime, 15);
			} else {
				logger.warn("No more terminal slots available");
			}

		}
	}

	public Time getCurrentTime() {

		return currentTime;
	}

	public void setCurrentTime(Time currentTime) {

		this.currentTime = currentTime;
	}
}
