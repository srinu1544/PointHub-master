package com.pointhub.db;

/**
 * Created by Lenovo1 on 18-12-2016.
 */

public class FirebaseData {

    String storename;
    String points;
    String billamount;

    public FirebaseData() {


    }

    public FirebaseData(String storename, String points, String billamount) {
        this.storename = storename;
        this.points = points;
        this.billamount = billamount;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getBillamount() {
        return billamount;
    }

    public void setBillamount(String billamount) {
        this.billamount = billamount;
    }
}
