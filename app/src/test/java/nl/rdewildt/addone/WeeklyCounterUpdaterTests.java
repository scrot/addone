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
    public void counterIncrease_IsCorrect(){
        Stats stats = new Stats();
        CounterUpdater updater = new WeeklyCounterUpdater(stats);
        assertThat(stats.getCounter(), is(0));

        // Increase counter by one
        updater.increaseCounter(1);
        assertThat(stats.getCounter(), is(1));
        assertThat(stats.getLastUpdated().getWeekOfWeekyear(), is(new DateTime().getWeekOfWeekyear()));

        //Only once per week update allowed
        updater.increaseCounter(1);
        assertThat(stats.getCounter(), is(1));
    }

    @Test
    public void counterDecrease_IsCorrect(){
        Stats stats = new Stats();
        DateTime lastUpdate = new DateTime().minusWeeks(3);

        stats.setCounter(3);
        stats.setLastUpdated(lastUpdate);

        CounterUpdater updater = new WeeklyCounterUpdater(stats);
        assertThat(stats.getCounter(), is(3));

        //Penalty for each week not updated
        updater.decreaseCounter();
        assertThat(stats.getCounter(), is(0));
        assertThat(stats.getLastUpdated().getWeekOfWeekyear(), is(new DateTime().getWeekOfWeekyear()));

        //No penalty when updated the same week
        updater.decreaseCounter();
        assertThat(stats.getCounter(), is(0));

        //Score cannot go lower than 0
        stats.setLastUpdated(lastUpdate);
        assertThat(stats.getCounter(), is(0));
        updater.decreaseCounter();
        assertThat(stats.getCounter(), is(0));

    }
}
