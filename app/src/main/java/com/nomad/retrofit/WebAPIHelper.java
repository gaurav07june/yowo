package com.nomad.retrofit;


import com.google.gson.Gson;
import com.nomad.model.BookingReqModel;
import com.nomad.model.BookingResModel;
import com.nomad.model.CMSResponseModel;
import com.nomad.model.CateringPackageResModel;
import com.nomad.model.CheckAvailabilityResModel;
import com.nomad.model.CitySuburbResModel;
import com.nomad.model.GenericResponseModel;
import com.nomad.model.LocationListReqModel;
import com.nomad.model.LocationListResModel;
import com.nomad.model.MeetingSpacesResModel;
import com.nomad.model.NotificationStatusResModel;
import com.nomad.model.Profile;
import com.nomad.model.SeatCheckInReqModel;
import com.nomad.model.ToggleFavouriteReqModel;
import com.nomad.model.VenueDetailReqModel;
import com.nomad.model.VenueDetailResModel;
import com.nomad.model.VenueListingReqModel;
import com.nomad.model.VenueListingResModel;
import com.nomad.model.VenueSuggestionReqModel;
import com.nomad.model.VenueSuggestionResModel;
import com.nomad.util.AppGlobalData;
import com.nomad.util.Constants;
import com.nomad.model.LoginReqModel;
import com.nomad.model.LoginResModel;
import com.nomad.model.MyVenueResponseModel;
import com.nomad.model.NotiSettingReqModel;
import com.nomad.model.UpdatePasswordReqModel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;

public class WebAPIHelper {
    private static WebAPIHelper ourInstance = new WebAPIHelper();
    public static WebAPIHelper getInstance() {
        return ourInstance;
    }
    private WebAPIHelper() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(Constants.RETROFIT.TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(Constants.RETROFIT.TIMEOUT, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API.BASE_URL)
                .client(okHttpClient)

                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitApi = retrofit.create(WebAPI.class);
    }
    WebAPI retrofitApi = null;
    public void doSignin(LoginReqModel model, Callback<GenericResponseModel<LoginResModel>> responseCallback) {
        if (retrofitApi != null && model != null) {
            Call<GenericResponseModel<LoginResModel>> call = retrofitApi.doSignIn(getRequestBody(new Gson().toJson(model)));
             call.enqueue(responseCallback);
        }
    }

    public void getCMSContent(Callback<GenericResponseModel<CMSResponseModel>> responseCallback) {
        if (retrofitApi != null) {
            Call<GenericResponseModel<CMSResponseModel>> call = retrofitApi.getCMSContent();
            call.enqueue(responseCallback);
        }
    }
    public void getProfileData(Callback<GenericResponseModel<Profile>> responseCallback) {
        if (retrofitApi != null) {
            Call<GenericResponseModel<Profile>> call = retrofitApi.getProfileData(AppGlobalData.getInstance().getLoggedInCustomer().getId(),
                    getCommonHeader());
            call.enqueue(responseCallback);
        }
    }
    public void updatePassword(UpdatePasswordReqModel model, Callback<GenericResponseModel<Object>> responseCallback) {
        if (retrofitApi != null && model != null) {
            Call<GenericResponseModel<Object>> call = retrofitApi.updatePassword(getRequestBody(new Gson().toJson(model)), getCommonHeader());
            call.enqueue(responseCallback);
        }
    }

    public void updateNotificationStatus(NotiSettingReqModel model, Callback<GenericResponseModel<Object>> responseCallback){
        if (retrofitApi != null && model != null) {
            Call<GenericResponseModel<Object>> call = retrofitApi.updateNotificationStatus(getRequestBody(new Gson().toJson(model)), getCommonHeader());
            call.enqueue(responseCallback);
        }
    }

    public void getNotificationStatus(Callback<GenericResponseModel<NotificationStatusResModel>> responseCallback, int userId){
        if (retrofitApi != null) {
            Call<GenericResponseModel<NotificationStatusResModel>> call =
                    retrofitApi.getNotificationStatus(userId, getCommonHeader());
            call.enqueue(responseCallback);
        }
    }

    public void getUpcomingBookingList(BookingReqModel model, Callback<GenericResponseModel<BookingResModel>> responseCallback){
        if (retrofitApi != null && model != null) {

            Call<GenericResponseModel<BookingResModel>> call = retrofitApi.getUpcomingBookingList(getRequestBody(new Gson().toJson(model)));
            call.enqueue(responseCallback);
        }
    }
    public void getPastBookingList(BookingReqModel model, Callback<GenericResponseModel<BookingResModel>> responseCallback){
        if (retrofitApi != null && model != null) {
            Call<GenericResponseModel<BookingResModel>> call = retrofitApi.getPastBookingList(getRequestBody(new Gson().toJson(model)));
            call.enqueue(responseCallback);
        }
    }
    public void getVenueList(VenueListingReqModel model, Callback<GenericResponseModel<VenueListingResModel>> responseCallback){
        if (retrofitApi != null && model != null) {


            Call<GenericResponseModel<VenueListingResModel>> call =
                    retrofitApi.getVenueList(getRequestBody(new Gson().toJson(model)), getCommonHeader());
            call.enqueue(responseCallback);
        }
    }

    public void toggleFavouriteVenue(ToggleFavouriteReqModel model, Callback<GenericResponseModel<Object>> responseCallback){
        if (retrofitApi != null && model != null) {
            Call<GenericResponseModel<Object>> call = retrofitApi.toggleFavouriteVenue(getRequestBody(new Gson().toJson(model)), getCommonHeader());
            call.enqueue(responseCallback);
        }
    }

    public void getLocationList(LocationListReqModel model, Callback<GenericResponseModel<LocationListResModel>> responseModelCallback){
        if (retrofitApi != null && model != null) {


            Call<GenericResponseModel<LocationListResModel>> call =
                    retrofitApi.getLocationList(getRequestBody(new Gson().toJson(model)), getCommonHeader());
            call.enqueue(responseModelCallback);

        }
    }

    public void getVenueSuggestionList(VenueSuggestionReqModel model, Callback<GenericResponseModel<VenueSuggestionResModel>> responseModelCallback){
        if (retrofitApi != null && model != null) {


            Call<GenericResponseModel<VenueSuggestionResModel>> call =
                    retrofitApi.getVenueSuggestionList(getRequestBody(new Gson().toJson(model)), getCommonHeader());
            call.enqueue(responseModelCallback);

        }
    }
    public void getCityList(Callback<GenericResponseModel<CitySuburbResModel>> responseModelCallback){
        if (retrofitApi != null) {
            Call<GenericResponseModel<CitySuburbResModel>> call =
                    retrofitApi.getCityList(getCommonHeader());
            call.enqueue(responseModelCallback);
        }
    }

    public void getSuburbList(Callback<GenericResponseModel<CitySuburbResModel>> responseModelCallback, int cityId){
        if (retrofitApi != null){
            Call<GenericResponseModel<CitySuburbResModel>> call =
                    retrofitApi.getSuburbList(cityId, getCommonHeader());
            call.enqueue(responseModelCallback);
        }
    }

    public void getVenueDetail(VenueDetailReqModel model, Callback<GenericResponseModel<VenueDetailResModel>> responseModelCallback){
        if (retrofitApi != null && model != null) {


            Call<GenericResponseModel<VenueDetailResModel>> call =
                    retrofitApi.getVenueDetail(getRequestBody(new Gson().toJson(model)), getCommonHeader());
            call.enqueue(responseModelCallback);

        }
    }
    public void getMeetingSpaces(Callback<GenericResponseModel<MeetingSpacesResModel>> responseModelCallback, int venueId){
        if (retrofitApi != null){
            Call<GenericResponseModel<MeetingSpacesResModel>> call =
                    retrofitApi.getMeetinSpaces(venueId, getCommonHeader());
            call.enqueue(responseModelCallback);
        }
    }

    public void getCateringPackeges(Callback<GenericResponseModel<CateringPackageResModel>> responseModelCallback, int venueId){
        if (retrofitApi != null){
            Call<GenericResponseModel<CateringPackageResModel>> call =
                    retrofitApi.getCateringPackages(venueId, getCommonHeader());
            call.enqueue(responseModelCallback);
        }
    }

    public void getBookingList(BookingReqModel model, Callback<GenericResponseModel<BookingResModel>> responseModelCallback){
        if (retrofitApi != null && model != null) {


            Call<GenericResponseModel<BookingResModel>> call =
                    retrofitApi.getBookingList(getRequestBody(new Gson().toJson(model)), getCommonHeader());
            call.enqueue(responseModelCallback);

        }
    }

    public void getMyVenueList(Callback<GenericResponseModel<MyVenueResponseModel>> responseCallback, Map<String, String> queryMap){
        if (retrofitApi != null) {

            Call<GenericResponseModel<MyVenueResponseModel>> call = retrofitApi.getMyVenueList(queryMap, getCommonHeader());
            call.enqueue(responseCallback);
        }
    }

    public void doSeatCheckIn(SeatCheckInReqModel model, Callback<GenericResponseModel<Object>> responseModelCallback){
        if (retrofitApi != null && model != null) {


            Call<GenericResponseModel<Object>> call =
                    retrofitApi.doSeatCheckIn(getRequestBody(new Gson().toJson(model)), getCommonHeader());
            call.enqueue(responseModelCallback);

        }
    }

    public void doSeatCheckOut(Callback<GenericResponseModel<Object>> responseCallback, int checkedInId){
        if (retrofitApi != null){
            Call<GenericResponseModel<Object>> call = retrofitApi.doSeatCheckOut(checkedInId, getCommonHeader());
            call.enqueue(responseCallback);
        }
    }

    public void updateProfile(Callback<Void> responseCallBack, MultipartBody.Part fileToupload, Map<String, RequestBody> requestBodyMap ){
        if (retrofitApi != null){
            Call<Void> call = retrofitApi.updateProfile(fileToupload, requestBodyMap, getMultiPartHeader());
            call.enqueue(responseCallBack);
        }

    }

    public void checkMeetingRoomAvailability(Callback<GenericResponseModel<CheckAvailabilityResModel>> responseCallBack, Map<String, String> queryMap){
        if (retrofitApi != null){
            Call<GenericResponseModel<CheckAvailabilityResModel>> call =
                    retrofitApi.checkMeetingRoomAvailablility(queryMap, getCommonHeader());
            call.enqueue(responseCallBack);
        }
    }


    private RequestBody getRequestBody(String json) {
        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
    }
    private Map<String , String> getCommonHeader(){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        headerMap.put("Authorization", AppGlobalData.getInstance().getToken());
        return headerMap;
    }
    private Map<String , String> getMultiPartHeader(){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", AppGlobalData.getInstance().getToken());
        return headerMap;
    }


}
