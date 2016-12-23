package nl.rdewildt.addone.updater;

import org.joda.time.DateTime;

import nl.rdewildt.addone.model.Counter;

/**
 * Created by roydewildt on 29/08/16.
 */
public class WeeklyCounterUpdater extends CounterUpdater {
    public WeeklyCounterUpdater() {
    }

    public WeeklyCounterUpdater(Counter counter) {
        super(counter);
    }

    @Override
    public int cycleDiff() {
        Integer thisWeek = new DateTime().getWeekOfWeekyear();
        Integer lastWeek = getCounter().getLastUpdated().getWeekOfWeekyear();
        return thisWeek - lastWeek;
    }
}
