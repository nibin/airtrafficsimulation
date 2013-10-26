package com.nvarghese.ats;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.ats.dao.FlightDAO;
import com.nvarghese.ats.dao.TerminalDAO;
import com.nvarghese.ats.dao.TerminalSlotDAO;
import com.nvarghese.ats.domain.Flight;
import com.nvarghese.ats.domain.Terminal;
import com.nvarghese.ats.domain.TerminalSlot;
import com.nvarghese.ats.factory.FlightFactory;
import com.nvarghese.ats.factory.TerminalFactory;
import com.nvarghese.ats.factory.TerminalSlotFactory;
import com.nvarghese.ats.params.Location;
import com.nvarghese.ats.type.Time;
import com.nvarghese.ats.utils.TimeUtils;

public class AtsManager {

	static Logger logger = LoggerFactory.getLogger(AtsManager.class);

	// private Queue<Flight> arrivalQueue = new

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

		initializeFlights(40);

		displayAllFlights();

	}

	private void displayAllFlights() {

		List<Flight> flights = FlightDAO.getAllFlights();
		for (Flight flight : flights) {
			StringBuffer buf = new StringBuffer();
			buf.append("-----------------\n");
			buf.append("Flight UniqueId		: " + flight.getUniqueId() + "\n");
			buf.append("Airline			: " + flight.getAirline() + "\n");
			buf.append("Flight Origin		: " + flight.getOrigin().getName() + "\n");
			buf.append("Flight Destination	: " + flight.getDestination().getName() + "\n");
			buf.append("Departure Time		: " + flight.getDepartureTime().getFormattedTime() + "\n");
			buf.append("Arrival Time		: " + flight.getArrivalTime().getFormattedTime() + "\n");

			TerminalSlot slot = TerminalSlotDAO.getTerminalSlot(flight.getTerminalSlotUniqueId());
			buf.append("Terminal (slot)		: " + slot.getTerminalId() + "(" + slot.getSlotId() + ")" + "\n");

			buf.append("-----------------" + "\n");
			System.out.println(buf.toString());
		}

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
}
