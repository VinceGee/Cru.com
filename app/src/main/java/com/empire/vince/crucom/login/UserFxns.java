package com.empire.vince.crucom.login;

import android.content.Context;

import com.empire.vince.crucom.AppConfig;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VinceGee on 04/01/2016.
 */
public class UserFxns {

    private JSONParser jsonParser;

    private static String forpass_tag = "forpass";
    private static String chgpass_tag = "chgpass";

    // constructor
    public UserFxns(){
        jsonParser = new JSONParser();
    }

    /**
     * Function to change password
     **/

    public JSONObject chgPass( String nyowani, String email){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", chgpass_tag));
        params.add(new BasicNameValuePair("nyowani", nyowani));
        params.add(new BasicNameValuePair("email", email));
        //params.add(new BasicNameValuePair("kudhara", kudhara));

        JSONParser jsonParser = new JSONParser();
        return jsonParser.getJSONFromUrl(AppConfig.URL_CHGERESET, params);
    }

    /**
     * Function to reset the password
     **/

    public JSONObject forPass(String forgotpassword){
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("tag", forpass_tag));
        params.add(new BasicNameValuePair("forgotpassword", forgotpassword));

        JSONParser jsonParser = new JSONParser();
        return jsonParser.getJSONFromUrl(AppConfig.URL_CHGERESET, params);
    }

//VinceGee fxns
    /*public JSONObject changePassword(String nyowani, String kudhara){
        List<Pair<String, String>> params = new ArrayList<>();
        params.add(new Pair<>("tag", chgpass_tag));
        params.add(new Pair<>("nyowani", nyowani));
        params.add(new Pair<>("kudhara", kudhara));

        JSONParser jsonParser = new JSONParser();
        JSONObject json = jsonParser.getJSONFromUrl(AppConfig.URL_CHGERESET, params);
        return json;

    }*/

   /* public JSONObject changePassword(String nyowani, String kudhara){
        ContentValues params = new ContentValues();
        params.put("tag", chgpass_tag);
        params.put("nyowani", nyowani);
        params.put("kudhara", kudhara);

        JSONParser jsonParser = new JSONParser();
        JSONObject json = jsonParser.getJSONFromUrl(AppConfig.URL_CHGERESET, params);
        return json;
    }*/


    ////////////////////////////OLD STUFF
   /* *//**
     * Function to change password
     **//*

    public JSONObject chgPass(String newpas, String email){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", chgpass_tag));

        params.add(new BasicNameValuePair("newpas", newpas));
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.getJSONFromUrl(chgpassURL, params);
        return json;
    }





    *//**
     * Function to reset the password
     **//*

    public JSONObject forPass(String forgotpassword){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", forpass_tag));
        params.add(new BasicNameValuePair("forgotpassword", forgotpassword));
        JSONObject json = jsonParser.getJSONFromUrl(forpassURL, params);
        return json;
    }
*/

}
