package nl.rdewildt.addone;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class StatsValidationTests {
    @Test
    public void statsInit_isCorrect() throws Exception {
        Stats stats = new Stats();
        assertThat(stats.getCounter(), is(0));
        assertThat(stats.getLastUpdated().getWeekOfWeekyear(), is(new DateTime().getWeekOfWeekyear() - 1));
    }
}