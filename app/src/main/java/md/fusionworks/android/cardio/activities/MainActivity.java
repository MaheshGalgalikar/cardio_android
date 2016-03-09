package md.fusionworks.android.cardio.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;


import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.android.cardio.R;
import md.fusionworks.android.cardio.fragments.HistoricalDataVisualizationFragment;
import md.fusionworks.android.cardio.fragments.KPIFragment;
import md.fusionworks.android.cardio.fragments.RealTimeDataVisualizationFragment;
import md.fusionworks.android.cardio.fragments.SettingsFragment;
import md.fusionworks.android.cardio.services.ServiceManager;
import md.fusionworks.android.cardio.storage.LocalStorage;

public class MainActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    Toolbar toolBar;
    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @Bind(R.id.navigationView)
    NavigationView navigationView;

    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ServiceManager.newInstance(this).startRealTimeDataGeneratorService();

        ButterKnife.bind(this);

        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("Real-Time data visualization");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupDrawerLayout();

        showFragment(R.id.containerLayout, RealTimeDataVisualizationFragment.newInstance());
    }

    private void setupDrawerLayout() {

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.title_activity_main, R.string.title_activity_main);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {

                    case R.id.realTimeDataVisualizationItem:

                        toolBar.setTitle("Real-Time data visualization");
                        showFragment(R.id.containerLayout, RealTimeDataVisualizationFragment.newInstance());
                        break;

                    case R.id.historicalDataVisualizationItem:

                        toolBar.setTitle("Historical data visualization");
                        showFragment(R.id.containerLayout, HistoricalDataVisualizationFragment.newInstance());
                        break;

                    case R.id.kpiItem:

                        toolBar.setTitle("KPI");
                        showFragment(R.id.containerLayout, KPIFragment.newInstance());
                        break;

                    case R.id.settingsItem:

                        toolBar.setTitle("Settings");
                        showFragment(R.id.containerLayout, new SettingsFragment());
                        break;

                }

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.action_logout:
                showLogoutConfirmationDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showLogoutConfirmationDialog() {

        new MaterialDialog.Builder(this)
                .title("Logout?")
                .content("You will not be able to monitor graphs.")
                .positiveText("Logout")
                .negativeText("Cancel")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {

                        logout();
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {

                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {

                    }
                })
                .show();
    }

    private void logout() {

        LocalStorage.from(this).setIsLoggedIn(false);
        ServiceManager.newInstance(this).stopRealTimeDataGeneratorService();
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (android.os.Build.VERSION.SDK_INT < 5 && keyCode == 4 && event.getRepeatCount() == 0) {
            showLogoutConfirmationDialog();
            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

        showLogoutConfirmationDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ServiceManager.newInstance(this).stopRealTimeDataGeneratorService();
    }
}
