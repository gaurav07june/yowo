package com.nomad.util;

public class Constants {

    public static final int MAX_AMENITY_COUNT=5;

    public static final int TOOLBAR_COLOR_ANIM_DURATION=500;

    public static final int AMENITY_GRID_COLUMNS=4;
    public static final int REVEAL_ANIM_DURATION=300;
    public interface GPS_LOCATION
    {
        int UPDATE_INTERVAL=10000;
        int UPDATE_DISTANCE=10;
    }

    public interface PARAMS{
        String MEETING_ROOM_ID = "meetingRoomID";
        String BOOKING_DATE = "bookingDate";
        String BOOKING_START_TIME = "bookingStartTime";
        String BOOKING_END_TIME = "bookingEndTime";
    }

    public interface EXTRA
    {
        String ON_BOARDING_PAGES = "extra_on_boarding_pages";
        String COVER_IMAGES = "extra_cover_images";
        String COVER_IMAGE_POSITION = "extra_cover_image_position";
        String CITY_EXTRA= "extra_city";
        String VENUE_DETAIL_EXTRA = "extra_venue_detail";
        String IS_CURRENT_LOCATION_REQ = "extra_is_current_location";
        String SEARCH_ID = "extra_searchId";
        String AVAILABLE_DAYS = "extra_available_days";
        String IS_FAVOURITE = "extra_is_favourite";
        String MEETING_SPACE_AVAILABLE = "extra_meeting_space_available";
        String START_TIME = "extra_start_time";
        String END_TIME = "extra_end_time";
        String FILTER_DATA = "extra_filter_data";
        String EMAIL_NOTI_TYPE = "email";
        String PUSH_NOTI_TYPE = "push";
    }

    public interface FRAG{
        String VENUE_FRAGMENT = "tag_venue_fragment";
        String MY_BOOKING_FRAGMENT ="tag_my_booking_fragment";
        String FAVOURITE_FRAGMENT = "tag_favourite_fragment";
    }

    public interface API
    {
        String BASE_URL="http://www.demo2projects.com/ReworkAPI/api/";
        boolean STATUS_CODE_SUCCESS= true;
        int TOKEN_EXPIRED_CODE = 401;
        int TOKEN_SUCCESS_CODE = 200;

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
        int MARRIAGE_BOOKING_REQ = 71;
        int RESERVE_PRODUCT_REQ = 11;
        int UPCOMING_MARRIAGE_BOOKING_REQ = 31;
        int PAST_MARRIAGE_BOOKING_REQ = 32;
        int UPCOMING_PRODUCT_BOOKING_REQ = 51;
        int PAST_PRODUCT_BOOKING_REQ = 52;
        int UPCOMING_GENERAL_BOOKING_REQ = 61;
        int PAST_GENERAL_BOOKING_REQ = 62;

    }
    public interface EXTRAS
    {
        String SERVICE_CATEGORY_DATA="extra_service_category_data";
        String SALON_DATA="extra_salon_data";
        String SELECTED_SERVICES="extra_selected_services";
        String SELECTED_EMPLOYEE="extra_selected_employee";
        String SELECTED_DATE="extra_selected_date";
        String EMPLOYEE_WORKING_DAYS="extra_employee_working_days";
        String EMAIL_ADDRESS = "extra_email_address";
        String SELECTED_TIME_SLOT="extra_selected_time_slot";
        String AVAILABLE_TIME_SLOTS = "extra_available_time_slots";
        String SALON_IMAGE_URL = "extra_salon_image_url";
        String SALON_ID = "extra_salon_id";
        String MARRIAGE_BOOKING_MSG = "extra_marriage_booking_msg";
        String RESERVE_PRODUCT_MSG = "extra_res_pro_msg";
        String REQ_FROM_PAGE = "extra_request_code";
        String BOOKING_ID = "extra_booking_id";
        String RESERVED_PRODUCT_DETAIL = "extra_reserve_product";
        String PICK_UP_DATE = "extra_pickup_date";
        String SUBMIT_PICK_DATE = "extra_submit_date";

    }
    public interface  SERVICE_BOOKING
    {
        int TIME_SLOT_DIFF=30;
    }
    public static final int SPLASH_TIME_OUT =  3000;
    public static final int SPLASH_ANIM_TIME=400;

}
