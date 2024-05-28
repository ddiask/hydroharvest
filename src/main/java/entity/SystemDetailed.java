package entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.*;

public class SystemDetailed extends System{
    @JsonProperty("humidityLevel")
    private double humidityLevel;
    @JsonProperty("lightLevel")
    private double lightLevel;
    @JsonProperty("tankLevel")
    private double tankLevel;
    @JsonProperty("temperatureLevel")
    private double temperatureLevel;
    @JsonProperty("status")
    private String status;

    public SystemDetailed(double latitude, double longitude, String ip, String name, double humidityLevel,
                  double lightLevel, double tankLevel, double temperatureLevel,
                  String status) {
        super(latitude, longitude,ip,name);
        this.humidityLevel = humidityLevel;
        this.lightLevel = lightLevel;
        this.tankLevel = tankLevel;
        this.temperatureLevel = temperatureLevel;
        this.status=status;
    }


    public String getStatus() {
        return status;
    }

    public double getHumidityLevel() {
        return humidityLevel;
    }

    public double getLightLevel() {
        return lightLevel;
    }

    public double getTankLevel() {
        return tankLevel;
    }

    public double getTemperatureLevel() {
        return temperatureLevel;
    }
}
