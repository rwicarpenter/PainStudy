package edu.missouri.niaaa.pain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Util {
    static String TAG = "Util.java";
    
    public static final String  ADMIN_UID = "0000";
    
    
    
    
    /*for debug*/
    public static final boolean DEBUG_LIFECYCLE = true;
    public static final boolean DEBUG           = true;
    public static final boolean RELEASE         = false;
    
    
    /*  survey config*/
    public final static int MAX_REMINDER = 3;
    public final static int MAX_TRIGGER_MORNING = 1;//1
    public final static int MAX_TRIGGER_RANDOM = 6;//6
    public final static int MAX_TRIGGER_FOLLOWUP = 3;//3
    public final static int VOLUME = 8;//10
    public final static String PHONE_BASE_PATH = "sdcard/TestResult_craving/";
    
    
    
    
    /*constant value*/
    public static final String PKG_BASE = "edu.missouri.niaaa.pain.";
    public static final String REBOOT = "Intent_Reboot"; 
    /**
     *  0 - </br>
     *  1 - 
     * -1 - </br>
     *  2 - </br>
     * -2 - 
     *  3 - </br>
     * -3 - 
     *  4 - </br>
     */
    public static final String BD_ACTION_DAEMON_FUNC    = "Intent_Daemon";
    
    
    public static final String BD_ACTION_SURVEY_FUNC    = "Intent_Survey";
//    String schedule
    
    
    
    /*sharedPreference*/
    public static final String SP_BASE = PKG_BASE;
    public static final String SP_LOGIN                     = SP_BASE + "LOGIN";
    public static final String SP_LOGIN_KEY_STUDY_STARTTIME = SP_BASE + "STUDY_DAY_START";
    public static final String SP_LOGIN_KEY_USERID          = SP_BASE + "USER_ID";
    public static final String SP_LOGIN_KEY_USERPWD         = SP_BASE + "USER_PWD";
    
    
    
    
    
    
    /*broadcast actions*/
    public static final String BD_ACTION_BASE           = PKG_BASE;//+"action.";
    
    public static final String BD_ACTION_DAEMON         = BD_ACTION_BASE    + "DAEMON";
    
    public static final String BD_ACTION_SURVEY_TRIGGER = BD_ACTION_BASE    + "SURVEY_TRIGGER";
    public static final String BD_ACTION_SURVEY_REMINDS = BD_ACTION_BASE    + "SURVEY_REMINDS";
    
    
    public static final String BD_ACTION_SUSPENSION     = BD_ACTION_BASE    + "SUSPENSION";
    
    
    public final static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    
    
    
    
    /**
     * Logs to debug system life cycle, which is triggered by system inherently.
     *
     * @param s1 Class name
     * @param s2 Name of life cycle function, contain "~~~" for logcat messages filtering
     */
    public static void Log_lifeCycle(String s1, String s2){
        if(DEBUG_LIFECYCLE) {
            Log.d(s1,s2);
        }
    }

    /**
     * @param s2 contain "---" for logcat filtering if needed.
     */
    public static void Log_debug(String s1, String s2){
        if(DEBUG){
            Log.d(s1,s2);
        }
    }
    
    public static void Log_debug(String s1, boolean enableByClass, String s2){
        if(enableByClass) {
            Log_debug(s1,s2);
        }
    }

//    public static void Log_debug(String s1, boolean enableByClass, String s2, boolean enable){
//        if(enable)
//            Log_debug(s1, enableByClass, s2);
//    }
    
    
    
    public final static int CODE_SCHEDULE_MANUALLY = 10;
    public final static int CODE_SCHEDULE_AUTOMATIC = 11;
    
    /* random */
    public static void scheduleRandomSurvey(Context context, boolean startFromNoon, boolean autoTriggered) {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 23);//23
        c.set(Calendar.MINUTE, 59);//59

        long base = Calendar.getInstance().getTimeInMillis();
        if (startFromNoon) {
            Calendar b = Calendar.getInstance();
            b.set(Calendar.HOUR_OF_DAY, 12);
            b.set(Calendar.MINUTE, 0);
            b.set(Calendar.SECOND, 0);
            base = b.getTimeInMillis();
        }
        long peak = c.getTimeInMillis();

        long unit = (peak-base)/(MAX_TRIGGER_RANDOM+1);//7
        long r_unit = (peak-base)/((MAX_TRIGGER_RANDOM+1)*3);//21

        String random_schedule = new String();

        for(int i=1;i<MAX_TRIGGER_RANDOM+1;i++){//7
            random_schedule = random_schedule + (base+unit*i+(new Random().nextInt((int) (2*r_unit))-r_unit)+",");
        }


        //write to file random schedule
        String strArr[] = random_schedule.split(",");

        if(strArr.length != 1){
            int i =0;
            for(String str: strArr){
                Calendar c2 = Calendar.getInstance();
                c2.setTimeInMillis(Long.parseLong(str));
                Log.d("Random Schedule ", "each item is "+str+" "+c2.get(Calendar.HOUR_OF_DAY)+":"+c2.get(Calendar.MINUTE));

                strArr[i] = sdf.format(c2.getTime());
                i++;
                
                
                Intent itTrigger = new Intent(Util.BD_ACTION_SURVEY_TRIGGER);
                itTrigger.putExtra(Utilities.SV_NAME, i);
                PendingIntent piTrigger = PendingIntent.getBroadcast(context, i, itTrigger, Intent.FLAG_ACTIVITY_NEW_TASK);
                long time = Long.parseLong(str);
                am.setExact(AlarmManager.RTC_WAKEUP, time, piTrigger);
            }

//            try {
//                //craving gonna be different
////                writeEventToFile(context, (autoTriggered ? CODE_SCHEDULE_AUTOMATIC : CODE_SCHEDULE_MANUALLY),
////                        strArr[0], strArr[1], strArr[2], strArr[3], strArr[4], strArr[5]);
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
        }

        Utilities.getSP(context, Utilities.SP_RANDOM_TIME).edit().putString(Utilities.SP_KEY_RANDOM_TIME_SET, random_schedule).commit();

    }

    
    /*************************************************************************************************************/
    /*Morning*/
    
    public static void ScheduleMorning(Context context){
        
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        
        //default time to 12:00 at noon
        Calendar c = Utilities.getDefaultMorningCal(context);
        long defTime = c.getTimeInMillis();
        long time = Long.MAX_VALUE;

        time = Calendar.getInstance().getTimeInMillis();//Utilities.getSP(context, Utilities.SP_BED_TIME).getLong(Utilities.SP_KEY_BED_TIME_LONG, defTime);
        Util.Log_debug(TAG, "---MorningSruvey scheduled at "+Utilities.getTimeFromLong(time));
        
        Intent itTrigger = new Intent(Util.BD_ACTION_SURVEY_TRIGGER);
        itTrigger.putExtra(Utilities.SV_NAME, Utilities.SV_NAME_MORNING);
        PendingIntent piTrigger = PendingIntent.getBroadcast(context, 1, itTrigger, PendingIntent.FLAG_CANCEL_CURRENT);
        
        am.setExact(AlarmManager.RTC_WAKEUP, time, piTrigger);
    }
    
    public static void RescheduleMorning(Context context){
        
    }
    
    /*/Morning*/
    
    
    /**
     * when app reboot, check if there is any suspension at the time phone shut down.
     * if it still under that time period, set suspension again,
     * if it expired, clear the suspension flag.
     */
    public static void restoreSuspension(){
        
    }
    
}