package com.nomad.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nomad.R;
import com.nomad.model.PagerData;
import com.nomad.util.AppConstants;

public class OnBoardingFragment extends Fragment {

    public OnBoardingFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_on_boarding, container, false);
        Bundle args = getArguments();
        PagerData pagerData = (PagerData) args.getSerializable(AppConstants.EXTRA.ON_BOARDING_PAGES);
        TextView txtPagerText = view.findViewById(R.id.txtPagerText);
        txtPagerText.setText(pagerData.getText());
        ImageView imgPager = view.findViewById(R.id.imgPager);
        imgPager.setImageDrawable(getResources().getDrawable(pagerData.getImage()));
        return view;
    }


}
