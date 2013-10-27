package com.nvarghese.ats.command;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.ats.dao.FlightDAO;
import com.nvarghese.ats.domain.Flight;
import com.nvarghese.ats.type.Time;

public class SearchCommand {

	private int mode;
	private Time startTime;
	private Time endTime;
	private String destination;

	public static final int RANGE_MODE_ONLY = 1;
	public static final int DESTINATION_MODE_ONLY = 2;
	public static final int RANGE_DEST_MODE = 3;
	public static final int UNKNOWN_MODE = 0;

	static Logger logger = LoggerFactory.getLogger(SearchCommand.class);

	public SearchCommand() {

		mode = UNKNOWN_MODE;

	}

	public boolean parseCommand(String command) {

		boolean isParsable = false;
		String[] splits = command.split(",");
		if (splits.length == 2) {
			Time[] times = getRangeTimes(splits[0]);
			if (times != null && times.length == 2) {
				mode = RANGE_DEST_MODE;
				this.startTime = times[0];
				this.endTime = times[1];
				this.destination = splits[1].trim();
				isParsable = true;
			}
		} else if (splits.length == 1) {
			Time[] times = getRangeTimes(splits[0]);
			if (times != null && times.length == 2) {
				mode = RANGE_MODE_ONLY;
				this.startTime = times[0];
				this.endTime = times[1];
				isParsable = true;
			} else {
				this.destination = splits[0].trim();
				this.mode = DESTINATION_MODE_ONLY;
				isParsable = true;
			}
		}

		return isParsable;
	}

	public List<Flight> runCommand() {

		if (mode == RANGE_MODE_ONLY) {
			return FlightDAO.getAllFlightsReadyForDeparture(this.startTime, this.endTime);
		} else if (mode == DESTINATION_MODE_ONLY) {
			return FlightDAO.getAllFlightsReadyForDeparture(this.destination);
		} else if (mode == RANGE_DEST_MODE) {
			return FlightDAO.getAllFlightsReadyForDeparture(this.startTime, this.endTime, this.destination);
		} else {
			logger.warn("Unknown search mode detected");
			return null;
		}
	}

	private Time[] getRangeTimes(String timeStr) {

		String[] timeSplits = timeStr.split("-");
		if (timeSplits.length == 2) {

			String[] hourMinStart = timeSplits[0].split(":");
			String[] hourMinStop = timeSplits[1].split(":");

			if (hourMinStart.length == 2 && hourMinStop.length == 2) {
				Time startTime = new Time(Integer.parseInt(hourMinStart[0].trim()), Integer.parseInt(hourMinStart[1]
						.trim()));
				Time stopTime = new Time(Integer.parseInt(hourMinStop[0].trim()), Integer.parseInt(hourMinStop[1]
						.trim()));

				if (startTime.getResolvedMins() < stopTime.getResolvedMins()) {
					Time[] times = new Time[2];
					times[0] = startTime;
					times[1] = stopTime;
					return times;
				}
			}

		}

		return null;

	}

	public int getMode() {

		return mode;
	}

	public Time getStartTime() {

		return startTime;
	}

	public Time getEndTime() {

		return endTime;
	}

	public String getDestination() {

		return destination;
	}

	public void setMode(int mode) {

		this.mode = mode;
	}

	public void setStartTime(Time startTime) {

		this.startTime = startTime;
	}

	public void setEndTime(Time endTime) {

		this.endTime = endTime;
	}

	public void setDestination(String destination) {

		this.destination = destination;
	}

}
