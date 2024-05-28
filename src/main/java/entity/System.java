package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.*;

public class System {

    @JsonProperty("latitude")
    private  double latitude;
    @JsonProperty("longitude")
    private  double longitude;
    @JsonProperty("ip")
    private  String ip;
    @JsonProperty("name")
    private  String name;

    public System(double latitude, double longitude, String ip, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.ip = ip;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getIp() {
        return ip;
    }
}
