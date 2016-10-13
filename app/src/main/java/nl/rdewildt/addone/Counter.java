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
import java.io.FileNotFoundException;
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
    private Integer value;
    private DateTime lastUpdated;

    private Integer increaseRate;
    private Integer decreaseRate;

    private List<Bonus> bonuses;
    private List<Goal> goals;

    public Counter() {
        this.value = 0;
        this.lastUpdated = new DateTime().minusWeeks(1);

        this.increaseRate = 1;
        this.decreaseRate = 1;

        this.bonuses = new ArrayList<>();
        this.goals = new ArrayList<>();
    }

    public Counter(Integer value, DateTime lastUpdated, Integer increaseRate, Integer decreaseRate) {
        this.value = value;
        this.lastUpdated = lastUpdated;

        this.increaseRate = increaseRate;
        this.decreaseRate = decreaseRate;

        this.bonuses = new ArrayList<>();
        this.goals = new ArrayList<>();
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
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
            return this.value.equals(counter.getValue())
                    && lastUpdated.toString().equals(counter.getLastUpdated().toString());
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = 101;
        result = result * 31 + value.hashCode();
        result = result * 31 + getLastUpdated().hashCode();
        return result;
    }


    public static Counter readCounter(File path) throws IOException {
        File file = getCounterFile(path);
        Counter counter;
        try(BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            counter = getCounterGson().fromJson(fileReader, Counter.class);
        }
        return counter;
    }

    public static void writeCounter(Counter counter, File path) throws IOException {
        File file = getCounterFile(path);
        String counterJson = getCounterGson().toJson(counter);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(counterJson);
        }
    }

    public static Boolean IsValidCounterFile(File path) {
        File file = getCounterFile(path);
        Collection<String> counterFileKeys = new ArrayList<>();
        Set<Map.Entry<String, JsonElement>> counterFileKeySet;
        try(BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
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
        } catch (IOException e) {
            return false;
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

    public static File getCounterFile(File path){
        return new File(path, "counter.json");
    }

    private static Gson getCounterGson(){
        return new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeSerializer()).create();
    }
}
