package ipma;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ForecastData {
    @JsonProperty("precipitaProb")
    private String precipitaProb;
    @JsonProperty("tMin")
    private int tMin;
    @JsonProperty("tMax")
    private int tMax;
    @JsonProperty("predWindDir")
    private String predWindDir;
    @JsonProperty("idWeatherType")
    private int idWeatherType;
    @JsonProperty("classWindSpeed")
    private int classWindSpeed;
    @JsonProperty("longitude")
    private String longitude;
    @JsonProperty("classPrecInt")
    private Integer classPrecInt;
    @JsonProperty("globalIdLocal")
    private int globalIdLocal;
    @JsonProperty("latitude")
    private String latitude;

    // Getters and Setters
    public String getPrecipitaProb() {
        return precipitaProb;
    }

    public void setPrecipitaProb(String precipitaProb) {
        this.precipitaProb = precipitaProb;
    }

    public int getTMin() {
        return tMin;
    }

    public void setTMin(int tMin) {
        this.tMin = tMin;
    }

    public int getTMax() {
        return tMax;
    }

    public void setTMax(int tMax) {
        this.tMax = tMax;
    }

    public String getPredWindDir() {
        return predWindDir;
    }

    public void setPredWindDir(String predWindDir) {
        this.predWindDir = predWindDir;
    }

    public int getIdWeatherType() {
        return idWeatherType;
    }

    public void setIdWeatherType(int idWeatherType) {
        this.idWeatherType = idWeatherType;
    }

    public int getClassWindSpeed() {
        return classWindSpeed;
    }

    public void setClassWindSpeed(int classWindSpeed) {
        this.classWindSpeed = classWindSpeed;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Integer getClassPrecInt() {
        return classPrecInt;
    }

    public void setClassPrecInt(Integer classPrecInt) {
        this.classPrecInt = classPrecInt;
    }

    public int getGlobalIdLocal() {
        return globalIdLocal;
    }

    public void setGlobalIdLocal(int globalIdLocal) {
        this.globalIdLocal = globalIdLocal;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
