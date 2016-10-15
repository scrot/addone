package nl.rdewildt.addone;

import org.joda.time.DateTime;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

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
        assertThat(counter.getValue(), is(0));

        // Increase counter by one
        updater.increaseCounter(counter, 1);
        assertThat(counter.getValue(), is(1));
        assertThat(counter.getLastUpdated().getWeekOfWeekyear(), is(new DateTime().getWeekOfWeekyear()));

        //Only once per week update allowed
        updater.increaseCounter(counter, 1);
        assertThat(counter.getValue(), is(1));
    }

    @Test
    public void counterDecrease(){
        Counter counter = new Counter();
        DateTime lastUpdate = new DateTime().minusWeeks(3);

        counter.setValue(3);
        counter.setLastUpdated(lastUpdate);

        CounterUpdater updater = new WeeklyCounterUpdater();
        assertThat(counter.getValue(), is(3));

        //Penalty for each week not updated
        updater.decreaseCounter(counter, 1);
        assertThat(counter.getValue(), is(0));
        assertThat(counter.getLastUpdated().getWeekOfWeekyear(), is(new DateTime().getWeekOfWeekyear() - 1));

        //No penalty when updated the same week
        updater.decreaseCounter(counter, 1);
        assertThat(counter.getValue(), is(0));

        //Score cannot go lower than 0
        counter.setLastUpdated(lastUpdate);
        assertThat(counter.getValue(), is(0));
        updater.decreaseCounter(counter, 1);
        assertThat(counter.getValue(), is(0));

    }

    @Test
    public void updatedLastCycleCheck() throws IOException {
        WeeklyCounterUpdater updater = new WeeklyCounterUpdater();

        Counter counter = new Counter();
        DateTime lastUpdate = new DateTime().minusWeeks(2);
        counter.setLastUpdated(lastUpdate);
        assertThat(updater.noUpdateLastCycle(counter), is(true));

        counter = new Counter();
        lastUpdate = new DateTime().minusWeeks(1);
        counter.setLastUpdated(lastUpdate);
        assertThat(updater.noUpdateLastCycle(counter), is(false));

        counter = new Counter();
        lastUpdate = new DateTime().minusWeeks(0);
        counter.setLastUpdated(lastUpdate);
        assertThat(updater.noUpdateLastCycle(counter), is(false));

        counter = new Counter();
        lastUpdate = new DateTime().plusWeeks(2);
        counter.setLastUpdated(lastUpdate);
        assertThat(updater.noUpdateLastCycle(counter), is(false));
    }

    @Test
    public void isNewCycleCheck() throws IOException {
        WeeklyCounterUpdater updater = new WeeklyCounterUpdater();

        Counter counter = new Counter();
        DateTime lastUpdate = new DateTime().minusWeeks(2);
        counter.setLastUpdated(lastUpdate);
        assertThat(updater.isNewCycle(counter), is(true));

        counter = new Counter();
        lastUpdate = new DateTime().minusWeeks(1);
        counter.setLastUpdated(lastUpdate);
        assertThat(updater.isNewCycle(counter), is(true));

        counter = new Counter();
        lastUpdate = new DateTime().minusWeeks(0);
        counter.setLastUpdated(lastUpdate);
        assertThat(updater.isNewCycle(counter), is(false));

        counter = new Counter();
        lastUpdate = new DateTime().plusWeeks(1);
        counter.setLastUpdated(lastUpdate);
        assertThat(updater.isNewCycle(counter), is(false));
    }
}
