package pjcbm9.quicket.Ticket_List_Package;

import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;

import pjcbm9.quicket.Quicket_Package.Controller;
import pjcbm9.quicket.Quicket_Package.Ticket;
import pjcbm9.quicket.R;

/**
 * Created by Jason Crow on 11/19/2014.
 */
public class VerticalBarManager {
    // private variables
    private Controller controller;
    private Button activeB,overdueB,completedB,draftB,countLabel;
    private Ticket.Status status;
    private int activeTicketCount,overdueTicketCount,completedTicketCount,draftTicketCount;
    private LinearLayout verticalBarBackground;
    // constructor
    public VerticalBarManager(Context context, Button ActiveB, Button OverdueB, Button CompletedB, Button DraftB, Button bar_button,LinearLayout verticalBarBackground){
        this.controller = new Controller(context);
        this.activeB = ActiveB;
        this.overdueB = OverdueB;
        this.completedB = CompletedB;
        this.draftB = DraftB;
        this.countLabel = bar_button;
        this.verticalBarBackground = verticalBarBackground;
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
        updateVerticalBarBackground();
        controller.requestUpdateTickets();
        enableButtons();
        getTicketCounts();
        updateCountLabel();
        setUpButtons();
    }
    private void getTicketCounts(){
        activeTicketCount = controller.requestTicketCount(Ticket.Status.ACTIVE);
        overdueTicketCount = controller.requestTicketCount(Ticket.Status.OVERDUE);
        completedTicketCount = controller.requestTicketCount(Ticket.Status.COMPLETED);
        draftTicketCount = controller.requestTicketCount(Ticket.Status.DRAFT);
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
    private void updateVerticalBarBackground() {
        switch (status) {
            case ACTIVE:
                verticalBarBackground.setBackgroundResource(R.drawable.active_vertical_bar);
                break;
            case OVERDUE:
                verticalBarBackground.setBackgroundResource(R.drawable.overdue_vertical_bar);
                break;
            case COMPLETED:
                verticalBarBackground.setBackgroundResource(R.drawable.completed_vertical_bar);
                break;
            case DRAFT:
                verticalBarBackground.setBackgroundResource(R.drawable.draft_vertical_bar);
                break;
            case SEARCH:
                verticalBarBackground.setBackgroundResource(R.drawable.search_vertical_bar);
                break;
        }
    }
    private void enableButtons() {
        activeB.setEnabled(true);
        overdueB.setEnabled(true);
        completedB.setEnabled(true);
        draftB.setEnabled(true);
    }



}
