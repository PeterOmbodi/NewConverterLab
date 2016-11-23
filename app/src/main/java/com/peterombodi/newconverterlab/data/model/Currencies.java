package com.peterombodi.newconverterlab.data.model;

import java.util.HashMap;

/**
 * Created by Admin on 17.11.2016.
 */

public class Currencies {

    private HashMap<String, Currency> currencyHashMap;

    public Currencies() {
    }

    public HashMap<String, Currency> getCurrencyHashMap() {
        return currencyHashMap;
    }

    public void setCurrencyHashMap(HashMap<String, Currency> currencyHashMap) {
        this.currencyHashMap = currencyHashMap;
    }

    @Override
    public String toString() {
        return "Currencies{" +
                "currencyHashMap=" + currencyHashMap +
                '}';
    }
}
