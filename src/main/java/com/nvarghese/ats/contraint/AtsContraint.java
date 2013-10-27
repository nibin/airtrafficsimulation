package com.nvarghese.ats.contraint;

import com.nvarghese.ats.dao.RunwayDAO;
import com.nvarghese.ats.dao.TerminalSlotDAO;
import com.nvarghese.ats.domain.Runway;
import com.nvarghese.ats.domain.TerminalSlot;
import com.nvarghese.ats.type.Time;

public class AtsContraint {

	public ConstraintStatus checkContraintsForDepart(Time currentTime, Time expectedTime) {

		Runway runway = RunwayDAO.getAnyFreeRunway(currentTime, expectedTime);

		if (runway != null) {
			return ConstraintStatus.PASSED;
		} else {
			return ConstraintStatus.NO_RUNWAY_AVAILABLE;
		}

	}

	public ConstraintStatus checkContraintsForArrival(Time currentTime, Time expectedTime) {

		Runway runway = RunwayDAO.getAnyFreeRunway(currentTime, expectedTime);
		TerminalSlot slot = TerminalSlotDAO.getFreeTerminalSlot();

		if (runway != null && slot != null) {
			return ConstraintStatus.PASSED;
		} else if (runway == null) {
			return ConstraintStatus.NO_RUNWAY_AVAILABLE;
		} else if (slot == null) {
			return ConstraintStatus.NO_PARKING_SLOT_AVAILABLE;
		} else {
			return ConstraintStatus.UNKNOWN;
		}
	}

}
