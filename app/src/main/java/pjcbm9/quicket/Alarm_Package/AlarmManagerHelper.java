package pjcbm9.quicket.Alarm_Package;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;

import java.util.GregorianCalendar;

import pjcbm9.quicket.Quicket_Package.DBAdapter;
import pjcbm9.quicket.Quicket_Package.Ticket;

/**
 * Created by Jason Crow on 11/25/2014.
 */
public class AlarmManagerHelper extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //System.out.println("here");
        //Toast.makeText(context, "ALARM", Toast.LENGTH_LONG).show();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "YOUR TAG");
        //Acquire the lock
        wl.acquire();

        //You can do the processing here.
        Bundle extras = intent.getExtras();
        long ticket_ID;
        if(extras != null){
            //Make sure this intent has been sent by the one-time timer button.
            ticket_ID = intent.getLongExtra("ticketID",0);
            DBAdapter myDb = new DBAdapter(context);
            myDb.open();
            Ticket ticket = myDb.getTicket(ticket_ID);
            myDb.close();
            Intent in = new Intent(context, AlarmActivity.class);
            in.putExtra("ticket", ticket);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);
        }


        //Release the lock
        wl.release();
    }

    public static void setAlarm(Context context, long ticketID, long countDownDuration) {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerHelper.class);
        intent.putExtra("ticketID", ticketID);
        PendingIntent pi = PendingIntent.getBroadcast(context,(int)ticketID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Long time = new GregorianCalendar().getTimeInMillis() + countDownDuration;
        am.set(AlarmManager.RTC_WAKEUP, time, pi);
    }

    public static void cancelAlarm(Context context, long ticketID) {
        Intent intent = new Intent(context, AlarmManagerHelper.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, (int)ticketID, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

}