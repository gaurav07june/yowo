package com.nomad.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.nomad.R;
import com.nomad.adapter.SpinnerAdapter;
import com.nomad.databinding.FragmentAddNewCardBinding;
import com.nomad.model.GenericResponseModel;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;


public class AddNewCardFragment extends BaseFragment implements View.OnClickListener {
    private FragmentAddNewCardBinding binding;
    private OnAddCardInteractionListener mListener;
    private List<String> yearList = getYearList();
    private List<String> monthList = getMonthList();
    private SpinnerAdapter monthSpinnerAdapter, yearSpinerAdapter;

    public AddNewCardFragment() {
    }

    public static AddNewCardFragment newInstance() {
        AddNewCardFragment fragment = new AddNewCardFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_new_card, container, false);
        binding = DataBindingUtil.bind(view);
        binding.edtExpiryMonth.setOnClickListener(this);
        binding.edtExpiryYear.setOnClickListener(this);
        binding.btnPay.setOnClickListener(this);
        monthSpinnerAdapter = new SpinnerAdapter(getActivity(), R.layout.drop_down_simple_item,R.id.txtItem,monthList);
        yearSpinerAdapter = new SpinnerAdapter(getActivity(), R.layout.drop_down_simple_item, R.id.txtItem, yearList);
        binding.spinnerExpiryYear.setAdapter(yearSpinerAdapter);
        binding.spinnerExpiryMonth.setAdapter(monthSpinnerAdapter);
        binding.spinnerExpiryMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                int selectedPos=binding.spinnerExpiryMonth.getSelectedItemPosition();
                if (selectedPos == 0){
                    binding.edtExpiryMonth.setText("");
                }else{
                    binding.edtExpiryMonth.setText(monthList.get(selectedPos));
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerExpiryYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // Get select item
                int selectedPos=binding.spinnerExpiryYear.getSelectedItemPosition();
                if (selectedPos == 0){
                    binding.edtExpiryYear.setText("");
                }else{
                    binding.edtExpiryYear.setText(yearList.get(selectedPos));
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddCardInteractionListener) {
            mListener = (OnAddCardInteractionListener) context;
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
            case R.id.edtExpiryMonth:
                binding.spinnerExpiryMonth.performClick();
                break;
            case R.id.edtExpiryYear:
                binding.spinnerExpiryYear.performClick();
                break;
        }
    }

    public interface OnAddCardInteractionListener {
    }

    private List<String> getYearList(){
        List<String> list = new ArrayList<>();
        list.add("Select year");
        list.add("2018");
        list.add("2019");
        list.add("2020");
        list.add("2021");
        list.add("2022");
        list.add("2023");
        list.add("2024");
        list.add("2025");
        list.add("2026");
        list.add("2027");
        list.add("2028");
        list.add("2029");
        list.add("2030");
        list.add("2031");
        list.add("2032");
        list.add("2033");
        list.add("2034");
        list.add("2035");

        return list;
    }
    private List<String> getMonthList(){
        List<String> list = new ArrayList<>();
        list.add("Select month");
        list.add("01");
        list.add("02");
        list.add("03");
        list.add("04");
        list.add("05");
        list.add("06");
        list.add("07");
        list.add("08");
        list.add("09");
        list.add("10");
        list.add("11");
        list.add("12");
        return list;
    }
}
