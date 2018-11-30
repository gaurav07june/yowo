package com.nomad.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.nomad.R;
import com.nomad.adapter.SpinnerCityAdapter;
import com.nomad.adapter.SpinnerSuburbAdapter;
import com.nomad.customview.NMGTextView;
import com.nomad.customview.PickImageDialog;
import com.nomad.databinding.ActivityProfileBinding;
import com.nomad.model.City;
import com.nomad.model.CitySuburbResModel;
import com.nomad.model.EditProfileReqModel;
import com.nomad.model.GenericResponseModel;
import com.nomad.model.Profile;
import com.nomad.model.Suburb;
import com.nomad.retrofit.WebAPI;
import com.nomad.retrofit.WebAPIHelper;
import com.nomad.util.AppGlobalData;
import com.nomad.util.CircularProgressDialog;
import com.nomad.util.Constants;
import com.nomad.util.ImagePickUtil;
import com.nomad.util.PermissionHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends NetworkActivity implements View.OnClickListener, PickImageDialog.PickImageListener, AdapterView.OnItemSelectedListener {
    ActivityProfileBinding binding;
    List<EditText> editTextList = new ArrayList<>();
    PickImageDialog pickImageDialog;
    Profile profile;
    static final int REQUEST_IMABE_CAPTURE = 11;
    static final int REQUEST_IMAGE_FROM_GALLERY = 12;
    static final int IMAGE_PICK_PERMISSION = 21;
    static final int CAMERA_PERMISSION = 22;
    private static final int PROFILE_DATA_REQ_CODE = 51;
    private static final int CITY_LOCATION_REQ_CODE = 81;
    private static final int SUBURB_REQ_CODE = 82;
    private static final int UPDATE_PROFILE_REQ_CODE = 9;
    private static final int SAVE_CHANGED_PROFILE_DATA_REQ_CODE = 18;
    private int selectecCityId = 0;
    private List<City> cityList;
    private List<Suburb> suburbList;
    private SpinnerCityAdapter cityAdapter;
    private SpinnerSuburbAdapter suburbAdapter;

    private Uri imageFileUri;
    private int selectedSuburbId = 0;
    public CircularProgressDialog progressDialog;
    private File imageFile = null;
    private Bitmap userProfileBitmap = null;
    private static final int CAMERA_REQ = 1;
    private static final int GALLERY_REQ = 2;
    private static final int NORMAL_REQ = 3;
    private int reqType = NORMAL_REQ;
    private boolean isFirstTimeCity = true;
    private boolean isFirstTimeSuburb = true;

    Map<String, RequestBody> requestBodyMap;
    MultipartBody.Part fileToUpload = null;

    boolean isEditVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusBar();

    }

    @Override
    public void setContentView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        pickImageDialog = new PickImageDialog(this,this);
        //profile = AppGlobalData.getInstance().getLoggedInCustomer();
    }

    @Override
    public void getExtras() {

    }

    @Override
    public void initViews() {


        binding.layToolbar.titleTv.setText(getResources().getString(R.string.my_profile_caps));
        editTextList.add(binding.edtFirstName);
        editTextList.add(binding.edtLastName);
        //editTextList.add(binding.edtEmailId);

    }

    @Override
    public void setViews() {
        isFirstTimeCity = true;
        isFirstTimeCity = true;
        progressDialog=new CircularProgressDialog(this, false);
        binding.btnEditProfile.setOnClickListener(this);
        binding.btnDoneProfile.setOnClickListener(this);
        binding.viewTransOverlay.setOnClickListener(this);
        binding.txtChagePic.setOnClickListener(this);
        binding.edtSuburb.setOnClickListener(this);
        binding.edtCityLocation.setOnClickListener(this);
        binding.layToolbar.backBtn.setOnClickListener(this);
        binding.spinnerCity.setOnItemSelectedListener(this);
        binding.spinnerSuburb.setOnItemSelectedListener(this);

        checkInternetAndHitApi(PROFILE_DATA_REQ_CODE);
        checkInternetAndHitApi(CITY_LOCATION_REQ_CODE);
    }

    @Override
    public <D> void onApiSuccess(GenericResponseModel<D> model, int request_code) {
        switch (request_code){
            case PROFILE_DATA_REQ_CODE:
                if (model.getData() != null){
                    String tempAccessToken = AppGlobalData.getInstance().getToken();
                    GenericResponseModel<Profile> resModel = (GenericResponseModel<Profile>) model;
                    AppGlobalData.getInstance().setLoggedInCustomer(resModel.getData());
                    resModel.getData().setAccessToken(tempAccessToken);
                    profile = resModel.getData();
                    updateView(profile);
                }
                break;
            case CITY_LOCATION_REQ_CODE:
                if (model.getData() != null){
                    GenericResponseModel<CitySuburbResModel> responseModel = (GenericResponseModel<CitySuburbResModel>) model;
                    if (responseModel.getData().getCities() != null){
                        cityList = responseModel.getData().getCities();
                        cityList.add(0, getCityHint());
                        checkInternetAndHitApi(SUBURB_REQ_CODE);
                        populateCityList();

                    }
                }
            case SUBURB_REQ_CODE:
                if (model.getData() != null){
                    GenericResponseModel<CitySuburbResModel> responseModel = (GenericResponseModel<CitySuburbResModel>) model;
                    if (responseModel.getData().getSuburb() != null){
                        suburbList = responseModel.getData().getSuburb();
                        suburbList.add(0, getSuburbHint());
                    }else {
                        suburbList = new ArrayList<>();
                    }
                    if (suburbAdapter != null){
                        suburbAdapter.updateSuburbList(suburbList);

                    }else{
                        populateSuburbList();
                    }
                }
                break;
            case UPDATE_PROFILE_REQ_CODE:
                checkInternetAndHitApi(SAVE_CHANGED_PROFILE_DATA_REQ_CODE);
                break;
            case SAVE_CHANGED_PROFILE_DATA_REQ_CODE:
                if (model.getData() != null){
                    String tempAccessToken = AppGlobalData.getInstance().getToken();
                    GenericResponseModel<Profile> resModel = (GenericResponseModel<Profile>) model;
                    AppGlobalData.getInstance().setLoggedInCustomer(resModel.getData());
                    resModel.getData().setAccessToken(tempAccessToken);
                    AppGlobalData.getInstance().saveToPrefs(this.getApplicationContext());
                    Intent  intent = new Intent();
                    intent.putExtra("something", "something");
                    setResult(RESULT_OK, intent);
                    finish();
                    /*profile = resModel.getData();
                    updateView(profile);*/
                }
                break;
        }
    }
    private void populateCityList(){
        cityAdapter = new SpinnerCityAdapter(this , cityList);
        binding.spinnerCity.setAdapter(cityAdapter);
    }
    private void populateSuburbList(){
        suburbAdapter = new SpinnerSuburbAdapter(this, suburbList);
        binding.spinnerSuburb.setAdapter(suburbAdapter);
    }
    private void updateView(final Profile profile){
        ImageLoader.getInstance().displayImage(profile.getAvatar(), binding.imgProfilePic);
        binding.edtFirstName.setText(profile.getFirstName());
        binding.edtLastName.setText(profile.getLastName());
        binding.edtEmailId.setText(profile.getEmail());
        binding.edtCityLocation.setText(profile.getCity().getName());
        binding.txtUserName.setText(profile.getFirstName()+" "+profile.getLastName());
        binding.txtUserEmailId.setText(profile.getEmail());
        binding.edtSuburb.setText(profile.getSuburb().getName());
        selectecCityId = profile.getCity().getID();
        selectedSuburbId = profile.getSuburb().getId();

    }
    private City getCityHint(){
        City city = new City();
        city.setID(-1);
        city.setName("Select City");
        city.setType("");
        city.setLatitude(0.0);
        city.setLongitude(0.0);
        city.setCurrentLocation(false);
        return city;
    }
    private Suburb getSuburbHint(){
        Suburb suburb = new Suburb();
        suburb.setId(-1);
        suburb.setName("Select Suburb");
        suburb.setType("");
        return suburb;
    }

    @Override
    public <D> void onApiFail(GenericResponseModel<D> model, int request_code) {
        if (model != null){
            Toast.makeText(this, model.getMessage(), Toast.LENGTH_SHORT).show();
        }
        switch (request_code){
            case UPDATE_PROFILE_REQ_CODE:
                Toast.makeText(this, "Image upload failed.", Toast.LENGTH_SHORT).show();
                break;
            case SAVE_CHANGED_PROFILE_DATA_REQ_CODE:
                Toast.makeText(this, "Profile data is not saved. Please try again.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onApiException(Throwable t, int request_code) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
        switch (request_code){
            case UPDATE_PROFILE_REQ_CODE:
                Toast.makeText(this, "Image upload failed.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void hitApi(int request) {
        switch (request){
            case PROFILE_DATA_REQ_CODE:
                WebAPIHelper.getInstance().getProfileData(new APICallback<Profile>(PROFILE_DATA_REQ_CODE));
                break;
            case CITY_LOCATION_REQ_CODE:
                WebAPIHelper.getInstance().getCityList(new APICallback<CitySuburbResModel>(CITY_LOCATION_REQ_CODE));
                break;
            case SUBURB_REQ_CODE:
                WebAPIHelper.getInstance().getSuburbList(new APICallback<CitySuburbResModel>(SUBURB_REQ_CODE), selectecCityId);
                break;
            case UPDATE_PROFILE_REQ_CODE:
                WebAPIHelper.getInstance().updateProfile(new VoidAPICallback(UPDATE_PROFILE_REQ_CODE), fileToUpload, requestBodyMap);
                break;
            case SAVE_CHANGED_PROFILE_DATA_REQ_CODE:
                WebAPIHelper.getInstance().getProfileData(new APICallback<Profile>(SAVE_CHANGED_PROFILE_DATA_REQ_CODE));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnEditProfile:
                displayEditableView();
                break;
            case R.id.btnDoneProfile:
                if (isFormValidated()){
                    doImageUpload();
                    displayNonEditableView();
                }
                break;
            case R.id.viewTransOverlay:
                showChagePicDialog();
                break;
            case R.id.txtChagePic:
                showChagePicDialog();
                break;
            case R.id.edtSuburb:
                if (binding.edtFirstName.isFocusable()){
                    binding.spinnerSuburb.performClick();
                }
                break;
            case R.id.edtCityLocation:
                if (binding.edtFirstName.isFocusable()){
                    binding.spinnerCity.performClick();
                }
                break;
            case R.id.back_btn:
                super.onBackPressed();
                break;

        }
    }

    private boolean isFormValidated(){
        if (binding.edtFirstName.getText().toString().isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.first_name_empty_error), Toast.LENGTH_SHORT).show();
            binding.edtFirstName.requestFocus();
            return false;
        }
        if (binding.edtLastName.getText().toString().isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.last_name_empty_error), Toast.LENGTH_SHORT).show();
            binding.edtLastName.requestFocus();
            return false;
        }
        if (selectecCityId <= 0){
            Toast.makeText(this, getResources().getString(R.string.city_empty_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (selectedSuburbId <= 0){
            Toast.makeText(this, getResources().getString(R.string.suburb_empty_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showChagePicDialog(){
        pickImageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pickImageDialog.show();
    }

    private void displayEditableView(){
        binding.btnDoneProfile.setVisibility(View.VISIBLE);
        binding.btnEditProfile.setVisibility(View.GONE);
        binding.viewTransOverlay.setVisibility(View.VISIBLE);
        binding.txtChagePic.setVisibility(View.VISIBLE);
        setEditable(editTextList);
    }

    private void displayNonEditableView(){
        binding.btnDoneProfile.setVisibility(View.GONE);
        binding.btnEditProfile.setVisibility(View.VISIBLE);
        binding.viewTransOverlay.setVisibility(View.GONE);
        binding.txtChagePic.setVisibility(View.GONE);
        setNonEditable(editTextList);
    }

    private void setNonEditable(List<EditText> editTextList){
        for (EditText editText : editTextList){
            editText.setEnabled(false);
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
        }


    }

    private void setEditable(List<EditText> editTextList){
        for (EditText editText : editTextList){
            editText.setEnabled(true);
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMABE_CAPTURE && resultCode == Activity.RESULT_OK){
            reqType = CAMERA_REQ;
            Bundle bundle = data.getExtras();
            userProfileBitmap = (Bitmap) bundle.get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            userProfileBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            imageFile = new File(Environment.getExternalStorageDirectory(),"nomad_profile.jpg");
            FileOutputStream fo;
            try {
                fo = new FileOutputStream(imageFile);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (requestCode == REQUEST_IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            reqType = GALLERY_REQ;
            if (data == null) {
                Toast.makeText(this, "This image can not be shown", Toast.LENGTH_SHORT).show();
            } else {
                imageFileUri = data.getData();
                if (imageFileUri != null){
                    String filePath = getRealPathFromURIPath(imageFileUri, this);
                    imageFile = new File(filePath);
                }

            }
        }
    }
    private void doImageUpload(){
        MediaType simpleMediaType = MediaType.parse("text/plain");
        MediaType imageMediaType = MediaType.parse("image/*");

        requestBodyMap = new HashMap<>();
        requestBodyMap.put("firstName", RequestBody.create(simpleMediaType,
                binding.edtFirstName.getText().toString().trim()));
        requestBodyMap.put("lastName", RequestBody.create(simpleMediaType,
                binding.edtLastName.getText().toString().trim()));
        requestBodyMap.put("suburbId", RequestBody.create(simpleMediaType, String.valueOf(selectedSuburbId)));
        requestBodyMap.put("id", RequestBody.create(simpleMediaType,
                String.valueOf(AppGlobalData.getInstance().getLoggedInCustomer().getId())));

        if (imageFile != null){
            RequestBody mFile = RequestBody.create(imageMediaType, imageFile);
            fileToUpload = MultipartBody.Part.createFormData("file", imageFile.getName(), mFile);
        }
        checkInternetAndHitApi(UPDATE_PROFILE_REQ_CODE);


    }


    private void doPostUpdateTask(){
        if (reqType == CAMERA_REQ){
            binding.imgProfilePic.setImageBitmap(userProfileBitmap);
        }else if (reqType == GALLERY_REQ){
            setImageBitmap(imageFileUri);
        }

        Profile profile =  AppGlobalData.getInstance().getLoggedInCustomer();
        profile.addObserver(Dashboard.profileObserver);
        profile.setFirstName(binding.edtFirstName.getText().toString().trim());
        profile.setLastName(binding.edtLastName.getText().toString().trim());
        profile.getCity().setID(selectecCityId);
        Suburb suburb = profile.getSuburb();

        suburb.setId(selectedSuburbId);
        profile.setProfileChanged(true);
        AppGlobalData.getInstance().setLoggedInCustomer(profile);
        AppGlobalData.getInstance().saveToPrefs(getApplicationContext());

    }

    private Map<String , String> getMultiPartHeader(){
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", AppGlobalData.getInstance().getToken());
        return headerMap;
    }

    private void setImageBitmap(Uri uri){
        Bitmap bitmap = BitmapFactory.decodeFile(ImagePickUtil.getPath(this, uri));

        binding.imgProfilePic.setImageBitmap(bitmap);
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI,
                null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    @Override
    public void onCameraClick() {
        pickImageDialog.dismiss();
        if (PermissionHelper.getCameraPermission(this, CAMERA_PERMISSION)){
            Intent capturePicIntent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (capturePicIntent.resolveActivity(getPackageManager()) != null){
                startActivityForResult(capturePicIntent, REQUEST_IMABE_CAPTURE);
            }
        }
    }

    @Override
    public void onGalleryClick() {
        pickImageDialog.dismiss();
        if (PermissionHelper.getImagePickPermission(this, IMAGE_PICK_PERMISSION)){
            Intent picFromGalleryIntent = new Intent();
            picFromGalleryIntent.setType("image/*");
            picFromGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(picFromGalleryIntent, "Select Picture"), REQUEST_IMAGE_FROM_GALLERY);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case IMAGE_PICK_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent picFromGalleryIntent = new Intent();
                    picFromGalleryIntent.setType("image/*");
                    picFromGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(picFromGalleryIntent, "Select Picture"), REQUEST_IMAGE_FROM_GALLERY);
                }else{
                    Toast.makeText(this, "You can not change profile picture from gallery", Toast.LENGTH_LONG).show();
                }
                break;
            case CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent capturePicIntent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (capturePicIntent.resolveActivity(getPackageManager()) != null){
                        startActivityForResult(capturePicIntent, REQUEST_IMABE_CAPTURE);
                    }
                }else{
                    Toast.makeText(this, "You can not use camera to change profile picture",  Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        NMGTextView txtItem = view.findViewById(R.id.txtItem);
        Toast.makeText(this,txtItem.getText().toString()+" "+id, Toast.LENGTH_SHORT).show();
        switch (parent.getId()){
            case R.id.spinnerCity:
                if (isFirstTimeCity){
                    binding.edtCityLocation.setText(profile.getCity().getName());
                    isFirstTimeCity = false;
                    selectecCityId = profile.getCity().getID();
                }else if (id > 0){
                    binding.edtCityLocation.setText(txtItem.getText().toString().trim());
                    binding.edtSuburb.setText(getString(R.string.select_suburb));
                    selectecCityId = (int) id;
                    selectedSuburbId = 0;
                    checkInternetAndHitApi(SUBURB_REQ_CODE);
                }else{
                    binding.edtCityLocation.setText(getResources().getString(R.string.select_city));
                    binding.edtSuburb.setText(getResources().getString(R.string.select_suburb));
                    selectecCityId = 0;
                    selectedSuburbId = 0;
                }
                break;
            case R.id.spinnerSuburb:
                if (isFirstTimeSuburb){
                    binding.edtSuburb.setText(profile.getSuburb().getName());
                    selectedSuburbId = profile.getSuburb().getId();
                    isFirstTimeSuburb = false;
                }else if (id > 0){
                    binding.edtSuburb.setText(txtItem.getText().toString().trim());
                    selectedSuburbId = (int)id;
                }else{
                    binding.edtSuburb.setText(getResources().getString(R.string.select_suburb));
                    selectedSuburbId = 0;
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
