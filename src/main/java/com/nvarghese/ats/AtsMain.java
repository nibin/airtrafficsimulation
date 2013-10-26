package com.nvarghese.ats;

import java.util.Scanner;

import com.nvarghese.ats.params.Location;

public class AtsMain {

	public static Location BASE_AIRPORT_LOCATION = Location.BANGALORE;
	private static AtsManager atsManager = null;

	public static void main(String[] args) {

		atsManager = new AtsManager();
		atsManager.initializeAirport();

	}

	private static void readInputs() {

		System.out.print("Enter something here : ");

		String sWhatever;

		Scanner scanIn = new Scanner(System.in);
		sWhatever = scanIn.nextLine();

		scanIn.close();
		System.out.println(sWhatever);
	}

}
