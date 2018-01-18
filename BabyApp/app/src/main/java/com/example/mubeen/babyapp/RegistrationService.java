package com.example.mubeen.babyapp;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by Mubeen on 6/5/2017.
 */

public class RegistrationService extends IntentService {
    public RegistrationService() {
        super ("RegistrationService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID myid = InstanceID.getInstance(getApplicationContext());
        try {

            String registrationToken = myid.getToken(
                    getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                    null

            );
            Log.d("Token No",registrationToken);
            GcmPubSub subscription = GcmPubSub.getInstance(this);
            subscription.subscribe(registrationToken, "/topics/myApp", null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
