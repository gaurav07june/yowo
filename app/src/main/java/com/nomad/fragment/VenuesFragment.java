package com.nomad.fragment;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nomad.R;
import com.nomad.activity.BookMeetingSpaceActivity;
import com.nomad.activity.CitySelectActivity;
import com.nomad.activity.FilterActivity;
import com.nomad.activity.NetworkActivity;
import com.nomad.activity.VenueDetailActivity;
import com.nomad.adapter.VenueListAdapter;
import com.nomad.customview.AppBottomNavigationView;
import com.nomad.customview.LikeButton;
import com.nomad.customview.VenueCheckoutDialog;
import com.nomad.customview.VenueSearchBar;
import com.nomad.databinding.FragmentVenuesBinding;
import com.nomad.listener.DashboardFragmentListener;
import com.nomad.model.CheckedInVenue;
import com.nomad.model.City;
import com.nomad.model.GenericResponseModel;
import com.nomad.model.Profile;
import com.nomad.model.SeatCheckInReqModel;
import com.nomad.model.ToggleFavouriteReqModel;
import com.nomad.model.Venue;
import com.nomad.model.VenueFilterData;
import com.nomad.model.VenueListingReqModel;
import com.nomad.model.VenueListingResModel;
import com.nomad.model.VenueMapModel;
import com.nomad.model.VenueSuggestionReqModel;
import com.nomad.model.VenueSuggestionResModel;
import com.nomad.retrofit.WebAPIHelper;
import com.nomad.util.AppCommons;
import com.nomad.util.AppGlobalData;
import com.nomad.util.Constants;
import com.nomad.util.DrawableUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static android.app.Activity.RESULT_OK;


public class VenuesFragment extends BaseFragment implements VenueSearchBar.VenueSearchListener, View.OnClickListener,
        OnMapReadyCallback,GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener , VenueListAdapter.VenueListListener  {

    private DashboardFragmentListener mListener = null;
    private VenueListAdapter venueListAdapter;
    private List<Venue> venueList;
    private static final int VENUE_LIST_REQ_CODE = 61;
    private static final int TOGGLE_FAV_REQ_CODE = 62;
    private static final int FILTER_ACTIVITY_REQ_CODE = 999;
    private static final int PROFILE_DATA_REQ_CODE = 21;

    private static final int CHECKED_IN_VENUE_REQ_CODE = 55;

    private static final int SEAT_CHECK_IN_REQ_CODE = 31;
    private static final int SEAT_CHECK_OUT_REQ_CODE = 32;


    SupportMapFragment mapFragment;
    private Marker selectedMarker = null;
    ToggleFavouriteReqModel toggleFavouriteReqModel;
    private int selectedPosition;
    private VenueSuggestionReqModel venueSuggestionReqModel = new VenueSuggestionReqModel();
    private static final int VENUE_SUGGESTION_REQ_CODE = 12;

    private static final int CURRENT_LOCATION_REQ_CODE = 0;
    VenueListingReqModel venueListingReqModel = new VenueListingReqModel();
    VenueFilterData venueFilterData;
    String availableDaysString = null;
    GoogleMap map;
    private Observer venueObserver;
    private int clickedVenue = -1;
    private Profile profile;
    private List<Marker> markerList = new ArrayList<>();
    private City city;
    private SeatCheckInReqModel seatCheckInReqModel = new SeatCheckInReqModel();
    private int selectedVenuePosition = -1;
    private int checkedInVenuePosition = selectedVenuePosition;
    CheckedInVenue checkedInVenue = null;
    public static Observer suburbObserver;
    private static final int CHECKED_IN_VENUE_DETAIL_FOR_CHECK_OUT = 44;



    public VenuesFragment() {
        // Required empty public constructor
    }

    public static VenuesFragment newInstance() {
        VenuesFragment fragment = new VenuesFragment();
        Bundle args = new Bundle();
  /*      args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
       /*     mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
        }
    }

    private FragmentVenuesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_venues, container, false);
        binding = DataBindingUtil.bind(view);
        if (mapFragment == null){
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(this);
        }
        getChildFragmentManager().beginTransaction().replace(R.id.map_fragment_container, mapFragment).commit();
        return view;
    }


    @Override
    public void setViews() {
        checkInternetAndHitApi(PROFILE_DATA_REQ_CODE);
        venueObserver = new Observer() {
            @Override
            public void update(Observable o, Object newValue) {
                venueListAdapter.notifyItemChanged(clickedVenue);
            }
        };
        suburbObserver = new Observer() {
            @Override
            public void update(Observable o, Object arg) {

            }
        };
        binding.navMenuBtn.setOnClickListener(this);
        binding.cityTv.setOnClickListener(this);
        binding.layVenueDesc.imgClose.setOnClickListener(this);
        binding.venueSearchBar.setVenueSearchListener(this);
        binding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int verticalThreshHold = binding.appBarLayout.getHeight() - binding.toolbar.getHeight();
                if (Math.abs(verticalOffset) >= verticalThreshHold && !binding.toolbar.isActivated()) {
                    //Collapsed
                    binding.toolbar.setActivated(true);
                    binding.venueSearchBar.toggleLocationBtn(true);


                } else if (Math.abs(verticalOffset) < verticalThreshHold && binding.toolbar.isActivated()) {
                    //Expanding
                    binding.toolbar.setActivated(false);
                    binding.venueSearchBar.toggleLocationBtn(false);
                }
            }
        });

        venueListAdapter = new VenueListAdapter(getActivity(),this);
        binding.venueListRv.setAdapter(venueListAdapter);
        binding.venueListRv.setItemAnimator(null);
        binding.venueListRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    }

    public void updateView(){
        if (profile != null){
            city = AppGlobalData.getInstance().getLoggedInCustomer().getCity();
            city.setCurrentLocation(false);
            venueFilterData = AppGlobalData.getInstance().getLoggedInCustomer().getFilterData();
            binding.cityTv.setText(city.getName());
            venueListingReqModel.setUserId(AppGlobalData.getInstance().getLoggedInCustomer().getId());
            venueListingReqModel.setSearchId(city.getID());
            binding.venueSearchBar.selectFilter(AppGlobalData.getInstance().getLoggedInCustomer().isFilterSelected());
            venueListingReqModel.setLocationType(city.getType());
            venueListingReqModel.setLatitude(String.valueOf(city.getLatitude()));
            venueListingReqModel.setLongitude(String.valueOf(city.getLongitude()));
            venueListingReqModel.setAvailableDays(null);
            venueListingReqModel.setOpeningTime(null);
            venueListingReqModel.setClosingTime(null);
            venueListingReqModel.setMeetingSpaceAvailable(false);
            venueListingReqModel.setFavorite(false);
            venueListingReqModel.setPageNo(1);
            venueListingReqModel.setAppType("Mobile");

            if (AppGlobalData.getInstance().getLoggedInCustomer().isFilterSelected()){

                venueListingReqModel.setAvailableDays(getAvailableDays());
                venueListingReqModel.setOpeningTime(venueFilterData.getStartTime());
                venueListingReqModel.setClosingTime(venueFilterData.getEndTime());
                venueListingReqModel.setMeetingSpaceAvailable(venueFilterData.isMeetingSpaceAvailable());
                venueListingReqModel.setFavorite(venueFilterData.isFavourite());
            }
            venueListAdapter.clear();
            checkInternetAndHitApi(VENUE_LIST_REQ_CODE);
        }


    }

    private String getAvailableDays(){
        StringBuilder availableDays = new StringBuilder();
        if (venueFilterData.isSundaySelected()){
            availableDays.append("1,");
        }
        if (venueFilterData.isMondaySelected()){
            availableDays.append("2,");
        }
        if (venueFilterData.isTuesdaySelected()){
            availableDays.append("3,");
        }
        if (venueFilterData.isWednesdaySelected()){
            availableDays.append("4,");
        }
        if (venueFilterData.isThursdaySelected()){
            availableDays.append("5,");
        }
        if (venueFilterData.isFridaySelected()){
            availableDays.append("6,");
        }
        if (venueFilterData.isSaturdaySelected()){
            availableDays.append("7,");
        }

        if (availableDays.length() > 0){
            availableDaysString = availableDays.deleteCharAt(availableDays.length() - 1).toString();
        }
        return availableDaysString;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DashboardFragmentListener) {
            mListener = (DashboardFragmentListener) context;
        } else {
            throw new RuntimeException("DashboardFragmentListener must be implemented");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void openNavMenu() {
        mListener.openNavMenu();
    }

    @Override
    public void openVenue(Venue venue) {
        Intent intent = new Intent(getActivity().getApplicationContext(), VenueDetailActivity.class);
        intent.putExtra(Constants.EXTRA.CITY_EXTRA, city);
        intent.putExtra(Constants.EXTRA.VENUE_DETAIL_EXTRA, venue);
        startActivity(intent);
    }

    @Override
    public void performVenueSearch(String query) {
        Toast.makeText(getActivity(), "performSearch "+query, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void clearVenueSearch() {
        Toast.makeText(getActivity(), "clear venue search", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void toggleMap(boolean isVisible) {
        AppBottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation_view);
        Animation slideToBottom = AnimationUtils.loadAnimation(getActivity(), R.anim.item_animation_slide_to_bottom);
        binding.rltlayVenueDesc.setAnimation(slideToBottom);
        binding.rltlayVenueDesc.setVisibility(View.GONE);
        //TransitionDrawable transition = (TransitionDrawable) binding.toolbarBlurOverlay.getBackground();
        if (isVisible) {
            mListener.onMapViewSelected(true);
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.item_animation_slide_to_bottom);
            bottomNavigationView.setVisibility(View.GONE);
            bottomNavigationView.setAnimation(animation);
            binding.appBarLayout.setExpanded(false,true);
            //transition.startTransition(Constants.TOOLBAR_COLOR_ANIM_DURATION);
            binding.venueListRv.setVisibility(View.INVISIBLE);

            binding.mapFragmentContainer.setVisibility(View.VISIBLE);
            enterReveal(binding.mapFragmentContainer);
           // fadeAlpha(binding.venueToolbarImg,Constants.TOOLBAR_COLOR_ANIM_DURATION,false);

        } else {
            binding.appBarLayout.setExpanded(true,true);
            Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.item_animation_slide_from_bottom);
            bottomNavigationView.setAnimation(animation);
            bottomNavigationView.setVisibility(View.VISIBLE);
            mListener.onMapViewSelected(false);
            //transition.reverseTransition(Constants.TOOLBAR_COLOR_ANIM_DURATION);
            binding.venueListRv.setVisibility(View.VISIBLE);
            binding.mapFragmentContainer.setVisibility(View.INVISIBLE);
            enterReveal(binding.venueListRv);
           // fadeAlpha(binding.venueToolbarImg,Constants.TOOLBAR_COLOR_ANIM_DURATION,true);
        }
    }

    @Override
    public void performVenueSuggestion(String searchKey) {
        //Toast.makeText(getActivity(), isCurrentLocationReq+"", Toast.LENGTH_SHORT).show();
        if (city.isCurrentLocation()){
            venueSuggestionReqModel.setLocationId(null);
            venueSuggestionReqModel.setLatitude(venueListingReqModel.getLatitude());
            venueSuggestionReqModel.setLongitude(venueListingReqModel.getLongitude());
            venueSuggestionReqModel.setSearchKey(searchKey);
        }else{
            venueSuggestionReqModel.setSearchKey(searchKey);
            venueSuggestionReqModel.setLatitude(null);
            venueSuggestionReqModel.setLongitude(null);
            venueSuggestionReqModel.setLocationId(String.valueOf(venueListingReqModel.getSearchId()));
        }
        checkInternetAndHitApiWithoutLoader(VENUE_SUGGESTION_REQ_CODE);


    }

    public void updateVenueList(){
        checkInternetAndHitApi(VENUE_LIST_REQ_CODE);
    }

    @Override
    public void openFilterActivity() {
        startActivityForResult(new Intent(getActivity(), FilterActivity.class), FILTER_ACTIVITY_REQ_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        binding.venueSearchBar.selectFilter(AppGlobalData.getInstance().getLoggedInCustomer().isFilterSelected());
        if (requestCode == CURRENT_LOCATION_REQ_CODE ){
            if (resultCode == RESULT_OK){
                city = (City) data.getSerializableExtra(Constants.EXTRA.CITY_EXTRA);
                binding.cityTv.setText(city.getName());

                venueListingReqModel.setUserId(AppGlobalData.getInstance().getLoggedInCustomer().getId());
                venueListingReqModel.setLocationType(city.getType());
                venueListingReqModel.setSearchId(city.getID());
                venueListingReqModel.setLatitude(String.valueOf(city.getLatitude()));
                venueListingReqModel.setLongitude(String.valueOf(city.getLongitude()));
                venueListingReqModel.setAvailableDays(null);
                venueListingReqModel.setOpeningTime(null);
                venueListingReqModel.setClosingTime(null);
                venueListingReqModel.setMeetingSpaceAvailable(false);
                venueListingReqModel.setFavorite(false);
                venueListingReqModel.setAppType("Mobile");
                venueListingReqModel.setPageNo(1);
                if (AppGlobalData.getInstance().getLoggedInCustomer().isFilterSelected()){
                    venueListingReqModel.setAvailableDays(getAvailableDays());
                    venueListingReqModel.setOpeningTime(venueFilterData.getStartTime());
                    venueListingReqModel.setClosingTime(venueFilterData.getEndTime());
                    venueListingReqModel.setMeetingSpaceAvailable(venueFilterData.isMeetingSpaceAvailable());
                    venueListingReqModel.setFavorite(venueFilterData.isFavourite());
                }
                venueListAdapter.clear();
                checkInternetAndHitApi(VENUE_LIST_REQ_CODE);
            }
        }
        if (requestCode == FILTER_ACTIVITY_REQ_CODE){
            if (resultCode == RESULT_OK){
                venueFilterData =(VenueFilterData) data.getSerializableExtra(Constants.EXTRA.FILTER_DATA);
                venueListingReqModel.setPageNo(1);
                venueListingReqModel.setAppType("Mobile");
                venueListingReqModel.setAvailableDays(getAvailableDays());
                venueListingReqModel.setFavorite(venueFilterData.isFavourite());
                venueListingReqModel.setOpeningTime(venueFilterData.getStartTime());
                venueListingReqModel.setClosingTime(venueFilterData.getEndTime());
                venueListingReqModel.setMeetingSpaceAvailable(venueFilterData.isMeetingSpaceAvailable());
                venueListAdapter.clear();
                checkInternetAndHitApi(VENUE_LIST_REQ_CODE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_menu_btn:
                mListener.openNavMenu();
                break;
            case R.id.city_tv:
                openCitySelector();
                break;
            case R.id.imgClose:
                Animation slideToBottom = AnimationUtils.loadAnimation(getActivity(), R.anim.item_animation_slide_to_bottom);
                binding.rltlayVenueDesc.setAnimation(slideToBottom);
                binding.rltlayVenueDesc.setVisibility(View.GONE);
                break;
        }
    }

    private void openCitySelector() {

        Intent intent = new Intent(getContext(), CitySelectActivity.class);
        startActivityForResult(intent, CURRENT_LOCATION_REQ_CODE);
       // startActivity(intent);
    }

    @Override
    public <D> void onApiSuccess(GenericResponseModel<D> model, int request_code) {
        switch (request_code){
            case VENUE_LIST_REQ_CODE:
                GenericResponseModel<VenueListingResModel> resModel = (GenericResponseModel<VenueListingResModel>) model;
                if (model != null && resModel.getData().getVenues() != null){
                    venueList = resModel.getData().getVenues();
                    binding.resultCountTv.setText(String.format(getResources().getString(R.string.result), venueList.size()));
                    venueListAdapter.addVenues(venueList);
                    updateMap();
                }
                break;
            case TOGGLE_FAV_REQ_CODE:
                if (model.getMessage() != null){
                    Toast.makeText(getActivity(), model.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case VENUE_SUGGESTION_REQ_CODE:
                GenericResponseModel<VenueSuggestionResModel> responseModel =
                        (GenericResponseModel<VenueSuggestionResModel>)model;
                if (responseModel != null && responseModel.getData().getVenueList() != null){
                    if (responseModel.getData().getVenueList().size() > 0){
                        binding.venueSearchBar.addVenueSuggestionList(responseModel.getData().getVenueList());
                    }
                }
                break;
            case PROFILE_DATA_REQ_CODE:
                if (model.getData() != null){
                    String tempAccessToken = AppGlobalData.getInstance().getToken();
                    GenericResponseModel<Profile> profileResModel = (GenericResponseModel<Profile>) model;
                    profileResModel.getData().setAccessToken(tempAccessToken);
                    profile = profileResModel.getData();
                    updateView();
                    AppGlobalData.getInstance().setLoggedInCustomer(profileResModel.getData());
                    AppGlobalData.getInstance().saveToPrefs(getActivity().getApplicationContext());
                }
                break;
            case SEAT_CHECK_IN_REQ_CODE:

                Toast.makeText(getActivity(), model.getMessage(), Toast.LENGTH_SHORT).show();
                checkedInVenuePosition = selectedVenuePosition;
                venueList.get(selectedVenuePosition).setCheckedIn(true);
                venueListAdapter.notifyItemChanged(selectedVenuePosition);
                break;
            case SEAT_CHECK_OUT_REQ_CODE:

                if (checkedInVenuePosition == -1){
                    venueListAdapter.clear();
                    checkInternetAndHitApi(VENUE_LIST_REQ_CODE);
                }else{
                    venueList.get(checkedInVenuePosition).setCheckedIn(false);
                    venueListAdapter.notifyItemChanged(checkedInVenuePosition);
                }

                if (model.getMessage() != null){
                    Toast.makeText(getActivity(), model.getMessage(), Toast.LENGTH_SHORT).show();
                }
                AppGlobalData.getInstance().getLoggedInCustomer().getCheckedInVenue().setCheckInId(0);
                AppGlobalData.getInstance().saveToPrefs(getActivity().getApplicationContext());
                break;
            case CHECKED_IN_VENUE_REQ_CODE:
                if (model.getData() != null){
                    String tempAccessToken = AppGlobalData.getInstance().getToken();
                    GenericResponseModel<Profile> profileResModel = (GenericResponseModel<Profile>) model;
                    profileResModel.getData().setAccessToken(tempAccessToken);
                    AppGlobalData.getInstance().setLoggedInCustomer(profileResModel.getData());
                    AppGlobalData.getInstance().saveToPrefs(getActivity().getApplicationContext());
                    checkedInVenue = AppGlobalData.getInstance().getLoggedInCustomer().getCheckedInVenue();
                    if (checkedInVenue != null){
                        if (checkedInVenue.getCheckInId() == 0){
                            checkInternetAndHitApi(SEAT_CHECK_IN_REQ_CODE);
                        }else{
                            // show dialog of checked in venue
                            final VenueCheckoutDialog dialog = new VenueCheckoutDialog(getActivity(), checkedInVenue);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.show();
                            dialog.setCheckoutListener(new VenueCheckoutDialog.VenueCheckoutDialogListener() {
                                @Override
                                public void onVenueCheckout(int id) {
                                    checkInternetAndHitApi(SEAT_CHECK_OUT_REQ_CODE);
                                    Toast.makeText(getActivity(), "checkout "+id, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                        }
                    }else{
                        checkInternetAndHitApi(SEAT_CHECK_IN_REQ_CODE);
                    }
                }
                break;
            case CHECKED_IN_VENUE_DETAIL_FOR_CHECK_OUT:
                if (model.getData() != null){
                    String tempAccessToken = AppGlobalData.getInstance().getToken();
                    GenericResponseModel<Profile> profileResModel = (GenericResponseModel<Profile>) model;
                    profileResModel.getData().setAccessToken(tempAccessToken);
                    AppGlobalData.getInstance().setLoggedInCustomer(profileResModel.getData());
                    AppGlobalData.getInstance().saveToPrefs(getActivity().getApplicationContext());
                    checkedInVenue = AppGlobalData.getInstance().getLoggedInCustomer().getCheckedInVenue();
                    if (checkedInVenue != null){
                        if (checkedInVenue.getCheckInId() != 0){
                            checkInternetAndHitApi(SEAT_CHECK_OUT_REQ_CODE);
                        }else{
                            venueListAdapter.clear();
                            checkInternetAndHitApi(VENUE_LIST_REQ_CODE);
                        }
                    }else{
                        venueListAdapter.clear();
                        checkInternetAndHitApi(VENUE_LIST_REQ_CODE);
                    }
                }
                break;

        }
    }

    @Override
    public <D> void onApiFail(GenericResponseModel<D> model, int request_code) {
        Toast.makeText(getActivity(), model.getMessage(), Toast.LENGTH_SHORT).show();
        switch (request_code){
            case TOGGLE_FAV_REQ_CODE:
                venueList.get(selectedPosition).setFavorite(false);
                venueListAdapter.notifyItemChanged(selectedPosition);
                break;
            case PROFILE_DATA_REQ_CODE:
                profile =  AppGlobalData.getInstance().getLoggedInCustomer();
                updateView();
                break;
            case SEAT_CHECK_IN_REQ_CODE:
                Toast.makeText(getActivity(), "could not checked in.", Toast.LENGTH_SHORT).show();
                AppGlobalData.getInstance().getLoggedInCustomer().setCheckedInVenue(null);
                AppGlobalData.getInstance().saveToPrefs(getActivity().getApplicationContext());

                break;
            case SEAT_CHECK_OUT_REQ_CODE:
                if (model.getMessage() != null){
                    Toast.makeText(getActivity(), model.getMessage(), Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
    @Override
    public void onApiException(Throwable t, int request_code) {
        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
        switch (request_code){
            case TOGGLE_FAV_REQ_CODE:
                venueList.get(selectedPosition).setFavorite(false);
                venueListAdapter.notifyItemChanged(selectedPosition);
                break;
            case PROFILE_DATA_REQ_CODE:
                profile =  AppGlobalData.getInstance().getLoggedInCustomer();
                updateView();
                break;
            case SEAT_CHECK_IN_REQ_CODE:
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                AppGlobalData.getInstance().getLoggedInCustomer().setCheckedInVenue(null);
                AppGlobalData.getInstance().saveToPrefs(getActivity().getApplicationContext());
                break;
        }
    }


    @Override
    public void hitApi(int request) {
        switch (request){
            case VENUE_LIST_REQ_CODE:
                WebAPIHelper.getInstance().getVenueList(venueListingReqModel, new APICallback<VenueListingResModel>(VENUE_LIST_REQ_CODE));
                break;
            case TOGGLE_FAV_REQ_CODE:
                WebAPIHelper.getInstance().toggleFavouriteVenue(toggleFavouriteReqModel, new SilentAPICallback<>(TOGGLE_FAV_REQ_CODE));
                break;
            case VENUE_SUGGESTION_REQ_CODE:
                WebAPIHelper.getInstance().getVenueSuggestionList(venueSuggestionReqModel, new SilentAPICallback<VenueSuggestionResModel>(VENUE_SUGGESTION_REQ_CODE));
                break;
            case PROFILE_DATA_REQ_CODE:
                WebAPIHelper.getInstance().getProfileData(new APICallback<Profile>(PROFILE_DATA_REQ_CODE));
                break;
            case SEAT_CHECK_IN_REQ_CODE:
                WebAPIHelper.getInstance().doSeatCheckIn(seatCheckInReqModel, new APICallback<Object>(SEAT_CHECK_IN_REQ_CODE));
                break;
            case SEAT_CHECK_OUT_REQ_CODE:
                WebAPIHelper.getInstance().doSeatCheckOut(new APICallback<Object>(SEAT_CHECK_OUT_REQ_CODE),
                        AppGlobalData.getInstance().getLoggedInCustomer().getCheckedInVenue().getCheckInId());
                break;
            case CHECKED_IN_VENUE_REQ_CODE:
                WebAPIHelper.getInstance().getProfileData(new APICallback<Profile>(CHECKED_IN_VENUE_REQ_CODE));
                break;
            case CHECKED_IN_VENUE_DETAIL_FOR_CHECK_OUT:
                WebAPIHelper.getInstance().getProfileData(new APICallback<Profile>(CHECKED_IN_VENUE_DETAIL_FOR_CHECK_OUT));
                break;

        }
    }

    private void enterReveal(View view) {
        int cx = view.getMeasuredWidth() / 2;
        int cy = view.getMeasuredHeight() / 2;
        int finalRadius = Math.max(view.getWidth(), view.getHeight()) / 2;
        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

        anim.start();
    }
    private void fadeAlpha(View view,int duration, boolean toVisible) {

            AlphaAnimation alphaAnim = toVisible ? new AlphaAnimation(0f, 1.0f) : new AlphaAnimation(1.0f,0f);
            alphaAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            alphaAnim.setDuration(duration);
            alphaAnim.setFillAfter(true);
            view.startAnimation(alphaAnim);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        map.setOnMarkerClickListener(this);
        map.setOnMapClickListener(this);

    }

    private void updateMap(){
        if (markerList != null){
            if (markerList.size() > 0){
                map.clear();
                markerList.clear();
                selectedMarker = null;
            }
        }

        for (int i =0; i < venueList.size(); i++){
            VenueMapModel venueMapModel = new VenueMapModel();
            venueMapModel.setPosition(i);
            venueMapModel.setVenue(venueList.get(i));
            createMarker(venueMapModel);
        }
        boundMarkers();

    }

    private void boundMarkers(){
        if (markerList.size() > 0){
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : markerList) {
                builder.include(marker.getPosition());
            }

            LatLngBounds bounds = builder.build();
            int padding = 20; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            if (map != null){
                map.animateCamera(cu);
            }
        }
    }

    private Marker createMarker(VenueMapModel venueMapModel){
        Marker marker = null;
        if (map != null){
            marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(venueMapModel.getVenue().getLattitude(), venueMapModel.getVenue().getLongitude()))
                    .anchor(0.5f, 0.5f)
                    .title("")
                    .snippet("").icon(BitmapDescriptorFactory.fromBitmap(DrawableUtil.getSizedDrawable(getActivity(),75,50,R.drawable.map_pin_notselected))));
            marker.setTag(venueMapModel);
        }
        if (markerList != null){
            markerList.add(marker);
        }
        return marker;
    }

    @Override
    public void onMapClick(LatLng latLng) {


        Animation slideToBottom = AnimationUtils.loadAnimation(getActivity(), R.anim.item_animation_slide_to_bottom);
        binding.rltlayVenueDesc.setAnimation(slideToBottom);
        binding.rltlayVenueDesc.setVisibility(View.GONE);
        if(null != selectedMarker) {
            selectedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(DrawableUtil.getSizedDrawable(getActivity(),75,50,R.drawable.map_pin_notselected)));
        }

        selectedMarker = null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if (selectedMarker != null){
            selectedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(
                    DrawableUtil.getSizedDrawable(getActivity(),75,50,R.drawable.map_pin_notselected)));
        }
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(
                DrawableUtil.getSizedDrawable(getActivity(),90,60,R.drawable.map_pin)));

        selectedMarker = marker;
        final VenueMapModel venueMapModel = (VenueMapModel) marker.getTag();
        final Venue venue = venueMapModel.getVenue();
        venue.addObserver(venueObserver);
        ImageLoader.getInstance().displayImage(venue.getMainCoverPicture(), binding.layVenueDesc.venueCoverIv);
        binding.layVenueDesc.venueDistanceTv.setText
                (String.format(getActivity().getString(R.string.distance),Double.toString(venue.getDistance())));
        binding.layVenueDesc.venueTitleTv.setText(venue.getVenueName());
        binding.layVenueDesc.venueLocation.setText(venue.getVenueAddress());
        binding.layVenueDesc.venueTimingsTv.setText(venue.getTodayTimings());

        binding.layVenueDesc.mapCheckInBtn.setVisibility(venue.isCheckedIn() ? View.GONE : View.VISIBLE);
        binding.layVenueDesc.mapCheckOutBtn.setVisibility(venue.isCheckedIn() ? View.VISIBLE : View.GONE);
        binding.layVenueDesc.mapCheckInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seatCheckInReqModel.setVenueid(venue.getVenueId());
                seatCheckInReqModel.setUserId(AppGlobalData.getInstance().getLoggedInCustomer().getId());

                checkInternetAndHitApi(SEAT_CHECK_IN_REQ_CODE);
            }
        });
        binding.layVenueDesc.mapCheckOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternetAndHitApi(SEAT_CHECK_OUT_REQ_CODE);
            }
        });
        binding.layVenueDesc.amenitiesDefaultView.setAmenities(venue.getAmenities());
        binding.rltlayVenueDesc.setVisibility(View.VISIBLE);
        binding.layVenueDesc.addToFavBtn.setSelected(venue.isFavorite());
        binding.layVenueDesc.addToFavBtn.setLikeButtonListener(new LikeButton.LikeButtonListener() {
            @Override
            public void onLikedButtonClicked() {
                clickedVenue = venueMapModel.getPosition();
                boolean isFav = venue.isFavorite();
                venue.setFavorite(!isFav);

                toggleFavouriteReqModel = new ToggleFavouriteReqModel();
                toggleFavouriteReqModel.setUserId(AppGlobalData.getInstance().getLoggedInCustomer().getId());
                toggleFavouriteReqModel.setVenueId(venue.getVenueId());
                toggleFavouriteReqModel.setFavourite(venue.isFavorite());
                checkInternetAndHitApiWithoutLoader(TOGGLE_FAV_REQ_CODE);
                //checkInternetAndHitApi(TOGGLE_FAV_REQ_CODE);
            }
        });
        Animation slideFromBottom = AnimationUtils.loadAnimation(getActivity(), R.anim.item_animation_slide_from_bottom);

        binding.rltlayVenueDesc.setAnimation(slideFromBottom);
        binding.layVenueDesc.venueTitleTv.setText(venue.getVenueName());
        return true;

    }

    @Override
    public void onFavSelected(int position) {
        selectedPosition  = position;
        boolean isFav = venueList.get(position).isFavorite();
        venueList.get(position).setFavorite(!isFav);
        toggleFavouriteReqModel = new ToggleFavouriteReqModel();
        toggleFavouriteReqModel.setUserId(AppGlobalData.getInstance().getLoggedInCustomer().getId());
        toggleFavouriteReqModel.setVenueId(venueList.get(position).getVenueId());
        toggleFavouriteReqModel.setFavourite(!isFav);
        checkInternetAndHitApiWithoutLoader(TOGGLE_FAV_REQ_CODE);

    }


    @Override
    public void onCheckIn(int position) {
        Venue venue = venueList.get(position);

        venue.getOpeningTime();
        if (venue.getTodayTimings() == null){
            Toast.makeText(getActivity(), getResources().getString(R.string.venue_closed_today), Toast.LENGTH_SHORT).show();
        }else if(AppCommons.isValidCheckInTime(venue.getOpeningTime(), venue.getClosingTime())){
            selectedVenuePosition = position;
            seatCheckInReqModel.setUserId(AppGlobalData.getInstance().getLoggedInCustomer().getId());
            seatCheckInReqModel.setVenueid(venue.getVenueId());
            checkInternetAndHitApi(CHECKED_IN_VENUE_REQ_CODE);
            //checkInternetAndHitApi(SEAT_CHECK_IN_REQ_CODE);
        }else{
            Toast.makeText(getActivity(), getResources().getString(R.string.check_in_time_no_valid), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onBookMeetingSpace(int position) {
        Venue venue = venueList.get(position);
        if (venue.isMeetingSpaceAvailable()){

        }else{
            Toast.makeText(getActivity(), getResources().getString(R.string.no_meeting_space_available), Toast.LENGTH_SHORT).show();
        }
        /*Intent intent = new Intent(getActivity(), BookMeetingSpaceActivity.class);
        intent.putExtra(Constants.EXTRA.VENUE_DETAIL_EXTRA, venueList.get(position));
        startActivity(intent);*/
    }

    @Override
    public void onItemSelected(int position) {
        Intent intent = new Intent(getActivity().getApplicationContext(), VenueDetailActivity.class);
        intent.putExtra(Constants.EXTRA.CITY_EXTRA, city);
        intent.putExtra(Constants.EXTRA.VENUE_DETAIL_EXTRA, venueList.get(position));
        startActivity(intent);
    }

    @Override
    public void onCheckOut(int position) {
        checkedInVenuePosition = position;
        checkInternetAndHitApi(CHECKED_IN_VENUE_DETAIL_FOR_CHECK_OUT);
    }
}
