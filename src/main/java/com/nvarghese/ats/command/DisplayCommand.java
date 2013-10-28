package com.nvarghese.ats.command;

import java.util.List;

import com.nvarghese.ats.dao.FlightDAO;
import com.nvarghese.ats.dao.RunwayDAO;
import com.nvarghese.ats.dao.TerminalSlotDAO;
import com.nvarghese.ats.domain.Flight;
import com.nvarghese.ats.domain.Runway;
import com.nvarghese.ats.domain.TerminalSlot;

public class DisplayCommand {

	public static void displayFlights(List<Flight> flights) {

		System.out.println("FlightID\tAirline\tOrigin\tDestination\tArrival Time\tDeparture Time\tTerminal(Slot)\tDeparting?\tArrived?\n");
		for (Flight flight : flights) {
			StringBuffer buf = new StringBuffer();
			buf.append(flight.getUniqueId() + "\t");
			buf.append(flight.getAirline() + "\t");
			buf.append(flight.getOrigin().getSimpleName() + "\t");
			buf.append(flight.getDestination().getSimpleName() + "\t");
			buf.append(flight.getArrivalTime().getFormattedTime() + "\t");
			buf.append(flight.getDepartureTime().getFormattedTime() + "\t");

			TerminalSlot slot = TerminalSlotDAO.getTerminalSlot(flight.getTerminalSlotUniqueId());
			buf.append(slot.getTerminalId() + "(" + slot.getSlotId() + ")" + "\t");
			buf.append(flight.isDeparting() + "\t");
			buf.append(flight.isArrived() + "\t");
			System.out.println(buf.toString());
		}
	}

	public static void displayDepartureStatus(int flightId, int terminalSlotUniqueId, int runwayUniqueId) {

		System.out.println("FlightID\tAirline\tDestination\tDeparture Time\tFreed Terminal(Slot)\t Runway Assigned\n");
		Flight flight = FlightDAO.getFlight(flightId);

		StringBuffer buf = new StringBuffer();
		buf.append(flight.getUniqueId() + "\t");
		buf.append(flight.getAirline() + "\t");
		buf.append(flight.getDestination().getSimpleName() + "\t");
		buf.append(flight.getDepartureTime().getFormattedTime() + "\t");

		TerminalSlot slot = TerminalSlotDAO.getTerminalSlot(terminalSlotUniqueId);
		buf.append(slot.getTerminalId() + "(" + slot.getSlotId() + ")" + "\t");

		Runway runway = RunwayDAO.getRunway(runwayUniqueId);
		buf.append(runway.getUniqueId() + "(" + runway.getDirection() + ")" + "\t");

		System.out.println(buf.toString());

	}

	public static void displayArrivalStatus(Flight flight, int terminalSlotUniqueId, int runwayUniqueId, boolean status) {

		System.out
				.println("FlightID\tAirline\tOrigin\tArrival Time\tFreed Terminal(Slot)\t Runway Assigned\tConfirmed\n");
		StringBuffer buf = new StringBuffer();
		buf.append(flight.getUniqueId() + "\t");
		buf.append(flight.getAirline() + "\t");
		buf.append(flight.getOrigin().getSimpleName() + "\t");
		buf.append(flight.getArrivalTime().getFormattedTime() + "\t");

		if (terminalSlotUniqueId > TerminalSlot.FLIGHT_UNASSIGNED) {
			TerminalSlot slot = TerminalSlotDAO.getTerminalSlot(terminalSlotUniqueId);
			buf.append(slot.getTerminalId() + "(" + slot.getSlotId() + ")" + "\t");
		} else {
			buf.append("*(*)" + "\t");
		}

		if (runwayUniqueId > 0) {
			Runway runway = RunwayDAO.getRunway(runwayUniqueId);
			buf.append(runway.getUniqueId() + "(" + runway.getDirection() + ")" + "\t");
		} else {
			buf.append("*(*)" + "\t");
		}

		buf.append(status);

		System.out.println(buf.toString());

	}

}
