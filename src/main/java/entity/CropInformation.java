package entity;

public class CropInformation {

    private final String image;
    private final Long maxHumidity;
    private final Long minHumidity;
    private final Long maxTemperature;
    private final Long minTemperature;

    public CropInformation(String image, Long maxHumidity, Long minHumidity, Long maxTemperature, Long minTemperature) {
        this.image = image;
        this.maxHumidity = maxHumidity;
        this.minHumidity = minHumidity;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
    }

    public String getImage() {
        return image;
    }

    public Long getMaxHumidity() {
        return maxHumidity;
    }

    public Long getMinHumidity() {
        return minHumidity;
    }

    public Long getMaxTemperature() {
        return maxTemperature;
    }

    public Long getMinTemperature() {
        return minTemperature;
    }
}
