package pjcbm9.quicket;

import android.widget.Button;

/**
 * Created by Jason Crow on 11/19/2014.
 */
public class VerticalBarManager {
    // private variables
    private DBAdapter DB;
    private Button activeB,overdueB,completedB,draftB,countLabel;
    private Ticket.Status status;
    private int activeTicketCount,overdueTicketCount,completedTicketCount,draftTicketCount;
    // constructor
    public VerticalBarManager(DBAdapter db, Button ActiveB, Button OverdueB, Button CompletedB, Button DraftB, Button bar_button){
        this.DB = db;
        this.activeB = ActiveB;
        this.overdueB = OverdueB;
        this.completedB = CompletedB;
        this.draftB = DraftB;
        this.countLabel = bar_button;
    }
    private void updateCountLabel(){
        countLabel.setBackgroundResource(R.drawable.bar_button);
        switch(status){
            case ACTIVE: countLabel.setText(setCountLabel(activeTicketCount));
                break;
            case COMPLETED: countLabel.setText(setCountLabel(completedTicketCount));
                break;
            case OVERDUE: countLabel.setText(setCountLabel(overdueTicketCount));
                break;
            case DRAFT: countLabel.setText(setCountLabel(draftTicketCount));
                break;
        }
    }
    private static String setCountLabel(int count){
        if(count == 1){
            return count + " TICKET";
        } else {
            return count + " TICKETS";
        }
    }
    // Refresh methods gets called by TicketListController to refresh vertical buttons implementation
    public void UpdateVerticalButtons(Ticket.Status Status) {
        this.status = Status;
        enableButtons();
        DB.updateTickets();
        getTicketCounts();
        updateCountLabel();
        setUpButtons();
    }
    private void getTicketCounts(){
        activeTicketCount = DB.findTicketCountByStatus(Ticket.Status.ACTIVE);
        overdueTicketCount = DB.findTicketCountByStatus(Ticket.Status.OVERDUE);
        completedTicketCount = DB.findTicketCountByStatus(Ticket.Status.COMPLETED);
        draftTicketCount = DB.findTicketCountByStatus(Ticket.Status.DRAFT);
    }

    private void setUpButtons() {
        if (activeTicketCount == 0 || status.equals(Ticket.Status.ACTIVE)) {
            activeB.setEnabled(false);
        }
        if (overdueTicketCount == 0 || status.equals(Ticket.Status.OVERDUE)) {
            overdueB.setEnabled(false);
        }
        if (completedTicketCount == 0 || status.equals(Ticket.Status.COMPLETED)) {
            completedB.setEnabled(false);
        }
        if (draftTicketCount == 0 || status.equals(Ticket.Status.DRAFT)) {
            draftB.setEnabled(false);
        }
    }
    private void enableButtons() {
        activeB.setEnabled(true);
        overdueB.setEnabled(true);
        completedB.setEnabled(true);
        draftB.setEnabled(true);
    }



}
