package com.peterombodi.newconverterlab.presentation.screen.organisation_detail.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peterombodi.newconverterlab.data.model.Currency;
import com.peterombodi.newconverterlab.presentation.R;
import com.peterombodi.newconverterlab.presentation.screen.organisation_detail.presenter.CourseRVHolders;

import java.util.List;

/**
 * Created by Admin on 01.12.2016.
 */

public class CourseRVAdapter extends RecyclerView.Adapter<CourseRVHolders> {

    private List<Currency> itemList;


    public CourseRVAdapter(List<Currency> itemList) {

        this.itemList = itemList;

    }

    @Override
    public CourseRVHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_curses, parent, false);
        return new CourseRVHolders(layoutView);
    }


    @Override
    public void onBindViewHolder(CourseRVHolders holder, int position) {
        holder.tvCurrency.setText(itemList.get(position).getCurrency());
        holder.tvAsk.setText(itemList.get(position).getAsk());
        holder.tvBid.setText(itemList.get(position).getBid());
        String askDelta =itemList.get(position).getAskDelta();
        String bidDelta =itemList.get(position).getBidDelta();
        holder.tvAskDelta.setText(askDelta);
        holder.tvBidDelta.setText(bidDelta);
        holder.ivAsk.setImageResource(Float.parseFloat(askDelta)<0?
                R.drawable.ic_arrow_drop_down:
                R.drawable.ic_arrow_drop_up);
        holder.ivBid.setImageResource(Float.parseFloat(bidDelta)<0?
                R.drawable.ic_arrow_drop_down:
                R.drawable.ic_arrow_drop_up);
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
    public void addAll(List<Currency> list) {
        itemList.addAll(list);
        notifyDataSetChanged();
    }

    void animateTo(List<Currency> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }


    private void applyAndAnimateRemovals(List<Currency> newModels) {
        for (int i = itemList.size() - 1; i >= 0; i--) {
            final Currency model = itemList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Currency> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Currency model = newModels.get(i);
            if (!itemList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Currency> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Currency model = newModels.get(toPosition);
            final int fromPosition = itemList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    private Currency removeItem(int position) {
        final Currency model = itemList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    private void addItem(int position, Currency model) {
        itemList.add(position, model);
        notifyItemInserted(position);
    }

    private void moveItem(int fromPosition, int toPosition) {
        final Currency model = itemList.remove(fromPosition);
        itemList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }


}