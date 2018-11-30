package com.nomad.customview;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.nomad.R;
import com.nomad.databinding.CateringPackagesDefaultItemBinding;
import com.nomad.databinding.CateringPackagesDefaultItemBindingImpl;
import com.nomad.model.CateringPackage;
import com.nomad.model.SpanData;
import com.nomad.util.AppCommons;

import java.util.ArrayList;
import java.util.List;

import static com.nomad.util.AppCommons.generatePersonCountList;

public class CateringPackageView extends RecyclerView {
    private List<CateringPackage> cateringPackages = new ArrayList<>();
    private CateringPackageAdapter cateringPackageAdapter;
    private ItemType itemType = ItemType.ONLY_OUTPUT;
    private int totalPersons = 0;
    private int totalSelectedPersons=0;

    public enum ItemType {NO_OF_PERSON_INPUT, ONLY_OUTPUT}

    public CateringPackageView(@NonNull Context context) {
        super(context);
        init();
    }

    public CateringPackageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CateringPackageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        /*cateringPackages.add(new CateringPackage());
        cateringPackages.add(new CateringPackage());
        cateringPackages.add(new CateringPackage());*/
        setNestedScrollingEnabled(false);
        cateringPackageAdapter = new CateringPackageAdapter();
        setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        setAdapter(cateringPackageAdapter);

    }

    public void setCateringPackages(List<CateringPackage> cateringPackages) {
        this.cateringPackages.clear();
        this.cateringPackages.addAll(cateringPackages);
        cateringPackageAdapter.notifyDataSetChanged();
    }

    private class CateringPackageAdapter extends RecyclerView.Adapter<CateringPackageAdapter.CateringPackageHolder> {


        @NonNull
        @Override
        public CateringPackageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CateringPackageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.catering_packages_default_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CateringPackageHolder holder, int position) {
            if (itemType==ItemType.NO_OF_PERSON_INPUT)
            {
                int selectedForThisItem =  cateringPackages.get(position).getSelectedPersons();
                int leftPersons = totalPersons-(totalSelectedPersons-selectedForThisItem);
                holder.personCountAdapter.clear();
                holder.personCountAdapter.addAll(generatePersonCountList(leftPersons,true));
                holder.binding.numberOfPersonsSpinner.setText(String.valueOf(selectedForThisItem));

            }else{
                CateringPackage cateringPackage = cateringPackages.get(position);
                holder.binding.cateringPackageNameTv.setText(cateringPackage.getTitle());
                holder.binding.cateringPackageDescTv.setText(cateringPackage.getDescription());
                holder.binding.chargesTv.setText(AppCommons.getSpannedString(
                        new SpanData("$"+cateringPackage.getPerPersonCharge(), ContextCompat.getColor(getContext(),R.color.colorBlack), 1.2f, true),
                        new SpanData(getContext().getString(R.string.per_person), ContextCompat.getColor(getContext(),R.color.colorBlack),0.8f,false)
                ));
            }
        }

        @Override
        public int getItemCount() {
            return cateringPackages.size();
        }

        class CateringPackageHolder extends ViewHolder {
            CateringPackagesDefaultItemBinding binding;
            ArrayAdapter<String> personCountAdapter;

            public CateringPackageHolder(@NonNull View itemView) {
                super(itemView);
                binding = DataBindingUtil.bind(itemView);
                binding.numberOfPersonsHeaderTv.setVisibility(itemType == ItemType.NO_OF_PERSON_INPUT ? VISIBLE : GONE);
                binding.numberOfPersonsSpinner.setVisibility(itemType == ItemType.NO_OF_PERSON_INPUT ? VISIBLE : GONE);
                binding.numberOfPersonsSpinner.setEditable(false);
                binding.numberOfPersonsSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String item = personCountAdapter.getItem(position);
                        int itemIntVal = Integer.valueOf(item);
                        cateringPackages.get(getAdapterPosition()).setSelectedPersons(itemIntVal);
                        calcTotalSelectedPersons();
                        notifyDataSetChanged();
                    }
                });
                personCountAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,
                        new ArrayList<String>());
                binding.numberOfPersonsSpinner.setAdapter(personCountAdapter);
                binding.numberOfPersonsSpinner.setText("0");
            }
        }

    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public int getTotalPersons() {
        return totalPersons;
    }

    public void setTotalPersons(int totalPersons) {
        this.totalPersons = totalPersons;
    }

    private void calcTotalSelectedPersons()
    {
        totalSelectedPersons=0;
        for (CateringPackage cateringPackage : cateringPackages)
        {
            totalSelectedPersons+=cateringPackage.getSelectedPersons();
        }
    }
}
