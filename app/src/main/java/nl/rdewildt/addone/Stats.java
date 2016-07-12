package nl.rdewildt.addone;

import java.util.Observable;

/**
 * Created by roy on 7/11/16.
 */
public class Stats {
    private Integer counter;
    private StatsListener listener;

    public Stats(Integer counter) {
        this.counter = counter;
    }

    public Integer getCounter() {
        return counter;
    }

    public void IncrementCounter(Integer i){
        this.counter += i;
        this.listener.OnCounterIncreased(this);
    }

    public void resetCounter(){
        this.counter = 0;
        this.listener.OnCounterIncreased(this);
    }

    public void setStatsListener(StatsListener listener) {
        this.listener = listener;
    }
}
