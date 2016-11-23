package com.peterombodi.newconverterlab.data.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 17.11.2016.
 */

public class DataResponse {
    private String date;
    private ArrayList<Organization> organizations;
    private HashMap<String, String> orgTypes;
    private HashMap<String, String> currencies;
    private HashMap<String, String> regions;
    private HashMap<String, String> cities;

    public DataResponse() {}

    public DataResponse(String date) {}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(ArrayList<Organization> organizations) {
        this.organizations = organizations;
    }

    public HashMap<String, String> getOrgTypes() {
        return orgTypes;
    }

    public void setOrgTypes(HashMap<String, String> orgTypes) {
        this.orgTypes = orgTypes;
    }

    public HashMap<String, String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(HashMap<String, String> currencies) {
        this.currencies = currencies;
    }

    public HashMap<String, String> getRegions() {
        return regions;
    }

    public void setRegions(HashMap<String, String> regions) {
        this.regions = regions;
    }

    public HashMap<String, String> getCities() {
        return cities;
    }

    public void setCities(HashMap<String, String> cities) {
        this.cities = cities;
    }

    @Override
    public String toString() {
        return "DataResponse{" +
                "date='" + date + '\'' +
                ", organizations=" + organizations +
                ", orgTypes=" + orgTypes +
                ", currencies=" + currencies +
                ", regions=" + regions +
                ", cities=" + cities +
                '}';
    }
}
