package com.nvarghese.ats.dao;

import java.util.List;
import java.util.Set;

import com.nvarghese.ats.domain.TerminalSlot;
import com.nvarghese.ats.ds.DataStore;

public class TerminalSlotDAO {

	public static void save(TerminalSlot terminalSlot) {

		DataStore.getInstance().getTerminalSlotMap().put(terminalSlot.getSlotUniqueId(), terminalSlot);
	}

	public static void saveAll(List<TerminalSlot> terminalSlots) {

		for (TerminalSlot slot : terminalSlots) {
			save(slot);
		}

	}

	public static TerminalSlot getTerminalSlot(int id) {

		return DataStore.getInstance().getTerminalSlotMap().get(id);

	}

	public static TerminalSlot getFreeTerminalSlot() {

		Set<Integer> slotUniqueId = DataStore.getInstance().getTerminalSlotMap().keySet();

		for (Integer id : slotUniqueId) {
			TerminalSlot slot = DataStore.getInstance().getTerminalSlotMap().get(id);
			if (slot.getFlightUniqueId() == TerminalSlot.FLIGHT_UNASSIGNED) {
				return slot;
			}
		}

		return null;

	}

	public static void freeTerminalSlot(int terminalSlotId) {

		TerminalSlot slot = getTerminalSlot(terminalSlotId);
		if (slot != null) {
			slot.setFlightUniqueId(TerminalSlot.FLIGHT_UNASSIGNED);
			save(slot);
		}

	}

}
