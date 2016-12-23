package nl.rdewildt.addone.model;

import org.joda.time.DateTime;

/**
 * Created by roy on 7/11/16.
 */
public class Counter {
    private Integer value;
    private Integer subValue;

    private DateTime lastUpdated;

    public Counter() {
        this.value = 0;
        this.subValue = 0;
        this.lastUpdated = new DateTime().minusWeeks(1);
    }

    public Counter(Integer value, DateTime lastUpdated, Integer increaseRate, Integer decreaseRate, Integer maxSubValue) {
        this.value = value;
        this.subValue = 0;

        this.lastUpdated = lastUpdated;
    }

    public void reset(){
        this.value = 0;
        this.subValue = 0;
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
        result = result * 31 + getValue().hashCode();
        result = result * 31 + getSubValue().hashCode();
        result = result * 31 + getLastUpdated().hashCode();
        return result;
    }
}
