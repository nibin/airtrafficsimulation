package test.nvarghese.ats.command;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.nvarghese.ats.command.SearchCommand;


public class TestSearchCommand {
	
	@Test
	public void testParseCommand() {
		
		SearchCommand searchCommand = new SearchCommand();
		searchCommand.parseCommand("05:30-10:30, Delhi");		
		Assert.assertEquals(SearchCommand.RANGE_DEST_MODE, searchCommand.getMode());
		
		searchCommand.parseCommand("05 : 30 - 10 : 30, Delhi");
		Assert.assertEquals(SearchCommand.RANGE_DEST_MODE, searchCommand.getMode());
		
		searchCommand.parseCommand(" Delhi ");
		Assert.assertEquals(SearchCommand.DESTINATION_MODE_ONLY, searchCommand.getMode());
		
		searchCommand.parseCommand("05 : 30 - 10 : 30");
		Assert.assertEquals(SearchCommand.RANGE_MODE_ONLY, searchCommand.getMode());
		
		
	}

}
