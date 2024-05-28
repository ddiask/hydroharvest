package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Crop {

    @JsonProperty("systems")
    private List<System> systems;
    @JsonProperty("name")
    private String name;
    @JsonProperty("location")
    private String location;
    @JsonProperty("crop")
    private CropsEnum crop;
    @JsonProperty("password")
    private String password;
    @JsonProperty("idUser")
    private String idUser;

    public Crop(String name, String location, CropsEnum crop) {
        this.name = name;
        this.location = location;
        this.crop = crop;
    }



    public List<System> getSystems() {
        return systems;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public CropsEnum getCrop() {
        return crop;
    }

    public String getPassword() {
        return password;
    }

    public String getIdUser() {
        return idUser;
    }
}
