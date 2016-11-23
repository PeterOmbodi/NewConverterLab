package com.peterombodi.newconverterlab.presentation.screen.organisation_list;

/**
 * Created by Admin on 22.11.2016.
 */

public interface IRecyclerView {
    void openDetail(String id, String name, String region, String city, String address, String phone, String link,
                    String date, String dateDelta);

    void openLink(String url);

    void openMap(String region, String city, String address);

    void openCaller(String phone);
}
