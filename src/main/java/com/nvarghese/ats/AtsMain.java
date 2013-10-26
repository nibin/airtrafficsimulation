package com.nvarghese.ats;

import java.util.Scanner;

import com.nvarghese.ats.params.Location;

public class AtsMain {
	
	private static Location AIRPORT_LOCATION = Location.BANGALORE;

	public static void main(String[] args) {
		
		
		
		
		System.out.print("Enter something here : ");

		String sWhatever;

		Scanner scanIn = new Scanner(System.in);
		sWhatever = scanIn.nextLine();

		scanIn.close();
		System.out.println(sWhatever);
	}

}
