package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Calendar;
import java.util.List;

public class WaterClient {

    @JsonProperty("startDate")
    private Calendar startDate;
    @JsonProperty("endDate")
    private Calendar endDate;
    @JsonProperty("done")
    private boolean done;

    public WaterClient(Calendar startDate, Calendar endDate, boolean done) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.done = done;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public boolean isDone() {
        return done;
    }
}
