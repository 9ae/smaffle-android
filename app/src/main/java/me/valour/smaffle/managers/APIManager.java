package me.valour.smaffle.managers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by alice on 2/24/15.
 */
public class APIManager {

    public static final String host = "http://localhost:9900";

    private RequestQueue queue;
    private static Context ctx;

    private static APIManager ourInstance = null;

    public static APIManager getInstance(Context context) {
        if(ourInstance==null){
            ourInstance = new APIManager(context);
        }
        return ourInstance;
    }

    private APIManager(Context context) {

        ctx = context;
        queue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (queue == null) {
            queue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return queue;
    }

    public void post(String path, JSONObject params, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener){
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, host+path, params,
                responseListener, errorListener);
        queue.add(jsObjRequest);
    };
}
