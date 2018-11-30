package com.nomad.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nomad.R;
import com.nomad.adapter.SavedCardAdapter;
import com.nomad.databinding.FragmentSavedCardBinding;
import com.nomad.model.GenericResponseModel;

public class SavedCardFragment extends BaseFragment implements View.OnClickListener {

    private FragmentSavedCardBinding binding;
    private SavedCardAdapter adapter;
    private OnSavedCardInteractionListener mListener;
    public SavedCardFragment() {
    }

    public static SavedCardFragment newInstance() {
        SavedCardFragment fragment = new SavedCardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_saved_card, container, false);
        binding = DataBindingUtil.bind(view);
        binding.btnPay.setOnClickListener(this);
        adapter = new SavedCardAdapter();
        adapter.setCardListener(new SavedCardAdapter.CardListener() {
            @Override
            public void onCardDelete(int position) {

            }

            @Override
            public void onCardSelect(int position) {

            }
        });
        binding.recyclerSavedCard.setAdapter(adapter);
        binding.recyclerSavedCard.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSavedCardInteractionListener) {
            mListener = (OnSavedCardInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public void setViews() {

    }

    @Override
    public void hitApi(int request) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPay:
                Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    public interface OnSavedCardInteractionListener {
    }
}
