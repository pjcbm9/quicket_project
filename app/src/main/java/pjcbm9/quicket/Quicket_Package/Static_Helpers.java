package pjcbm9.quicket.Quicket_Package;

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

import junit.framework.Assert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import pjcbm9.quicket.CustomViews.CustomTypefaceSpan;
import pjcbm9.quicket.Maintenance_Package.MItem;
import pjcbm9.quicket.R;
import pjcbm9.quicket.Ticket_List_Package.ControlBarManager;

/**
 * Created by Jason Crow on 11/21/2014.
 */
public class Static_Helpers {
    public static Boolean reverseBoolean(Boolean True) {
        if (True) {
            return false;
        } else {
            return true;
        }
    }

    public static String DateToString(Date date) {
        SimpleDateFormat dt = new SimpleDateFormat("LLLL dd, yyyy hh:mm a");
        return dt.format(date);
    }

    public static ControlBarManager.Type convertStatusToControl(Ticket.Status status) {
        if (status.equals(Ticket.Status.SEARCH)) {
            return ControlBarManager.Type.SEARCH;
        } else {
            return ControlBarManager.Type.NONE;
        }
    }

    public static void conditionalToggle(Boolean enable, View view) {
        if (enable) {
            if (view.getVisibility() == View.GONE) {
                view.setVisibility(View.VISIBLE);
            }
        } else {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
            }
        }
    }

    public static void updateActionBar(Context context, String Action_Bar_Title) {
        Typeface mycustomFont = Typeface.createFromAsset(context.getAssets(), "fonts/spartan.otf");
        Spannable wordtoSpan = new SpannableString(" " + Action_Bar_Title + " ");
        wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLACK), 0, wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new BackgroundColorSpan(Color.LTGRAY), 0, wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new CustomTypefaceSpan("", mycustomFont), 0, wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ActionBar bar = ((Activity) context).getActionBar();
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

    public static int getResourceId(String pVariableName, String pResourcename, String pPackageName, Context context) {
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

    public static void startIntent(Context context, Class<?> c, Ticket PutExtraTicket) {
        Intent in = new Intent(context, c);
        in.putExtra("ticket", PutExtraTicket);
        context.startActivity(in);
    }

    public static void startIntent(Context context, Class<?> c, Ticket.Status status) {
        Intent in = new Intent(context, c);
        in.putExtra("status", status.toString());
        context.startActivity(in);
    }

    public static void DelayIntentAndFinish(final Context context, int milliseconds, final Class<?> intentClass) {
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

    public static void DelayFinish(final Context context, int milliseconds) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Activity activity = (Activity) context;
                activity.finish();
            }
        }, milliseconds);
    }

    public static void DelayIntent(final Context context, int milliseconds, final Class<?> intentClass) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startIntent(context, intentClass);
            }
        }, milliseconds);
    }

    public static void DelayIntent(final Context context, int milliseconds, final Class<?> intentClass, final Ticket.Status status) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startIntent(context, intentClass, status);
            }
        }, milliseconds);
    }

    public static void DelayIntent(final Context context, int milliseconds, final Class<?> intentClass, final Ticket ticket) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startIntent(context, intentClass, ticket);
            }
        }, milliseconds);
    }

    public static ArrayList<MItem> loadDefaultSpinnerContents(Controller ctrl, MItem.mType type) {
        ArrayList<MItem> Items = new ArrayList<MItem>();
        switch (type) {
            case TYPES:
                Items.add(new MItem(1, MItem.mType.TYPES, "Projector Issue"));
                Items.add(new MItem(2, MItem.mType.TYPES, "Computer Issue"));
                Items.add(new MItem(3, MItem.mType.TYPES, "Audio Issue"));
                Items.add(new MItem(4, MItem.mType.TYPES, "TideBreak Issue"));
                Items.add(new MItem(5, MItem.mType.TYPES, "Network Issue"));
                Items.add(new MItem(6, MItem.mType.TYPES, "Crestron Issue"));
                Items.add(new MItem(7, MItem.mType.TYPES, "Information Request"));
                Items.add(new MItem(8, MItem.mType.TYPES, "Printer Issue"));
                Items.add(new MItem(9, MItem.mType.TYPES, "Other"));
                break;
            case LOCATIONS:
                Items.add(new MItem(1, MItem.mType.LOCATIONS, "211"));
                Items.add(new MItem(2, MItem.mType.LOCATIONS, "212"));
                Items.add(new MItem(3, MItem.mType.LOCATIONS, "213"));
                Items.add(new MItem(4, MItem.mType.LOCATIONS, "218"));
                Items.add(new MItem(5, MItem.mType.LOCATIONS, "220"));
                Items.add(new MItem(6, MItem.mType.LOCATIONS, "320"));
                break;
            case USERS:
                Items.add(new MItem(1, MItem.mType.USERS, "Jason Crow"));
                Items.add(new MItem(2, MItem.mType.USERS, "Abe Rotich"));
                Items.add(new MItem(3, MItem.mType.USERS, "Scott Morris"));
                Items.add(new MItem(4, MItem.mType.USERS, "Nate Baker"));
                Items.add(new MItem(5, MItem.mType.USERS, "Fed"));
                Items.add(new MItem(6, MItem.mType.USERS, "Matt Sunderland"));
                break;
        }
        ctrl.requestInsertMaintenanceArray(Items);
        return Items;
    }
    public static Ticket.Field convertMItem(MItem.mType type){
        switch(type){
            case USERS: return Ticket.Field.ASSIGNEE;
            case TYPES: return Ticket.Field.TYPE;
            case LOCATIONS: return Ticket.Field.LOCATION;
        }
        Assert.fail();
        return null;
    }
    public static MItem.mType convertTicketField(Ticket.Field field){
        switch(field){
            case ASSIGNEE: return MItem.mType.USERS;
            case TYPE: return MItem.mType.TYPES;
            case LOCATION: return MItem.mType.LOCATIONS;
        }
        Assert.fail();
        return null;
    }
}


