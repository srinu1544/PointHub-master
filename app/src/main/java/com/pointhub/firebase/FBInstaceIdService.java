package com.pointhub.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Venu gopal on 03-11-2016.
 */

public class FBInstaceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "InstanceIdService";

    @Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();
        String userMailaid = FirebaseInstanceId.getInstance().getId();
        Log.e(TAG, "usermailid: >>>>>>" + userMailaid);

        Log.e(TAG, "Got token: " + token);

        // If you want to send messages to this application instance or manage this apps subscriptions on the server side, send the Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {

        // TODO: Implement this method to send token to your app server.
    }

}
