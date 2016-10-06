package nl.rdewildt.addone;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import nl.rdewildt.addone.updater.CounterUpdater;
import nl.rdewildt.addone.updater.WeeklyCounterUpdater;

/**
 * Created by roydewildt on 16/09/16.
 */
public class WeeklyCounterUpdaterTests {
    @Test
    public void counterIncrease(){
        Counter counter = new Counter();
        CounterUpdater updater = new WeeklyCounterUpdater();
        assertThat(counter.getCounter(), is(0));

        // Increase counter by one
        updater.increaseCounter(counter, 1);
        assertThat(counter.getCounter(), is(1));
        assertThat(counter.getLastUpdated().getWeekOfWeekyear(), is(new DateTime().getWeekOfWeekyear()));

        //Only once per week update allowed
        updater.increaseCounter(counter, 1);
        assertThat(counter.getCounter(), is(1));
    }

    @Test
    public void counterDecrease(){
        Counter counter = new Counter();
        DateTime lastUpdate = new DateTime().minusWeeks(3);

        counter.setCounter(3);
        counter.setLastUpdated(lastUpdate);

        CounterUpdater updater = new WeeklyCounterUpdater();
        assertThat(counter.getCounter(), is(3));

        //Penalty for each week not updated
        updater.decreaseCounter(counter, 1);
        assertThat(counter.getCounter(), is(0));
        assertThat(counter.getLastUpdated().getWeekOfWeekyear(), is(new DateTime().getWeekOfWeekyear()));

        //No penalty when updated the same week
        updater.decreaseCounter(counter, 1);
        assertThat(counter.getCounter(), is(0));

        //Score cannot go lower than 0
        counter.setLastUpdated(lastUpdate);
        assertThat(counter.getCounter(), is(0));
        updater.decreaseCounter(counter, 1);
        assertThat(counter.getCounter(), is(0));

    }
}
