package nl.rdewildt.addone;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class StatsTests {
    @Test
    public void statsInit() {
        Stats stats = new Stats();
        assertThat(stats.getCounter(), is(0));
        assertThat(stats.getLastUpdated().getWeekOfWeekyear(), is(new DateTime().getWeekOfWeekyear() - 1));
    }

    @Test
    public void statsEqual() {
        DateTime now = new DateTime();

        Stats a1 = new Stats();
        a1.setLastUpdated(now);
        Stats a2 = new Stats();
        a2.setLastUpdated(now);
        Stats b1 = new Stats();
        Stats b2 = new Stats();
        b2.setLastUpdated(now);
        b2.setCounter(1);

        assertThat(a1, is(a2));
        assertThat(a2, is(a1));
        assertThat(a1, not(b1));
        assertThat(a1, not(b2));

    }
}