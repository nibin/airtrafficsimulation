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

		System.out.println("FlightID\tAirline\tDestination\tDeparture Time\tTerminal(Slot)\n");
		for (Flight flight : flights) {
			StringBuffer buf = new StringBuffer();
			buf.append(flight.getUniqueId() + "\t");
			buf.append(flight.getAirline() + "\t");
			buf.append(flight.getDestination().getSimpleName() + "\t");
			buf.append(flight.getDepartureTime().getFormattedTime() + "\t");

			TerminalSlot slot = TerminalSlotDAO.getTerminalSlot(flight.getTerminalSlotUniqueId());
			buf.append(slot.getTerminalId() + "(" + slot.getSlotId() + ")" + "\t");
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

}
