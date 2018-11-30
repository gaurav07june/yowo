package com.nomad.util;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class InternetCheck extends AsyncTask<Void, Void, Boolean> {


    private Activity activity;
    private InternetCheckListener listener;

    public InternetCheck(Activity x){

        activity= x;

    }

    @Override
    protected Boolean doInBackground(Void... params) {


        Boolean b =InternetUtils.isInternetAvailable();


        return b;
    }

    @Override
    protected void onPostExecute(Boolean b) {
        listener.onComplete(b);
        super.onPostExecute(b);
    }

    public void isInternetConnectionAvailable(InternetCheckListener x){
        listener=x;
        execute();
    }

    /**
     * Returns true if internet is available else returns false.
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
    private boolean hasInternetAccess() {
        if (isNetworkAvailable()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://clients3.google.com/generate_204").openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 204 &&
                        urlc.getContentLength() == 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("TAG", "No network available!");
        }
        return false;
    }

    public interface InternetCheckListener{
        void onComplete(boolean connected);
    }

}