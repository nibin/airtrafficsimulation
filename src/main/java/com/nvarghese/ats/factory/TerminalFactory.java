package com.nvarghese.ats.factory;

import com.nvarghese.ats.domain.Terminal;

public class TerminalFactory {

	private static int terminalCounter = 1;

	public static Terminal createTerminal() {

		Terminal terminal = new Terminal(terminalCounter);
		terminalCounter++;

		return terminal;
	}

}
