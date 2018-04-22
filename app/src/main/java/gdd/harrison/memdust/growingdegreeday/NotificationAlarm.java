package gdd.harrison.memdust.growingdegreeday;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;

public class NotificationAlarm extends BroadcastReceiver {

    PendingIntent sender;
    SharedPreferences prefs;
    GDDDataOrganizer gDDO;

    @Override
    public void onReceive(Context context, Intent intent) {
        prefs = context.getSharedPreferences("gdd.PREFS",0);
        initGDDDataOrganizer();
        //checkForFrost(context);
        checkForGrowthCycle(context);

    }

    private void initGDDDataOrganizer() {
        gDDO = new GDDDataOrganizer();
        gDDO.setLatitude(prefs.getString("currLatitude","42.150"));
        gDDO.setLongitude(prefs.getString("currLongitude","-91.424"));
        gDDO.beginRetrievingData();
    }

    private void checkForGrowthCycle(Context context) {
        int comparedInt = gDDO.getCurrentDay();
        String currentLayer = "";
        if (comparedInt >= gDDO.getAllCornStages().get("v2")) {
            if (comparedInt >= gDDO.getAllCornStages().get("v4")) {
                if (comparedInt >= gDDO.getAllCornStages().get("v6")) {
                    if (comparedInt > gDDO.getAllCornStages().get("v8")) {
                        if (comparedInt >= gDDO.getAllCornStages().get("v10")) {
                            if (comparedInt >= gDDO.getAllCornStages().get("silking")) {
                                if (comparedInt >= gDDO.getAllCornStages().get("black")) {
                                    currentLayer="black";
                                }
                                else{currentLayer = "silking";}
                            }else{currentLayer = "v10";}
                        } else{currentLayer = "v8";}
                    } else{currentLayer = "v6";}
                } else{currentLayer="v4";}
            } else{currentLayer = "v2";}
        }
        makePush(context, "Corn has reached " + currentLayer + " stage!");

    }

    private void checkForFrost(Context context) {
        makePush(context, "Warning! Frost is forecasted!");
    }

    private void makePush(Context context,String title) {
        NotificationManager NM =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notify = new Notification(android.R.drawable.stat_notify_more,"GDD Notification",System.currentTimeMillis());
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "CHANNEL")
                .setSmallIcon(R.drawable.common_google_signin_btn_text_light_normal)
                .setContentTitle("GDD App")
                .setContentText(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (mNotificationManager != null) {
            mNotificationManager.notify(001, mBuilder.build());
        }

    }

    public void setAlarm(Context context){
        Log.d("CREATION","Setting alarm...");
        Calendar refCalendar = Calendar.getInstance();
        Intent intent = new Intent(context, NotificationAlarm.class);
        sender = PendingIntent.getBroadcast(context, 192837,intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am != null) {
            am.setRepeating(AlarmManager.RTC_WAKEUP, refCalendar.getTimeInMillis(),1000*15,sender);
        }

    }

    public void stopAlarm(Context context){
        Intent intent = new Intent(context, NotificationAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 192837, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
