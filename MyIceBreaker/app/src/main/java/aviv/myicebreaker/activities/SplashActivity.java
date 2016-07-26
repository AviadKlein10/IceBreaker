package aviv.myicebreaker.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

import aviv.myicebreaker.R;
import aviv.myicebreaker.Realm.RealmManager;
import aviv.myicebreaker.Singleton;
import aviv.myicebreaker.module.JsonParser;
import aviv.myicebreaker.module.User;
import aviv.myicebreaker.network.Connectivity;
import aviv.myicebreaker.network.SplashDoneDownload;
import io.realm.Realm;

public class SplashActivity extends AppCompatActivity implements SplashDoneDownload {

    private static final String TAG = Connectivity.class.getSimpleName();
    private static final String USER_KEY = "userKey";
    private Connectivity connectivity;
    private User localUser;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private ProgressDialog progressDialog;
    private String id, name, email, photoName, photoUrl;
    private Realm realm;
    private Button realButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);
        facebookInit();
        init();
        realButton = (Button)findViewById(R.id.realButton);
        realButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
                progressDialog = new ProgressDialog(SplashActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }
        });


    }

    private void init() {
        connectivity = new Connectivity(this);
        connectivity.testLogin();
        localUser = loadUserSharedPreferences();
        Log.d("mamo", localUser.getFirstName());


    }

    private void facebookInit() {


        callbackManager = CallbackManager.Factory.create();
        RealmManager.getInstance(getApplicationContext());
        realm = RealmManager.getRealm();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_photos", "email", "user_birthday"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
               Log.d("accesdToken",  loginResult.getAccessToken() +"");
                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("GraphResponse", response.getRawResponse());
                        Log.d("LoginResults", loginResult.getAccessToken().getToken() + "");

                        //  loginButton.setVisibility(View.GONE);
                        // progressLayout.setVisibility(View.VISIBLE);

                        try {
                            JSONObject jsonObject = new JSONObject(response.getRawResponse());
                            id = jsonObject.getString("id");
                            name = jsonObject.getString("name");


                            realm.beginTransaction();

                            //   userDetails = realm.createObject(UserDetails.class);
//
                            //   userDetails.setId(id);
                            //   userDetails.setName(name);
                            //   userDetails.setEmail(email);

                            realm.commitTransaction();


                            new GraphRequest(
                                    AccessToken.getCurrentAccessToken(),
                                    "/me/albums",
                                    null,
                                    HttpMethod.GET,
                                    new GraphRequest.Callback() {
                                        public void onCompleted(GraphResponse response) {
                                            Log.d("FacebookAlbumRes", response.toString());

                                            try {
                                                JSONObject jsObj = new JSONObject(response.getRawResponse());

                                                JSONArray jarray = jsObj.getJSONArray("data");
                                                for (int i = 0; i < jarray.length(); i++) {

                                                    JSONObject albums = jarray.getJSONObject(i);
                                                    final String albumId = albums.getString("id");
                                                    final String albumNames = albums.getString("name");
                                                    final String albumDate = albums.getString("created_time");

                                                    Log.d("AlbumsId", albumId + "\n");

                                                    final Bundle params = new Bundle();
                                                    params.putBoolean("redirect", false);

                                                    new GraphRequest(
                                                            AccessToken.getCurrentAccessToken(),
                                                            "/" + albumId + "/picture",
                                                            params,
                                                            HttpMethod.GET,
                                                            new GraphRequest.Callback() {
                                                                public void onCompleted(GraphResponse response) {
                                                                    try {

                                                                        Log.d("AlbumResponse", response.getRawResponse());

                                                                        JSONObject jPics = new JSONObject(response.getRawResponse());

                                                                        JSONObject jData = jPics.getJSONObject("data");

                                                                        String albumPicture = jData.getString("url");

                                                                        Log.d("AlbumsPics", albumId + " -- " + albumPicture + "\n");

                                                                        realm.beginTransaction();

                                                                        //   fbAlbum = realm.createObject(FacebookAlbums.class);
                                                                        //   fbAlbum.setAlbumId(albumId);
                                                                        //   fbAlbum.setAlbumName(albumNames);
                                                                        //   fbAlbum.setAlbumCreateddate(albumDate);
                                                                        //   fbAlbum.setAlbumUrl(albumPicture);

                                                                        realm.commitTransaction();

                                                                        //    fbAlbumList = realm.where(FacebookAlbums.class).findAll();

                                                                        new GraphRequest(
                                                                                AccessToken.getCurrentAccessToken(),
                                                                                "/" + albumId + "/photos",
                                                                                params,
                                                                                HttpMethod.GET,
                                                                                new GraphRequest.Callback() {
                                                                                    public void onCompleted(GraphResponse response) {
                                                                                        Log.d("AlbumPictureResponse", response.getRawResponse() + "\n");

                                                                                        try {
                                                                                            JSONObject jObj = new JSONObject(response.getRawResponse());
                                                                                            JSONArray jArray = jObj.getJSONArray("data");

                                                                                            for (int i = 0; i < jArray.length(); i++) {
                                                                                                JSONObject jPhoto = jArray.getJSONObject(i);

                                                                                                final String photoId = jPhoto.getString("id");

                                                                                                if (jPhoto.has("name")) {
                                                                                                    photoName = jPhoto.getString("name");
                                                                                                } else {
                                                                                                    photoName = "no";
                                                                                                }

                                                                                                final String photoCreatedDate = jPhoto.getString("created_time");

                                                                                                new GraphRequest(
                                                                                                        AccessToken.getCurrentAccessToken(),
                                                                                                        "/" + photoId + "/picture",
                                                                                                        params,
                                                                                                        HttpMethod.GET,
                                                                                                        new GraphRequest.Callback() {
                                                                                                            public void onCompleted(GraphResponse response) {
                                                                                                                Log.d("PhotoResponse", photoId + " -- " + response + "\n");

                                                                                                                try {
                                                                                                                    JSONObject jPics = new JSONObject(response.getRawResponse());
                                                                                                                    JSONObject jPicsData = jPics.getJSONObject("data");

                                                                                                                    if (jPicsData.has("url")) {
                                                                                                                        photoUrl = jPicsData.getString("url");
                                                                                                                        Log.d("photourl", photoUrl);
                                                                                                                    } else {
                                                                                                                        photoUrl = "no image";
                                                                                                                    }

                                                                                                                    realm.beginTransaction();

                                                                                                                    //   fbPhotos = realm.createObject(FacebookPhotos.class);
                                                                                                                    //   fbPhotos.setAlbumId(albumId);
                                                                                                                    //   fbPhotos.setPhotoId(photoId);
                                                                                                                    //   fbPhotos.setPhotoName(photoName);
                                                                                                                    //   fbPhotos.setPhotoDate(photoCreatedDate);
                                                                                                                    //   fbPhotos.setPhotoUrl(photoUrl);

                                                                                                                    realm.commitTransaction();

                                                                                                                    //   fbPhotoList = realm.where(FacebookPhotos.class).findAll();

                                                                                                                } catch (Exception e) {
                                                                                                                    e.printStackTrace();
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                ).executeAsync();
                                                                                            }
                                                                                        } catch (Exception e) {
                                                                                            e.printStackTrace();
                                                                                        }
                                                                                    }
                                                                                }
                                                                        ).executeAsync();

                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                        Log.d("AlbumException", e.toString());
                                                                    }
                                                                }
                                                            }
                                                    ).executeAsync();
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                            ).executeAsync();
                            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                            startActivity(intent);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("GraphExeception", e.toString());
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,gender,birthday,email,bio,photos{link}");
                parameters.putString("redirect", "false");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                progressDialog.dismiss();

            }

            @Override
            public void onError(FacebookException error) {
error.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }

    private User loadUserSharedPreferences() {
        User user = new User("test", "vdsvd3442w",321312);
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String restoredUser = sharedPreferences.getString(USER_KEY, "");
        if (restoredUser.length() > 0) {

            Log.d("restored user", restoredUser + " z");
            user = JsonParser.parseUserToObject(restoredUser);

        } else {
            Log.d("no user", " login00");
        }
        return user;
    }

    @Override
    public void onLoginDone(User localUser, Exception e) {
        if (localUser != null) {
            saveUserSharedPreferences(localUser);
            //TODO  loading
            Singleton.getInstance().setUser(localUser);
            Log.d(getClass().getSimpleName(), "User Ok " + localUser.getFirstName());
        } else {
            Log.e(getClass().getSimpleName(), " Problem With User: " + e.getMessage());
        }
    }

    private void saveUserSharedPreferences(User localUser) {
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String userStr = JsonParser.parseUserToJson(localUser);
        editor.putString(USER_KEY, userStr);
        Log.d("local user ", userStr);
        editor.apply();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }
}
