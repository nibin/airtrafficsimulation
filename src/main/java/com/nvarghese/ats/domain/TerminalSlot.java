package com.nvarghese.ats.domain;

public class TerminalSlot {

	public static int FLIGHT_UNASSIGNED = 0;

	private final int slotUniqueId;
	private final int slotId;
	private final int terminalId;
	private int flightUniqueId;

	public TerminalSlot(int slotUniqueId, int terminalId, int slotId) {

		this.slotUniqueId = slotUniqueId;
		this.terminalId = terminalId;
		this.slotId = slotId;
	}

	public int getSlotId() {

		return slotId;
	}

	public int getFlightUniqueId() {

		return flightUniqueId;
	}

	public void setFlightUniqueId(int flightUniqueId) {

		this.flightUniqueId = flightUniqueId;
	}

	public int getTerminalId() {

		return terminalId;
	}

	public void unAssignFlight() {

		this.flightUniqueId = FLIGHT_UNASSIGNED;
	}

	public int getSlotUniqueId() {

		return slotUniqueId;
	}

}
