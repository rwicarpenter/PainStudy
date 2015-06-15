package edu.missouri.niaaa.pain.survey;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import edu.missouri.niaaa.pain.Util;

public class SurveyBroad extends BroadcastReceiver {
    String TAG = "SurveyBroad.java";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Util.Log_lifeCycle(TAG, "OnReceive~~~ "+intent.getAction()+" "+intent.getIntExtra(Util.SV_TYPE, -1));
        Util.Log_lifeCycle(TAG, "~~~seq is "+intent.getIntExtra(Util.SV_SEQ, -1));

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        String action = intent.getAction();

        int surveyType = intent.getIntExtra(Util.SV_TYPE, -1);//protect -1 later
        int surveySeq = intent.getIntExtra(Util.SV_SEQ, -1);//protect -1 later

        if(action.equals(Util.BD_ACTION_SURVEY_TRIGGER)){
            Util.Log_debug(TAG, "action~~~ "+action);
            
            Util.scheduleSurveyReminders(context, surveyType, surveySeq);
            
        }
        else if(action.equals(Util.BD_ACTION_SURVEY_REMINDS)){
            int remind_seq = intent.getIntExtra(Util.SV_REMIND_SEQ, -1);//protect -1 later
            Util.Log_debug(TAG, "action~~~ "+action+" "+intent.getIntExtra(Util.SV_SEQ, -1)+" "+intent.getIntExtra(Util.SV_REMIND_SEQ, -1));

            Intent launchSurvey = new Intent(context, SurveyAct.class);
            launchSurvey.putExtra(Util.SV_TYPE, surveyType);
            launchSurvey.putExtra(Util.SV_SEQ, surveySeq);
            launchSurvey.putExtra(Util.SV_REMIND_SEQ, remind_seq);

            launchSurvey.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            launchSurvey.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(launchSurvey);
        }
        else if(action.equals(Util.BD_ACTION_SURVEY_ISOLATE)){
            
        }

    }

}
