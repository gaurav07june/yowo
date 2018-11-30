package com.nomad.activity;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.nomad.R;
import com.nomad.databinding.ActivityPaymentOptionBinding;
import com.nomad.fragment.AddNewCardFragment;
import com.nomad.fragment.SavedCardFragment;
import com.nomad.model.GenericResponseModel;

public class PaymentOptionActivity extends NetworkActivity implements View.OnClickListener, SavedCardFragment.OnSavedCardInteractionListener, AddNewCardFragment.OnAddCardInteractionListener {
    ActivityPaymentOptionBinding binding;
    private final static int SAVED_CARDS_FILTER = 1;
    private final static int ADD_NEW_CARD_FILTER = 2;
    private final static int TOGGLE_DURATION = 500;
    @Override
    public void setContentView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_option);
    }

    @Override
    public void getExtras() {

    }

    @Override
    public void initViews() {

    }

    @Override
    public void setViews() {
        binding.backBtn.setOnClickListener(this);
        binding.txtSavedCardButton.setOnClickListener(this);
        binding.txtAddNewCardButton.setOnClickListener(this);
        toggleCardsFilter(SAVED_CARDS_FILTER);
        showFragment(SavedCardFragment.class);
    }

    @Override
    public <D> void onApiSuccess(GenericResponseModel<D> model, int request_code) {

    }

    @Override
    public <D> void onApiFail(GenericResponseModel<D> model, int request_code) {

    }

    @Override
    public void onApiException(Throwable t, int request_code) {

    }

    @Override
    public void hitApi(int request) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_btn:
                super.onBackPressed();
                break;
            case R.id.txtAddNewCardButton:
                toggleCardsFilter(ADD_NEW_CARD_FILTER);
                showFragment(AddNewCardFragment.class);
                break;
            case R.id.txtSavedCardButton:
                toggleCardsFilter(SAVED_CARDS_FILTER);
                showFragment(SavedCardFragment.class);
                break;
        }
    }
    private void toggleCardsFilter(int filter) {
        switch (filter) {
            case SAVED_CARDS_FILTER:
                ((TransitionDrawable) binding.txtSavedCardButton.getBackground()).startTransition(TOGGLE_DURATION);
                ((TransitionDrawable) binding.txtAddNewCardButton.getBackground()).resetTransition();
                binding.txtSavedCardButton.setSelected(true);
                binding.txtAddNewCardButton.setSelected(false);
                break;
            case ADD_NEW_CARD_FILTER:
                ((TransitionDrawable) binding.txtSavedCardButton.getBackground()).resetTransition();
                ((TransitionDrawable) binding.txtAddNewCardButton.getBackground()).startTransition(TOGGLE_DURATION);
                binding.txtSavedCardButton.setSelected(false);
                binding.txtAddNewCardButton.setSelected(true);
                break;
        }
    }


    private void showFragment(Class fragment) {

        if (fragment == AddNewCardFragment.class) {
            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.frmLayCard, AddNewCardFragment.newInstance()).commit();

        }
        if (fragment == SavedCardFragment.class){
            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.frmLayCard, SavedCardFragment.newInstance()).commit();
        }
    }
}
