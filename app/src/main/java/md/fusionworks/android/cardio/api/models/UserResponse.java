package md.fusionworks.android.cardio.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 26.08.2015.
 */
public class UserResponse {

    @SerializedName("id")
    private String id;
    @SerializedName("email")
    private String email;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
