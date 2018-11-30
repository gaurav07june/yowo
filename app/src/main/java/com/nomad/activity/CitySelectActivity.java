package com.nomad.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.nomad.R;
import com.nomad.adapter.LocationAutoCompleteAdapter;
import com.nomad.databinding.ActivityCitySelectBinding;
import com.nomad.listener.AppLocationPermissionListener;
import com.nomad.model.City;
import com.nomad.model.GenericResponseModel;
import com.nomad.model.LocationListReqModel;
import com.nomad.model.LocationListResModel;
import com.nomad.retrofit.WebAPIHelper;
import com.nomad.util.AppGlobalData;
import com.nomad.util.CircularProgressDialog;
import com.nomad.util.Constants;
import com.nomad.util.PermissionHelper;

import java.util.List;
import java.util.Locale;

public class CitySelectActivity extends NetworkSilentActivity implements LocationListener, AppLocationPermissionListener, View.OnClickListener {
    private static final int LOCATION_PERMISSION = 101;
    private boolean hasPermissionToFetchLocation =false;
    private LocationManager locationManager;
    private ActivityCitySelectBinding binding;
    private CircularProgressDialog progressDialog;
    private static  final int GET_LOCATION_SUGESTION_REQ_CODE = 501;
    LocationListReqModel locationListReqModel;
    LocationAutoCompleteAdapter adapter;
    private List<City> searchCityList;
    private static final String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain"
    };

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
        try{
            switch (request_code){
                case GET_LOCATION_SUGESTION_REQ_CODE:
                        GenericResponseModel<LocationListResModel> responseModel =
                                (GenericResponseModel<LocationListResModel>) model;
                        if (responseModel.getData() != null && responseModel.getData().getLocationList().size() > 0){
                            searchCityList = responseModel.getData().getLocationList();
                            adapter.addList(searchCityList);
                        }
                    break;
            }
        }catch (Exception e){
        }
    }

    @Override
    public <D> void onApiFail(GenericResponseModel<D> model, int request_code) {
        Toast.makeText(this, model.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onApiException(Throwable t, int request_code) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hitApi(int request) {
        switch (request){
            case GET_LOCATION_SUGESTION_REQ_CODE:

                WebAPIHelper.getInstance().getLocationList(locationListReqModel,  new APICallback<LocationListResModel>(GET_LOCATION_SUGESTION_REQ_CODE));

                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_city_select);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        progressDialog=new CircularProgressDialog(this,true);
        binding.currentLocationBtn.setOnClickListener(this);
        locationListReqModel = new LocationListReqModel();
        if (PermissionHelper.getLocationPermission(this, LOCATION_PERMISSION)) {
            PermissionHelper.requestFineLocationAccess(this, LOCATION_PERMISSION);
        }
        adapter = new LocationAutoCompleteAdapter(this);
        binding.citySearchBox.setAdapter(adapter);
        binding.citySearchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CitySelectActivity.this, "item Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setAutoCompleteListener(new LocationAutoCompleteAdapter.LocationAutoListener() {
            @Override
            public void getCityList(String searchKey) {
                locationListReqModel.setSearchKey(searchKey);

                checkInternetAndHitApi(GET_LOCATION_SUGESTION_REQ_CODE);
            }

            @Override
            public void onCitySelected(int position) {
                //Toast.makeText(CitySelectActivity.this, searchCityList.get(position).getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                searchCityList.get(position).setCurrentLocation(false);
                intent.putExtra(Constants.EXTRA.CITY_EXTRA, searchCityList.get(position));
                intent.putExtra(Constants.EXTRA.IS_CURRENT_LOCATION_REQ, false);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        binding.citySearchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityName = adapter.getItem(position).getName();
                binding.citySearchBox.setText(cityName);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PermissionHelper.requestFineLocationAccess(this, LOCATION_PERMISSION);
                } else {
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case LOCATION_PERMISSION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        hasPermissionToFetchLocation=true;
                        break;
                    case Activity.RESULT_CANCELED:
                        hasPermissionToFetchLocation=false;
                        break;
                }
                break;
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        try {
            progressDialog.dismiss();
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            City currentCity = new City();
            currentCity.setID(0);
            currentCity.setType(null);
            currentCity.setCurrentLocation(true);
            currentCity.setName(addresses.get(0).getAdminArea());
            currentCity.setLatitude(addresses.get(0).getLatitude());
            currentCity.setLongitude(addresses.get(0).getLongitude());
            AppGlobalData.getInstance().setUserGpsCity(currentCity);
            Intent intent = new Intent();
            intent.putExtra(Constants.EXTRA.CITY_EXTRA, currentCity);
            intent.putExtra(Constants.EXTRA.IS_CURRENT_LOCATION_REQ, true);
            setResult(RESULT_OK, intent);
            finish();
            //setCityResult(currentCity);
            //Log.d("Current Location", currentCity.getName());
        } catch (Exception e) {

        }

    }


    @Override
    public void hasLocationAccess() {
        hasPermissionToFetchLocation=true;
    }

    private void fetchCurrentLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.current_location_btn:
                if(hasPermissionToFetchLocation)
                {
                    progressDialog.show();
                    fetchCurrentLocation();

                }
                else
                {

                    Toast.makeText(getApplicationContext(), R.string.fetch_location_negative, Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }

    private void setCityResult(City city)
    {

        if (city.getName()==null || city.getName().isEmpty())
        {
            Toast.makeText(getApplicationContext(), R.string.fetch_location_negative, Toast.LENGTH_SHORT).show();

        }
        else
        {
            Toast.makeText(getApplicationContext(), "On Location Selected "+city.getName()+" "+city.getLatitude()+" "+city.getLongitude(), Toast.LENGTH_SHORT).show();

        }
    }

}
