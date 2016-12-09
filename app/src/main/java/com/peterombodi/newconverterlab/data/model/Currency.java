package com.peterombodi.newconverterlab.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Currency implements Parcelable {
    private String ask;
    private String bid;

    private String currency;
    private String askDelta;
    private String bidDelta;
    private String currencyId;

    public Currency() {
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAskDelta() {
        return askDelta;
    }

    public void setAskDelta(String askDelta) {
        this.askDelta = askDelta;
    }

    public String getBidDelta() {
        return bidDelta;
    }

    public void setBidDelta(String bidDelta) {
        this.bidDelta = bidDelta;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "ask='" + ask + '\'' +
                ", bid='" + bid + '\'' +
                ", currency='" + currency + '\'' +
                ", askDelta='" + askDelta + '\'' +
                ", bidDelta='" + bidDelta + '\'' +
                ", currencyId='" + currencyId + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ask);
        dest.writeString(this.bid);
        dest.writeString(this.currency);
        dest.writeString(this.askDelta);
        dest.writeString(this.bidDelta);
        dest.writeString(this.currencyId);
    }

    protected Currency(Parcel in) {
        this.ask = in.readString();
        this.bid = in.readString();
        this.currency = in.readString();
        this.askDelta = in.readString();
        this.bidDelta = in.readString();
        this.currencyId = in.readString();
    }

    public static final Parcelable.Creator<Currency> CREATOR = new Parcelable.Creator<Currency>() {
        @Override
        public Currency createFromParcel(Parcel source) {
            return new Currency(source);
        }

        @Override
        public Currency[] newArray(int size) {
            return new Currency[size];
        }
    };
}
