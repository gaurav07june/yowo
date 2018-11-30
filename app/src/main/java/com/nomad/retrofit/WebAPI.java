package com.nomad.retrofit;


import com.nomad.model.BookingResModel;
import com.nomad.model.CMSResponseModel;
import com.nomad.model.CateringPackageResModel;
import com.nomad.model.CheckAvailabilityResModel;
import com.nomad.model.CitySuburbResModel;
import com.nomad.model.GenericResponseModel;
import com.nomad.model.LocationListResModel;
import com.nomad.model.LoginResModel;
import com.nomad.model.MeetingSpacesResModel;
import com.nomad.model.MyVenueResponseModel;
import com.nomad.model.NotificationStatusResModel;
import com.nomad.model.Profile;
import com.nomad.model.VenueDetailResModel;
import com.nomad.model.VenueListingResModel;
import com.nomad.model.VenueSuggestionResModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface WebAPI {

    @POST("login")
    Call<GenericResponseModel<LoginResModel>> doSignIn(@Body RequestBody requestBody);

    @GET("5bbc84873200000f0027ecb8")
    Call<GenericResponseModel<CMSResponseModel>> getCMSContent();

    @GET("myprofile")
    Call<GenericResponseModel<Profile>> getProfileData(@Query("userId") int userId, @HeaderMap Map<String, String> headers);

    @POST("ChangePassword")
    Call<GenericResponseModel<Object>> updatePassword(@Body RequestBody requestBody, @HeaderMap Map<String, String> headers);

    @PUT("Notification")
    Call<GenericResponseModel<Object>> updateNotificationStatus(@Body RequestBody requestBody, @HeaderMap Map<String, String> headers);

    @GET("Notification")
    Call<GenericResponseModel<NotificationStatusResModel>> getNotificationStatus(@Query("userId") int userId, @HeaderMap Map<String, String> headers);



    @POST("5bbf4da434000036006fcf25")
    Call<GenericResponseModel<BookingResModel>> getUpcomingBookingList(@Body RequestBody requestBody);

    @POST("5bbf3dae34000050006fce92")
    Call<GenericResponseModel<BookingResModel>> getPastBookingList(@Body RequestBody requestBody);

    @POST("VenueList")
    Call<GenericResponseModel<VenueListingResModel>> getVenueList(@Body RequestBody requestBody, @HeaderMap Map<String, String> headers);

    @POST("MyVenue")
    Call<GenericResponseModel<Object>> toggleFavouriteVenue(@Body RequestBody requestBody, @HeaderMap Map<String, String> headers);

    @POST("LocationSuggestion")
    Call<GenericResponseModel<LocationListResModel>> getLocationList(@Body RequestBody requestBody, @HeaderMap Map<String, String> headers);


    @POST("VenueSuggestion")
    Call<GenericResponseModel<VenueSuggestionResModel>> getVenueSuggestionList(@Body RequestBody requestBody, @HeaderMap Map<String, String> headers);

    @GET("CitySuburb")
    Call<GenericResponseModel<CitySuburbResModel>> getCityList(@HeaderMap Map<String, String> headers);

    @GET("CitySuburb")
    Call<GenericResponseModel<CitySuburbResModel>> getSuburbList(@Query("cityId") int cityId, @HeaderMap Map<String, String> headers);


    @POST("venuedetail")
    Call<GenericResponseModel<VenueDetailResModel>> getVenueDetail(@Body RequestBody requestBody, @HeaderMap Map<String,  String> headers);

    @GET("MeetingRoom")
    Call<GenericResponseModel<MeetingSpacesResModel>> getMeetinSpaces(@Query("venueId") int venueId, @HeaderMap Map<String, String> headers);

    @GET("FoodPackage")
    Call<GenericResponseModel<CateringPackageResModel>> getCateringPackages(@Query("venueId") int venueId, @HeaderMap Map<String, String> headers);

    @POST("BookingList")
    Call<GenericResponseModel<BookingResModel>> getBookingList(@Body RequestBody requestBody, @HeaderMap Map<String, String> headers);

    @Multipart
    @POST("MyProfile")
    Call<Void> updateProfile(@Part MultipartBody.Part file,@PartMap Map<String, RequestBody> requestBodyMap, @HeaderMap Map<String, String> headers);

    @GET("MyVenue")
    Call<GenericResponseModel<MyVenueResponseModel>> getMyVenueList(@QueryMap Map<String, String> queryMap, @HeaderMap Map<String, String> headers);

    @POST("SeatCheckedIn")
    Call<GenericResponseModel<Object>> doSeatCheckIn(@Body RequestBody requestBody, @HeaderMap Map<String, String> headers);

    @PUT("SeatCheckedIn")
    Call<GenericResponseModel<Object>> doSeatCheckOut(@Query("userCheckInId") int userCheckInId, @HeaderMap Map<String, String> headers);

    @GET("meetingroomavailibility")
    Call<GenericResponseModel<CheckAvailabilityResModel>> checkMeetingRoomAvailablility(@QueryMap Map<String, String> queryMap,
                                                                                        @HeaderMap Map<String, String> headers);


}
