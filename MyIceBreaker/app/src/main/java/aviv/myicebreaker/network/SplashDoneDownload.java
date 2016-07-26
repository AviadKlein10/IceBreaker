package aviv.myicebreaker.network;

import aviv.myicebreaker.module.User;

/**
 * Created by Aviv on 03/07/2016.
 */
public interface SplashDoneDownload extends BaseListener {
    void onLoginDone(User localUser, Exception e);
}
