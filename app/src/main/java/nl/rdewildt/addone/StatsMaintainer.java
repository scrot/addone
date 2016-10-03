package nl.rdewildt.addone;

import java.io.File;

import nl.rdewildt.addone.updater.CounterUpdater;
import nl.rdewildt.addone.updater.WeeklyCounterUpdater;


/**
 * Created by roydewildt on 28/08/16.
 */
public class StatsMaintainer {
    private File statsFile;
    private CounterUpdater counterUpdater;
    private CounterListener counterListener;

    public StatsMaintainer(File statsFile){
        this.statsFile = statsFile;

        if(Stats.readStats(statsFile) == null){
            Stats.writeStats(new Stats(), statsFile);
        }

        this.counterUpdater = new WeeklyCounterUpdater();
    }

    public void increaseCounter(Integer i){
        Stats stats = getStats();
        counterUpdater.increaseCounter(stats, i);
        Stats.writeStats(stats, statsFile);
        triggerCounterListener();
    }

    public void decreaseCounter(){
        Stats stats = getStats();
        counterUpdater.decreaseCounter(stats, 1);
        Stats.writeStats(stats, statsFile);
        triggerCounterListener();
    }

    public void resetStats(){
        Stats.writeStats(new Stats(), statsFile);
        triggerCounterListener();
    }

    public Stats getStats() {
        return Stats.readStats(statsFile);
    }

    public void setCounterListener(CounterListener f){
        this.counterListener = f;
    }

    public void triggerCounterListener(){
        if(counterListener != null) {
            counterListener.onChanged(getStats().getCounter());
        }
    }
}
