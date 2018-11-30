package com.nomad.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.nomad.R;
import com.nomad.databinding.VenueSearchListItemBinding;
import com.nomad.model.Venue;

import java.util.ArrayList;
import java.util.List;

public class VenueAutoCompleteAdapter extends ArrayAdapter<Venue> {
    private ArrayList<Venue> resultList = new ArrayList<>();
    private Context mContext;
    private VenueAutoCompleteListener listener;
    public VenueAutoCompleteAdapter(Context context) {
        super(context, 0);
        mContext=context;
    }


    public void addList(ArrayList<Venue> resultList){
        if (resultList != null){
            this.resultList.clear();
            this.resultList.addAll(resultList);
            notifyDataSetChanged();
        }else {
            this.resultList = resultList;
            notifyDataSetChanged();
        }
    }
    @Override
    public int getCount() {
        // Last item will be the footer
        return resultList.size();
    }

    @Override
    public Venue getItem(int position) {
        return resultList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        VenueSearchListItemBinding binding;
        if(listItem == null)
        {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.venue_search_list_item,parent,false);
            binding = DataBindingUtil.bind(listItem);
            listItem.setTag(binding);

        }
        else
        {
        binding = (VenueSearchListItemBinding) listItem.getTag();
        }

        binding.venueTitleTv.setText(resultList.get(position).getVenueName());


        return listItem;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    String searchKey = constraint.toString();
                    listener.getVenueSuggestion(searchKey);
                     /*resultList.clear();
                     resultList.addAll(searchVenueApi(constraint.toString()));
                     filterResults.values = resultList;
                     filterResults.count = resultList.size();*/
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };

        return filter;
    }

    public interface VenueAutoCompleteListener{
        void getVenueSuggestion(String searchKey);
    }

    public void setVenueAutoCompleteListener(VenueAutoCompleteListener listener){
        this.listener = listener;
    }

    private List<Venue> searchVenueApi(String s) {
        List<Venue> venueList = new ArrayList<>();
        Venue v1 = new Venue();
        v1.setVenueName("StarBucks");
        Venue v2 = new Venue();
        v2.setVenueName("StarKing");
        venueList.add(v1);
        venueList.add(v2);
        return venueList;
    }
}
