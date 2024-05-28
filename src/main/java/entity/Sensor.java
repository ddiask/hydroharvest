package entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Sensor {

    @JsonProperty("name")
    private String name;
    @JsonProperty("value")
    private double value;
    @JsonProperty("unit")
    private String unit;

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }
}
