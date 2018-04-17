package gdd.harrison.memdust.growingdegreeday;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by cacto on 4/17/2018.
 */

public class NotificationAlarm extends BroadcastReceiver {

    AlarmManager am;
    PendingIntent sender;

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = "Test alarm successful!";
        Toast.makeText(context, message,Toast.LENGTH_LONG).show();
        Log.d("CREATION", "Alarm went off!");
//        Intent intent2 = new Intent(context, TripNotification.class);
//        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.s

    }
    public void setAlarm(Context context){
        Log.d("CREATION","Setting alarm...");
        Calendar refCalendar = Calendar.getInstance();

        refCalendar.add(Calendar.SECOND,30);
        Intent intent = new Intent(context, NotificationAlarm.class);
        sender = PendingIntent.getBroadcast(context, 192837,intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am != null) {
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, refCalendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,sender);
        }

    }

    public void stopAlarm(Context context){
        Intent intent = new Intent(context, NotificationAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 192837, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 14);
//        calendar.set(Calendar.MINUTE,15);
//
//        notificationAlarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, frostCheckerPending);
//        Log.d("CREATION","Alarm set?");
//    }
}
