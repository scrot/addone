package nl.rdewildt.addone;


import org.joda.time.DateTime;

/**
 * Created by roy on 7/11/16.
 */
public class Stats {
    private Integer counter;
    private DateTime lastUpdated;

    public Stats() {
        this.counter = 0;
        this.lastUpdated = new DateTime().minusWeeks(1);
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public DateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(DateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
