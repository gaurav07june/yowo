package com.nomad.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nomad.R;
import com.nomad.adapter.MyBookingsAdapter;
import com.nomad.databinding.FragmentUpcomingBookingsBinding;

public class UpcomingBookingsFragment extends Fragment {

    public UpcomingBookingsFragment() {
        // Required empty public constructor
    }
 public static UpcomingBookingsFragment newInstance() {
        UpcomingBookingsFragment fragment = new UpcomingBookingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    private FragmentUpcomingBookingsBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_upcoming_bookings, container, false);
        binding = DataBindingUtil.bind(view);
        binding.upcomingBookingRv.setAdapter(new MyBookingsAdapter());
        binding.upcomingBookingRv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
