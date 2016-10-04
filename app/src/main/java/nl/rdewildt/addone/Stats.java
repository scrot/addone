package nl.rdewildt.addone;


import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by roy on 7/11/16.
 */
public class Stats {
    private Integer counter;
    private DateTime lastUpdated;
    private List<String> goals;

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

    public List<String> getGoals() {
        return goals;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public void setLastUpdated(DateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setGoals(List<String> goals) {
        this.goals = goals;
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


    public static Stats readStats(File statsFile) throws FileNotFoundException {

        Stats stats = null;
        stats = getStatsGson().fromJson(new FileReader(statsFile), Stats.class);
        return stats;
    }

    public static void writeStats(Stats stats, File statsFile) throws IOException {
        String statsJson = getStatsGson().toJson(stats);
        try (FileWriter writer = new FileWriter(statsFile)) {
            writer.write(statsJson);
            writer.flush();
        }
    }

    public static Boolean IsValidStatsFile(File statsFile) throws IOException {
        Collection<String> statsFileKeys = new ArrayList<>();
        Set<Map.Entry<String, JsonElement>> statsFileKeySet;
        try(FileReader fileReader = new FileReader(statsFile)) {
            try (JsonReader jsonReader = new JsonReader(fileReader)) {
                JsonElement statsJsonElement = new JsonParser().parse(jsonReader);
                if(statsJsonElement.isJsonNull()){
                    return false;
                }
                else {
                    statsFileKeySet = statsJsonElement
                            .getAsJsonObject()
                            .entrySet();
                }
            }
        }

        for(Map.Entry<String, JsonElement> statsFileKey : statsFileKeySet){
            statsFileKeys.add(statsFileKey.getKey());
        }

        Collection<String> statsKeys = new ArrayList<>();
        List<Field> statsKeysSet = Arrays.asList(Stats.class.getDeclaredFields());

        for(Field field : statsKeysSet){
            statsKeys.add(field.getName());
        }

        return statsKeys.size() == statsFileKeys.size() && statsKeys.containsAll(statsFileKeys);
    }

    private static Gson getStatsGson(){
        return new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeSerializer()).create();
    }
}
