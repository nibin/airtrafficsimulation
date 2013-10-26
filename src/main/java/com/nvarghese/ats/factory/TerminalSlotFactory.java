package com.nvarghese.ats.factory;

import java.util.ArrayList;
import java.util.List;

import com.nvarghese.ats.domain.TerminalSlot;

public class TerminalSlotFactory {

	private static int terminalSlotUniqueIdCounter = 1;

	public static List<TerminalSlot> createTerminalSlots(int terminalId, int count) {

		List<TerminalSlot> terminalSlots = new ArrayList<TerminalSlot>();
		for (int i = 1; i <= count; i++) {
			TerminalSlot slot = new TerminalSlot(terminalSlotUniqueIdCounter, terminalId, i);
			terminalSlotUniqueIdCounter++;
			terminalSlots.add(slot);
		}

		return terminalSlots;

	}

}
