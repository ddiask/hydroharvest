package ipma;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WeatherForecast {
    @JsonProperty("owner")
    private String owner;
    @JsonProperty("country")
    private String country;
    @JsonProperty("forecastDate")
    private String forecastDate;
    @JsonProperty("data")
    private List<ForecastData> data;
    @JsonProperty("dataUpdate")
    private String dataUpdate;

    // Getters and Setters
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getForecastDate() {
        return forecastDate;
    }

    public void setForecastDate(String forecastDate) {
        this.forecastDate = forecastDate;
    }

    public List<ForecastData> getData() {
        return data;
    }

    public void setData(List<ForecastData> data) {
        this.data = data;
    }

    public String getDataUpdate() {
        return dataUpdate;
    }

    public void setDataUpdate(String dataUpdate) {
        this.dataUpdate = dataUpdate;
    }
}
