package api.users;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserTime {

    @JsonProperty
    private String time;
    @JsonProperty
    private String job;

    public UserTime(String time, String jod) {
        this.time = time;
        this.job = jod;
    }

    public String getTime() {
        return time;
    }

    public String getJob() {
        return job;
    }
}
