package nl.rdewildt.addone.model;

import org.joda.time.DateTime;

/**
 * Created by roy on 7/11/16.
 */
public class Counter {
    private Integer value;
    private Integer subValue;

    private DateTime lastUpdated;

    private Integer increaseRate;
    private Integer decreaseRate;

    private Integer maxSubValue;

    public Counter() {
        this.value = 0;
        this.subValue = 1;

        this.lastUpdated = new DateTime().minusWeeks(1);

        this.increaseRate = 1;
        this.decreaseRate = 1;
        this.maxSubValue = 3;
    }

    public Counter(Integer value, DateTime lastUpdated, Integer increaseRate, Integer decreaseRate, Integer maxSubValue) {
        this.value = value;
        this.subValue = 0;

        this.lastUpdated = lastUpdated;

        this.increaseRate = increaseRate;
        this.decreaseRate = decreaseRate;

        this.maxSubValue = maxSubValue;
    }

    public void reset(){
        this.value = 0;
        this.lastUpdated = new DateTime().minusWeeks(1);
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getSubValue() {
        return subValue;
    }

    public void setSubValue(Integer subValue) {
        this.subValue = subValue;
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

    public Integer getMaxSubValue() {
        return maxSubValue;
    }

    public void setMaxSubValue(Integer maxSubValue) {
        this.maxSubValue = maxSubValue;
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
}
