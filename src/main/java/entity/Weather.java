package entity;

public class Weather {

    private String windSpeed;
    private String rainIntensity;
    private String precipitaProb;
    private String predWindDir;
    private int tMax;
    private int tMin;
    private String weatherType;

    public Weather(String windSpeed, String rainIntensity, String precipitaProb, String predWindDir,
                   int tMax, int tMin, String weatherType) {
        this.windSpeed = windSpeed;
        this.rainIntensity = rainIntensity;
        this.precipitaProb = precipitaProb;
        this.predWindDir = predWindDir;
        this.tMax = tMax;
        this.tMin = tMin;
        this.weatherType=weatherType;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getRainIntensity() {
        return rainIntensity;
    }

    public void setRainIntensity(String rainIntensity) {
        this.rainIntensity = rainIntensity;
    }

    public String getPrecipitaProb() {
        return precipitaProb;
    }

    public void setPrecipitaProb(String precipitaProb) {
        this.precipitaProb = precipitaProb;
    }

    public String getPredWindDir() {
        return predWindDir;
    }

    public void setPredWindDir(String predWindDir) {
        this.predWindDir = predWindDir;
    }

    public int gettMax() {
        return tMax;
    }

    public void settMax(int tMax) {
        this.tMax = tMax;
    }

    public int gettMin() {
        return tMin;
    }

    public void settMin(int tMin) {
        this.tMin = tMin;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }
}


