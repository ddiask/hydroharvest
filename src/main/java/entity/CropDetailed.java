package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Iterator;
import java.util.List;

public class CropDetailed extends Crop{

    @JsonProperty("cropStatus")
    private final String cropStatus;
    @JsonProperty("id")
    private final String id;
    @JsonProperty("systemsDetails")
    private List<SystemDetailed> systemsDetails;
    @JsonProperty("image")
    private String image;

    public CropDetailed(List<SystemDetailed> systemsDetails, String name,
                        String location, CropsEnum crop, String cropStatus,
                        String id, String image){
        super(name, location, crop);
        this.systemsDetails=systemsDetails;
        this.cropStatus=cropStatus;
        this.id=id;
        this.image=image;
    }

    public String getCropStatus() {
        return cropStatus;
    }

    public String getId() {
        return id;
    }

    public Iterator<SystemDetailed> getSystemsDetails() {
        return systemsDetails.iterator();
    }

    public String getImage() {
        return image;
    }
}
