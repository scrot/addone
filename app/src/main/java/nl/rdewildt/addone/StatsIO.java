package nl.rdewildt.addone;

import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by roydewildt on 02/10/2016.
 */

public class StatsIO {
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
