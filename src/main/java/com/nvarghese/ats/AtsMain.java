package com.nvarghese.ats;

import org.apache.log4j.PropertyConfigurator;

import com.nvarghese.ats.params.Location;

public class AtsMain {

	public static Location BASE_AIRPORT_LOCATION = Location.BANGALORE;
	private static AtsConsole console = null;

	public static void main(String[] args) {

		PropertyConfigurator.configure(AtsMain.class.getClassLoader().getResource("log4j.properties"));

		AtsManager.getInstance().initializeAirport();

		console = new AtsConsole();

		while (true) {
			console.showConsole();
		}

	}
}
