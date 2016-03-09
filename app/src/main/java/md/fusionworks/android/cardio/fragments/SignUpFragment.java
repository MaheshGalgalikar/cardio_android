package md.fusionworks.android.cardio.fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.android.cardio.R;
import md.fusionworks.android.cardio.activities.MainActivity;
import md.fusionworks.android.cardio.api.ServiceGenerator;
import md.fusionworks.android.cardio.api.models.SignUpRequest;
import md.fusionworks.android.cardio.api.services.CardioService;
import md.fusionworks.android.cardio.listeners.LoginFragmentsCommunicator;
import md.fusionworks.android.cardio.storage.LocalStorage;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.mime.TypedByteArray;


public class SignUpFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.logoField)
    TextView logoField;
    @Bind(R.id.firstNameField)
    EditText firstNameField;
    @Bind(R.id.lastNameField)
    EditText lastNameField;
    @Bind(R.id.emailField)
    EditText emailField;
    @Bind(R.id.passwordField)
    EditText passwordField;
    @Bind(R.id.signUpButton)
    Button signUpButton;
    @Bind(R.id.loginLink)
    TextView loginLink;

    private Typeface freestyleScriptRegularTypeface;
    private LoginFragmentsCommunicator loginFragmentsCommunicator;

    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance() {

        return new SignUpFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        freestyleScriptRegularTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Freestyle Script-Regular.ttf");
        logoField.setTypeface(freestyleScriptRegularTypeface);

        signUpButton.setOnClickListener(this);
        loginLink.setOnClickListener(this);
    }

    public void onSignUpSuccess() {

        signUpButton.setEnabled(true);
        LocalStorage.from(getActivity()).setIsLoggedIn(true);
        hideLoadingDialog();

        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public void signUp() {

        if (!validate()) {
            onSignUpFailed();
            return;
        }

        showLoadingDialog();
        signUpButton.setEnabled(false);

        String firstName = firstNameField.getText().toString();
        String lastName = lastNameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        CardioService cardioService = ServiceGenerator.createService(CardioService.class, CardioService.URL_CARDIO, null);
        cardioService.signUp(new SignUpRequest(firstName, lastName, email, password), new Callback<String>() {
            @Override
            public void success(String loginResponse, retrofit.client.Response response) {

                onSignUpSuccess();
            }

            @Override
            public void failure(RetrofitError error) {

                onSignUpSuccess();
                String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                Log.v("failure", json.toString());
            }
        });
    }

    public void onSignUpFailed() {

        Toast.makeText(getActivity(), "Sign Up failed", Toast.LENGTH_LONG).show();
        hideLoadingDialog();
        signUpButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String firstName = firstNameField.getText().toString();
        String lastName = lastNameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if (firstName.isEmpty() || firstName.length() < 3) {
            firstNameField.setError("at least 3 characters");
            valid = false;
        } else {
            lastNameField.setError(null);
        }

        if (lastName.isEmpty() || lastName.length() < 3) {
            lastNameField.setError("at least 3 characters");
            valid = false;
        } else {
            lastNameField.setError(null);
        }

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

            case R.id.signUpButton:

                signUp();
                break;
            case R.id.loginLink:

                loginFragmentsCommunicator.onGoToLoginFragment();
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
