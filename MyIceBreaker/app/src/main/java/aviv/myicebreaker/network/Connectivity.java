package aviv.myicebreaker.network;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import aviv.myicebreaker.module.JsonParser;
import aviv.myicebreaker.module.User;

/**
 * Created by Aviv on 03/07/2016.
 */
public class Connectivity {

    static final String TAG = "connectivity";




    private Context context;
    private BaseListener baseListener;

    public Connectivity(Activity activity) {
        this.context = activity;
        this.baseListener = (BaseListener) activity;
    }



    public void login(int userId, String accessToken) {
        JsonObject json = new JsonObject();
        json.addProperty("userId", userId);
        json.addProperty("accessToken", accessToken);

        Ion.with(context)
                .load("http://example.com/thing.json")
                .setJsonObjectBody(json)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        if (e == null) {
                            User user = JsonParser.parseUserToObject(result.getResult());
                            ((SplashDoneDownload) baseListener).onLoginDone(user, null);
                        } else {
                            if (result != null) {
                                if (result.getHeaders().code() == 404) {
                                    Log.e(TAG, " Page Not Found! 404 error");
                                }
                            }
                            ((SplashDoneDownload) baseListener).onLoginDone(null, e);
                        }
                    }

                });

    }

    public void testLogin() {
        JsonObject json = new JsonObject();
        json.addProperty("name", "HELLO");


        Ion.with(context)
                .load("http://79.178.127.180:8082/testpost/")
                .setJsonObjectBody(json)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        if (e == null) {
                            User user = new User("Aviad","1",2);
                            ((SplashDoneDownload) baseListener).onLoginDone(user, null);
                        } else {
                            if (result != null) {
                                if (result.getHeaders().code() == 404) {
                                    Log.e(TAG, " Page Not Found! 404 error");
                                }
                            }
                            ((SplashDoneDownload) baseListener).onLoginDone(null, e);
                        }
                    }

                });

    }
}
