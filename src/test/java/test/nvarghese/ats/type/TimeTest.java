package test.nvarghese.ats.type;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.nvarghese.ats.type.Time;

public class TimeTest {

	@Test
	public void testFormattedTime() {

		Assert.assertEquals("05:40", new Time(5, 40).getFormattedTime());
		Assert.assertEquals("22:02", new Time(22, 2).getFormattedTime());
		Assert.assertEquals("21:21", new Time(21, 21).getFormattedTime());

	}

	@Test
	public void testResolvedMins() {

		Assert.assertEquals(235, new Time(3, 55).getResolvedMins());
	}

}
