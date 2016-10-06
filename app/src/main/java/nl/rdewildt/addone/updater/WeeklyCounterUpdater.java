package nl.rdewildt.addone.updater;

import org.joda.time.DateTime;

import nl.rdewildt.addone.Counter;

/**
 * Created by roydewildt on 29/08/16.
 */
public class WeeklyCounterUpdater implements CounterUpdater {

    @Override
    public void increaseCounter(Counter counter, Integer i) {
        if(weeksSinceLastUpdate(counter) > 0){
            counter.setCounter(counter.getCounter() + i);
            counter.setLastUpdated(new DateTime());
        }
    }

    @Override
    public void decreaseCounter(Counter counter, Integer i) {
        Integer weekdiff = weeksSinceLastUpdate(counter);
        if(weekdiff > 0 && counter.getCounter() > 0){
            counter.setCounter(counter.getCounter() - (i * weekdiff));
            counter.setLastUpdated(new DateTime());
        }
    }

    private Integer weeksSinceLastUpdate(Counter counter){
        Integer thisWeek = new DateTime().getWeekOfWeekyear();
        Integer lastWeek = counter.getLastUpdated().getWeekOfWeekyear();
        return thisWeek - lastWeek;
    }


}
