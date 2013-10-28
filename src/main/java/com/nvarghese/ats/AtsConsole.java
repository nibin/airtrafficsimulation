package com.nvarghese.ats;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.ats.command.ArrivalCommand;
import com.nvarghese.ats.command.DepartureCommand;
import com.nvarghese.ats.command.DisplayCommand;
import com.nvarghese.ats.command.SearchCommand;
import com.nvarghese.ats.dao.FlightDAO;
import com.nvarghese.ats.domain.Flight;
import com.nvarghese.ats.factory.FlightFactory;
import com.nvarghese.ats.params.Location;
import com.nvarghese.ats.type.Time;
import com.nvarghese.ats.utils.TimeUtils;

public class AtsConsole {

	private static final int SEARCH_OP = 1;
	private static final int DEPARTURE_OP = 2;
	private static final int ARRIVAL_OP = 3;
	private static final int RUN_AIRPORT_OP = 4;
	private static final int EXIT_OP = 0;

	private static final String HELP_KEY = "h";
	private static final String LIST_KEY = "l";

	static Logger logger = LoggerFactory.getLogger(AtsConsole.class);

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
			logger.error("Failed to run the command. Reason: {}", e.getMessage());
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
			case RUN_AIRPORT_OP:
				processRunAirportOperation(scanner);
				break;
			case EXIT_OP:
				System.out.println("GoodBye!");
				scanner.close();
				System.exit(0);

		}

	}

	private void processRunAirportOperation(Scanner scanner) {

		System.out.println("[*] User selected Run Airport operation (press 'h' for help):");
		String commandStr = scanner.nextLine();
		if (commandStr.equalsIgnoreCase(HELP_KEY)) {

			System.out.println(" [?] Allowed options are: ");
			System.out.println("     (a) Run Airport operation by fast forwarding time (HH:MM): ");
			System.out.println("     (b) Add random number of arrival flight: ");
			System.out.println("     (c) See airport status");
			System.out.println("     (d) Go back to main menu");
			System.out.println("     For eg: 41, Chicago, 06:10");
			System.out.print(" Select a mode to continue (a) or (b) or (c) or (d): ");

			String selectedMode = scanner.nextLine();
			switch (selectedMode) {
				case "a":
					System.out.print("   Please enter the fast forwarded airport time (> "
							+ AtsManager.getInstance().getCurrentTime().getFormattedTime() + ")");
					String nTimeStr = scanner.nextLine();
					Time newTime = TimeUtils.parseTime(nTimeStr);
					if (newTime.getResolvedMins() > AtsManager.getInstance().getCurrentTime().getResolvedMins()) {
						AtsManager.getInstance().fastForwardRunwayProcessing(newTime);
					} else {
						System.out.println(" [-] Selected time is less than airport time of "
								+ AtsManager.getInstance().getCurrentTime().getFormattedTime());
						processRunAirportOperation(scanner);
					}
					break;
				case "b":
					System.out.print("   Please enter the random number of arrival flights: ");
					int newArrivalFlightCount = Integer.parseInt(scanner.nextLine());
					Flight[] flights = createRandomFlightForArrival(newArrivalFlightCount);
					ArrivalCommand arrivalCommand = new ArrivalCommand();
					arrivalCommand.processFlightsForArrival(flights);
					processRunAirportOperation(scanner);
					break;
				case "c":
					List<Flight> fls = FlightDAO.getAllFlights();
					DisplayCommand.displayFlights(fls);
					processRunAirportOperation(scanner);
					break;
				case "d":
					System.out.print("   Press any key to go back to main menu: ");
					scanner.nextLine();
			}

		}

	}

	private void processArrivalOperation(Scanner scanner) {

		System.out.println("[*] User selected Arrival operation");
		System.out.print(" Please enter the arriving flight details (press 'h' for help): ");
		String commandStr = scanner.nextLine();

		if (commandStr.equalsIgnoreCase(HELP_KEY)) {
			Location[] locs = Location.getFilteredLocations(AtsMain.BASE_AIRPORT_LOCATION);
			String locStrings = "";
			for (Location loc : locs) {
				if (loc != Location.NONE)
					locStrings += loc.getSimpleName() + ", ";
			}

			System.out
					.println(" [?] Allowed format of values are (Flight Unique Id, Origin, Scheduled arrival time): ");
			System.out.println("     (a) Flight unique id should be more than " + FlightFactory.flightNumberTracker);
			System.out.println("     (b) Origin should be a value within the list: " + locStrings);
			System.out.println("     (c) Schedule arrival time should be of Format HH:MM and greater than "
					+ AtsManager.getInstance().getCurrentTime().getFormattedTime());
			System.out.println("     For eg: 41, Chicago, 06:10");
			System.out.print(" Press any key to continue arrival operation: ");
			processArrivalOperation(scanner);
		} else {
			ArrivalCommand arrivalCommand = new ArrivalCommand();
			Flight flight = arrivalCommand.parseFlightDetails(commandStr);
			if (flight != null) {
				arrivalCommand.processFlightForArrival(flight);
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
			List<Flight> flights = FlightDAO.getAllFlightsReadyForDeparture(Time.airportStartTime(),
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

	private Flight[] createRandomFlightForArrival(int newArrivalFlightCount) {

		Flight[] flights = new Flight[newArrivalFlightCount];
		Random random = new Random();

		Location[] locs = Location.getFilteredLocations(AtsMain.BASE_AIRPORT_LOCATION);

		for (int i = 0; i < newArrivalFlightCount; i++) {
			Time depTime = TimeUtils.getTime(random
					.nextInt(AtsManager.getInstance().getCurrentTime().getResolvedMins()));
			int minsToShutDown = Time.airportShutdownTime().getResolvedMins()
					- AtsManager.getInstance().getCurrentTime().getResolvedMins();
			Time arrivalTime = TimeUtils.addTime(depTime, random.nextInt(minsToShutDown));
			
			while(arrivalTime.getResolvedMins() <= AtsManager.getInstance().getCurrentTime().getResolvedMins()) {
				arrivalTime = TimeUtils.addTime(depTime, random.nextInt(minsToShutDown));
			}
			FlightFactory.flightNumberTracker++;
			
			Flight flight = FlightFactory.createFlight(FlightFactory.flightNumberTracker, locs[random.nextInt(locs.length)],
					AtsMain.BASE_AIRPORT_LOCATION, depTime, arrivalTime, false, true);
			flights[i] = flight;
		}
		return flights;
	}

}
