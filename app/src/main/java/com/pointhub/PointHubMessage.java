package com.pointhub;

/**
 * Created by Provigil on 05-06-2016.
 */
public class PointHubMessage {

    String type;
    String billAmount;
    String storeName;
    String points;
    String deviceid;

    private PointHubMessage(){

    }

    public PointHubMessage(String type, String billAmount, String deviceid, String storeName, String points){
        this.type = type;
        this.billAmount = billAmount;
        this.deviceid=deviceid;
        this.storeName = storeName;
        this.points = points;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setstoreName(String userName) {
        this.storeName = storeName;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}