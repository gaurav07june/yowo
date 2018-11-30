package com.nomad.util;

public class AppConstants {

    public static final int MAX_AMENITY_COUNT=5;

    public interface GPS_LOCATION
    {
        int UPDATE_INTERVAL=10000;
        int UPDATE_DISTANCE=10;
    }

    public interface EXTRA
    {
        String ON_BOARDING_PAGES = "on_boarding_pages";

        String CMS_TYPE = "cms_type";
    }
    public interface API
    {
        String BASE_URL="http://www.demo2projects.com/ReworkAPI/api/";
        boolean STATUS_CODE_SUCCESS= true;
        int TOKEN_EXPIRED_CODE = 401;

    }
    public interface RETROFIT
    {
        int TIMEOUT=60;
    }
    public interface PREF
    {
        String DIRECTORY_INITIALIZED="pref_directory_initialized";
        String LOGGED_IN_CUSTOMER_DATA="pref_logged_in_customer_data";
        String CUSTOMER_TOKEN="pref_customer_token";
        String IS_GUEST = "prefs_is_guest";
    }

    public interface CONSTANTS{
        String PRIVACY_POLICY = "privacy_policy";
        String TERMS_AND_CONDITION = "terms_and_conditions";
        String ABOUT_US = "about_us";
    }
    public interface FIREBASE_CONFIG{
        // global topic to receive app wide push notifications
        public static final String TOPIC_GLOBAL = "global";

        // broadcast receiver intent filters
        public static final String REGISTRATION_COMPLETE = "registrationComplete";
        public static final String PUSH_NOTIFICATION = "pushNotification";

        // id to handle the notification in the notification tray
        public static final int NOTIFICATION_ID = 100;
        public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

        public static final String SHARED_PREF = "firebase_pref";


    }
    public static final int SPLASH_TIME_OUT =  3000;
    public static final int SPLASH_ANIM_TIME=400;

}
