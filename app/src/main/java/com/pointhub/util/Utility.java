package com.pointhub.util;

import android.content.Context;

import com.google.gson.Gson;
import com.pointhub.PointHubMessage;
import com.pointhub.db.DatabaseHelper;
import com.pointhub.db.Points;

import java.util.ArrayList;

/**
 * Created by Venu gopal on 17-11-2016.
 */

public class Utility {


    static Gson gson = null;

    static boolean testing = false;

    public static synchronized Gson getGsonObject() {

        if(null == gson){
            gson = new Gson();
        }

        return gson;
    }

    public static synchronized boolean isTesting () {
        return testing;
    }

    public static synchronized boolean savePointsToMobile(Context context, String pointString) {
        boolean success = false;
         try{

             PointHubMessage pointHubMessage = getGsonObject().fromJson(pointString, PointHubMessage.class);
             success = saveToDB(context,pointHubMessage);

             success = true;
           }catch (Exception ex){
            ex.printStackTrace();
        }
        return success;
    }

    public static boolean saveToDB(Context context, PointHubMessage pointHubMessage) {
        boolean success = false;
        try {

            String type = pointHubMessage.getType();
            String storeName = pointHubMessage.getStoreName();
            String presentPoints = pointHubMessage.getPoints();

            DatabaseHelper dbHelper = new DatabaseHelper(context);

            boolean newStore = true;

            ArrayList<Points> storePoints = dbHelper.getAllPoints(storeName);
            int sPoints = 0;
            if(null == storePoints || storePoints.size()==0) {
                sPoints = 0;
            } else {
                newStore = false;
                for(Points pts:storePoints){
                    try {
                        int p = Integer.parseInt(pts.getPoints());
                        sPoints = sPoints + p;
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }


            Integer calculatedPoints=null;
            if("Earn".equalsIgnoreCase(type)){
                calculatedPoints  = addPoints(sPoints, presentPoints);
            } else {
                calculatedPoints  = deductPoints(sPoints, presentPoints);
            }

            String lastUpdate = DatabaseHelper.getInstance(context).getDateTime();
            Points updatedPoints = new Points(storeName, calculatedPoints.toString(), lastUpdate);

            if(newStore)
                dbHelper.createPoints(updatedPoints);
            else
                dbHelper.updatePoints(updatedPoints);

            success = true;

    }catch (Exception ex){
        ex.printStackTrace();
    }
    return success;
    }

    private static Integer deductPoints(int storePoints, String presentPoints) {

        int pp=Integer.parseInt(presentPoints);
        int deducedPoints=storePoints-pp;
        return deducedPoints;
    }

    private static Integer addPoints(int storePoints, String presentPoints) {

        int pp=Integer.parseInt(presentPoints);
        int addPoints=storePoints+pp;
        return addPoints;
    }

}
