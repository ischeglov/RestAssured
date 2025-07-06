package api.registration;

public class UnSuccessfulRegistration {

    private String error;

    public UnSuccessfulRegistration(String error) {
        this.error = error;
    }

    public UnSuccessfulRegistration() {
    }

    public String getError() {
        return error;
    }
}
