package edu.missouri.niaaa.pain;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import edu.missouri.niaaa.pain.activity.DialogActivity;

/**
 * @author Chen
 *
 * @params
 *
 */
public class DaemonReceiver extends BroadcastReceiver {

    final static String TAG = "DaemonReceiver.java";
    boolean logEnable = true;

    public static final String BD_ACTION_DAEMON_FUNC    = "Intent_Daemon";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Util.Log_debug(TAG, "on receiver daemon");

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int fun = intent.getIntExtra(BD_ACTION_DAEMON_FUNC, 0);

        String ID = Util.getSP(context, Util.SP_LOGIN).getString(Util.SP_LOGIN_KEY_USERID, "");
        String PWD = Util.getSP(context, Util.SP_LOGIN).getString(Util.SP_LOGIN_KEY_USERPWD, "");

        if(ID.equals("") || PWD.equals("")){
            //bypass daemon if no id and pwd assigned

        }
        else if(fun == 0){//set alarm
            Util.Log_debug(TAG, "on receiver daemon 0");
            //Noon
            setAlarm(context, am, 1, getProperTime(12, 20));

            //Midnight
            setAlarm(context, am, 2, getProperTime(23, 59));

            //Three oclock
            setAlarm(context, am, 3, getProperTime(3, 0));

            // Ricky 9pm
            setAlarm(context, am, 4, getProperTime(21, 0));

        }
        else if(fun == 1){//Noon
            Util.Log_debug(TAG, "on receiver daemon 1");

            //today at noon
            Util.morningComplete(context, true, true);

            Toast.makeText(context, "Noon daemon trigger random schedules", Toast.LENGTH_LONG).show();

            //Noon
            setAlarm(context, am, 1, getProperTime(12, 20));

            Util.debugDT("noon", getProperTime(12, 20));
        }
        else if(fun == -1){//cancel noon
            Util.Log_debug(TAG, "on receiver daemon -1");

            setAlarm(context, am, 1, getProperTime(12, 20));

            Util.debugDT("cancel noon", getProperTime(12, 20));
        }
        else if(fun == 2){//Midnight
            Util.Log_debug(TAG, "on receiver daemon 2");

            //close sensor

            //##??
            //cancel all survey (follow-ups are allowed base on new requirement)
//            Util.cancelSchedule(context);

            //reset sp
//          getSP(context, SP_RANDOM_TIME).edit().clear().commit();
//          getSP(context, SP_SURVEY).edit().clear().commit();


            Toast.makeText(context, "MIDNIGHT close sensor and cancel survey", Toast.LENGTH_LONG).show();

            //Midnight
            setAlarm(context, am, 2, getProperTime(23, 59));
        }
        else if(fun == 3){//three o'clock
            Util.Log_debug(TAG, "on receiver daemon 3");

            //close location
            Util.stopRecordingLocation(context);

            //next day at 3
            Util.rescheduleMorningSurvey(context);

            //cancel followup
//            Utilities.cancelTrigger(context);

            //reset sp
//          getSP(context, SP_RANDOM_TIME).edit().clear().commit();
//          getSP(context, SP_SURVEY).edit().clear().commit();

            Toast.makeText(context, "THREE'O close gps", Toast.LENGTH_LONG).show();

            //Three o'clock
            setAlarm(context, am, 3, getProperTime(3, 0));

            //reset all, send 0 broadcast??

        }
        else if(fun == -3){//cancel three o'clock //useless for now
            Util.Log_debug(TAG, "on receiver daemon -3");

//          Intent itTrigger3 = new Intent(BD_ACTION_DAEMON);
//          itTrigger3.putExtra(BD_ACTION_DAEMON_FUNC, 3);//int
//          PendingIntent piTrigger3 = PendingIntent.getBroadcast(context, 3, itTrigger3, PendingIntent.FLAG_CANCEL_CURRENT);
//
            //          am.set(AlarmManager.RTC_WAKEUP, getProperTime(3, 0)+getDayLong(), piTrigger3);
        }
        else if (fun == 4) {// 9pm alarm dialog
            Intent dialogIntent = new Intent(context, DialogActivity.class);
            dialogIntent.putExtra(DialogActivity.DIALOG_FLAG, DialogActivity.DIALOG_CHARGE_REMIND);

            dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(dialogIntent);

            Toast.makeText(context, "Reseting the 9pm reminder for tomorrow", Toast.LENGTH_LONG).show();

            //
            setAlarm(context, am, 4, getProperTime(21, 0));
//            Intent itTrigger4 = new Intent(Util.BD_ACTION_DAEMON);
//            itTrigger4.putExtra(BD_ACTION_DAEMON_FUNC, 4);// int
//            PendingIntent piTrigger4 = PendingIntent.getBroadcast(context, 4, itTrigger4, PendingIntent.FLAG_CANCEL_CURRENT);
//
//            Util.setAlarmExact(am, getProperTime(21, 0), piTrigger4);
        }
        else{
            Util.Log_debug(TAG, "on receiver daemon else");
        }

    }

    private void setAlarm(Context context, AlarmManager am, int num, long time){
        Intent itTrigger = new Intent(Util.BD_ACTION_DAEMON);
        itTrigger.putExtra(BD_ACTION_DAEMON_FUNC, num);// int
        PendingIntent piTrigger = PendingIntent.getBroadcast(context, num, itTrigger, PendingIntent.FLAG_CANCEL_CURRENT);

        Util.setAlarmExact(am, time, piTrigger);
    }

    private long getProperTime(int hour, int minute){
        Calendar c = Calendar.getInstance();
        Calendar s = Calendar.getInstance();
        s.set(Calendar.HOUR_OF_DAY, hour);
        s.set(Calendar.MINUTE, minute);
        s.set(Calendar.SECOND, 0);
        s.set(Calendar.MILLISECOND, 0);
        if(c.after(s)){
            return s.getTimeInMillis() + getDayLong();
        }
        return s.getTimeInMillis();
    }

    public static long getDayLong(){
        return 24*60*60*1000;
    }
}
