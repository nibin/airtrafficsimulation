package test.nvarghese.ats.type;

import org.testng.annotations.Test;

import junit.framework.Assert;

import com.nvarghese.ats.type.Time;
import com.nvarghese.ats.utils.TimeUtils;

public class TimeUtilsTest {

	@Test
	public void testAddTime() {

		Time time = new Time(5, 55);
		Time newTime = TimeUtils.addTime(time, 45);

		Assert.assertEquals(6, newTime.getHour());
		Assert.assertEquals(40, newTime.getMins());

	}

}
