package md.fusionworks.android.cardio.api.models;

import com.google.gson.annotations.SerializedName;


/**
 * Created by admin on 13.08.2015.
 */

public class SignUpRequest {

    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

    public SignUpRequest(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
