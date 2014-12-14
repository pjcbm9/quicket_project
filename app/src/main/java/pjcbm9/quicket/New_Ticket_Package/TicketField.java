package pjcbm9.quicket.New_Ticket_Package;

import pjcbm9.quicket.Quicket_Package.Ticket;

/**
 * Created by Jason Crow on 12/11/2014.
 */
public interface TicketField {
    abstract public void setField(Ticket.Field field);
    abstract public Ticket.Field getField();
    abstract public void setData(String data);
    abstract public String getData();
    abstract public Boolean isSet();
}
