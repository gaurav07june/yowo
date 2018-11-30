package com.nomad.util;

import android.content.Context;
import com.google.gson.Gson;
import com.nomad.model.City;
import com.nomad.model.Profile;


public class AppGlobalData {
    private static final AppGlobalData ourInstance = new AppGlobalData();
    private boolean isGuest = false;

    public boolean isGuest() {
        return isGuest;
    }

    public void setGuest(boolean guest) {
        isGuest = guest;
    }

    public static AppGlobalData getInstance() {
        return ourInstance;
    }

    private AppGlobalData() {
    }

    private City userGpsCity=null;
    private Profile loggedInCustomer;
    private String token = "";


    public City getUserGpsCity() {
        return userGpsCity;
    }

    public void setUserGpsCity(City userGpsCity) {
        this.userGpsCity = userGpsCity;
    }

    public Profile getLoggedInCustomer() {
        return loggedInCustomer;
    }

    public void setLoggedInCustomer(Profile loggedInCustomer) {
        this.loggedInCustomer = loggedInCustomer;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Clear all the login data after user press logout.
     * @param context
     */
    public void logOut(Context context)
    {
      //  LoginManager.getInstance().logOut();
        Prefs.clearAll(context);
    }

    /**
     * Set login data via Preference.
     * @param context
     * @return
     */
    public boolean setFromPrefs(Context context)
    {
        loggedInCustomer=null;
        token=null;
        isGuest = false;
        String customerData =  Prefs.getString(context, Constants.PREF.LOGGED_IN_CUSTOMER_DATA,null);
        token =  Prefs.getString(context, Constants.PREF.CUSTOMER_TOKEN,null);
        isGuest = Prefs.getBoolean(context, Constants.PREF.IS_GUEST, false);
        if (customerData!=null)
            loggedInCustomer=new Gson().fromJson(customerData,Profile.class);

        return  loggedInCustomer != null && token != null && !isGuest;
    }

    /**
     * Save login data to prefs.
     * @param context
     */
    public void saveToPrefs(Context context) {
        if (loggedInCustomer!=null)
        Prefs.saveString(context, Constants.PREF.LOGGED_IN_CUSTOMER_DATA,new Gson().toJson(loggedInCustomer));
        Prefs.saveBoolean(context, Constants.PREF.IS_GUEST, isGuest);
        if (token!=null)
            Prefs.saveString(context, Constants.PREF.CUSTOMER_TOKEN,token);

    }
}
