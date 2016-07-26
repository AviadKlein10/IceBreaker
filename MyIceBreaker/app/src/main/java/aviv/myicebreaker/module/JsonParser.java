package aviv.myicebreaker.module;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aviv on 03/07/2016.
 */
public class JsonParser {

    private static final String FIRST_NAME = "firstName";
    private static final String USER_ID = "userId";
    private static final String AGE = "age";



    public static User parseUserToObject(String result){
        User  user = null;

        try {
            JSONObject jsonUserObject = new JSONObject(result);
            String firstName = jsonUserObject.getString(FIRST_NAME);
            int userID = jsonUserObject.getInt(USER_ID);
            int age = jsonUserObject.getInt(AGE);

            user = new User(firstName,""+userID,age);

        } catch (JSONException e) {
            Log.e("No User Saved", " 404");
        }
        return user;
    }

    public static String parseUserToJson(User localUser){

        JSONObject jsonUserObject = new JSONObject();
        try {
            jsonUserObject.put("firstName",localUser.getFirstName());
            jsonUserObject.put("userId",localUser.getUserId());
            jsonUserObject.put("age",localUser.getFirstName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("local user parser", jsonUserObject.toString());
        return jsonUserObject.toString();
    }
}
