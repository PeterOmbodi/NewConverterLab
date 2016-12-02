package com.peterombodi.newconverterlab.data.model;

public class Currency {
    private String ask;
    private String bid;

    private String currency;
    private String askDelta;
    private String bidDelta;

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

    @Override
    public String toString() {
        return "Currency{" +
                "ask='" + ask + '\'' +
                ", bid='" + bid + '\'' +
                ", currency='" + currency + '\'' +
                ", askDelta='" + askDelta + '\'' +
                ", bidDelta='" + bidDelta + '\'' +
                '}';
    }
}
