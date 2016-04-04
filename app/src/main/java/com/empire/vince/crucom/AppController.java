package com.empire.vince.crucom;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by VinceGee on 1/25/2016.
 */
public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    public static final boolean DEBUG = true;
    public static final String INTENT_EXTRA_TASK_ID = "extraTaskId";
    public static final String INTENT_EXTRA_TASK_LIST_POSITION = "extraTaskListPosition";
    public static final String INTENT_EXTRA_TASK_TITLE = "extraTaskTitle";
    public static final String INTENT_EXTRA_NOTIFICATION = "extraNotification";

    public static final Gson gson = new Gson();

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        refWatcher = LeakCanary.install(this);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static RefWatcher getRefWatcher(Context context) {
        AppController application = (AppController) context.getApplicationContext();
        return application.refWatcher;
    }



    /**
     * Returns the default reminder date which is tomorrow at 9 am
     */
    public static Date getDefaultReminderDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 9);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }
}
