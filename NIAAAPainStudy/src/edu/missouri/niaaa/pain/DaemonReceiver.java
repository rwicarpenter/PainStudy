package edu.missouri.niaaa.pain;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import edu.missouri.niaaa.pain.activity.DialogActivity;
import edu.missouri.niaaa.pain.location.LocationUtilities;

/**
 * @author Chen
 *
 * @params
 *
 */
public class DaemonReceiver extends BroadcastReceiver {

    final static String TAG = "DaemonReceiver.java";
    boolean logEnable = true;

    long tolerace = 60*1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Util.Log_debug(TAG, "on receiver daemon");

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int fun = intent.getIntExtra(Util.BD_ACTION_DAEMON_FUNC, 0);

        if(fun == 0){//set alarm
            Util.Log_debug(TAG, "on receiver daemon 0");
            //Noon
            Intent itTrigger1 = new Intent(Util.BD_ACTION_DAEMON);
            itTrigger1.putExtra(Util.BD_ACTION_DAEMON_FUNC, 1);//int
            PendingIntent piTrigger1 = PendingIntent.getBroadcast(context, 1, itTrigger1, Intent.FLAG_ACTIVITY_NEW_TASK);

            am.setExact(AlarmManager.RTC_WAKEUP, getProperTime(12, 20), piTrigger1);

            //Midnight
            Intent itTrigger2 = new Intent(Util.BD_ACTION_DAEMON);
            itTrigger2.putExtra(Util.BD_ACTION_DAEMON_FUNC, 2);//int
            PendingIntent piTrigger2 = PendingIntent.getBroadcast(context, 2, itTrigger2, Intent.FLAG_ACTIVITY_NEW_TASK);

            am.setExact(AlarmManager.RTC_WAKEUP, getProperTime(23, 59), piTrigger2);

            //Three oclock
            Intent itTrigger3 = new Intent(Util.BD_ACTION_DAEMON);
            itTrigger3.putExtra(Util.BD_ACTION_DAEMON_FUNC, 3);//int
            PendingIntent piTrigger3 = PendingIntent.getBroadcast(context, 3, itTrigger3, Intent.FLAG_ACTIVITY_NEW_TASK);

            am.setExact(AlarmManager.RTC_WAKEUP, getProperTime(3, 0), piTrigger3);

            // Ricky 9pm
            Intent itTrigger4 = new Intent(Util.BD_ACTION_DAEMON);
            itTrigger4.putExtra(Util.BD_ACTION_DAEMON_FUNC, 4);// int
            PendingIntent piTrigger4 = PendingIntent.getBroadcast(context, 4, itTrigger4, Intent.FLAG_ACTIVITY_NEW_TASK);

            am.setExact(AlarmManager.RTC_WAKEUP, getProperTime(21, 0), piTrigger4);

        }
        else if(fun == 1){//Noon
            Util.Log_debug(TAG, "on receiver daemon 1");

            //today at noon
            Util.morningComplete(context, true, true);

            Toast.makeText(context, "Noon daemon trigger random popups for you.", Toast.LENGTH_LONG).show();

            //Noon
            Intent itTrigger1 = new Intent(Util.BD_ACTION_DAEMON);
            itTrigger1.putExtra(Util.BD_ACTION_DAEMON_FUNC, 1);//int
            PendingIntent piTrigger1 = PendingIntent.getBroadcast(context, 1, itTrigger1, Intent.FLAG_ACTIVITY_NEW_TASK);

            am.setExact(AlarmManager.RTC_WAKEUP, getProperTime(12, 20), piTrigger1);
        }
        else if(fun == -1){//cancel noon
            Util.Log_debug(TAG, "on receiver daemon -1");

            Intent itTrigger1 = new Intent(Util.BD_ACTION_DAEMON);
            itTrigger1.putExtra(Util.BD_ACTION_DAEMON_FUNC, 1);//int
            PendingIntent piTrigger1 = PendingIntent.getBroadcast(context, 1, itTrigger1, Intent.FLAG_ACTIVITY_NEW_TASK);

            am.setExact(AlarmManager.RTC_WAKEUP, getProperTime(12, 20)+Util.getDayLong(), piTrigger1);
        }
        else if(fun == 2){//Midnight
            Util.Log_debug(TAG, "on receiver daemon 2");


            //cancel all survey (follow-ups are allowed base on new requirement)
//            Util.cancelSchedule(context);

            //reset sp
//          getSP(context, SP_RANDOM_TIME).edit().clear().commit();
//          getSP(context, SP_SURVEY).edit().clear().commit();


            Toast.makeText(context, "MIND close sensor and cancel all the survey", Toast.LENGTH_LONG).show();

            //Midnight
            Intent itTrigger2 = new Intent(Util.BD_ACTION_DAEMON);
            itTrigger2.putExtra(Util.BD_ACTION_DAEMON_FUNC, 2);//int
            PendingIntent piTrigger2 = PendingIntent.getBroadcast(context, 2, itTrigger2, Intent.FLAG_ACTIVITY_NEW_TASK);

            am.setExact(AlarmManager.RTC_WAKEUP, getProperTime(23, 59), piTrigger2);
        }
        else if(fun == 3){//three oclock
            Util.Log_debug(TAG, "on receiver daemon 3");

            //close location
            context.sendBroadcast(new Intent(LocationUtilities.ACTION_STOP_LOCATION));

            //next day at 3
            Util.rescheduleMorningSurvey(context);

            //cancel followup
//            Utilities.cancelTrigger(context);

            //reset sp
//          getSP(context, SP_RANDOM_TIME).edit().clear().commit();
//          getSP(context, SP_SURVEY).edit().clear().commit();

            Toast.makeText(context, "THRE cancel survey for you", Toast.LENGTH_LONG).show();

            //Three oclock
            Intent itTrigger3 = new Intent(Util.BD_ACTION_DAEMON);
            itTrigger3.putExtra(Util.BD_ACTION_DAEMON_FUNC, 3);//int
            PendingIntent piTrigger3 = PendingIntent.getBroadcast(context, 3, itTrigger3, Intent.FLAG_ACTIVITY_NEW_TASK);

            am.setExact(AlarmManager.RTC_WAKEUP, getProperTime(3, 0), piTrigger3);

            //reset all, send 0 broadcast??


        }
        else if(fun == -3){//cancel three oclock //useless for now
            Util.Log_debug(TAG, "on receiver daemon -3");

//          Intent itTrigger3 = new Intent(BD_ACTION_DAEMON);
//          itTrigger3.putExtra(BD_ACTION_DAEMON_FUNC, 3);//int
//          PendingIntent piTrigger3 = PendingIntent.getBroadcast(context, 3, itTrigger3, Intent.FLAG_ACTIVITY_NEW_TASK);
//
            //          am.set(AlarmManager.RTC_WAKEUP, getProperTime(3, 0)+getDayLong(), piTrigger3);
        }
        else if (fun == 4) {// 9pm alarm dialog
            Intent timeoutIntent = new Intent(context, DialogActivity.class);
            timeoutIntent.putExtra(DialogActivity.DIALOG_FLAG, DialogActivity.DIALOG_CHARGE_REMIND);
            context.startActivity(timeoutIntent);

            //
            Intent itTrigger4 = new Intent(Util.BD_ACTION_DAEMON);
            itTrigger4.putExtra(Util.BD_ACTION_DAEMON_FUNC, 4);// int
            PendingIntent piTrigger4 = PendingIntent.getBroadcast(context, 4, itTrigger4, Intent.FLAG_ACTIVITY_NEW_TASK);

            am.setExact(AlarmManager.RTC_WAKEUP, getProperTime(21, 0), piTrigger4);

            Toast.makeText(context, "Reseting the 9pm reminder for tomorrow", Toast.LENGTH_LONG).show();

        }
        else{

        }

    }

    private long getProperTime(int hour, int minute){
        Calendar c = Calendar.getInstance();
        Calendar s = Calendar.getInstance();
        s.set(Calendar.HOUR_OF_DAY, hour);
        s.set(Calendar.MINUTE, minute);
        s.set(Calendar.SECOND, 0);
        s.set(Calendar.MILLISECOND, 0);
        if(c.after(s)){
            return s.getTimeInMillis() + Util.getDayLong();
        }
        return s.getTimeInMillis();
    }

}
