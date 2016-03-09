package md.fusionworks.android.cardio.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.android.cardio.R;
import md.fusionworks.android.cardio.api.ServiceGenerator;
import md.fusionworks.android.cardio.api.models.UserResponse;
import md.fusionworks.android.cardio.api.services.CardioService;
import md.fusionworks.android.cardio.storage.LocalStorage;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SplashActivity extends AppCompatActivity {

    private static final int STATUS_UNAUTHORIZED = 401;

    @Bind(R.id.logoField)
    TextView logoField;

    private Typeface freestyleScriptRegularTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        freestyleScriptRegularTypeface = Typeface.createFromAsset(getAssets(), "fonts/Freestyle Script-Regular.ttf");
        logoField.setTypeface(freestyleScriptRegularTypeface);

        CardioService cardioService = ServiceGenerator.createService(CardioService.class, CardioService.URL_CARDIO, LocalStorage.from(getApplicationContext()).getAuthToken());
        cardioService.user(new Callback<UserResponse>() {
            @Override
            public void success(UserResponse userResponse, Response response) {

                Intent mainActivityIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainActivityIntent);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {

                int status = error.getResponse().getStatus();
                switch (status) {

                    case STATUS_UNAUTHORIZED:

                        LocalStorage.from(SplashActivity.this).setIsLoggedIn(false);
                        Intent loginActivityIntent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(loginActivityIntent);
                        finish();
                        break;
                    default:
                        finish();
                }
            }
        });
    }
}
