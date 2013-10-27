package com.nvarghese.ats;

import java.util.List;
import java.util.Scanner;

import com.nvarghese.ats.command.ArrivalCommand;
import com.nvarghese.ats.command.DepartureCommand;
import com.nvarghese.ats.command.DisplayCommand;
import com.nvarghese.ats.command.SearchCommand;
import com.nvarghese.ats.dao.FlightDAO;
import com.nvarghese.ats.domain.Flight;
import com.nvarghese.ats.factory.FlightFactory;
import com.nvarghese.ats.params.Location;
import com.nvarghese.ats.type.Time;

public class AtsConsole {

	private static final int SEARCH_OP = 1;
	private static final int DEPARTURE_OP = 2;
	private static final int ARRIVAL_OP = 3;
	private static final int RUN_AIRPORT_OP = 4;
	private static final int EXIT_OP = 0;

	private static final String HELP_KEY = "h";
	private static final String LIST_KEY = "l";

	public AtsConsole() {

	}

	public void showConsole() {

		Scanner scanner = new Scanner(System.in);
		System.out.println("--------------------------------------------------");
		System.out.println("** Airport Traffic Simulation Console.**");
		System.out.println("** Current ATS Time: " + AtsManager.getInstance().getCurrentTime().getFormattedTime()
				+ " **");
		System.out.println(" Press 1 for Search Operation:");
		System.out.println(" Press 2 for Departure Operation:");
		System.out.println(" Press 3 for Arrival Operation:");
		System.out.println(" Press 4 for Run Airport Simulation:");
		System.out.println(" Press 0 to Exit:");
		System.out.print(" [+] Please enter your choise(0-4): ");

		try {
			int command = Integer.parseInt(scanner.nextLine());
			processCommand(command, scanner);
		} catch (Exception e) {

		}

	}

	private void processCommand(int command, Scanner scanner) {

		switch (command) {
			case SEARCH_OP:
				processSearchOperation(scanner);
				break;
			case DEPARTURE_OP:
				processDepartureOperation(scanner);
				break;
			case ARRIVAL_OP:
				processArrivalOperation(scanner);
				break;
			case EXIT_OP:
				System.out.println("GoodBye!");
				System.exit(0);

		}

		// TODO Auto-generated method stub

	}

	private void processArrivalOperation(Scanner scanner) {
		
		System.out.println("[*] User selected Arrival operation");
		System.out.print(" Please enter the arriving flight details (press 'h' for help): ");		
		String commandStr = scanner.nextLine();
		
		if (commandStr.equalsIgnoreCase(HELP_KEY)) {
			Location[] locs = Location.getFilteredLocations(AtsMain.BASE_AIRPORT_LOCATION);
			String locStrings = "";
			for(Location loc: locs) {
				if(loc!= Location.NONE)
					locStrings += loc.getSimpleName() + ", ";
			}
			
			System.out.println(" [?] Allowed format of values are (Flight Unique Id, Origin, Scheduled arrival time): ");
			System.out.println("     (a) Flight unique id should be more than " + FlightFactory.flightNumberTracker);
			System.out.println("     (b) Origin should be a value within the list: " + locStrings);			
			System.out.println("     (c) Schedule arrival time should be of Format HH:MM and greater than " + AtsManager.getInstance().getCurrentTime().getFormattedTime());
			System.out.println("     For eg: 41, Chicago, 06:10");
			System.out.print(" Press any key to continue arrival operation: ");
			processArrivalOperation(scanner);
		} else {
			ArrivalCommand arrivalCommand = new ArrivalCommand();
			boolean isParsable = arrivalCommand.parseFlightDetails(commandStr);
			if (isParsable) {
				arrivalCommand.processFlightForArrival();
			} else {
				System.out.println(" [-] Error in arrival command. Unable to parse.");
				processArrivalOperation(scanner);
			}
		}
		
		
	}

	private void processDepartureOperation(Scanner scanner) {

		System.out.println("[*] User selected Departure operation");
		System.out.print(" Please enter the departure flight details (press 'l' for list): ");
		String commandStr = scanner.nextLine();

		if (commandStr.equalsIgnoreCase(LIST_KEY)) {
			System.out.println(" [?] Available flights for departure are: ");
			List<Flight> flights = FlightDAO.getAllFlightReadyForDeparture(Time.airportStartTime(),
					Time.airportShutdownTime());
			DisplayCommand.displayFlights(flights);
			System.out.print(" Press any key to continue departure operation: ");
			scanner.nextLine();
			processDepartureOperation(scanner);
		} else {
			DepartureCommand command = new DepartureCommand();
			command.processFlightForDeparture(Integer.parseInt(commandStr));
			System.out.print(" Press any key to go back to main menu: ");
			scanner.nextLine();
		}

	}

	private void processSearchOperation(Scanner scanner) {

		System.out.println("[*] User selected Search operation");
		System.out.print(" Please enter the search string (press 'h' for help): ");
		String commandStr = scanner.nextLine();

		if (commandStr.equalsIgnoreCase(HELP_KEY)) {
			System.out.println(" [?] Available search string operations are: ");
			System.out.println("     (a) Time Range Format: HH:MM-HH:MM (For eg: 05:30 - 07:30)");
			System.out.println("     (b) Destination Format: Destination (For eg: Delhi)");
			System.out
					.println("     (c) Time range with destination: HH:MM-HH:MM, Destination (For eg: 05:30 - 07:30, Delhi)");
			System.out.print(" Press any key to continue search operation: ");
			scanner.nextLine();
			processSearchOperation(scanner);
		} else {
			SearchCommand command = new SearchCommand();
			boolean isParsable = command.parseCommand(commandStr);
			if (isParsable) {
				List<Flight> flights = command.runCommand();
				DisplayCommand.displayFlights(flights);
				System.out.print(" Press any key to go back to main menu: ");
				scanner.nextLine();
			} else {
				System.out.println(" [-] Error in search command. Unable to parse.");
				processSearchOperation(scanner);
			}
		}

	}

}
