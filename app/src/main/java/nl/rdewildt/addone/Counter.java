package nl.rdewildt.addone;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by roy on 7/11/16.
 */
public class Counter {
    private Integer counter;
    private DateTime lastUpdated;

    private Integer increaseRate;
    private Integer decreaseRate;

    private List<Bonus> bonuses;
    private List<Goal> goals;

    public Counter() {
        this.counter = 0;
        this.lastUpdated = new DateTime().minusWeeks(1);

        this.increaseRate = 1;
        this.decreaseRate = 1;

        this.bonuses = new ArrayList<>();
        this.goals = new ArrayList<>();
    }

    public Counter(Integer counter, DateTime lastUpdated, Integer increaseRate, Integer decreaseRate) {
        this.counter = counter;
        this.lastUpdated = lastUpdated;

        this.increaseRate = increaseRate;
        this.decreaseRate = decreaseRate;

        this.bonuses = new ArrayList<>();
        this.goals = new ArrayList<>();
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

    public Integer getIncreaseRate() {
        return increaseRate;
    }

    public void setIncreaseRate(Integer increaseRate) {
        this.increaseRate = increaseRate;
    }

    public Integer getDecreaseRate() {
        return decreaseRate;
    }

    public void setDecreaseRate(Integer decreaseRate) {
        this.decreaseRate = decreaseRate;
    }

    public List<Bonus> getBonuses() {
        return bonuses;
    }

    public void setBonuses(List<Bonus> bonuses) {
        this.bonuses = bonuses;
    }

    public void addBonus(Bonus bonus){
        this.bonuses.add(bonus);
    }

    public void removeBonus(Bonus bonus){
        if(this.bonuses.contains(bonus)) {
            this.bonuses.remove(bonus);
        }
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void addGoal(Goal goal){
        this.goals.add(goal);
    }

    public void removeGoal(Goal goal){
        if(this.goals.contains(goal)) {
            this.goals.remove(goal);
        }
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Counter){
            Counter counter = (Counter) obj;
            return this.counter.equals(counter.getCounter())
                    && lastUpdated.toString().equals(counter.getLastUpdated().toString());
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


    public static Counter readCounter(File counterFile) throws IOException {
        Counter counter = null;
        try(BufferedReader fileReader = new BufferedReader(new FileReader(counterFile))) {
            counter = getCounterGson().fromJson(fileReader, Counter.class);
        }
        return counter;
    }

    public static void writeCounter(Counter counter, File counterFile) throws IOException {
        String counterJson = getCounterGson().toJson(counter);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(counterFile))) {
            writer.write(counterJson);
        }
    }

    public static Boolean IsValidCounterFile(File counterFile) throws IOException {
        Collection<String> counterFileKeys = new ArrayList<>();
        Set<Map.Entry<String, JsonElement>> counterFileKeySet;
        try(BufferedReader fileReader = new BufferedReader(new FileReader(counterFile))) {
            try (JsonReader jsonReader = new JsonReader(fileReader)) {
                JsonElement counterJsonElement = new JsonParser().parse(jsonReader);
                if(counterJsonElement.isJsonNull()){
                    return false;
                }
                else {
                    counterFileKeySet = counterJsonElement
                            .getAsJsonObject()
                            .entrySet();
                }
            }
        }

        for(Map.Entry<String, JsonElement> counterFileKey : counterFileKeySet){
            counterFileKeys.add(counterFileKey.getKey());
        }

        Collection<String> counterKeys = new ArrayList<>();
        List<Field> counterKeysSet = Arrays.asList(Counter.class.getDeclaredFields());

        for(Field field : counterKeysSet){
            counterKeys.add(field.getName());
        }

        return counterKeys.size() == counterFileKeys.size() && counterKeys.containsAll(counterFileKeys);
    }

    private static Gson getCounterGson(){
        return new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeSerializer()).create();
    }
}
