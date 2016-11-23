package com.peterombodi.newconverterlab.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Admin on 22.11.2016.
 */

public class OrganizationRV implements Parcelable {

    private String id;
    private String title;
    private String region;
    private String city;
    private String phone;
    private String address;
    private String link;
    private String date;
    private String dateDelta;

    public OrganizationRV() {
    }

    public OrganizationRV(String id, String title, String region, String city, String phone, String address, String link, String date, String dateDelta) {
        this.id = id;
        this.title = title;
        this.region = region;
        this.city = city;
        this.phone = phone;
        this.address = address;
        this.link = link;
        this.date = date;
        this.dateDelta = dateDelta;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateDelta() {
        return dateDelta;
    }

    public void setDateDelta(String dateDelta) {
        this.dateDelta = dateDelta;
    }

    @Override
    public String toString() {
        return "OrganizationRV{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", region='" + region + '\'' +
                ", city='" + city + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", link='" + link + '\'' +
                ", date='" + date + '\'' +
                ", dateDelta='" + dateDelta + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.region);
        dest.writeString(this.city);
        dest.writeString(this.phone);
        dest.writeString(this.address);
        dest.writeString(this.link);
        dest.writeString(this.date);
        dest.writeString(this.dateDelta);
    }

    protected OrganizationRV(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.region = in.readString();
        this.city = in.readString();
        this.phone = in.readString();
        this.address = in.readString();
        this.link = in.readString();
        this.date = in.readString();
        this.dateDelta = in.readString();
    }

    public static final Parcelable.Creator<OrganizationRV> CREATOR = new Parcelable.Creator<OrganizationRV>() {
        @Override
        public OrganizationRV createFromParcel(Parcel source) {
            return new OrganizationRV(source);
        }

        @Override
        public OrganizationRV[] newArray(int size) {
            return new OrganizationRV[size];
        }
    };
}
