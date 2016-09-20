package nl.rdewildt.addone.updater;

import org.joda.time.DateTime;

import nl.rdewildt.addone.Stats;

/**
 * Created by roydewildt on 29/08/16.
 */
public class WeeklyCounterUpdater implements CounterUpdater {

    @Override
    public Stats increaseCounter(Stats stats, Integer i) {
        if(weeksSinceLastUpdate(stats) > 0){
            return new Stats(stats.getCounter() + i);
        }
        return stats;
    }

    @Override
    public Stats decreaseCounter(Stats stats) {
        if(weeksSinceLastUpdate(stats) > 0 && stats.getCounter() > 0){
            return new Stats(stats.getCounter() - weeksSinceLastUpdate(stats));
        }
        return stats;
    }

    private Integer weeksSinceLastUpdate(Stats stats){
        Integer thisWeek = new DateTime().getWeekOfWeekyear();
        Integer lastWeek = stats.getLastUpdated().getWeekOfWeekyear();
        return thisWeek - lastWeek;
    }


}
