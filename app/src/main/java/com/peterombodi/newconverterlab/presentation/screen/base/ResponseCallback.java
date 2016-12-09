package com.peterombodi.newconverterlab.presentation.screen.base;

public interface ResponseCallback<V> {

    void onRefreshResponse(V _data);

    void onSavedData(int _records,String _bankId,String _updateDate);

    void onSaveRefresh(int _itemNo, int _itemTotal);

    void onRefreshFailure();

}
