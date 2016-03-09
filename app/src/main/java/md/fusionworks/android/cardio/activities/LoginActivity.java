package md.fusionworks.android.cardio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import md.fusionworks.android.cardio.R;
import md.fusionworks.android.cardio.fragments.LoginFragment;
import md.fusionworks.android.cardio.fragments.SignUpFragment;
import md.fusionworks.android.cardio.listeners.LoginFragmentsCommunicator;
import md.fusionworks.android.cardio.services.ServiceManager;
import md.fusionworks.android.cardio.storage.LocalStorage;

public class LoginActivity extends BaseActivity implements LoginFragmentsCommunicator {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        showFragment(R.id.fragmentContainer, LoginFragment.newInstance());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGoToSignUpFragment() {

        showFragment(R.id.fragmentContainer, SignUpFragment.newInstance());
    }

    @Override
    public void onGoToLoginFragment() {

        showFragment(R.id.fragmentContainer, LoginFragment.newInstance());
    }
}
