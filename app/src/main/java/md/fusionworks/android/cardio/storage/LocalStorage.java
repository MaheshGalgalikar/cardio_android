package md.fusionworks.android.cardio.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by admin on 04.08.2015.
 */
public class LocalStorage {

    private static final String KEY_AUTH_TOKEN = "KEY_AUTH_TOKEN";
    private static final String KEY_LOGIN_EMAIL_FIELD = "KEY_LOGIN_EMAIL_FIELD";
    private static final String KEY_LOGIN_PASSWORD_FIELD = "KEY_LOGIN_PASSWORD_FIELD";
    private static final String KEY_SEND_DATA_TO_SERVER = "KEY_SEND_DATA_TO_SERVER";
    private static final String KEY_IS_LOGGED_IN = "KEY_IS_LOGGED_IN";

    private static LocalStorage instance;
    private final SharedPreferences sharedPreferences;

    private LocalStorage(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static LocalStorage from(Context paramContext) {
        if (instance == null) {
            instance = new LocalStorage(paramContext);
        }
        return instance;
    }

    public void clearAll() {
        this.sharedPreferences.edit().clear().commit();
    }

    public void setAuthToken(String authToken) {
        this.sharedPreferences.edit().putString(KEY_AUTH_TOKEN, authToken).commit();
    }

    public String getAuthToken() {
        return this.sharedPreferences.getString(KEY_AUTH_TOKEN, null);
    }

    public void setLoginEmail(String email) {
        this.sharedPreferences.edit().putString(KEY_LOGIN_EMAIL_FIELD, email).commit();
    }

    public String getLoginEmail() {
        return this.sharedPreferences.getString(KEY_LOGIN_EMAIL_FIELD, null);
    }

    public void setLoginPassword(String password) {
        this.sharedPreferences.edit().putString(KEY_LOGIN_PASSWORD_FIELD, password).commit();
    }

    public String getLoginPassword() {
        return this.sharedPreferences.getString(KEY_LOGIN_PASSWORD_FIELD, null);
    }

    public void setSendDataToServer(boolean value) {
        this.sharedPreferences.edit().putBoolean(KEY_SEND_DATA_TO_SERVER, value).commit();
    }

    public boolean getSendDataToServer() {
        return this.sharedPreferences.getBoolean(KEY_SEND_DATA_TO_SERVER, false);
    }

    public void setIsLoggedIn(boolean value) {
        this.sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, value).commit();
    }

    public boolean getIsLoggedIn() {
        return this.sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}
