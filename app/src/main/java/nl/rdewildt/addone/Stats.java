package nl.rdewildt.addone;


import org.joda.time.DateTime;

/**
 * Created by roy on 7/11/16.
 */
public final class Stats {
    private final Integer counter;
    private final DateTime lastUpdated;

    public Stats() {
        this.counter = 0;
        this.lastUpdated = new DateTime().minusWeeks(1);
    }

    public Stats(Integer counter){
        this.counter = 0;
        this.lastUpdated = new DateTime();
    }

    public Integer getCounter() {
        return counter;
    }

    public DateTime getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Stats){
            Stats stats = (Stats) obj;
            return counter.equals(stats.getCounter())
                    && lastUpdated.toString().equals(stats.getLastUpdated().toString());
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = 101;
        result = result * 31 + counter.hashCode();
        result = result * 31 + getLastUpdated().hashCode();
        return result;
    }
}
