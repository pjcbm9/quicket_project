package pjcbm9.quicket.New_Ticket_Package;

import pjcbm9.quicket.Quicket_Package.Ticket;

/**
 * Created by Jason Crow on 12/6/2014.
 */
// This class encapsulates data associated with a ticket field
// **currently used for ancillary purposes in the NewTicket Activity
/** CRITICAL: a ticket field that must be filled out for the ticket to sumbitted as Active, instead of draft. Is associated with a label that runs an animation to indicate that it is critical**/
public class TicketRow implements TicketField {
    private TicketField dataField; // the View that holds the critical data for its respective field
    private Ticket.Field field; // the type of field (refers to the fields of a ticket)
    private TicketLabel label; // the view which contains the label for the field
    private Boolean isCritical;
    // constructor for critical ticket fields
    public TicketRow(Ticket.Field field, TicketLabel label, TicketField dataField) {
        this.label = label;
        this.dataField = dataField;
        this.isCritical = true;
        setField(field);
    }
    // constructor for non critical ticket fields
    public TicketRow(Ticket.Field field,TicketField dataField){
        this.dataField = dataField;
        this.isCritical = false;
        setField(field);
    }
    // returns state of the CriticalField
    @Override
    public Boolean isSet(){
        if(dataField.isSet()){
            if(isCritical){label.stopAnim();}
            return true;
        } else {
            if(isCritical){label.startAnim();}
            return false;
        }
    }

    // gets data from dataField
    @Override
    public String getData() {
        return dataField.getData();
    }

    @Override
    public void setField(Ticket.Field field) {
        this.field = field;
        dataField.setField(field);
    }

    // returns CriticalField's ticket field type
    @Override
    public Ticket.Field getField() {
        return field;
    }

    @Override
    public void setData(String data) {
        dataField.setData(data);
    }

}
