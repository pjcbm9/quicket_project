package pjcbm9.quicket;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jason Crow on 11/21/2014.
 */
public class Static_Helpers {
    public static Boolean reverseBoolean(Boolean True){
        if(True){
            return false;
        } else {
            return true;
        }
    }
    public static String DateToString(Date date){
        SimpleDateFormat dt = new SimpleDateFormat("LLLL dd, yyyy hh:mm a");
        return dt.format(date);
    }
    public static ControlBarManager.Type convertStatusToControl(Ticket.Status status){
        if(status.equals(Ticket.Status.SEARCH)){
            return ControlBarManager.Type.SEARCH;
        } else {
            return ControlBarManager.Type.NONE;
        }
    }
    public static void conditionalToggle(Boolean enable,View view){
        if(enable){
            if(view.getVisibility() == View.GONE){
                view.setVisibility(View.VISIBLE);
            }
        } else {
            if(view.getVisibility() == View.VISIBLE){
                view.setVisibility(View.GONE);
            }
        }
    }
    public static void updateActionBar(Context context,String Action_Bar_Title) {
        Typeface mycustomFont = Typeface.createFromAsset(context.getAssets(),"fonts/spartan.otf");
        Spannable wordtoSpan = new SpannableString(" " + Action_Bar_Title + " ");
        wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLACK), 0, wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new BackgroundColorSpan(Color.LTGRAY), 0, wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new CustomTypefaceSpan("", mycustomFont), 0, wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ActionBar bar = ((Activity)context).getActionBar();
        bar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.actionbarbackground));
        bar.setTitle(wordtoSpan);
    }
    public static void setUpAnimation(String anim_Name, Context context, View object) {
        int resourceID = getResourceId(anim_Name, "drawable", context.getPackageName(), context);
        object.setBackgroundResource(resourceID);
        AnimationDrawable frameAnimation = (AnimationDrawable) object.getBackground();
        frameAnimation.stop();
        frameAnimation.start();
    }
    public static int getResourceId(String pVariableName, String pResourcename, String pPackageName, Context context)
    {
        try {
            return context.getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static void startIntent(Context context, Class<?> c) {
        Intent in = new Intent(context, c);
        context.startActivity(in);
    }
    public static void startIntent(Context context, Class<?> c, String PutExtraTitle, String PutExtraString, Ticket PutExtraTicket) {
        Intent in = new Intent(context, c);
        if(PutExtraTicket == null) {
            in.putExtra(PutExtraTitle, PutExtraString);
        } else {
            in.putExtra(PutExtraTitle, PutExtraTicket);
        }
        context.startActivity(in);
    }
    public static void DelayIntentAndFinish(final Context context, int milliseconds, final Class<?> intentClass ) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Activity activity = (Activity) context;
                activity.finish();
                startIntent(context, intentClass);
            }
        }, milliseconds);
    }
    public static void DelayFinish(final Context context,int milliseconds){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Activity activity = (Activity) context;
                activity.finish();
            }
        }, milliseconds);
    }
    public static void DelayIntent(final Context context, int milliseconds, final Class<?> intentClass ) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startIntent(context, intentClass);
            }
        }, milliseconds);
    }
    public static void DelayIntent(final Context context, int milliseconds, final Class<?> intentClass, final String PutExtraTitle, final String PutExtraString, final Ticket PutExtraTicket ) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Activity activity = (Activity) context;
                //activity.finish();
                startIntent(context, intentClass, PutExtraTitle, PutExtraString, PutExtraTicket);
            }
        }, milliseconds);
    }
    public static void DelayIntentAndFinish(final Context context, int milliseconds, final Class<?> intentClass, final String PutExtraTitle, final String PutExtraString, final Ticket PutExtraTicket ) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Activity activity = (Activity) context;
                activity.finish();
                startIntent(context, intentClass, PutExtraTitle, PutExtraString, PutExtraTicket);
            }
        }, milliseconds);
    }
}
