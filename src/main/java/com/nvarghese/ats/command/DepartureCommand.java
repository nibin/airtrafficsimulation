package com.nvarghese.ats.command;

import java.util.List;

import com.nvarghese.ats.AtsManager;
import com.nvarghese.ats.contraint.AtsContraint;
import com.nvarghese.ats.contraint.ConstraintStatus;
import com.nvarghese.ats.dao.FlightDAO;
import com.nvarghese.ats.dao.RunwayDAO;
import com.nvarghese.ats.dao.TerminalSlotDAO;
import com.nvarghese.ats.domain.Flight;
import com.nvarghese.ats.domain.Runway;
import com.nvarghese.ats.domain.TerminalSlot;
import com.nvarghese.ats.type.Time;
import com.nvarghese.ats.utils.TimeUtils;

public class DepartureCommand {

	public DepartureCommand() {

	}

	public boolean processFlightForDeparture(int flightUniqueId) {

		boolean isFlightDepartOperationCompleted = false;
		Flight flight = FlightDAO.getFlight(flightUniqueId);
		if (flight != null && flight.isDeparting()) {

			Time currentTime = AtsManager.getInstance().getCurrentTime();
			//Time currentTime = flight.getDepartureTime();
			Time expectedTime = TimeUtils.addTime(currentTime, 5);
			AtsContraint contraint = new AtsContraint();
			ConstraintStatus status = contraint.checkContraintsForDepart(currentTime, expectedTime);
			if (status == ConstraintStatus.PASSED) {

				int freedTerminalSlotId = flight.getTerminalSlotUniqueId();

				// update flight departure
				FlightDAO.removeDepatureTimeMapping(flight);
				flight.setDepartureTime(currentTime);
				TerminalSlotDAO.freeTerminalSlot(freedTerminalSlotId);
				flight.setTerminalSlotUniqueId(TerminalSlot.FLIGHT_UNASSIGNED);

				Runway runway = RunwayDAO.getAnyFreeRunway(currentTime, expectedTime);
				flight.setRunwayUniqueId(runway.getUniqueId());

				runway.addFlightToDepart(flight);

				processFlightsToDepart(currentTime, expectedTime);
				FlightDAO.save(flight);

				DisplayCommand.displayDepartureStatus(flight.getUniqueId(), freedTerminalSlotId, runway.getUniqueId());
				AtsManager.getInstance().processRunways();

				isFlightDepartOperationCompleted = true;
			} else {
				//TODO
			}
		} else {
			/* do nothin */
		}

		return isFlightDepartOperationCompleted;
	}

	public void processFlightsToDepart(Time startTime, Time endTime) {

		List<Flight> flights = FlightDAO.getAllFlightsReadyForDeparture(startTime, endTime, false);
		AtsContraint contraint = new AtsContraint();
		for (Flight flight : flights) {
			ConstraintStatus status = contraint.checkContraintsForDepart(startTime, endTime);
			if (status == ConstraintStatus.PASSED) {
				int freedTerminalSlotId = flight.getTerminalSlotUniqueId();

				// update flight departure
				FlightDAO.removeDepatureTimeMapping(flight);
				flight.setDepartureTime(startTime);
				TerminalSlotDAO.freeTerminalSlot(freedTerminalSlotId);
				flight.setTerminalSlotUniqueId(TerminalSlot.FLIGHT_UNASSIGNED);

				Runway runway = RunwayDAO.getAnyFreeRunway(startTime, endTime);
				flight.setRunwayUniqueId(runway.getUniqueId());

				runway.addFlightToDepart(flight);
				FlightDAO.save(flight);
			} else {
				//TODO
			}
		}

	}

}
