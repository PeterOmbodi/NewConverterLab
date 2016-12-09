package com.peterombodi.newconverterlab.presentation.screen.organisationDetail.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.peterombodi.newconverterlab.presentation.R;
import com.peterombodi.newconverterlab.presentation.screen.organisationDetail.IDetailFragment;


/**
 * Created by Admin on 27.04.2016.
 */
public class CourseRVHolders extends RecyclerView.ViewHolder {

    public TextView tvCurrency;
    public TextView tvAsk;
    public TextView tvBid;
    public TextView tvAskDelta;
    public TextView tvBidDelta;
    public ImageView ivAsk;
    public ImageView ivBid;
    private IDetailFragment.IPresenter iPresenter;

    public CourseRVHolders(View itemView) {
        super(itemView);

        tvCurrency = (TextView) itemView.findViewById(R.id.tv_currency_IC);
        tvAsk = (TextView) itemView.findViewById(R.id.tv_ask_IC);
        tvBid = (TextView) itemView.findViewById(R.id.tv_bid_IC);
        tvAskDelta = (TextView) itemView.findViewById(R.id.tv_ask_delta_IC);
        tvBidDelta = (TextView) itemView.findViewById(R.id.tv_bid_delta_IC);
        ivAsk = (ImageView) itemView.findViewById(R.id.iv_ask_IC);
        ivBid = (ImageView) itemView.findViewById(R.id.iv_bid_IC);
    }

}
