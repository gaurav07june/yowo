package com.nomad.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nomad.R;
import com.nomad.customview.NMGTextView;
import com.nomad.model.City;
import com.nomad.model.Suburb;

import java.util.List;

public class SpinnerCityAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    List<City> cityList;
    Context context;

    NMGTextView txtItem, txtId;
    View viewSeparator;

    public SpinnerCityAdapter(@NonNull Context context, List<City> cityList) {
        this.context = context;

        this.cityList = cityList;
        layoutInflater = LayoutInflater.from(context);
    }

    public View createItemView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView =layoutInflater.inflate(R.layout.city_suburb_spinner_item_lay, parent, false);
        }
        txtItem = convertView.findViewById(R.id.txtItem);
        txtId = convertView.findViewById(R.id.txtId);
        viewSeparator = convertView.findViewById(R.id.viewSeparator);
        txtItem.setText(cityList.get(position).getName());
        txtId.setText(String.valueOf(cityList.get(position).getID()));
        return convertView;
    }

    @Override
    public int getCount() {
        return cityList.size();
    }
    @Override
    public City getItem(int position) {
        return cityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return cityList.get(position ).getID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);

    }
}
