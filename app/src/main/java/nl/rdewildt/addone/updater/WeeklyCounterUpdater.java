package nl.rdewildt.addone.updater;

import org.joda.time.DateTime;

import nl.rdewildt.addone.Stats;

/**
 * Created by roydewildt on 29/08/16.
 */
public class WeeklyCounterUpdater implements CounterUpdater {

    @Override
    public void increaseCounter(Stats stats, Integer i) {
        if(weeksSinceLastUpdate(stats) > 0){
            stats.setCounter(stats.getCounter() + i);
            stats.setLastUpdated(new DateTime());
        }
    }

    @Override
    public void decreaseCounter(Stats stats, Integer i) {
        Integer weekdiff = weeksSinceLastUpdate(stats);
        if(weekdiff > 0 && stats.getCounter() > 0){
            stats.setCounter(stats.getCounter() - (i * weekdiff));
            stats.setLastUpdated(new DateTime());
        }
    }

    private Integer weeksSinceLastUpdate(Stats stats){
        Integer thisWeek = new DateTime().getWeekOfWeekyear();
        Integer lastWeek = stats.getLastUpdated().getWeekOfWeekyear();
        return thisWeek - lastWeek;
    }


}
