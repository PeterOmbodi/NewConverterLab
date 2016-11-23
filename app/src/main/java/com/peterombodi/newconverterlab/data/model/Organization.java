package com.peterombodi.newconverterlab.data.model;

import java.util.HashMap;

/**
 * Created by Admin on 17.11.2016.
 */

public class Organization {
    private String id;
    private String title;
    private String regionId;
    private String cityId;
    private String phone;
    private String address;
    private String link;
    private String dateUpdate;
    private String datePrevUpdate;
    private HashMap<String, Currency> currencies;
    public HashMap<String, Currency> getCurrencies() {
        return currencies;
    }

    public Organization() {
    }

    public void setCurrencies(HashMap<String, Currency> currencies) {
        this.currencies = currencies;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getDatePrevUpdate() {
        return datePrevUpdate;
    }

    public void setDatePrevUpdate(String datePrevUpdate) {
        this.datePrevUpdate = datePrevUpdate;
    }



    @Override
    public String toString() {
        return "Organization{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", regionId='" + regionId + '\'' +
                ", cityId='" + cityId + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", link='" + link + '\'' +
                ", dateUpdate='" + dateUpdate + '\'' +
                ", datePrevUpdate='" + datePrevUpdate + '\'' +
                ", currencies=" + currencies +
                '}';
    }
}
