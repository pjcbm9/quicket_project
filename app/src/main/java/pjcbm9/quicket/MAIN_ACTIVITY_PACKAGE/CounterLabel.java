package pjcbm9.quicket.MAIN_ACTIVITY_PACKAGE;

import android.content.Context;
import android.util.AttributeSet;

import pjcbm9.quicket.CustomViews.SpartanTextView;
import pjcbm9.quicket.R;
import pjcbm9.quicket.Quicket_Package.Ticket;

/**
 * Created by Jason Crow on 12/5/2014.
 */
public class CounterLabel extends SpartanTextView {
    public static final int ActiveEnabled = R.drawable.trendyblue2;
    public static final int OverdueEnabled = R.drawable.trendyred2;
    public static final int CompletedEnabled = R.drawable.trendygreen4;
    public static final int Disabled = R.drawable.silver_disabled;
    private Ticket.Status status;
    public CounterLabel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CounterLabel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CounterLabel(Context context) {
        super(context);
    }
    private void SetEnabled( Boolean isEnabled){
        if(isEnabled){
            setEnabledBackground();
        } else {
            setBackgroundResource(Disabled);
        }
    }
    private void setEnabledBackground(){
        switch(status){
            case ACTIVE: setBackgroundResource(ActiveEnabled);
                break;
            case OVERDUE: setBackgroundResource(OverdueEnabled);
                break;
            case COMPLETED: setBackgroundResource(CompletedEnabled);
                break;
        }
    }
    public void setup(Ticket.Status Status){
        this.status = Status;
    }
    public Boolean update(int count){
        if(count == 0){
            SetEnabled(false);
            return false;
        } else {
            SetEnabled(true);
            return true;
        }
    }
}
