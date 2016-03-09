package md.fusionworks.android.cardio.api.models;

import com.google.gson.annotations.SerializedName;


/**
 * Created by admin on 13.08.2015.
 */

public class LoginResponse {

    @SerializedName("authToken")
    private String authToken;

    public LoginResponse() {
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
