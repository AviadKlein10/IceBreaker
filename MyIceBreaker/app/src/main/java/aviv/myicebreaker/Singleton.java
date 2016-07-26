package aviv.myicebreaker;

import aviv.myicebreaker.module.User;

/**
 * Created by Aviv on 03/07/2016.
 */
public class Singleton{

    private static Singleton instance = new Singleton();

    private User user;

    private Singleton() {}

    public static Singleton getInstance() {
        return instance;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}