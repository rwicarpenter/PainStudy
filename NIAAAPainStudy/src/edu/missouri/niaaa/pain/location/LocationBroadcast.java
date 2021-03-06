package edu.missouri.niaaa.pain.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import edu.missouri.niaaa.pain.Util;

public class LocationBroadcast extends BroadcastReceiver {

    String TAG = "Location Broadcast";
    public static LocationManager locationM;
    public static String ID;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub


        String action = intent.getAction();
        locationM = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        ID = Util.getSP(context, Util.SP_LOGIN).getString(Util.SP_LOGIN_KEY_USERID, "");

        try {
            LocationUtilities.publicKey = Util.getPublicKey(context);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if(action.equals(LocationUtilities.ACTION_START_LOCATION)){
            Util.Log_debug(TAG, "location recording start");
            LocationUtilities.requestLocation(locationM);

            /*acquire wake lock*/
        }

        else if(action.equals(LocationUtilities.ACTION_STOP_LOCATION)){
            Util.Log_debug(TAG, "location recording stop");
            LocationUtilities.removeLocation(locationM);

            /*release wake lock*/
        }
    }

}
