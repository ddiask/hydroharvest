package entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SensorData {

    @JsonProperty("ip")
    private String ip;
    @JsonProperty("sensors")
    private List<Sensor> sensors;

    public String getIp() {
        return ip;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }
}
