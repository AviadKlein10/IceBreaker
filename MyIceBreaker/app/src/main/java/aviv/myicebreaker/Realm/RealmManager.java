package aviv.myicebreaker.Realm;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by TWINS on 06/12/15.
 */
public class RealmManager {

    public static RealmManager realmManager;
    public static RealmConfiguration realmConfig;
    public static Realm realm;

    public static Context appContext;

    public static RealmManager getInstance(Context context) {

        if (realmManager == null) {
            realmManager = new RealmManager();
            realmConfig = new RealmConfiguration.Builder(context).build();
            realm = Realm.getInstance(realmConfig);

            appContext = context;
        }

        return realmManager;
    }

    public static Context getContext() {
        return appContext;
    }

    public static Realm getRealm() {
        return realm;
    }
}
