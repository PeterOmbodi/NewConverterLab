package com.peterombodi.newconverterlab.presentation.screen.presenter;

import android.util.Log;

import com.peterombodi.newconverterlab.data.model.DataResponse;
import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.data.model.UpdateInfo;
import com.peterombodi.newconverterlab.domain.IDomain;
import com.peterombodi.newconverterlab.presentation.ObjectGraph;
import com.peterombodi.newconverterlab.presentation.R;
import com.peterombodi.newconverterlab.presentation.screen.IListFragment;
import com.peterombodi.newconverterlab.presentation.screen.ResponseCallback;

import java.util.ArrayList;

import static com.peterombodi.newconverterlab.global.Constants.FORMAT_LOCAL_DATE_SHORT;
import static com.peterombodi.newconverterlab.global.Constants.FORMAT_SERVER_DATE;
import static com.peterombodi.newconverterlab.utils.Utils.transformDate;

/**
 * Created by Admin on 23.11.2016.
 */

public class BankListPresenter implements IListFragment.IPresenter, ResponseCallback<DataResponse> {

	private static final String TAG = "BankListPresenter";
	private IListFragment.IView mView;
	private IDomain mDomain;
	private ObjectGraph mGraph;
	private boolean isRefreshData;

	@Override
	public void registerView(IListFragment.IView _view) {
		this.mView = _view;
		if (mDomain == null) {
			mGraph = ObjectGraph.getInstance();
			mDomain = mGraph.getDomainModule();
			mDomain.setCallback(this);
		}
	}

	@Override
	public void unRegisterView() {
		mView = null;
	}

	@Override
	public void presenterSetRV(ArrayList<OrganizationRV> _rvArrayList) {
		if (mView != null) mView.setRvArrayList(_rvArrayList);
	}

	@Override
	public void refreshData() {
		isRefreshData = true;
		mGraph = ObjectGraph.getInstance();
		mDomain = mGraph.getDomainModule();
		mDomain.getData(this, null);
	}

	@Override
	public void loadDbData(String _filter) {
		if (mView != null && !isRefreshData) mView.getDbData(_filter);
	}

	@Override
	public void onLoadDbData(ArrayList<OrganizationRV> _rvArrayList) {
		if (_rvArrayList != null) {
			if (_rvArrayList.size() == 0 && !mView.isSearching()) {
				refreshData();
			} else {
				presenterSetRV(_rvArrayList);
			}
			loadDbUpdateInfo();
		} else {
			mView.showMessage(R.string.msg_no_data_geted);
		}
	}

	@Override
	public void loadDbUpdateInfo() {
		mView.getDbUpdateInfo();
	}

	@Override
	public void onLoadDbUpdateInfo(UpdateInfo updateInfo) {
		if (updateInfo != null) {
			if (updateInfo.getUpdateDate() != null)
				mView.showUpdateInfo(transformDate(updateInfo.getUpdateDate(), FORMAT_SERVER_DATE, FORMAT_LOCAL_DATE_SHORT), updateInfo.getUpdateCount());

		} else {
			mView.showMessage(R.string.msg_no_data_geted);
		}
	}

	@Override
	public void onRefreshResponse(DataResponse _data) {
		Log.d(TAG, "onRefreshResponse getOrganizations().size() = " + _data.getOrganizations().size() + " / " + _data.getDate());
		if (mView != null)
			mView.showUpdateInfo(transformDate(_data.getDate(), FORMAT_SERVER_DATE, FORMAT_LOCAL_DATE_SHORT), _data.getOrganizations().size());
	}

	@Override
	public void onSavedData(int _records, String _bankId, String _updateDate) {
		isRefreshData = false;
		if (mView != null) mView.getDbData(null);
	}

	@Override
	public void onSaveRefresh(int _itemNo, int _itemTotal) {
		if (mView != null) mView.showProgress(_itemNo, _itemTotal);
	}

	@Override
	public void onRefreshFailure() {
	}

	@Override
	public void presenterOpenDetail(OrganizationRV _organizationRV) {
		mView.viewOpenDetail(_organizationRV);
	}

	@Override
	public void presenterOpenLink(String _url) {
		mView.viewOpenLink(_url);
	}

	@Override
	public void presenterOpenMap(String _region, String _city, String _address, String _title) {
		mView.viewOpenMap(_region, _city, _address, _title);
	}

	@Override
	public void presenterOpenCaller(String _phone) {
		mView.viewOpenCaller(_phone);
	}


}
