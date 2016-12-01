package com.peterombodi.newconverterlab.presentation.screen.organisation_list.view;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.global.Constants;
import com.peterombodi.newconverterlab.presentation.R;
import com.peterombodi.newconverterlab.presentation.screen.organisation_list.IListFragment;
import com.peterombodi.newconverterlab.presentation.screen.organisation_list.presenter.BankRVHolders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.peterombodi.newconverterlab.presentation.Application.getContext;

/**
 * Created by Admin on 22.11.2016.
 */

public class BankRVAdapter extends RecyclerView.Adapter<BankRVHolders> {

    private static final String TAG = "BankRVAdapter";
    private List<OrganizationRV> itemList;
    private IListFragment.IPresenter iRecyclerView;
//    private IGetAction iRecyclerView;
//    private Context context;
    private Date yesterday = new Date(System.currentTimeMillis() - 1000L * 60L * 60L * 24L);


    public BankRVAdapter(IListFragment.IPresenter presenter, List<OrganizationRV> itemList) {
        this.itemList = itemList;
        this.iRecyclerView = presenter;
    }

//    public BankRVAdapter(Context context, List<OrganizationRV> itemList) {
//        this.itemList = itemList;
//        this.iRecyclerView = (IGetAction) context;
//    }


    @Override
    public BankRVHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_org, parent, false);

        return new BankRVHolders(layoutView, iRecyclerView);
    }

    @Override
    public void onBindViewHolder(BankRVHolders holder, int position) {

        holder.organizationRV = itemList.get(position);
        holder.tvId.setText(itemList.get(position).getId());
        holder.tvName.setText(itemList.get(position).getTitle());
        holder.tvRegion.setText(itemList.get(position).getRegion());
        holder.tvCity.setText(itemList.get(position).getCity());
        holder.tvPhone.setText(itemList.get(position).getPhone());
        holder.tvAddress.setText(itemList.get(position).getAddress());
        holder.tvLink.setText(itemList.get(position).getLink());
        String textDate = itemList.get(position).getDate();
        holder.tvDate.setText(textDate);
        holder.tvDateDelta.setText(itemList.get(position).getDateDelta());
        holder.tvDateShort.setText(textDate.substring(0, 16).replace("T", " "));

        Date date = null;
        try {
            date = new SimpleDateFormat(Constants.FORMAT_DATE, Locale.US).parse(textDate.substring(0, 16).replace("T", " "));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tvDateShort.setTextColor(date.after(yesterday) ?
                ContextCompat.getColor(getContext(), R.color.colorAccent) :
                ContextCompat.getColor(getContext(), R.color.colorSecondText));


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    // Clean all elements of the recycler
    public void clear() {
        itemList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<OrganizationRV> list) {
        itemList.addAll(list);
        notifyDataSetChanged();
    }

    public void animateTo(List<OrganizationRV> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }


    private void applyAndAnimateRemovals(List<OrganizationRV> newModels) {
        for (int i = itemList.size() - 1; i >= 0; i--) {
            final OrganizationRV model = itemList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<OrganizationRV> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final OrganizationRV model = newModels.get(i);
            if (!itemList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<OrganizationRV> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final OrganizationRV model = newModels.get(toPosition);
            final int fromPosition = itemList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public OrganizationRV removeItem(int position) {
        final OrganizationRV model = itemList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, OrganizationRV model) {
        itemList.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final OrganizationRV model = itemList.remove(fromPosition);
        itemList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

}