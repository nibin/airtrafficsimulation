package com.nvarghese.ats.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.ats.AtsMain;
import com.nvarghese.ats.AtsManager;
import com.nvarghese.ats.contraint.AtsContraint;
import com.nvarghese.ats.contraint.ConstraintStatus;
import com.nvarghese.ats.dao.FlightDAO;
import com.nvarghese.ats.dao.RunwayDAO;
import com.nvarghese.ats.dao.TerminalSlotDAO;
import com.nvarghese.ats.domain.Flight;
import com.nvarghese.ats.domain.Runway;
import com.nvarghese.ats.domain.TerminalSlot;
import com.nvarghese.ats.factory.FlightFactory;
import com.nvarghese.ats.params.Location;
import com.nvarghese.ats.type.Time;
import com.nvarghese.ats.utils.TimeUtils;

public class ArrivalCommand {
	
	private Flight flight;
	
	static Logger logger = LoggerFactory.getLogger(ArrivalCommand.class);
	

	public boolean parseFlightDetails(String commandStr) {

		boolean isParsable = false;
		String[] splits = commandStr.split(",");
		try {
			if (splits.length == 3) {
				Integer uniqueId = Integer.parseInt(splits[0].trim());
				Location originLoc = Location.getLocation(splits[1].trim());
				Time arrivalTime = TimeUtils.parseTime(splits[2].trim());

				if (uniqueId > FlightFactory.flightNumberTracker && arrivalTime != null && originLoc != Location.NONE
						&& originLoc != AtsMain.BASE_AIRPORT_LOCATION) {
					Time depTime = TimeUtils.subtractTime(arrivalTime, 100);
					Flight flight = FlightFactory.createFlight(uniqueId, originLoc, AtsMain.BASE_AIRPORT_LOCATION, depTime,
							arrivalTime, false, true);
					this.flight = flight;
					isParsable = true;
				}
			}
		} catch (Exception e) {
			logger.error("Failed to parse arrival command string. Reason: {}", e.getMessage(), e);
		}

		return isParsable;
	}


	public void processFlightForArrival() {
		
		Time arrivalStartTime = flight.getArrivalTime();
		Time arrivalEndTime = TimeUtils.addTime(arrivalStartTime, 5);
		AtsContraint contraint = new AtsContraint();
		ConstraintStatus status = contraint.checkContraintsForArrival(arrivalStartTime, arrivalEndTime);
		if(status == ConstraintStatus.PASSED) {
			
			Runway runway = RunwayDAO.getAnyFreeRunway(arrivalStartTime, arrivalEndTime);
			flight.setRunwayUniqueId(runway.getUniqueId());
			
			TerminalSlot terminalSlot = TerminalSlotDAO.getFreeTerminalSlot();
			terminalSlot.setFlightUniqueId(this.flight.getUniqueId());
			flight.setTerminalSlotUniqueId(terminalSlot.getSlotUniqueId());
			
			runway.addFlightToArrive(flight);
			FlightDAO.save(flight);
			
			DisplayCommand.displayArrivalStatus(flight, terminalSlot.getSlotUniqueId(), runway.getUniqueId(), true);
			AtsManager.getInstance().processRunways();
			
		} else if (status == ConstraintStatus.NO_PARKING_SLOT_AVAILABLE 
				|| status == ConstraintStatus.NO_RUNWAY_AVAILABLE) {
			
			Time tentativeArrivalTime = contraint.findProbableTerminalSlotFreeTime(flight.getArrivalTime());
			if(tentativeArrivalTime != null) {
				flight.setArrivalTime(tentativeArrivalTime);
				FlightDAO.addToUnassignedArrivalQueue(flight);
				DisplayCommand.displayArrivalStatus(flight, 0, 0, false);
			}
			
		}
		
		
	}
}
