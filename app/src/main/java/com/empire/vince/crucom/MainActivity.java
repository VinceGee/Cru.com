package com.empire.vince.crucom;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.empire.vince.crucom.build.MainActivityYeSimplistic;
import com.empire.vince.crucom.dialogs.SweetAlertDialog;
import com.empire.vince.crucom.login.ChangePassword;
import com.empire.vince.crucom.login.LoginActivity;
import com.empire.vince.crucom.login.SQLiteHandler;
import com.empire.vince.crucom.login.SessionManager;
import com.empire.vince.crucom.win.WinActivity;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ////////////////////LOGOUT////////////////////////////////////////


        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_settings) {
            return true;
        }else if(id == R.id.menu_logout){
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setCustomImage(R.mipmap.ic_launcher)
                    .setTitleText("Cru.com")
                    .setContentText("Are you sure you want to logout?")
                    .setCancelText("No,cancel please!")
                    .setConfirmText("Logout!")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            // reuse previous dialog instance, keep widget user state, reset them if you need
                            sDialog.setTitleText("Cru.com")
                                    .setContentText("You have cancelled the logout")
                                    .setConfirmText("OK")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(null)
                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            sDialog.dismiss();

                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            logoutUser();
                        }
                    })
                    .show();

        }

        return true;
    }

    //@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent home = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(home);
        } else if (id == R.id.nav_win) {
            Intent win = new Intent(getApplicationContext(), WinActivity.class);
            startActivity(win);
        } else if (id == R.id.nav_build) {
            Intent build = new Intent(getApplicationContext(), MainActivityYeSimplistic.class);
            startActivity(build);

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_password) {
            Intent changePwd = new Intent(getApplicationContext(), ChangePassword.class);
            startActivity(changePwd);
        } else if (id == R.id.nav_figures) {

        } else if (id == R.id.nav_abtcru) {

        } else if (id == R.id.nav_abtapp) {

        }else if (id == R.id.nav_settings) {
            if (android.os.Build.VERSION.SDK_INT >= 11) {
                startActivity(new Intent(MainActivity.this,
                        com.empire.vince.crucom.settings.SettingsActivity.class));
            } else {
                startActivity(new Intent(MainActivity.this,
                        com.empire.vince.crucom.settings.v7.SettingsActivity.class));
            }

        }else if (id == R.id.nav_logout) {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setCustomImage(R.mipmap.ic_launcher)
                    .setTitleText("Cru.com")
                    .setContentText("Are you sure you want to logout?")
                    .setCancelText("No,cancel please!")
                    .setConfirmText("Logout!")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            // reuse previous dialog instance, keep widget user state, reset them if you need
                            sDialog.setTitleText("Cru.com")
                                    .setContentText("You have cancelled the logout")
                                    .setConfirmText("OK")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(null)
                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            sDialog.dismiss();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            logoutUser();
                        }
                    })
                    .show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
