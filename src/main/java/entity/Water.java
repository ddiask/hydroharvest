package entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Calendar;
import java.util.List;

public class Water {

    @JsonProperty("startDate")
    private Calendar startDate;
    @JsonProperty("endDate")
    private Calendar endDate;
    @JsonProperty("systems")
    private List<String> systems;

    @JsonIgnore
    private boolean done;

    public Calendar getStartDate() {
        return startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public List<String> getSystems() {
        return systems;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public void setSystems(List<String> systems) {
        this.systems = systems;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
