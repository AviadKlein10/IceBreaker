package aviv.myicebreaker.module;

/**
 * Created by Aviv on 03/07/2016.
 */
public class User {
    private String firstName, accessToken;
    private int userId;

    public User(String firstName,String accessToken, int userId) {
        this.firstName = firstName;
        this.accessToken = accessToken;
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

