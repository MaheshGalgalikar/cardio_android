package md.fusionworks.android.cardio.fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import butterknife.ButterKnife;
import butterknife.Bind;
import md.fusionworks.android.cardio.R;
import md.fusionworks.android.cardio.activities.MainActivity;
import md.fusionworks.android.cardio.api.ServiceGenerator;
import md.fusionworks.android.cardio.api.models.LoginRequest;
import md.fusionworks.android.cardio.api.models.LoginResponse;
import md.fusionworks.android.cardio.api.services.CardioService;
import md.fusionworks.android.cardio.listeners.LoginFragmentsCommunicator;
import md.fusionworks.android.cardio.storage.LocalStorage;
import retrofit.Callback;
import retrofit.RetrofitError;


public class LoginFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.logoField)
    TextView logoField;
    @Bind(R.id.emailField)
    EditText emailField;
    @Bind(R.id.passwordField)
    EditText passwordField;
    @Bind(R.id.loginButton)
    Button loginButton;
    @Bind(R.id.signUpLink)
    TextView signUpLink;

    private Typeface freestyleScriptRegularTypeface;
    private LoginFragmentsCommunicator loginFragmentsCommunicator;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {

        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        freestyleScriptRegularTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Freestyle Script-Regular.ttf");
        logoField.setTypeface(freestyleScriptRegularTypeface);

        populateLoginFields();

        loginButton.setOnClickListener(this);
        signUpLink.setOnClickListener(this);
    }

    private void populateLoginFields() {

        LocalStorage localStorage = LocalStorage.from(getActivity());

        emailField.setText(localStorage.getLoginEmail());
        passwordField.setText(localStorage.getLoginPassword());
    }

    private void saveLoginFields() {

        LocalStorage localStorage = LocalStorage.from(getActivity());

        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        localStorage.setLoginEmail(email);
        localStorage.setLoginPassword(password);
    }

    public void login() {

//        if (!validate()) {
//            onLoginFailed();
//            return;
//        }

//        showLoadingDialog();
//        loginButton.setEnabled(false);

        onLoginSuccess();

//        String email = emailField.getText().toString();
//        String password = passwordField.getText().toString();
//
//        CardioService cardioService = ServiceGenerator.createService(CardioService.class, CardioService.URL_CARDIO, null);
//        cardioService.login(new LoginRequest(email, password), new Callback<LoginResponse>() {
//            @Override
//            public void success(LoginResponse loginResponse, retrofit.client.Response response) {
//
//                LocalStorage.from(getActivity()).setAuthToken(loginResponse.getAuthToken());
//                onLoginSuccess();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//
//                onLoginFailed();
//            }
//        });
    }

    public void onLoginSuccess() {

        loginButton.setEnabled(true);
        saveLoginFields();
        LocalStorage.from(getActivity()).setIsLoggedIn(true);
        hideLoadingDialog();

        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public void onLoginFailed() {

        Toast.makeText(getActivity(), "Login failed", Toast.LENGTH_LONG).show();
        hideLoadingDialog();
        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("enter a valid email address");
            valid = false;
        } else {
            emailField.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordField.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.loginButton:

                login();
                break;
            case R.id.signUpLink:

                loginFragmentsCommunicator.onGoToSignUpFragment();
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            loginFragmentsCommunicator = (LoginFragmentsCommunicator) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement LoginFragmentsCommunicator");
        }
    }
}
