package pjcbm9.quicket.New_Ticket_Package;

import android.content.Context;
import android.util.AttributeSet;

import pjcbm9.quicket.CustomViews.SpartanEditText;
import pjcbm9.quicket.Quicket_Package.Ticket;

/**
 * Created by Jason Crow on 12/7/2014.
 */
// TicketEditText is an EditText View that inherits SpartanEditText, which sets its font to Spartan,
// and implements CriticalDate
public class TicketEditText extends SpartanEditText implements TicketField {
    private Ticket.Field field; // As of (12/7/14) must be NAME or CUSTOMER
    public static final String false_condition = "";
    public TicketEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public TicketEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TicketEditText(Context context) {
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
        return getText().toString();
    }

    @Override
    public Boolean isSet() {
        return !(getText().toString().equals(false_condition));
    }

}
