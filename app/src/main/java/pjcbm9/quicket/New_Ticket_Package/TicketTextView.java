package pjcbm9.quicket.New_Ticket_Package;

import android.content.Context;
import android.util.AttributeSet;

import pjcbm9.quicket.CustomViews.SpartanTextView;
import pjcbm9.quicket.Quicket_Package.Ticket;

/**
 * Created by Jason Crow on 12/11/2014.
 */
public class TicketTextView extends SpartanTextView implements TicketField {
    private Ticket.Field field;
    public TicketTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TicketTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TicketTextView(Context context) {
        super(context);
    }


    @Override
    public void setField(Ticket.Field field) {
        this.field = field;
    }

    @Override
    public Ticket.Field getField() {
        return field;
    }

    @Override
    public void setData(String data) {
        setText(data);
    }

    @Override
    public String getData() {
        return (String)getText();
    }

    @Override
    public Boolean isSet() {
        return true; // TicketTextView will always be set as they are non critical
    }
}
