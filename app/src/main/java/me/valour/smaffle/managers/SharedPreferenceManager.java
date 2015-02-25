package me.valour.smaffle.managers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by alice on 2/24/15.
 */
public class SharedPreferenceManager {

    private final SharedPreferences settings;
    private final Activity act;
    private final String app = "me.valour.smaffle";

    private String aii;
    private String auth_token;

    private static SharedPreferenceManager ourInstance = null;

    public static SharedPreferenceManager getInstance(Activity act){
        if(ourInstance==null){
            ourInstance = new SharedPreferenceManager(act);
        }

        return ourInstance;
    }

    private SharedPreferenceManager(Activity act){
        this.act = act;
        settings = act.getSharedPreferences(app, Context.MODE_PRIVATE);

        aii = settings.getString("aii", null);
        auth_token = settings.getString("token", null);
    }

    public boolean hasToken(){
        return auth_token != null;
    }

    public String getToken(){
        return auth_token;
    }

    public void register(){
        final String aii = generateID();
        RequestQueue queue = Volley.newRequestQueue(this.act);

        try {
            JSONObject obj = new JSONObject();
            obj.put("aii", aii);

            APIManager.getInstance(this.act).post("/register", obj,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Response", response.toString());
                            try {
                                String token = response.getString("auth_token");
                                saveCredentials(aii, token);
                            } catch (JSONException ex){

                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub

                        }
                    });
        } catch (JSONException ex){

        }
    }

    private String generateID() {

        // use the ANDROID_ID constant, generated at the first device boot
        String deviceId = Settings.Secure.getString(this.act.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        // in case known problems are occured
        if ("9774d56d682e549c".equals(deviceId) || deviceId == null) {

            // get a unique deviceID like IMEI for GSM or ESN for CDMA phones
            // don't forget:
            //    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
            deviceId = ((TelephonyManager) this.act.getApplicationContext()
                    .getSystemService(Context.TELEPHONY_SERVICE))
                    .getDeviceId();

            // if nothing else works, generate a random number
            if (deviceId == null) {

                Random tmpRand = new Random();
                deviceId = String.valueOf(tmpRand.nextLong());
            }

        }

        // any value is hashed to have consistent format
        return getHash(deviceId);
    }

    // generates a SHA-1 hash for any string
    private String getHash(String stringToHash) {

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] result = null;

        try {
            result = digest.digest(stringToHash.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();

        for (byte b : result)
        {
            sb.append(String.format("%02X", b));
        }

        String messageDigest = sb.toString();
        return messageDigest;
    }


    private void saveCredentials(String aii, String auth_token){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("aii", aii);
        editor.putString("auth_token", auth_token);
        editor.apply();
        this.aii = aii;
        this.auth_token = auth_token;
    }

}
