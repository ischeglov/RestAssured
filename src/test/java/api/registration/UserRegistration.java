package api.registration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRegistration {

    @JsonProperty
    private String email;
    @JsonProperty
    private String password;

    public UserRegistration(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserRegistration() {
    }
}
