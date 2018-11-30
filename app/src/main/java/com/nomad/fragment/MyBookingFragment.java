package com.nomad.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nomad.R;
import com.nomad.adapter.MyVenueListAdapter;
import com.nomad.adapter.PastBookingAdapter;
import com.nomad.adapter.UpcomingBookingAdapter;
import com.nomad.databinding.FragmentMyBookingBinding;
import com.nomad.listener.DashboardFragmentListener;
import com.nomad.model.BookingReqModel;
import com.nomad.model.BookingResModel;
import com.nomad.model.BookingsGeneralModel;
import com.nomad.model.GenericResponseModel;
import com.nomad.retrofit.WebAPIHelper;
import com.nomad.util.AppGlobalData;

import java.util.List;

public class MyBookingFragment extends BaseFragment implements View.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int UPCOMING_BOOKING_FILTER = 1;
    private static final int PAST_BOOKING_FILTER = 2;
    private static final int UPCOMING_PAST_TOGGLE_ANIM_DUR = 500;
    private FragmentMyBookingBinding binding;

    private List<BookingsGeneralModel> upcomingBookingList;
    private List<BookingsGeneralModel> pastBookingList;
    BookingReqModel reqModel = new BookingReqModel();
    UpcomingBookingAdapter upcomingBookingAdapter;
    PastBookingAdapter pastBookingAdapter;
    private final static String UPCOMING_REQ = "U";
    private final static String PAST_REQ = "P";
    private final static int BOOKING_LIST_REQ_CODE = 77;
    private String mParam1;
    private String mParam2;

    private DashboardFragmentListener mListener;

    public MyBookingFragment() {
        // Required empty public constructor
    }

    public static MyBookingFragment newInstance() {
        MyBookingFragment fragment = new MyBookingFragment();
        Bundle args = new Bundle();/*
        args.putString(ARG_PARAM1, param1);
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
        View view = inflater.inflate(R.layout.fragment_my_booking, container, false);
        binding = DataBindingUtil.bind(view);
        return view;
    }

    @Override
    public <D> void onApiSuccess(GenericResponseModel<D> model, int request_code) {
        switch (request_code){
            case BOOKING_LIST_REQ_CODE:
                if (model.getData() != null){
                    GenericResponseModel<BookingResModel> resModel = (GenericResponseModel<BookingResModel>) model;
                    if (reqModel.getType().equalsIgnoreCase(UPCOMING_REQ)){
                        upcomingBookingList = resModel.getData().getBookings();
                        if (upcomingBookingList.size() ==0){
                            binding.layNoRecordFound.setVisibility(View.VISIBLE);

                        }else{
                            binding.layNoRecordFound.setVisibility(View.GONE);
                        }
                        upcomingBookingAdapter.clear();
                        upcomingBookingAdapter.addBookingList(upcomingBookingList);
                    }else {
                        pastBookingList = resModel.getData().getBookings();
                        if (pastBookingList.size() ==0){
                            binding.layNoRecordFound.setVisibility(View.VISIBLE);

                        }else{
                            binding.layNoRecordFound.setVisibility(View.GONE);
                        }
                        pastBookingAdapter.clear();
                        pastBookingAdapter.addBookingList(pastBookingList);
                    }

                }

                break;

        }

    }

    @Override
    public <D> void onApiFail(GenericResponseModel<D> model, int request_code) {
        if (model != null && model.getMessage() != null){
            Toast.makeText(getActivity(), model.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onApiException(Throwable t, int request_code) {

    }

    @Override
    public void setViews() {
        reqModel.setType(UPCOMING_REQ);
        reqModel.setUserId(AppGlobalData.getInstance().getLoggedInCustomer().getId());
        reqModel.setPageNo(1);
        binding.layToolbar.titleTv.setText(getResources().getString(R.string.my_bookings));
        binding.layToolbar.backBtn.setVisibility(View.GONE);
        binding.layToolbar.btnNav.setVisibility(View.VISIBLE);


        binding.layToolbar.btnNav.setOnClickListener(this);
        binding.txtUpcomingButton.setOnClickListener(this);
        binding.txtPastBookingButton.setOnClickListener(this);
        upcomingBookingAdapter = new UpcomingBookingAdapter(getActivity());
        upcomingBookingAdapter.setUpcomingBookingListener(new UpcomingBookingAdapter.UpcomingBookingListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getActivity(), position+" upcoming item", Toast.LENGTH_SHORT).show();
                ((LinearLayoutManager)binding.myBoookingRv.getLayoutManager()).scrollToPositionWithOffset(position,200);
            }

            @Override
            public void onSubmenuClick(int position) {
                Toast.makeText(getActivity(), "upcoming submenu"+position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCheckIn(int position) {
                Toast.makeText(getActivity(), "upcoming checkin "+position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCheckOut(int position) {
                Toast.makeText(getActivity(), "upcoming chekc out"+position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMapButtonClicked(int position) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=28.5905901,77.0669218");

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
                //Toast.makeText(getActivity(), "map"+position, Toast.LENGTH_SHORT).show();
            }
        });
        binding.myBoookingRv.setAdapter(upcomingBookingAdapter);
        binding.myBoookingRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        pastBookingAdapter = new PastBookingAdapter();
        pastBookingAdapter.setPastBookingListener(new PastBookingAdapter.PastBookingListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getActivity(), "past item"+position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSubmenuClick(int position) {
                Toast.makeText(getActivity(), "past submenu "+position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMapButtonClicked(int position) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=28.5905901,77.0669218");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(mapIntent);
                }

            }
        });
        binding.pastBoookingRv.setAdapter(pastBookingAdapter);
        binding.pastBoookingRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        toggleBookingsFilter(UPCOMING_BOOKING_FILTER);

        checkInternetAndHitApi(BOOKING_LIST_REQ_CODE);
    }

    @Override
    public void hitApi(int request) {
        switch (request){
            case BOOKING_LIST_REQ_CODE:

                WebAPIHelper.getInstance().getBookingList(reqModel, new APICallback<BookingResModel>(BOOKING_LIST_REQ_CODE));
                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnNav:
                mListener.openNavMenu();
                break;
            case R.id.txtUpcomingButton:
                toggleBookingsFilter(UPCOMING_BOOKING_FILTER);
                reqModel.setType(UPCOMING_REQ);
                checkInternetAndHitApi(BOOKING_LIST_REQ_CODE);
                break;
            case R.id.txtPastBookingButton:
                toggleBookingsFilter(PAST_BOOKING_FILTER);
                reqModel.setType(PAST_REQ);
                checkInternetAndHitApi(BOOKING_LIST_REQ_CODE);
                break;
        }
    }
    // TODO: Rename method, update argument and hook method into UI event
  /* public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/
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
    private void toggleBookingsFilter(int filter) {
        switch (filter) {
            case UPCOMING_BOOKING_FILTER:
                binding.myBoookingRv.setVisibility(View.VISIBLE);
                binding.pastBoookingRv.setVisibility(View.GONE);
                ((TransitionDrawable) binding.txtUpcomingButton.getBackground()).startTransition(UPCOMING_PAST_TOGGLE_ANIM_DUR);
                ((TransitionDrawable) binding.txtPastBookingButton.getBackground()).resetTransition();
                binding.txtUpcomingButton.setSelected(true);
                binding.txtPastBookingButton.setSelected(false);
                break;
            case PAST_BOOKING_FILTER:
                binding.pastBoookingRv.setVisibility(View.VISIBLE);
                binding.myBoookingRv.setVisibility(View.GONE);
                ((TransitionDrawable) binding.txtUpcomingButton.getBackground()).resetTransition();
                ((TransitionDrawable) binding.txtPastBookingButton.getBackground()).startTransition(UPCOMING_PAST_TOGGLE_ANIM_DUR);
                binding.txtUpcomingButton.setSelected(false);
                binding.txtPastBookingButton.setSelected(true);
                break;
        }
    }
    private void showUpcomingList(){
    }
    private void showPastBookingList(){
    }
}
