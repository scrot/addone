package nl.rdewildt.addone;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import nl.rdewildt.addone.updater.CounterUpdater;
import nl.rdewildt.addone.updater.WeeklyCounterUpdater;

/**
 * Created by roydewildt on 28/08/16.
 */
public class StatsMaintainer {
    private Stats stats;
    private CounterUpdater counterUpdater;
    private CounterListener counterListener;

    public StatsMaintainer() {
        this.stats = new Stats();
        initialize();
    }

    public StatsMaintainer(File statsFile) throws IOException, JSONException {
        this.stats = readStats(statsFile);
        initialize();
    }

    private void initialize(){
        this.counterUpdater = new WeeklyCounterUpdater(this.stats);
    }

    public void increaseCounter(Integer i){
        this.counterUpdater.increaseCounter(i);
        this.counterListener.onChanged(getCounter());
    }

    public void decreaseCounter(){
        this.counterUpdater.decreaseCounter();
        this.counterListener.onChanged(getCounter());
    }

    public Integer getCounter(){
        return getStats().getCounter();
    }

    public Stats getStats(){
        return this.stats;
    }

    public void setCounterListener(CounterListener f){
        this.counterListener = f;
    }

    public Stats readStats(File statsFile) throws JSONException, IOException {
        return new Gson().fromJson(new FileReader(statsFile), Stats.class);
    }

    public void writeStats(Context context) throws IOException {
        String statsJson = new Gson().toJson(stats);
        try (FileOutputStream fileOutputStream = context.openFileOutput("stats.json", Context.MODE_PRIVATE)) {
            fileOutputStream.write(statsJson.getBytes());
        }
    }
}
