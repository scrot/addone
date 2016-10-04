package nl.rdewildt.addone;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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

        try {
            if(!Stats.IsValidStatsFile(statsFile)){
                Stats.writeStats(new Stats(), statsFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.counterUpdater = new WeeklyCounterUpdater();
    }

    public void increaseCounter(Integer i) throws IOException {
        Stats stats = getStats();
        counterUpdater.increaseCounter(stats, i);
        Stats.writeStats(stats, statsFile);
        triggerCounterListener();
    }

    public void decreaseCounter() throws IOException {
        Stats stats = getStats();
        counterUpdater.decreaseCounter(stats, 1);
        Stats.writeStats(stats, statsFile);
        triggerCounterListener();
    }

    public void resetStats() throws IOException {
        Stats.writeStats(new Stats(), statsFile);
        triggerCounterListener();
    }

    public Stats getStats() throws FileNotFoundException {
        return Stats.readStats(statsFile);
    }

    public void setCounterListener(CounterListener f){
        this.counterListener = f;
    }

    public void triggerCounterListener() throws FileNotFoundException {
        if(counterListener != null) {
            counterListener.onChanged(getStats().getCounter());
        }
    }
}
