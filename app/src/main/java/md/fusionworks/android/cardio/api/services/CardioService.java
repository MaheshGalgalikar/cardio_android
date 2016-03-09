package md.fusionworks.android.cardio.api.services;

import java.util.ArrayList;
import java.util.List;

import md.fusionworks.android.cardio.api.models.DataRequest;
import md.fusionworks.android.cardio.api.models.UserResponse;
import md.fusionworks.android.cardio.models.Data;
import md.fusionworks.android.cardio.api.models.LoginRequest;
import md.fusionworks.android.cardio.api.models.LoginResponse;
import md.fusionworks.android.cardio.api.models.SignUpRequest;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Created by admin on 13.08.2015.
 */
public interface CardioService {

    String URL_CARDIO = "http://54.149.201.31:9000/";
    String SERVICE_LOGIN = "/user/login";
    String SERVICE_SIGN_UP = "/user";
    String SERVICE_DATA = "/data";
    String SERVICE_DATA_FILE = "/data/file";
    String SERVICE_USER = "/user";

    @POST(SERVICE_LOGIN)
    void login(@Body LoginRequest body, Callback<LoginResponse> callback);

    @POST(SERVICE_SIGN_UP)
    void signUp(@Body SignUpRequest body, Callback<String> callback);

    @POST(SERVICE_DATA)
    void data(@Body DataRequest body, Callback<String> callback);

    @GET(SERVICE_DATA)
    void getData(@Query("start") long start, @Query("end") long end, Callback<ArrayList<Data>> callback);

    @Multipart
    @POST(SERVICE_DATA_FILE)
    void uploadFileData(@Part("file") TypedFile file, Callback<String> callback);

    @GET(SERVICE_USER)
    void user(Callback<UserResponse> callback);
}
