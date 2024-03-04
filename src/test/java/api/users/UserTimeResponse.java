package api.users;

public class UserTimeResponse {

    private String time;
    private String job;
    private String updatedAt;

    public UserTimeResponse(String time, String jod, String updatedAt) {
        this.time = time;
        this.job = jod;
        this.updatedAt = updatedAt;
    }

    public UserTimeResponse() {
    }

    public String getTime() {
        return time;
    }

    public String getJob() {
        return job;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
