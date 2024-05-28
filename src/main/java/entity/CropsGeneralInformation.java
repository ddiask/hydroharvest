package entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CropsGeneralInformation {
    @JsonProperty("crops")
    List<CropDetailed> crops;

    public CropsGeneralInformation(List<CropDetailed> crops) {
        this.crops = crops;
    }

    public List<CropDetailed> getCrops() {
        return crops;
    }
}
