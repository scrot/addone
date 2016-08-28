package nl.rdewildt.addone;


import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by roy on 7/11/16.
 */
public class Stats {
    private Integer counter;
    private Calendar lastUpdated;

    public Stats() {
        this.counter = 0;
        this.lastUpdated = new GregorianCalendar();
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
        this.lastUpdated = new GregorianCalendar();
    }

    public Calendar getLastUpdated() {
        return lastUpdated;
    }
}
