package pjcbm9.quicket.Ticket_List_Package;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;

import pjcbm9.quicket.CustomViews.SpartanButton;
import pjcbm9.quicket.R;
import pjcbm9.quicket.Quicket_Package.Ticket;

/**
 * Created by Jason Crow on 12/4/2014.
 */
public class TicketButton extends SpartanButton {
    private Ticket.Status status;
    public TicketButton(Context context) {
        super(context);
    }

    public TicketButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TicketButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public void formatText(String ticketTitle,int ticketNameLength){
        Spannable wordtoSpan = new SpannableString(ticketTitle);
        wordtoSpan.setSpan(new RelativeSizeSpan(1.5f), 0, ticketNameLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new ForegroundColorSpan(Color.WHITE), ticketNameLength + 1, wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(wordtoSpan);
    }
    public void updateBackground(Ticket.Status Status, Boolean showCheckboxes){
        if(setStatus(Status)){
            setBackground(showCheckboxes);
        }
    }
    private void setBackground(Boolean showCheckboxes){
        switch(status){
            case ACTIVE: updateBackground(showCheckboxes);
                break;
            case DRAFT: setBackgroundResource(R.drawable.trendy_gold);
                break;
            case OVERDUE: setBackgroundResource(R.drawable.trendyred);
                break;
            case COMPLETED: setBackgroundResource(R.drawable.trendygreen);
                break;
        }
    }
    private Boolean setStatus(Ticket.Status Status){
        if(Status == null || !Status.equals(status)){
            this.status = Status;
        }else if(Status.equals(status)){
            return false;
        }
        return true;
    }
    private void updateBackground(Boolean showCheckboxes){
        if(showCheckboxes){
            setBackgroundResource(R.drawable.active_ticket);
        }else{
            setBackgroundResource(R.drawable.active_ticket_left);
        }
    }

}
