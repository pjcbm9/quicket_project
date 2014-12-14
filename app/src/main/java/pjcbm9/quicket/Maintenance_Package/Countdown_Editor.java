package pjcbm9.quicket.Maintenance_Package;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import pjcbm9.quicket.R;

import static pjcbm9.quicket.Maintenance_Package.CountdownButtonManager.getTimeUnitValue;
import static pjcbm9.quicket.Quicket_Package.Static_Helpers.setUpAnimation;

/**
 * Created by Jason Crow on 11/19/2014.
 */
public class Countdown_Editor implements View.OnClickListener {
    private Context context;
    private Button day_up,hour_up,minute_up,second_up;
    private Button day_down,hour_down,minute_down,second_down;
    private TextView days,hours,minutes,seconds;
    private TextView day_label,hour_label,minute_label,second_label;
    private long countdown;
    public static final String filename = "savedFile";
    public Boolean countdownEditorMode;
    private CountdownButtonManager buttonManager;
    private SharedPreferences savedData;
    final static long dayToMillis = 86400000;
    final static long hourToMillis = 3600000;
    final static long minuteToMillis = 60000;
    final static long secondToMillis = 1000;

    public Countdown_Editor(Context c){
        this.context = c;
        this.buttonManager = new CountdownButtonManager();
        this.countdownEditorMode = false;
        this.savedData = context.getSharedPreferences(filename,0);
        initializeVariables();
        setListeners();
        setFonts();
        loadInitialCountdown();
    }
    public Boolean getCountdownEditorMode(){
        return countdownEditorMode;
    }
    public void setCountdownEditorMode(Boolean bool){
        this.countdownEditorMode = bool;
    }
    public void saveCountdown() {
        updateCountdown();
        SharedPreferences.Editor editor = savedData.edit();
        editor.putLong("countdown", countdown);
        editor.commit();
    }
    private void updateCountdown(){
        int days_int = getTimeUnitValue(days);
        int hours_int = getTimeUnitValue(hours);
        int minutes_int = getTimeUnitValue(minutes);
        int seconds_int = getTimeUnitValue(seconds);
        long millis = (days_int*dayToMillis) + (hours_int*hourToMillis) + (minutes_int*minuteToMillis) + (seconds_int*secondToMillis);
        countdown = millis;
    }
    private void getCountdown() {
        countdown = savedData.getLong("countdown",86400000*7);
    }
    public void loadInitialCountdown(){
        getCountdown();
        String days_str = String.format("%02d",TimeUnit.MILLISECONDS.toDays(countdown));
        String hours_str = String.format("%02d",TimeUnit.MILLISECONDS.toHours(countdown)%24);
        String minutes_str = String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(countdown) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(countdown)));
        String seconds_str = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(countdown) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(countdown)));
        days.setText(days_str);
        hours.setText(hours_str);
        minutes.setText(minutes_str);
        seconds.setText(seconds_str);
        setupInitialEnabledButtons();
    }
    private void setupInitialEnabledButtons(){
        System.out.println("days");
        buttonManager.setupEnableButtons(days,7,day_up,day_down);
        updateButtonBackgrounds(day_up, day_down);
        System.out.println("hours");
        buttonManager.setupEnableButtons(hours,24,hour_up,hour_down);
        updateButtonBackgrounds(hour_up,hour_down);
        System.out.println("minutes");
        buttonManager.setupEnableButtons(minutes,60,minute_up,minute_down);
        updateButtonBackgrounds(minute_up,minute_down);
        System.out.println("seconds");
        buttonManager.setupEnableButtons(seconds,60,second_up,second_down);
        updateButtonBackgrounds(second_up,second_down);
    }
    private void initializeVariables() {
        day_up = (Button)((Activity)context).findViewById(R.id.day_up);
        hour_up = (Button)((Activity)context).findViewById(R.id.hour_up);
        minute_up = (Button)((Activity)context).findViewById(R.id.minute_up);
        second_up = (Button)((Activity)context).findViewById(R.id.second_up);
        day_down = (Button)((Activity)context).findViewById(R.id.day_down);
        hour_down = (Button)((Activity)context).findViewById(R.id.hour_down);
        minute_down = (Button)((Activity)context).findViewById(R.id.minute_down);
        second_down = (Button)((Activity)context).findViewById(R.id.second_down);
        days = (TextView)((Activity)context).findViewById(R.id.days);
        hours = (TextView)((Activity)context).findViewById(R.id.hours);
        minutes = (TextView)((Activity)context).findViewById(R.id.minutes);
        seconds = (TextView)((Activity)context).findViewById(R.id.seconds);
        day_label = (TextView)((Activity)context).findViewById(R.id.day_label);
        hour_label = (TextView)((Activity)context).findViewById(R.id.hour_label);
        minute_label = (TextView)((Activity)context).findViewById(R.id.minute_label);
        second_label = (TextView)((Activity)context).findViewById(R.id.second_label);
    }
    private void setListeners() {
        day_up.setOnClickListener(this);
        hour_up.setOnClickListener(this);
        minute_up.setOnClickListener(this);
        second_up.setOnClickListener(this);
        day_down.setOnClickListener(this);
        hour_down.setOnClickListener(this);
        minute_down.setOnClickListener(this);
        second_down.setOnClickListener(this);
    }
    private void setFonts() {
        Typeface spartan_font = Typeface.createFromAsset(context.getAssets(),"fonts/spartan.otf");
        day_label.setTypeface(spartan_font);
        hour_label.setTypeface(spartan_font);
        minute_label.setTypeface(spartan_font);
        second_label.setTypeface(spartan_font);
        Typeface digital_font = Typeface.createFromAsset(context.getAssets(),"fonts/dsdigit.ttf");
        days.setTypeface(digital_font);
        hours.setTypeface(digital_font);
        minutes.setTypeface(digital_font);
        seconds.setTypeface(digital_font);
    }
    private void updateButtonBackgrounds(Button up,Button down){
        if(up.isEnabled()){
            up.setBackgroundResource(R.drawable.up_button);
        } else if(!up.isEnabled()){
            up.setBackgroundResource(R.drawable.button_disabled);
        }
        if(down.isEnabled()){
            down.setBackgroundResource(R.drawable.down_button);
        } else if(!down.isEnabled()){
            down.setBackgroundResource(R.drawable.button_disabled);
        }
    }
    private void incrementWrapper(TextView label,Button up,Button down,CountdownButtonManager.TimeUnit unit){
        setUpAnimation("up_button_anim", context, up);
        buttonManager.buttonAction(CountdownButtonManager.ButtonAction.INCREMENT, label, up, down, unit);
        updateButtonBackgrounds(up,down);
    }
    private void decrementWrapper(TextView label,Button up,Button down,CountdownButtonManager.TimeUnit unit){
        setUpAnimation("down_button_anim", context, down);
        buttonManager.buttonAction(CountdownButtonManager.ButtonAction.DECREMENT, label, up, down, unit);
        updateButtonBackgrounds(up,down);
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case(R.id.day_up):
                incrementWrapper(days, day_up, day_down, CountdownButtonManager.TimeUnit.DAY);
                break;
            case(R.id.hour_up):
                incrementWrapper(hours, hour_up, hour_down, CountdownButtonManager.TimeUnit.HOUR);
                break;
            case(R.id.minute_up):
                incrementWrapper(minutes, minute_up, minute_down, CountdownButtonManager.TimeUnit.MINUTE);
                break;
            case(R.id.second_up):
                incrementWrapper(seconds, second_up, second_down, CountdownButtonManager.TimeUnit.SECOND);
                break;
            case(R.id.day_down):
                decrementWrapper(days, day_up, day_down, CountdownButtonManager.TimeUnit.DAY);
                break;
            case(R.id.hour_down):
                decrementWrapper(hours, hour_up, hour_down, CountdownButtonManager.TimeUnit.HOUR);
                break;
            case(R.id.minute_down):
                decrementWrapper(minutes, minute_up, minute_down, CountdownButtonManager.TimeUnit.MINUTE);
                break;
            case(R.id.second_down):
                decrementWrapper(seconds, second_up, second_down, CountdownButtonManager.TimeUnit.SECOND);
                break;
        }
    }
}
