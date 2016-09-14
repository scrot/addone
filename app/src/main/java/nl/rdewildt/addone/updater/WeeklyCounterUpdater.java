package nl.rdewildt.addone.updater;

import org.joda.time.DateTime;

import nl.rdewildt.addone.Stats;

/**
 * Created by roydewildt on 29/08/16.
 */
public class WeeklyCounterUpdater implements CounterUpdater {
    private Stats stats;

    public WeeklyCounterUpdater(Stats stats){
        this.stats = stats;
    }


    @Override
    public void increaseCounter(Integer i) {
        if(weeksSinceLastUpdate() > 0){
            stats.setCounter(stats.getCounter() + i);
        }
    }

    @Override
    public void decreaseCounter(Integer i) {
        if(weeksSinceLastUpdate() > 0){
            stats.setCounter(stats.getCounter() - (i * weeksSinceLastUpdate()));
        }
    }

    private Integer weeksSinceLastUpdate(){
        Integer thisWeek = new DateTime().getWeekOfWeekyear();
        Integer lastWeek = stats.getLastUpdated().getWeekOfWeekyear();
        return thisWeek - lastWeek;
    }


}
