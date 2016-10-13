package nl.rdewildt.addone;

/**
 * Created by roydewildt on 06/10/2016.
 */

public class Goal {
    private String name;
    private String summary;
    private Integer requiredPoints;

    public Goal(String name, String summary, Integer requiredPoints) {
        this.name = name;
        this.summary = summary;
        this.requiredPoints = requiredPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getRequiredPoints() {
        return requiredPoints;
    }

    public void setRequiredPoints(Integer requiredPoints) {
        this.requiredPoints = requiredPoints;
    }
}
