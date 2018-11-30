package com.nomad.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nomad.R;
import com.nomad.adapter.MyVenueListAdapter;
import com.nomad.databinding.FragmentFavouriteBinding;
import com.nomad.listener.DashboardFragmentListener;
import com.nomad.model.GenericResponseModel;
import com.nomad.model.MyVenueReqModel;
import com.nomad.model.MyVenueResponseModel;
import com.nomad.model.ToggleFavouriteReqModel;
import com.nomad.model.Venue;
import com.nomad.retrofit.WebAPIHelper;
import com.nomad.util.AppGlobalData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavouriteFragment extends BaseFragment implements View.OnClickListener, MyVenueListAdapter.FavouriteListListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int FAVOURITE_TYPE = 1;
    private static final int PAST_CHEKCIN_TYPE = 2;
    private static final int FAVOURITE_FILTER = 1;
    private static final int PAST_LOG_FILTER = 2;
    private static final int TOGGLE_FAV_REQ_CODE = 72;
    private static final int FAVOURITE_PAST_TOGGLE_ANIM_DUR = 500;
    private static final int MYVENUE_LIST_REQ_CODE = 71;
    private MyVenueListAdapter myVenueListAdapter;
    private List<Venue> favouriteList = new ArrayList<>();
    private List<Venue> pastlogList = new ArrayList<>();
    private Map<String, String> queryMap = new HashMap<>();
    private ToggleFavouriteReqModel toggleFavouriteReqModel = new ToggleFavouriteReqModel();
    private int selectedPosition = -1;

    private FragmentFavouriteBinding binding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DashboardFragmentListener mListener = null;

    public FavouriteFragment() {

    }


    public static FavouriteFragment newInstance() {
        FavouriteFragment fragment = new FavouriteFragment();
        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite,container,false);
        binding = DataBindingUtil.bind(view);
        return view;
    }

    @Override
    public <D> void onApiSuccess(GenericResponseModel<D> model, int request_code) {
        switch (request_code){
            case MYVENUE_LIST_REQ_CODE:
                if (model.getData() != null){
                    GenericResponseModel<MyVenueResponseModel> responseModel = (GenericResponseModel<MyVenueResponseModel>) model;
                    if (responseModel.getData().getFavorite() != null){
                        favouriteList = responseModel.getData().getFavorite();

                    }else if(responseModel.getData().getPastlog() != null){
                        pastlogList = responseModel.getData().getPastlog();

                    }
                    if (queryMap.get("type").equals("F")){
                        showFavouriteList();
                    }else if (queryMap.get("type").equals("P")){
                        showPastLogList();
                    }
                }

                break;
            case TOGGLE_FAV_REQ_CODE:
                if (model.getMessage() != null){
                    Toast.makeText(getActivity(), model.getMessage(), Toast.LENGTH_SHORT).show();
                }
                checkInternetAndHitApi(MYVENUE_LIST_REQ_CODE);
                break;
        }

    }

    @Override
    public <D> void onApiFail(GenericResponseModel<D> model, int request_code) {
        if (model.getMessage() != null){
            Toast.makeText(getActivity(), model.getMessage(), Toast.LENGTH_SHORT).show();
        }
        switch (request_code){
            case TOGGLE_FAV_REQ_CODE:
                myVenueListAdapter.notifyItemChanged(selectedPosition);
                break;
        }

    }

    @Override
    public void onApiException(Throwable t, int request_code) {
        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
        switch (request_code){
            case TOGGLE_FAV_REQ_CODE:
                myVenueListAdapter.notifyItemChanged(selectedPosition);
                break;
        }
    }

    @Override
    public void setViews() {
        queryMap.put("type", "F");
        queryMap.put("userId", String.valueOf(AppGlobalData.getInstance().getLoggedInCustomer().getId()));
        queryMap.put("latitude", "28.696");
        queryMap.put("longitude","77.1526");
        toggleFavouriteReqModel.setUserId(AppGlobalData.getInstance().getLoggedInCustomer().getId());
        binding.layToolbar.titleTv.setText(getResources().getString(R.string.my_venues));
        binding.layToolbar.backBtn.setVisibility(View.GONE);
        binding.layToolbar.btnNav.setVisibility(View.VISIBLE);
        binding.layToolbar.btnNav.setOnClickListener(this);

        binding.txtFavouriteButton.setOnClickListener(this);
        binding.txtPastLogButton.setOnClickListener(this);

        myVenueListAdapter = new MyVenueListAdapter(getActivity());
        myVenueListAdapter.setFavouriteListListener(this);
        binding.favouriteRv.setAdapter(myVenueListAdapter);

        binding.favouriteRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        toggleFavPastFilter(FAVOURITE_FILTER);
        checkInternetAndHitApi(MYVENUE_LIST_REQ_CODE);
    }

    @Override
    public void hitApi(int request) {
        switch (request){
            case MYVENUE_LIST_REQ_CODE:

                WebAPIHelper.getInstance().getMyVenueList(new APICallback<MyVenueResponseModel>(MYVENUE_LIST_REQ_CODE), queryMap);
                break;

            case TOGGLE_FAV_REQ_CODE:
                WebAPIHelper.getInstance().toggleFavouriteVenue(toggleFavouriteReqModel, new SilentAPICallback<>(TOGGLE_FAV_REQ_CODE));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnNav:
                mListener.openNavMenu();
                break;
            case R.id.txtFavouriteButton:
                toggleFavPastFilter(FAVOURITE_FILTER);
                queryMap.remove("type");
                queryMap.put("type", "F");
                checkInternetAndHitApi(MYVENUE_LIST_REQ_CODE);

                break;
            case R.id.txtPastLogButton:
                toggleFavPastFilter(PAST_LOG_FILTER);
                queryMap.remove("type");
                queryMap.put("type", "P");
                checkInternetAndHitApi(MYVENUE_LIST_REQ_CODE);

                break;
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DashboardFragmentListener) {
            mListener = (DashboardFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DashboardFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void toggleFavPastFilter(int filter) {
        switch (filter) {
            case FAVOURITE_FILTER:
                ((TransitionDrawable) binding.txtFavouriteButton.getBackground()).startTransition(FAVOURITE_PAST_TOGGLE_ANIM_DUR);
                ((TransitionDrawable) binding.txtPastLogButton.getBackground()).resetTransition();
                binding.txtFavouriteButton.setSelected(true);
                binding.txtPastLogButton.setSelected(false);

                break;
            case PAST_LOG_FILTER:
                ((TransitionDrawable) binding.txtFavouriteButton.getBackground()).resetTransition();
                ((TransitionDrawable) binding.txtPastLogButton.getBackground()).startTransition(FAVOURITE_PAST_TOGGLE_ANIM_DUR);
                binding.txtFavouriteButton.setSelected(false);
                binding.txtPastLogButton.setSelected(true);

                break;
        }
    }

    private void showFavouriteList(){
        binding.txtNoOfItem.setText(String.format(getActivity().getString(R.string.venue_no), favouriteList.size()));
        myVenueListAdapter.clear();
        myVenueListAdapter.addFavourite(favouriteList);
    }

    private void showPastLogList(){
        binding.txtNoOfItem.setText(String.format(getActivity().getString(R.string.venue_no), pastlogList.size()));
        myVenueListAdapter.clear();
        myVenueListAdapter.addPastLog(pastlogList);
    }

    @Override
    public void onCheckIn(int position, int viewType) {

        Toast.makeText(getActivity(), "onCheckIn "+position+ viewType, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCheckOut(int position, int viewType) {
        Toast.makeText(getActivity(), "onCheckout "+position+ viewType, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBookMeetingSpace(int position, int viewType) {
        Toast.makeText(getActivity(),"onBookMeetingSpace "+position + viewType, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFavouriteClicked(int position, int viewType) {
        Toast.makeText(getActivity(), "onFavourite "+position+ viewType, Toast.LENGTH_SHORT).show();
        selectedPosition = position;
        switch (viewType){
            case FAVOURITE_TYPE:
                if (favouriteList.size() > 0){
                    toggleFavouriteReqModel.setVenueId(favouriteList.get(position).getVenueId());
                    toggleFavouriteReqModel.setFavourite(!favouriteList.get(position).isFavorite());
                    checkInternetAndHitApiWithoutLoader(TOGGLE_FAV_REQ_CODE);
                }

                break;
            case PAST_CHEKCIN_TYPE:
                if (pastlogList.size() > 0){
                    toggleFavouriteReqModel.setVenueId(pastlogList.get(position).getVenueId());
                    toggleFavouriteReqModel.setFavourite(!pastlogList.get(position).isFavorite());
                    checkInternetAndHitApiWithoutLoader(TOGGLE_FAV_REQ_CODE);
                }
                break;
        }

    }
}
