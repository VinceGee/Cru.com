package com.empire.vince.crucom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.empire.vince.crucom.build.MainActivityYeSimplistic;
import com.empire.vince.crucom.dialogs.SweetAlertDialog;
import com.empire.vince.crucom.login.ChangePassword;
import com.empire.vince.crucom.login.LoginActivity;
import com.empire.vince.crucom.login.SQLiteHandler;
import com.empire.vince.crucom.login.SessionManager;
import com.empire.vince.crucom.login.Wins;
import com.empire.vince.crucom.slider.ChildAnimationExample;
import com.empire.vince.crucom.slider.SliderLayout;
import com.empire.vince.crucom.win.WinActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BaseSliderView.OnSliderClickListener{
    SliderLayout mDemoSlider;
    private SQLiteHandler db;
    private SessionManager session;
    private ProgressDialog pDialog;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static int countWinsList;
    private static int countWinsVerification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);

        TextView nameView = (TextView) hView.findViewById(R.id.nameView);
        TextView emailView = (TextView) hView.findViewById(R.id.emailView);

        ////////////////////LOGOUT////////////////////////////////////////
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        nameView.setText(name);
        emailView.setText(email);


        if(!db.isContentEmpty()){
            if(isInternetOn()){

                ArrayList<Wins>  wins = db.getSavedDetails();
                countWinsList = wins.size();
                for(Wins wns : wins){
                    String sname = wns.getName();
                    String swinmeth = wns.getWinmeth() ;
                    String semail = wns.getEmail();
                    String sno = wns.getNo();
                    String swinner = wns.getWinner();
                    String spassword = wns.getPassword();
                    countWinsVerification+=1;


                    addWin(sname, semail, spassword, sno, swinner, swinmeth);
                }


                db.deleteSavedContent();
              //  Toast.makeText(getApplicationContext(),"Win updated",Toast.LENGTH_LONG).show();
            }
        }
      //

        //        ******slider***********
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("1", R.drawable.s1);
        file_maps.put("2",R.drawable.s2);
        file_maps.put("3", R.drawable.s3);
        file_maps.put("4", R.drawable.s4);

        for(String iname : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    //  .description(name)
                    .image(file_maps.get(iname))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);


            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", iname);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new ChildAnimationExample());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);


    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

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
                    .setTitleText("Cru.comm")
                    .setContentText("Are you sure you want to logout?")
                    .setCancelText("No,cancel please!")
                    .setConfirmText("Logout!")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            // reuse previous dialog instance, keep widget user state, reset them if you need
                            sDialog.setTitleText("Cru.comm")
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
                    .setTitleText("Cru.comm")
                    .setContentText("Are you sure you want to logout?")
                    .setCancelText("No,cancel please!")
                    .setConfirmText("Logout!")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            // reuse previous dialog instance, keep widget user state, reset them if you need
                            sDialog.setTitleText("Cru.comm")
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

    //check internet connection
    public boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {


            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {


            return false;
        }
        return false;
    }


    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void addWin(final String name, final String email, final String password, final String pnumber,final String winner, final String winmeth) {
        // Tag used to cancel the request
        String tag_string_req = "winning";

       // pDialog.setMessage("Adding your win...");
       // showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_WIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
             //   hideDialog();
                {
                    if(countWinsList == countWinsVerification)
                        Toast.makeText(getApplicationContext(), "Win has been added successfully!", Toast.LENGTH_SHORT).show();

                    // Launch login activity
                   /* Intent intent = new Intent(getApplicationContext(), WinActivity.class);
                    startActivity(intent);
                    finish();*/
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("pnumber", pnumber);
                params.put("winner", winner);
                params.put("winmeth", winmeth);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /*private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }*/
}
