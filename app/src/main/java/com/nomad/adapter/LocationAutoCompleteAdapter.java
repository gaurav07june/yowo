package com.nomad.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.nomad.R;
import com.nomad.model.City;

import java.util.List;

public class LocationAutoCompleteAdapter extends ArrayAdapter implements Filterable {

    private List<City> cityList;
    LocationAutoListener listener;


    public void setAutoCompleteListener(LocationAutoListener listener){
        this.listener = listener;
    }

    public LocationAutoCompleteAdapter(@NonNull Context context) {
        super(context, 0);
    }


    public void addList(List<City> cityList){

        if (this.cityList != null){
            this.cityList.clear();
            this.cityList.addAll(cityList);
            notifyDataSetChanged();
        }else{
            this.cityList = cityList;
            notifyDataSetChanged();
        }

    }

    @Override
    public int getCount(){
        return cityList.size();
    }

    @Override
    public City getItem(int position){
        return cityList.get(position);
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            public FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint != null){
                    try{
                        //get data from the web
                        String term = constraint.toString();

                        listener.getCityList(term);

                    }catch (Exception e){

                    }
                   /* filterResults.values = cityList;
                    filterResults.count = cityList.size();*/
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if(results != null && results.count > 0){
                    notifyDataSetChanged();
                }else{
                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView txtCityLocation;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.auto_complete_location_layout,parent,false);
        txtCityLocation = view.findViewById(R.id.txtCityLocation);
        txtCityLocation.setText(cityList.get(position).getName());



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onCitySelected(position);
            }
        });
        return view;
    }

    public interface LocationAutoListener{
        void getCityList(String searchKey);
        void onCitySelected(int position);
    }
}