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

import com.nomad.R;
import com.nomad.customview.NMGTextView;
import com.nomad.model.City;
import com.nomad.model.Suburb;

import java.util.List;

public class SpinnerSuburbAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    List<Suburb> suburbList;
    Context context;
    NMGTextView txtItem, txtId;
    View viewSeparator;


    public SpinnerSuburbAdapter(@NonNull Context context , List<Suburb> suburbList){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.suburbList = suburbList;

    }

    public void updateSuburbList(List<Suburb> suburbList){
        this.suburbList.clear();
        this.suburbList.addAll(suburbList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (suburbList.size());
    }

    @Override
    public Suburb getItem(int position) {
        return suburbList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return suburbList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }




    public View createItemView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView =layoutInflater.inflate(R.layout.city_suburb_spinner_item_lay, parent, false);
        }
        txtItem = convertView.findViewById(R.id.txtItem);
        txtId = convertView.findViewById(R.id.txtId);
        viewSeparator = convertView.findViewById(R.id.viewSeparator);
        txtId.setText(String.valueOf(suburbList.get(position).getId()));
        txtItem.setText(suburbList.get(position).getName());

        return convertView;
    }
}
