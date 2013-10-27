package integration.ats.dao;

import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.nvarghese.ats.AtsMain;
import com.nvarghese.ats.dao.FlightDAO;
import com.nvarghese.ats.dao.TerminalDAO;
import com.nvarghese.ats.dao.TerminalSlotDAO;
import com.nvarghese.ats.domain.Flight;
import com.nvarghese.ats.domain.Terminal;
import com.nvarghese.ats.domain.TerminalSlot;
import com.nvarghese.ats.ds.DataStore;
import com.nvarghese.ats.factory.FlightFactory;
import com.nvarghese.ats.factory.TerminalFactory;
import com.nvarghese.ats.factory.TerminalSlotFactory;
import com.nvarghese.ats.params.Location;
import com.nvarghese.ats.type.Time;
import com.nvarghese.ats.utils.TimeUtils;

public class TestFlightDAO {

	@BeforeClass
	public void initialize() {

		Random random = new Random();

		// create terminal1 with 20 slots
		Terminal terminal1 = TerminalFactory.createTerminal();
		TerminalDAO.save(terminal1);
		List<TerminalSlot> terminal1Slots = TerminalSlotFactory.createTerminalSlots(terminal1.getTerminalId(), 20);
		TerminalSlotDAO.saveAll(terminal1Slots);

		Location[] locs = Location.getFilteredLocations(AtsMain.BASE_AIRPORT_LOCATION);
		Time startTime = Time.airportStartTime();
		Time allottedDepartureTime = TimeUtils.copy(startTime);

		for (int i = 0; i < 20; i++) {

			Flight flight = FlightFactory.createFlight(AtsMain.BASE_AIRPORT_LOCATION,
					locs[random.nextInt(locs.length)], allottedDepartureTime, new Time(21, 02), false, false);

			TerminalSlot slot = TerminalSlotDAO.getFreeTerminalSlot();
			if (slot != null) {

				slot.setFlightUniqueId(flight.getUniqueId());
				flight.setTerminalSlotUniqueId(slot.getSlotUniqueId());

				flight.setDeparting(true);
				FlightDAO.save(flight);
				allottedDepartureTime = TimeUtils.addTime(allottedDepartureTime, 15);
			} else {
				// do nothing
			}

		}
	}

	@Test
	public void testGetAllFlightReadyForDeparture() {

		Assert.assertEquals(5, FlightDAO.getAllFlightsReadyForDeparture(new Time(5, 15), new Time(6, 15)).size());

		Assert.assertEquals(4, FlightDAO.getAllFlightsReadyForDeparture(new Time(5, 20), new Time(6, 15)).size());

	}

	@AfterClass
	public void cleanUp() {

		DataStore.getInstance().getFlightMap().clear();
		DataStore.getInstance().getTerminalMap().clear();
		DataStore.getInstance().getTerminalSlotMap().clear();
		DataStore.getInstance().getDepartureDestinationMap().clear();
		DataStore.getInstance().getDepartureTreeMap().clear();
	}

}
