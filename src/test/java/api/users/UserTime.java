package api.users;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserTime {

    @JsonProperty
    private String name;
    @JsonProperty
    private String job;

    public UserTime(String name, String job) {
        this.name = name;
        this.job = job;
    }

    public UserTime() {
    }
}
