package nl.rdewildt.addone.updater;

import org.joda.time.DateTime;

import nl.rdewildt.addone.Counter;

/**
 * Created by roydewildt on 29/08/16.
 */
public class WeeklyCounterUpdater implements CounterUpdater {

    @Override
    public void increaseCounter(Counter counter, Integer i) {
        if(cyclesSinceLastUpdate(counter) > 0){
            counter.setValue(counter.getValue() + i);
            counter.setLastUpdated(new DateTime());
        }
    }

    @Override
    public void decreaseCounter(Counter counter, Integer i) {
        Integer weekdiff = cyclesSinceLastUpdate(counter);
        if(weekdiff > 0 && counter.getValue() > 0){
            counter.setValue(counter.getValue() - (i * weekdiff));
            counter.setLastUpdated(new DateTime().minusWeeks(1));
        }
    }

    @Override
    public Boolean isNewCycle(Counter counter) {
        return cyclesSinceLastUpdate(counter) > 0;
    }

    @Override
    public Boolean noUpdateLastCycle(Counter counter) {
        return cyclesSinceLastUpdate(counter) > 1;
    }

    private Integer cyclesSinceLastUpdate(Counter counter){
        Integer thisWeek = new DateTime().getWeekOfWeekyear();
        Integer lastWeek = counter.getLastUpdated().getWeekOfWeekyear();
        return thisWeek - lastWeek;
    }

}
