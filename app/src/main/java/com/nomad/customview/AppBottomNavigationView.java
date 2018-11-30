package com.nomad.customview;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.nomad.R;
import com.nomad.databinding.HomeBottomNavigationContentBinding;

public class AppBottomNavigationView extends FrameLayout implements View.OnClickListener{

    private int selectedId;
    private OnBottomNavigationListener listener;

    public AppBottomNavigationView(@NonNull Context context) {
        super(context);
        init();
    }

    public AppBottomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AppBottomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public AppBottomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private HomeBottomNavigationContentBinding binding;

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.home_bottom_navigation_content, this, false);
        addView(view);
        binding = DataBindingUtil.bind(view);
        selectVenuesBtn();
        binding.venuesBtn.setOnClickListener(this);
        binding.myBookingsBtn.setOnClickListener(this);
        binding.favoritesBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        unSelectCurrentSelected();
        selectedId=v.getId();
        v.setSelected(true);

        switch (v.getId())
        {
            case R.id.venues_btn:
                listener.onVenueClicked();
                break;
            case R.id.my_bookings_btn:
                listener.onMyBookingClicked();

                break;
            case R.id.favorites_btn:
                listener.onFavouriteClicked();
                break;
        }
    }

    private void unSelectCurrentSelected()
    {
        if (selectedId!=0)
            findViewById(selectedId).setSelected(false);
    }

    public void selectVenuesBtn()
    {
        unSelectCurrentSelected();
        selectedId=binding.venuesBtn.getId();
        binding.venuesBtn.setSelected(true);
    }

    public void selectMyBookingsBtn()
    {
        unSelectCurrentSelected();
        selectedId=binding.myBookingsBtn.getId();
        binding.myBookingsBtn.setSelected(true);
    }

    public void selectFavoritesBtn()
    {
        unSelectCurrentSelected();
        selectedId=binding.favoritesBtn.getId();
        binding.favoritesBtn.setSelected(true);
    }

    public interface OnBottomNavigationListener{
        void onVenueClicked();
        void onMyBookingClicked();
        void onFavouriteClicked();
    }

    public void setListener(OnBottomNavigationListener listener){
        this.listener = listener;
    }
}
