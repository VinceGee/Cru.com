package com.empire.vince.crucom.win;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.empire.vince.crucom.AppController;
import com.empire.vince.crucom.R;
import com.empire.vince.crucom.AppConfig;
import com.empire.vince.crucom.login.SQLiteHandler;
import com.empire.vince.crucom.login.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;

/**
 * Created by VinceGee on 03/25/2016.
 */
public class WinActivity extends Activity {
    private static final String TAG = WinActivity.class.getSimpleName();
    @InjectView(R.id.win_name) EditText win_name;
    @InjectView(R.id.win_spinner) Spinner win_spinner;
    @InjectView(R.id.win_email) EditText win_email;
    @InjectView(R.id.win_number) EditText win_number;
    @InjectView(R.id.win_password) EditText win_password;

    @InjectView(R.id.btnWin) Button btnWin;

    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    String winmeth;
    String winner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
        //ButterKnife.inject(this);

        win_name = (EditText) findViewById(R.id.win_name);
        win_email = (EditText) findViewById(R.id.win_email);
        win_password = (EditText) findViewById(R.id.win_password);
        win_number = (EditText) findViewById(R.id.win_number);
        win_spinner = (Spinner) findViewById(R.id.win_spinner);

        btnWin = (Button) findViewById(R.id.btnWin);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        final String username = user.get("name");



        // Register Button Click event
        btnWin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = win_name.getText().toString().trim();
                String email = win_email.getText().toString().trim();
                String password = win_password.getText().toString().trim();
                String pnumber = win_number.getText().toString().trim();
                String winner = username.trim();
                String winmeth = win_spinner.getSelectedItem().toString().trim();


                if (!name.isEmpty() && !email.isEmpty() && !pnumber.isEmpty() && !password.isEmpty() && !winmeth.isEmpty()) {
                    addWin(name,email,password, pnumber, winner, winmeth);
                } else {
                    Toast.makeText(getApplicationContext(), "You have to fill in all details", Toast.LENGTH_LONG).show();
                }
            }
        });

        init();
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void addWin(final String name, final String email, final String password, final String pnumber,final String winner, final String winmeth) {
        // Tag used to cancel the request
        String tag_string_req = "winning";

        pDialog.setMessage("Adding your win...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_WIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                     {
                         Toast.makeText(getApplicationContext(), "Win has been added successfully!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(getApplicationContext(), WinActivity.class);
                        startActivity(intent);
                        finish();
                    }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void init() {
        win_spinner = (Spinner) findViewById(R.id.win_spinner);

        //Get the options from the string array in strings.
        ArrayAdapter<CharSequence> spinner = ArrayAdapter.createFromResource(this, R.array.win_methods, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        spinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        win_spinner.setAdapter(spinner);
        win_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                winmeth = (String) win_spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "Please Choose a win method", Toast.LENGTH_LONG).show();
            }
        });

    }
}
