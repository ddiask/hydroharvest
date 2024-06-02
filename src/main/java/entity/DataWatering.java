package entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataWatering {

    @JsonProperty("start")
    private long start;
    @JsonProperty("end")
    private long end;

    public DataWatering(long start, long end){
        this.end=end;
        this.start=start;
    }
}
