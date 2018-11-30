package com.nomad.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.nomad.R;
import com.nomad.customview.AppBottomNavigationView;
import com.nomad.customview.NMGTextView;
import com.nomad.databinding.ActivityDashboardBinding;
import com.nomad.fragment.FavouriteFragment;
import com.nomad.fragment.MyBookingFragment;
import com.nomad.fragment.UpcomingBookingsFragment;
import com.nomad.fragment.VenuesFragment;
import com.nomad.listener.AppLocationPermissionListener;
import com.nomad.listener.DashboardFragmentListener;
import com.nomad.model.GenericResponseModel;

import com.nomad.model.LoginReqModel;
import com.nomad.model.Profile;
import com.nomad.retrofit.WebAPIHelper;
import com.nomad.util.AppGlobalData;
import com.nomad.util.AppConstants;
import com.nomad.util.AppGlobalData;
import com.nomad.util.Constants;
import com.nomad.util.ImagePickUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dashboard extends NetworkActivity implements DashboardFragmentListener, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private ActivityDashboardBinding binding;
    private boolean isMapViewSelected = false;
    CircleImageView userImage;
    NMGTextView txtUserName;
    NMGTextView txtUserEmail;
    private static final int PORFILE_UPDATE_REQ_CODE = 33;
    private static final int PROFILE_REQ_CODE = 11;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    public static Observer profileObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusBar();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        userImage= binding.navigationMenu.getHeaderView(0).findViewById(R.id.user_image_iv);
        txtUserName = (NMGTextView) binding.navigationMenu.getHeaderView(0).findViewById(R.id.user_name_tv);
        txtUserEmail = (NMGTextView) binding.navigationMenu.getHeaderView(0).findViewById(R.id.user_email_tv);
        //Toast.makeText(this, AppGlobalData.getInstance().getToken(), Toast.LENGTH_SHORT).show();
        binding.navigationMenu.getHeaderView(0).findViewById(R.id.nav_menu_back_btn).setOnClickListener(this);
        binding.navigationMenu.getHeaderView(0).findViewById(R.id.lnrlayProfile).setOnClickListener(this);
        ImageLoader.getInstance().displayImage(AppGlobalData.getInstance().getLoggedInCustomer().getAvatar(), userImage);
        txtUserName.setText(AppGlobalData.getInstance().getLoggedInCustomer().getFirstName()+" "+AppGlobalData.getInstance().getLoggedInCustomer().getLastName());
        txtUserEmail.setText(AppGlobalData.getInstance().getLoggedInCustomer().getEmail());
        profileObserver = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                checkInternetAndHitApiWithoutLoader(PROFILE_REQ_CODE);
            }
        };
        showFragment(VenuesFragment.class);
        binding.navigationMenu.setNavigationItemSelectedListener(this);
        binding.bottomNavigationView.setListener(new AppBottomNavigationView.OnBottomNavigationListener() {
            @Override
            public void onVenueClicked() {
                showFragment(VenuesFragment.class);
            }

            @Override
            public void onMyBookingClicked() {
                showFragment(MyBookingFragment.class);
            }

            @Override
            public void onFavouriteClicked() {
                showFragment(FavouriteFragment.class);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PORFILE_UPDATE_REQ_CODE){
            if (resultCode == RESULT_OK){
                ImageLoader.getInstance().displayImage(AppGlobalData.getInstance().getLoggedInCustomer().getAvatar(), userImage);

                txtUserName.setText(AppGlobalData.getInstance().getLoggedInCustomer().getFirstName()+
                        " "+AppGlobalData.getInstance().getLoggedInCustomer().getLastName());
                VenuesFragment venuesFragment = (VenuesFragment) getSupportFragmentManager().findFragmentByTag(Constants.FRAG.VENUE_FRAGMENT);
                venuesFragment.updateView();
            }
        }

    }

    private void showFragment(Class fragment) {

        if (fragment == VenuesFragment.class) {

            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.dashboard_container, VenuesFragment.newInstance(),Constants.FRAG.VENUE_FRAGMENT).commit();

        }
        if (fragment == FavouriteFragment.class){
            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.dashboard_container, FavouriteFragment.newInstance(), Constants.FRAG.FAVOURITE_FRAGMENT).commit();
        }
        if (fragment == MyBookingFragment.class){
            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.dashboard_container, MyBookingFragment.newInstance(), Constants.FRAG.MY_BOOKING_FRAGMENT).commit();
        }
        /*if (fragment == UpcomingBookingsFragment.class){
            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.dashboard_container, UpcomingBookingsFragment.newInstance()).commit();
        }*/

    }

    @Override
    public void openNavMenu() {
        binding.drawerLayout.openDrawer(Gravity.START);
    }

    @Override
    public void onMapViewSelected(boolean isMapView) {
        isMapViewSelected = isMapView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_menu_back_btn:
                binding.drawerLayout.closeDrawer(Gravity.START);
                break;
            case R.id.lnrlayProfile:
                startActivityForResult(new Intent(this, ProfileActivity.class), PORFILE_UPDATE_REQ_CODE);
                break;
        }
    }


    @Override
    public void setContentView() {

    }

    @Override
    public void getExtras() {

    }

    @Override
    public void initViews() {

    }

    @Override
    public void setViews() {

    }

    @Override
    public <D> void onApiSuccess(GenericResponseModel<D> model, int request_code) {
        switch (request_code){
            case PROFILE_REQ_CODE:
                if (model.getData() != null){
                    GenericResponseModel<Profile> resModel = (GenericResponseModel<Profile>) model;
                    String avatar = resModel.getData().getAvatar();
                    String firstName = resModel.getData().getFirstName();
                    String lastName = resModel.getData().getLastName();
                    AppGlobalData.getInstance().getLoggedInCustomer().setAvatar(avatar);
                    AppGlobalData.getInstance().getLoggedInCustomer().setFirstName(firstName);
                    AppGlobalData.getInstance().getLoggedInCustomer().setLastName(lastName);
                    AppGlobalData.getInstance().saveToPrefs(getApplicationContext());
                    ImageLoader.getInstance().displayImage(avatar, userImage);
                    txtUserName.setText(String.format(getString(R.string.user_name_format),firstName, lastName));
                }
                break;
        }
    }

    @Override
    public <D> void onApiFail(GenericResponseModel<D> model, int request_code) {

    }

    @Override
    public void onApiException(Throwable t, int request_code) {

    }

    @Override
    public void hitApi(int request) {
        switch (request){
            case PROFILE_REQ_CODE:

                WebAPIHelper.getInstance().getProfileData(new APICallback<Profile>(PROFILE_REQ_CODE));
                break;
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.settings:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.about_us:
                startActivity(new Intent(this, CMSActivity.class).putExtra(AppConstants.EXTRA.CMS_TYPE, AppConstants.CONSTANTS.ABOUT_US));
                break;
            case R.id.privacy_policty:
                startActivity(new Intent(this, CMSActivity.class).putExtra(AppConstants.EXTRA.CMS_TYPE, AppConstants.CONSTANTS.PRIVACY_POLICY));
                break;
            case R.id.t_and_c:
                startActivity(new Intent(this, CMSActivity.class).putExtra(AppConstants.EXTRA.CMS_TYPE, AppConstants.CONSTANTS.TERMS_AND_CONDITION));
                break;
            case R.id.logout:
                AppGlobalData.getInstance().logOut(this);
                startActivity(new Intent(this, SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                break;
        }
        binding.drawerLayout.closeDrawer(Gravity.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.dashboard_container);

        if (fragment instanceof VenuesFragment && !isMapViewSelected){
            super.onBackPressed();
        }else{
            showFragment(VenuesFragment.class);
            binding.bottomNavigationView.selectVenuesBtn();
            binding.bottomNavigationView.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.item_animation_slide_from_bottom);
            binding.bottomNavigationView.setAnimation(animation);
            isMapViewSelected = false;
        }

    }
}
