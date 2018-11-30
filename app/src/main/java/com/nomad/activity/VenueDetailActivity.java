package com.nomad.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.nomad.R;
import com.nomad.adapter.VenueListAdapter;
import com.nomad.customview.CateringPackageView;
import com.nomad.customview.CoverImagesPager;
import com.nomad.customview.MeetingSpacesView;
import com.nomad.customview.OperationTimingsPopup;
import com.nomad.databinding.ActivityVenueDetailBinding;
import com.nomad.fragment.BaseFragment;
import com.nomad.model.CateringPackage;
import com.nomad.model.CateringPackageResModel;
import com.nomad.model.City;
import com.nomad.model.GenericResponseModel;
import com.nomad.model.MeetingSpace;
import com.nomad.model.MeetingSpacesResModel;
import com.nomad.model.ToggleFavouriteReqModel;
import com.nomad.model.Venue;
import com.nomad.model.VenueDetailReqModel;
import com.nomad.model.VenueDetailResModel;
import com.nomad.model.Venuedetail;
import com.nomad.retrofit.WebAPIHelper;
import com.nomad.util.AppGlobalData;
import com.nomad.util.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VenueDetailActivity extends NetworkActivity implements View.OnClickListener {

    private OperationTimingsPopup operationTimingsPopup;
    private ActivityVenueDetailBinding binding;
    private boolean appBarExpanded = true;
    private static final int IMAGE_SLIDER_RQ = 101;
    private static final int VENUE_DETAIL_REQ_CODE = 11;
    private static final int TOGGLE_FAV_REQ_CODE = 26;
    private static final int MEETING_SPACE_REQ_CODE = 27;
    private static final int CATERING_PACKAGE_REQ_CODE = 29;
    private Venue venue;
    private City city;
    private Venuedetail venuedetail;
    List<String> coverImages = new ArrayList<>();
    ToggleFavouriteReqModel toggleFavouriteReqModel;
    private boolean isFavourite= false;
    private List<MeetingSpace> meetinSpaceList = new ArrayList<>();
    private List<CateringPackage> cateringPackageList = new ArrayList<>();

    private VenueDetailReqModel venueDetailReqModel = new VenueDetailReqModel();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView() {
        transparentStatusBar();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_venue_detail);
    }

    @Override
    public void getExtras() {
        Intent intent = getIntent();
        venue = (Venue)intent.getSerializableExtra(Constants.EXTRA.VENUE_DETAIL_EXTRA);
        city = (City) intent.getSerializableExtra(Constants.EXTRA.CITY_EXTRA);

    }

    @Override
    public void initViews() {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void setViews() {
        binding.cateringPackagesRv.setItemType(CateringPackageView.ItemType.ONLY_OUTPUT);
        initVenueDetailReq();
        checkInternetAndHitApi(VENUE_DETAIL_REQ_CODE);
        checkInternetAndHitApi(MEETING_SPACE_REQ_CODE);
        checkInternetAndHitApi(CATERING_PACKAGE_REQ_CODE);
        binding.bookMeetingSpaceBtn.setOnClickListener(this);
        binding.btnLike.setOnClickListener(this);
        binding.venuePhoneTv.setOnClickListener(this);
        binding.cardViewBackArrow.setOnClickListener(this);
        binding.mapBtn.setOnClickListener(this);
        binding.coverPager.setOnCoverImageClickListener(new CoverImagesPager.OnCoverImageListener() {
            @Override
            public void onCoverImageClick(View view, int position) {
                Intent intent = new Intent(VenueDetailActivity.this, ImageSliderActivity.class);
                intent.putExtra(Constants.EXTRA.COVER_IMAGE_POSITION, position);
                intent.putStringArrayListExtra(Constants.EXTRA.COVER_IMAGES, (ArrayList<String>) coverImages);
                startActivityForResult(intent, IMAGE_SLIDER_RQ);
            }

        });


        binding.meetingSpaceView.setMeetingSpaceInteractionListener(new MeetingSpacesView.MeetingSpaceInteractionListener() {
            @Override
            public void onMeetingSpaceFocus(final int scrollY) {
                Log.d("", "");
                int height = VenueDetailActivity.this.getResources().getDimensionPixelSize(R.dimen._240sdp);
                int translateY = (binding.meetingSpacesLay.getTop() + scrollY - (binding.detailNestedSv.getHeight() - height));
                translateY += binding.checkInControlLay.getHeight();
                if (binding.meetingSpacesLay.getTop() + scrollY + height +binding.checkInControlLay.getHeight()> binding.detailNestedSv.getScrollY() + binding.detailNestedSv.getHeight())
                    binding.detailNestedSv.scrollTo(0, translateY);

            }
        });

        binding.detailNestedSv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                operationTimingsPopup.hide();
                return false;
            }
        });

        binding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                appBarExpanded = (appBarLayout.getHeight() - appBarLayout.getBottom()) == 0;
            }
        });
        binding.timingsLay.setOnClickListener(this);
    }

    private void updateView(){
        binding.imgBackArrow.setOnClickListener(this);
        isFavourite = venuedetail.isFavorite();
        binding.btnLike.setSelected(isFavourite);
        String[] imageArray = null;
        if (!venuedetail.getMainCoverPicture().equals("") || venuedetail.getMainCoverPicture() != null){
            imageArray = venuedetail.getMainCoverPicture().split(",");
        }
        for (String s : imageArray){
            coverImages.add(s);
        }
        binding.coverPager.setZoomable(false);
        binding.coverPager.setCoverImages(coverImages);
        ImageLoader.getInstance().displayImage(venuedetail.getLogoImage(), binding.venueLogo);
        binding.venueTitleTv.setText(venuedetail.getVenueName());
        binding.venueTimingsTv.setText(venuedetail.getTodayTimings());
        binding.venuePhoneTv.setText(venuedetail.getContactNumber());
        operationTimingsPopup = new OperationTimingsPopup(this, binding.detailContainer, binding.timingsLay, venuedetail.getOperationTimes());
        binding.venueAddressTv.setText(venuedetail.getVenueAddress());
        if (venuedetail.getAmenities() != null){
            binding.amenitiesGridView.setAmenities(venuedetail.getAmenities());
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.timings_lay:
                toggleOperationTimingsPopup();
                break;
            case R.id.cardViewBackArrow:
                super.onBackPressed();
                break;
            case R.id.btnLike:
                toggleFavouriteReqModel = new ToggleFavouriteReqModel();
                toggleFavouriteReqModel.setUserId(AppGlobalData.getInstance().getLoggedInCustomer().getId());
                toggleFavouriteReqModel.setFavourite(!isFavourite);
                toggleFavouriteReqModel.setVenueId(venue.getVenueId());
                checkInternetAndHitApi(TOGGLE_FAV_REQ_CODE);
                break;
            case R.id.venue_phone_tv:
                makePhoneCall();
                break;
            case R.id.map_btn:
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+venuedetail.getLattitude()+","+venuedetail.getLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(this.getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
                break;
            case R.id.book_meeting_space_btn:

                if (venuedetail.isMeetingSpaceAvailable()){
                    startActivity(new Intent(this , BookMeetingSpaceActivity.class)
                            .putExtra(Constants.EXTRA.VENUE_DETAIL_EXTRA, venuedetail));
                }else{
                    Toast.makeText(this, getResources().getString(R.string.no_meeting_space_available), Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }

    public void makePhoneCall(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + binding.venuePhoneTv.getText().toString().trim()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void initVenueDetailReq(){
        venueDetailReqModel.setUserId(AppGlobalData.getInstance().getLoggedInCustomer().getId());
       venueDetailReqModel.setSearchId(city.getID());
       venueDetailReqModel.setVenueId(venue.getVenueId());
       venueDetailReqModel.setAppType("Mobile");
       venueDetailReqModel.setLocationType(city.getType());
       venueDetailReqModel.setLatitude(String.valueOf(city.getLatitude()));
       venueDetailReqModel.setLongitude(String.valueOf(city.getLongitude()));
       venueDetailReqModel.setPageNo(1);
    }

    @Override
    public <D> void onApiSuccess(GenericResponseModel<D> model, int request_code) {
        switch (request_code){
            case VENUE_DETAIL_REQ_CODE:
                if (model.getData() != null){
                    GenericResponseModel<VenueDetailResModel> resModel = (GenericResponseModel<VenueDetailResModel>) model;
                    venuedetail =  resModel.getData().getVenuedetail();
                    updateView();
                }
                break;
            case TOGGLE_FAV_REQ_CODE:
                if (model.getMessage() != null){
                    Toast.makeText(this, model.getMessage(), Toast.LENGTH_SHORT).show();
                    isFavourite = !isFavourite;
                    binding.btnLike.setSelected(isFavourite);

                }
                break;
            case MEETING_SPACE_REQ_CODE:
                if (model.getData() != null){
                    GenericResponseModel<MeetingSpacesResModel> resModel =
                            (GenericResponseModel<MeetingSpacesResModel>) model;
                    meetinSpaceList = resModel.getData().getMeetingRooms();
                    binding.meetingSpaceView.addMeetingSpacesList(meetinSpaceList);
                }
                break;
            case CATERING_PACKAGE_REQ_CODE:
                if (model.getData() != null){
                    GenericResponseModel<CateringPackageResModel> resModel =
                            (GenericResponseModel<CateringPackageResModel>) model;
                    cateringPackageList = resModel.getData().getCateringPackages();
                    binding.cateringPackagesRv.setCateringPackages(cateringPackageList);
                    break;

                }
        }
    }
    @Override
    public <D> void onApiFail(GenericResponseModel<D> model, int request_code) {
        switch (request_code){
            case TOGGLE_FAV_REQ_CODE:
               /* isFavourite = !isFavourite;
                binding.btnLike.setSelected(isFavourite);*/
                break;
        }
        if (model != null){
            Toast.makeText(this, model.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onApiException(Throwable t, int request_code) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
        switch (request_code){
            case TOGGLE_FAV_REQ_CODE:
                /*isFavourite = !isFavourite;
                binding.btnLike.setSelected(isFavourite);*/
                break;
        }
    }

    @Override
    public void hitApi(int request) {
        switch (request){
            case VENUE_DETAIL_REQ_CODE:
                WebAPIHelper.getInstance().getVenueDetail(venueDetailReqModel, new APICallback<VenueDetailResModel>(VENUE_DETAIL_REQ_CODE));
                break;
            case TOGGLE_FAV_REQ_CODE:
                WebAPIHelper.getInstance().toggleFavouriteVenue(toggleFavouriteReqModel, new APICallback<>(TOGGLE_FAV_REQ_CODE));
                break;
            case MEETING_SPACE_REQ_CODE:
                WebAPIHelper.getInstance().getMeetingSpaces(new APICallback<MeetingSpacesResModel>(MEETING_SPACE_REQ_CODE), venue.getVenueId());
                break;
            case CATERING_PACKAGE_REQ_CODE:
                WebAPIHelper.getInstance().getCateringPackeges(new APICallback<CateringPackageResModel>(CATERING_PACKAGE_REQ_CODE), venue.getVenueId());
                break;

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE_SLIDER_RQ:
                if (resultCode == RESULT_OK) {
                    binding.coverPager.setCurrentItem(data.getIntExtra(Constants.EXTRA.COVER_IMAGE_POSITION, 0));
                }
                break;
        }
    }


    private void toggleOperationTimingsPopup() {
        if (appBarExpanded && !operationTimingsPopup.isShowing()) {

            binding.appBarLayout.setExpanded(false, true);
            binding.appBarLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    operationTimingsPopup.showHideToggle();
                }
            }, 500);
        } else {

            operationTimingsPopup.showHideToggle();
        }

    }

    @Override
    public void onBackPressed() {
        if (operationTimingsPopup.isShowing())
        {
            operationTimingsPopup.hide();
        }
        else
        {
            super.onBackPressed();
        }
    }
}
