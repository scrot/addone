package nl.rdewildt.addone;


import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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

    public Stats(Integer counter){
        this.counter = counter;
        this.lastUpdated = new DateTime();
    }

    public Integer getCounter() {
        return counter;
    }

    public DateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public void setLastUpdated(DateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
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


    public static Stats readStats(File statsFile) {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeSerializer());

        Stats stats = null;
        try {
            stats = gsonBuilder.create().fromJson(new FileReader(statsFile), Stats.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stats;
    }

    public static void writeStats(Stats stats, File statsFile) {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeSerializer());

        String statsJson = gsonBuilder.create().toJson(stats);
        try (FileWriter writer = new FileWriter(statsFile)) {
            writer.write(statsJson);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
