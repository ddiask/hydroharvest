package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Calendar;

import java.util.Date;
import com.google.cloud.Timestamp;

public class WaterClient {

    @JsonProperty("startDate")
    private Date startDate;
    @JsonProperty("endDate")
    private Date endDate;
    @JsonProperty("done")
    private boolean done;

    public WaterClient(Date startDate, Date endDate, boolean done) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.done = done;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public boolean isDone() {
        return done;
    }
}
