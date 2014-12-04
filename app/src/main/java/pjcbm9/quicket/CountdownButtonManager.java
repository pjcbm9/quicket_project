package pjcbm9.quicket;

import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Jason Crow on 11/20/2014.
 */
public class CountdownButtonManager {
    public  enum TimeUnit {
        DAY,
        HOUR,
        MINUTE,
        SECOND
    }
    public enum ButtonAction {
        INCREMENT,
        DECREMENT,
    }
    public void buttonAction(ButtonAction action,TextView TimeUnitLabel,Button up,Button down,TimeUnit unit){
        switch(unit){
            case DAY: enableButtons(commitAction(action,TimeUnitLabel),7,up,down);
                break;
            case HOUR: enableButtons(commitAction(action,TimeUnitLabel),24,up,down);
                break;
            case MINUTE: enableButtons(commitAction(action,TimeUnitLabel),60,up,down);
                break;
            case SECOND: enableButtons(commitAction(action,TimeUnitLabel),60,up,down);
                break;
        }
    }
    private int commitAction(ButtonAction action,TextView TimeUnitLabel){
        int value = getTimeUnitValue(TimeUnitLabel);
        switch(action){
            case INCREMENT: value++;
                break;
            case DECREMENT: value--;
                break;
        }
        TimeUnitLabel.setText(String.format("%02d", value));
        return value;
    }
    public static int getTimeUnitValue(TextView label){
        return Integer.parseInt((String)label.getText());
    }
    public void setupEnableButtons(TextView label,int upperLimit,Button up,Button down){
        enableButtons(getTimeUnitValue(label),upperLimit,up,down);
    }
    private void enableButtons(int value,int upperLimit,Button up,Button down){
        up.setEnabled(true);
        down.setEnabled(true);
        if(value == upperLimit){
            up.setEnabled(false);
        } else if(value == 0) {
            down.setEnabled(false);
        }
    }

}
