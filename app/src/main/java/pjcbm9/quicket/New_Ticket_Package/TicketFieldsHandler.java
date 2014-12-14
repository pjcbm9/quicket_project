package pjcbm9.quicket.New_Ticket_Package;

import pjcbm9.quicket.CustomViews.SpartanButton;
import pjcbm9.quicket.R;
import pjcbm9.quicket.Quicket_Package.Ticket;

/**
 * Created by Jason Crow on 12/6/2014.
 */
// class processes an array of critical fields
// binds the array of criticalfields with a ticket data router, which enables the fields to be updated
// as well as the Button which is enabled if all critical fields are set, and disabled if any critical fields are not set
public class TicketFieldsHandler {
    private TicketField[] fields;
    private Ticket ticket;
    private SpartanButton submit_button;
    // constructor
    public TicketFieldsHandler(TicketField[] fields,Ticket ticket,SpartanButton submit_button){
        this.fields = fields;
        this.ticket = ticket;
        this.submit_button = submit_button;
        submit_button.setEnabled(false); // by default the submit button is disabled, because by default the fields are not set
        /*syncFieldWithTicket(); // upon initialization the critical fields need to be synced with their corresponding values in the ticket object
                               // contained in the ticketDataRouter*/
    }

    // syncs critical fields with their value in the ticket object contained in the ticket router object (data)
    public void syncFieldsWithTicket(){
        for(TicketField tf : fields){
            tf.setData(ticket.getTicketData(tf.getField())); // sets fields data
        }
        handleFieldsState();
    }
    // actions when all critical fields are set
    private void fieldStatesAreSet(){
        ticket.setStatus(Ticket.Status.ACTIVE);
        submit_button.setEnabled(true);
        submit_button.setBackgroundResource( R.drawable.complete_button );
    }
    // actions any critical fields are not set
    private void fieldStatesNotSet(){
        ticket.setStatus(Ticket.Status.DRAFT);
        submit_button.setEnabled(false);
        submit_button.setBackgroundResource(R.drawable.generic_button);
    }
    // runs routine to check the states of all the critical fields then
    // either sets their state as true or false, and updates the status of the ticket contained
    // in the ticketDataRouter to ACTIVE or DRAFT respectively
    public void handleFieldsState(){
        if(allFieldsAreSet()){
            fieldStatesAreSet();
        } else {
            fieldStatesNotSet();
        }
    }
    // syncs the ticket's values for each field corresponding to a critical field with the value of that
    // critical field's data
    public void syncTicketWithFields(){
        for(TicketField tf : fields){
            ticket.setTicketData(tf.getData(),tf.getField());
        }
    }
    // checks if all Critical Fields are set
    private Boolean allFieldsAreSet(){
        Boolean areSet = true;
        for(TicketField tf : fields){
            if(!tf.isSet()){
                areSet = false; // if any field isn't set return false
            }
        }
        return areSet;
    }

}