package com.nomad.customview;

import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nomad.R;
import com.nomad.adapter.VenueAutoCompleteAdapter;
import com.nomad.databinding.VenueSearchBarBinding;
import com.nomad.model.Venue;

import java.util.ArrayList;

public class VenueSearchBar extends FrameLayout implements View.OnClickListener {
    private static final long TOGGLE_ICON_ANIM_DUR = 500;
    private VenueAutoCompleteAdapter adapter;

    public VenueSearchBar(@NonNull Context context) {
        super(context);
        init();
    }

    public VenueSearchBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VenueSearchBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public VenueSearchBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private VenueSearchBarBinding binding;

    public void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.venue_search_bar, this, false);
        addView(view);
        binding = DataBindingUtil.bind(view);
        binding.mapBtn.setOnClickListener(this);
        binding.filterBtn.setOnClickListener(this);
        binding.searchBtn.setOnClickListener(this);
        binding.searchBoxOverlay.setOnClickListener(this);
        binding.closeSearchIcon.setOnClickListener(this);
        binding.locationBtn.setOnClickListener(this);
        adapter = new VenueAutoCompleteAdapter(getContext());
        adapter.setVenueAutoCompleteListener(new VenueAutoCompleteAdapter.VenueAutoCompleteListener() {
            @Override
            public void getVenueSuggestion(String searchKey) {
                venueSearchListener.performVenueSuggestion(searchKey);

            }
        });
        binding.searchBox.setAdapter(adapter);

        binding.searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    toggleSearch(false);
                    venueSearchListener.performVenueSearch(v.getText().toString());
                    return true;
                }
                return false;
            }
        });
        binding.searchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Venue venue = (Venue) binding.searchBox.getAdapter().getItem(position);
                binding.searchBox.setText("");
                toggleSearch(false);
                venueSearchListener.openVenue(venue);
            }
        });
    }


    public void addVenueSuggestionList(ArrayList<Venue> venueArrayList){
        adapter.addList(venueArrayList);
    }
    public void toggleLocationBtn(boolean isMenuBtn) {
        binding.locationBtn.setImageResource(isMenuBtn ? R.drawable.ic_menu_ham : R.drawable.location);
        binding.locationBtn.setSelected(isMenuBtn);
        animateToggleIcon(binding.locationBtn);
    }

    public void toggleMapBtn() {
        binding.mapBtn.setSelected(!binding.mapBtn.isSelected());
        binding.mapBtn.setImageResource(binding.mapBtn.isSelected() ? R.drawable.ic_listview : R.drawable.ic_mapview);
        animateToggleIcon(binding.mapBtn);
    }


    private void animateToggleIcon(View view) {
        AlphaAnimation alphaAnim = new AlphaAnimation(0.2f, 1.0f);
        alphaAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnim.setDuration(TOGGLE_ICON_ANIM_DUR);
        view.startAnimation(alphaAnim);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_btn:
                toggleMapBtn();
                venueSearchListener.toggleMap(binding.mapBtn.isSelected());
                break;
            case R.id.filter_btn:
                //v.setSelected(!v.isSelected());
                venueSearchListener.openFilterActivity();
                //animateToggleIcon(v);
                break;
            case R.id.search_box_overlay:
                toggleSearch(true);
                break;
            case R.id.search_btn:
                toggleSearch(true);
                break;
            case R.id.close_search_icon:
                binding.searchBox.setText("");
                toggleSearch(false);
                venueSearchListener.clearVenueSearch();
                break;
            case R.id.location_btn:
                if (binding.locationBtn.isSelected())
                {
                    venueSearchListener.openNavMenu();
                }
                break;

        }
    }

    public void selectFilter(boolean isSelected){
        binding.filterBtn.setSelected(isSelected);
    }



    private void toggleSearch(boolean enabled) {
        binding.searchBox.setEnabled(enabled);
        binding.searchToolsLayout.setVisibility(enabled ? GONE : VISIBLE);
        binding.closeSearchIcon.setVisibility(enabled ? VISIBLE : GONE);
        binding.searchBoxOverlay.setVisibility(enabled ? GONE : VISIBLE);
        binding.searchBox.setSelection(binding.searchBox.getText().length());
        if (enabled)
            requestFocus(binding.searchBox);
        else
            clearFocus(binding.searchBox);

    }

    private void requestFocus(View view) {
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);

    }

    private void clearFocus(View view) {
        view.clearFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), 0);

    }

    private VenueSearchListener venueSearchListener=null;

    public VenueSearchListener getVenueSearchListener() {
        return venueSearchListener;
    }

    public void setVenueSearchListener(VenueSearchListener venueSearchListener) {
        this.venueSearchListener = venueSearchListener;
    }

    public interface VenueSearchListener {
        void openNavMenu();

        void openVenue(Venue venue);

        void performVenueSearch(String query);

        void clearVenueSearch();

        void toggleMap(boolean isVisible);

        void performVenueSuggestion(String searchKey);

        void openFilterActivity();

    }
}
