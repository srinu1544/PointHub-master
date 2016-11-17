package com.pointhub.gcm;

import com.google.gson.Gson;
import com.pointhub.PointHubMessage;
import com.pointhub.util.Utility;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Venu gopal on 16-11-2016.
 */

public class GCMToken {

    // API key will be found in your firebase console --> your project --> settings --> cloud messaging --> server key.
    private static String API_KEY = "AIzaSyD447hNzkrGgpwTSjRjrVk4KliJn1hDPKQ";

    public static void sendNotification(PointHubMessage msg) {
        try {

            String storeId = msg.getUserName();
            String sellerRegisteredToken = getTokenOfSeller(storeId);
            Gson gson = Utility.getGsonObject();
            String message = gson.toJson(msg);

            // Send notification.
            sendNotification(sellerRegisteredToken, message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String getTokenOfSeller(String storeId) {

        String token = null;
        try {

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return token;
    }

    private static String sendNotification(String to, String msg) {

        String response = "";
        try {
            // Prepare JSON containing the GCM message content. What to send and where to send.
            JSONObject jGcmData = new JSONObject();
            JSONObject jData = new JSONObject();

            jData.put("message", msg);
            jGcmData.put("to", to);
            // What to send in GCM message.
            jGcmData.put("data", jData);

            // Create connection to send GCM Message request.
            URL url = new URL("https://android.googleapis.com/gcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Send GCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jGcmData.toString().getBytes());

            // Read GCM response.
            InputStream inputStream = conn.getInputStream();
            response = getStringFromInputStream(inputStream);
            System.out.println(response);
            System.out.println("Check your device/emulator for notification or logcat for confirmation of the receipt of the GCM message.");

        } catch (Exception e) {

            //System.out.println("Unable to send GCM message.");
            // System.out.println("Please ensure that API_KEY has been replaced by the server " + "API key, and that the device's registration token is correct (if specified).");
            e.printStackTrace();
        }
        return response;
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

}
