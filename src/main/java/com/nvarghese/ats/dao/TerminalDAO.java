package com.nvarghese.ats.dao;

import com.nvarghese.ats.domain.Terminal;
import com.nvarghese.ats.ds.DataStore;

public class TerminalDAO {

	public static void save(Terminal terminal) {

		DataStore.getInstance().getTerminalMap().put(terminal.getTerminalId(), terminal);
	}

	public static Terminal getTerminal(int id) {

		return DataStore.getInstance().getTerminalMap().get(id);

	}

}
