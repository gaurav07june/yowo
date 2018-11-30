package com.nomad.adapter;

import android.app.Activity;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nomad.R;
import com.nomad.model.City;
import com.nomad.model.Suburb;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<String> {
    LayoutInflater layoutInflater;
    List<String> itemList;

    Context context;
    int resource;

    public SpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<String> itemList) {
        super(context, resource, textViewResourceId, itemList);
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.resource = resource;
        this.itemList = itemList;
    }



   /* public SpinnerAdapter(@NonNull Context context, @LayoutRes int resource,
                          @NonNull List<String> itemList) {
        super(context, resource, 0, itemList);

        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.resource = resource;
        this.itemList = itemList;
    }*/
    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }


    public View createItemView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView =layoutInflater.inflate(resource, parent, false);
        }


        TextView txtItem = (TextView) convertView.findViewById(R.id.txtItem);
        txtItem.setText(itemList.get(position));
        return convertView;

    }
}
