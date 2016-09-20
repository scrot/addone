package nl.rdewildt.addone;

import android.content.Context;

import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import nl.rdewildt.addone.updater.CounterUpdater;
import nl.rdewildt.addone.updater.WeeklyCounterUpdater;

/**
 * Created by roydewildt on 28/08/16.
 */
public class StatsMaintainer {
    private Context context;
    private File statsFile;
    private CounterUpdater counterUpdater;
    private CounterListener counterListener;

    public StatsMaintainer(Context context){
        this.context = context;
        this.statsFile = new File(context.getFilesDir(), "stats.json");
        if(readStats() == null){
            writeStats(new Stats());
        }
        this.counterUpdater = new WeeklyCounterUpdater();
    }

    public void increaseCounter(Integer i){
        setStats(counterUpdater.increaseCounter(getStats(), i));
    }

    public void decreaseCounter(){
        setStats(counterUpdater.decreaseCounter(getStats()));
    }

    public Stats getStats(){
        return readStats();
    }


    public void setStats(Stats stats){
        writeStats(stats);
        if(counterListener != null) {
            counterListener.onChanged(stats.getCounter());
        }
    }

    public void setCounterListener(CounterListener f){
        this.counterListener = f;
    }

    public Stats readStats() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeSerializer());

        Stats stats = null;
        try {
            return gsonBuilder.create().fromJson(new FileReader(statsFile), Stats.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return stats;
    }

    public void writeStats(Stats stats) {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeSerializer());
        String statsJson = gsonBuilder.create().toJson(stats);
        try (FileOutputStream fileOutputStream = context.openFileOutput("stats.json", Context.MODE_PRIVATE)) {
            fileOutputStream.write(statsJson.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
