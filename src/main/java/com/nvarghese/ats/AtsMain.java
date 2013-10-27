package com.nvarghese.ats;

import com.nvarghese.ats.params.Location;

public class AtsMain {

	public static Location BASE_AIRPORT_LOCATION = Location.BANGALORE;
	private static AtsConsole console = null;
	

	public static void main(String[] args) {

		
		AtsManager.getInstance().initializeAirport();
		
		console = new AtsConsole();
		
		while(true) {
			console.showConsole();
		}

	}
}
