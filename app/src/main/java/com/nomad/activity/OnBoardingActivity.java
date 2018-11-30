package com.nomad.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nomad.R;
import com.nomad.fragment.OnBoardingFragment;
import com.nomad.model.PagerData;
import com.nomad.util.AppConstants;
import com.nomad.util.AppGlobalData;
import com.tompee.circularviewpager.CircularViewPager;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingActivity extends BaseActivity {

    private CircularViewPager viewPager;
    private ImageView imgPaginationDot1, imgPaginationDot2;
    private List<PagerData> pagerDataList = new ArrayList<>();
    private int[] drawableImage = {R.drawable.pager1, R.drawable.pager2};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusBar();
        //transparentStatusNavBar();

        setContentView(R.layout.activity_on_boarding);
        initPagerDataList();
        imgPaginationDot1 = findViewById(R.id.imgPaginationDot1);
        imgPaginationDot2 = findViewById(R.id.imgPaginationDot2);


        viewPager = findViewById(R.id.viewPager);
        viewPager.setFragmentPagerAdapter(getSupportFragmentManager(),
                new CircularViewPager.GetFragmentItemListener() {
                    @Override
                    public Fragment getFragment(int position) {
                        Fragment fragment = new OnBoardingFragment();
                        Bundle args = new Bundle();
                        args.putSerializable(AppConstants.EXTRA.ON_BOARDING_PAGES, pagerDataList.get(position));
                        fragment.setArguments(args);
                        return fragment;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0)
                    updateDots(drawableImage.length-1);
                else
                if (position == viewPager.getAdapter().getCount())
                    updateDots(0);
                else
                    updateDots(position-1);
            }

            @Override
            public void onPageScrollStateChanged(int position) {


            }
        });

    }

    private void initPagerDataList() {
        pagerDataList.clear();
        pagerDataList.add(new PagerData(drawableImage[0], getResources().getString(R.string.pager_text1)));
        pagerDataList.add(new PagerData(drawableImage[1], getResources().getString(R.string.pager_text2)));

    }


    private void updateDots(int position) {
        imgPaginationDot1.setImageDrawable(getResources().getDrawable(R.drawable.pagination_dot_notselected));
        imgPaginationDot2.setImageDrawable(getResources().getDrawable(R.drawable.pagination_dot_notselected));
        switch (position) {
            case 0:
                imgPaginationDot1.setImageDrawable(getResources().getDrawable(R.drawable.pagination_dot_selected));
                break;
            case 1:
                imgPaginationDot2.setImageDrawable(getResources().getDrawable(R.drawable.pagination_dot_selected));
                break;
        }
    }

    public void onSignIn(View view) {

        startActivity(new Intent(OnBoardingActivity.this, SignInActivity.class));

    }
}
