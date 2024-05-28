package entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Client {


    @JsonProperty("name")
    private String name;
    @JsonProperty("id")
    private String id;
    @JsonProperty("mail")
    private String mail;
    @JsonProperty("password")
    private String password;


    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }
}
