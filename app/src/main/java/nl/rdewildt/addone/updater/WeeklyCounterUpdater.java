package nl.rdewildt.addone.updater;

import org.joda.time.DateTime;

import nl.rdewildt.addone.model.Counter;

/**
 * Created by roydewildt on 29/08/16.
 */
public class WeeklyCounterUpdater extends CounterUpdater {
    public WeeklyCounterUpdater() {}

    @Override
    public int cycleDiff(Counter counter) {
        Integer thisWeek = new DateTime().getWeekOfWeekyear();
        Integer lastWeek = counter.getLastUpdated().getWeekOfWeekyear();
        return thisWeek - lastWeek;
    }
}
