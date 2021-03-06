package com.peterombodi.newconverterlab.presentation.screen.recycleView;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.presentation.R;
import com.peterombodi.newconverterlab.presentation.screen.IListFragment;

/**
 * Created by Admin on 22.11.2016.
 */

public class BankRVHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = "BankRVHolders";
    public TextView tvId;
    public TextView tvName;
    public TextView tvRegion;
    public TextView tvCity;
    public TextView tvPhone;
    public TextView tvAddress;
    public TextView tvLink;
    public TextView tvDate;
    public TextView tvDateDelta;
    public TextView tvDateShort;

    private IListFragment.IPresenter iPresenter;
//    private IGetAction iPresenter;
public OrganizationRV organizationRV;
    private ImageButton ibLink;
    private ImageButton ibMap;
    private ImageButton ibCall;
    private ImageButton ibNext;

    public BankRVHolders(View itemView, IListFragment.IPresenter iRecyclerView) {

        super(itemView);

        tvId = (TextView) itemView.findViewById(R.id.tv_id_ILO);
        tvName = (TextView) itemView.findViewById(R.id.tv_name_ILO);
        tvRegion = (TextView) itemView.findViewById(R.id.tv_region_ILO);
        tvCity = (TextView) itemView.findViewById(R.id.tv_city_ILO);
        tvPhone = (TextView) itemView.findViewById(R.id.tv_phone_ILO);
        tvAddress = (TextView) itemView.findViewById(R.id.tv_address_ILO);
        tvLink = (TextView) itemView.findViewById(R.id.tv_link_ILO);
        tvDate = (TextView) itemView.findViewById(R.id.tv_date_ILO);
        tvDateDelta = (TextView) itemView.findViewById(R.id.tv_date_delta_ILO);
        tvDateShort = (TextView) itemView.findViewById(R.id.tv_date_short_ILO);

        this.iPresenter = iRecyclerView;

        ibLink = (ImageButton) itemView.findViewById(R.id.ib_link_ILO);
        ibLink.setOnClickListener(this);
        ibMap = (ImageButton) itemView.findViewById(R.id.ib_map_ILO);
        ibMap.setOnClickListener(this);
        ibCall = (ImageButton) itemView.findViewById(R.id.ib_phone_ILO);
        ibCall.setOnClickListener(this);
        ibNext = (ImageButton) itemView.findViewById(R.id.ib_next_ILO);
        ibNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ib_link_ILO:
                iPresenter.presenterOpenLink(this.tvLink.getText().toString());
                break;
            case R.id.ib_map_ILO:
                iPresenter.presenterOpenMap(this.tvRegion.getText().toString(),
                        this.tvCity.getText().toString(),
                        this.tvAddress.getText().toString(),
                        this.tvName.getText().toString()
                );
                break;
            case R.id.ib_phone_ILO:
                iPresenter.presenterOpenCaller(this.tvPhone.getText().toString());
                break;
            case R.id.ib_next_ILO:
                Log.d(TAG,"presenterOpenDetail");
                iPresenter.presenterOpenDetail(this.organizationRV);
                break;

        }
    }

}
