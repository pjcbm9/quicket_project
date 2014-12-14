package pjcbm9.quicket.New_Ticket_Package;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;

import pjcbm9.quicket.CustomViews.SpartanTextView;
import pjcbm9.quicket.R;

/**
 * Created by Jason Crow on 12/7/2014.
 */
// TICKETLABEL extends SpartanTextView for its spartan font
//    Primary utility is that it has a set background
//    and it can start and stop and animation background
//    **USED for indicating that a ticket field is critical, and if it has been set
public class TicketLabel extends SpartanTextView {
    private AnimationDrawable anim;
    public TicketLabel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        startAnim();
    }

    public TicketLabel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        startAnim();
    }

    public TicketLabel(Context context) {
        super(context);
        init();
        startAnim();
    }
    private void init() {
        setBackgroundResource(R.drawable.blue_anim);
        anim = (AnimationDrawable)getBackground();
        startAnim();
    }
    private void StartAnim() {
        anim.stop();
        anim.start();
        anim.setOneShot(false);
    }
    public void startAnim(){
        StartAnim();
    }
    public void stopAnim(){
        if(anim.isRunning()){
            anim.setOneShot(true);
        }
    }
}
