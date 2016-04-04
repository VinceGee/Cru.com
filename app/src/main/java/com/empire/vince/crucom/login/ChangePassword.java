package com.empire.vince.crucom.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.empire.vince.crucom.MainActivity;
import com.empire.vince.crucom.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import butterknife.InjectView;

/**
 * Created by VinceGee on 04/01/2016.
 */
public class ChangePassword extends Activity{
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static final String TAG = "ChangePassword";


    //EditText newpass;
    TextView alert;
    Button changepass;
    Button cancel;

    @InjectView(R.id.current_password)    EditText _curpasswordText;
    @InjectView(R.id.newpass)        EditText _newpasswordText;
    @InjectView(R.id.conf_newpass)       EditText _confpasswordText;
    @InjectView(R.id.btn_update_pwd)      Button _updateButton;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_changepassword);

        _curpasswordText = (EditText) findViewById(R.id.current_password);
        _newpasswordText = (EditText) findViewById(R.id.newpass);
        _confpasswordText = (EditText) findViewById(R.id.conf_newpass);

        _updateButton = (Button) findViewById(R.id.btn_update_pwd);
        _updateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               // NetAsync(v);
                new ProcessRegister().execute();
            }
        });



       /* _newpasswordText = (EditText) findViewById(R.id.newpass);
        //alert = (TextView) findViewById(R.id.alertpass);
        _updateButton = (Button) findViewById(R.id.btn_update_pwd);

        _updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new changePassword().execute();
            }
        });*/

    }

    public void NetAsync(View view){
        new NetCheck().execute();
    }

    /*private class changePassword extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        String nyowani,email;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SQLiteHandler db = new SQLiteHandler(getApplicationContext());

            HashMap<String,String> user = new HashMap<String, String>();
            user = db.getUserDetails();

            nyowani = _newpasswordText.getText().toString();
            email = user.get("email");

            pDialog = new ProgressDialog(ChangePassword.this);
            pDialog.setTitle("Merchant");
            pDialog.setMessage("Changing password ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            UserFxns userFunction = new UserFxns();
            JSONObject json = userFunction.chgPass(nyowani, email);
            Log.d("Button", "Register");
            return json;

        }


        @Override
        protected void onPostExecute(JSONObject json) {

            try {
                if (json.getString(KEY_SUCCESS) != null) {
                   // alert.setText("");
                    String res = json.getString(KEY_SUCCESS);
                    String red = json.getString(KEY_ERROR);


                    if (Integer.parseInt(res) == 1) {
                        *//**
                         * Dismiss the process dialog
                         **//*
                        pDialog.dismiss();
                        Toast.makeText(getBaseContext(),"Password Updated successfully",Toast.LENGTH_LONG).show();


                    } else if (Integer.parseInt(red) == 2) {
                        pDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Failed to update password", Toast.LENGTH_LONG).show();
                    } else {
                        pDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Error occured in changing password\nPlease try again.", Toast.LENGTH_LONG).show();

                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();


            }

        }
    }*/


    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(ChangePassword.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args){
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                nDialog.dismiss();
                new ProcessRegister().execute();
            }
            else{
                nDialog.dismiss();
                //alert.setText("Error in Network Connection");
                Toast.makeText(getApplicationContext(), "Error in Network Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {


        private ProgressDialog pDialog;

        String newpas,email;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SQLiteHandler db = new SQLiteHandler(getApplicationContext());

            HashMap<String,String> user = new HashMap<String, String>();
            user = db.getUserDetails();

            newpas = _newpasswordText.getText().toString();
            email = user.get("email");

            pDialog = new ProgressDialog(ChangePassword.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {


            UserFxns userFunction = new UserFxns();
            JSONObject json = userFunction.chgPass(newpas, email);
            Log.d("Button", "Register");
            return json;


        }


        @Override
        protected void onPostExecute(JSONObject json) {

            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    //alert.setText("");
                    String res = json.getString(KEY_SUCCESS);
                    String red = json.getString(KEY_ERROR);


                    if (Integer.parseInt(res) == 1) {
                        /**
                         * Dismiss the process dialog
                         **/
                        pDialog.dismiss();
                        Toast.makeText(getBaseContext(),"Password Updated successfully",Toast.LENGTH_LONG).show();

                    } else if (Integer.parseInt(red) == 2) {
                        pDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Invalid password", Toast.LENGTH_LONG).show();                    }
                    else {
                        pDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Error occured in changing password\nPlease try again.", Toast.LENGTH_LONG).show();
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();


            }

        }
    }

    /*public boolean validate() {
        boolean valid = true;

        String cur_passwd = _curpasswordText.getText().toString();
        String new_passwd = _newpasswordText.getText().toString();
        String conf_passwd = _confpasswordText.getText().toString();

        if (cur_passwd.isEmpty() || cur_passwd.length() < 4 || cur_passwd.length() > 10) {
            _curpasswordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _curpasswordText.setError(null);
        }

        if (new_passwd.isEmpty() || new_passwd.length() < 4 || new_passwd.length() > 10)
        {
            _newpasswordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _newpasswordText.setError(null);
        }

        if (conf_passwd.isEmpty() || conf_passwd.length() < 4 || conf_passwd.length() > 10)
        {
            _confpasswordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _confpasswordText.setError(null);
        }

        if (new_passwd.equals(conf_passwd)){
           // finish();
        }
        else{
            _confpasswordText.setError("Password mismatch.");
            valid = false;
        }
        return valid;
    }

    public void changePwd() {
        Log.d(TAG, "Update");
        if (!validate()) {
            onChangeFailed();
            return;
        }

        new changePassword().execute();

    }

    public void onChangeFailed() {
        Toast.makeText(getBaseContext(), "Failed to update password.", Toast.LENGTH_LONG).show();
        _updateButton.setEnabled(true);
    }*/
}
