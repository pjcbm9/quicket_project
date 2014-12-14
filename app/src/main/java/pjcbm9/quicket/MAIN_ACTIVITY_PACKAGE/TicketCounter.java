package pjcbm9.quicket.MAIN_ACTIVITY_PACKAGE;

import android.widget.Button;

import pjcbm9.quicket.CustomViews.DigitTextView;
import pjcbm9.quicket.CustomViews.SpartanButton;
import pjcbm9.quicket.Quicket_Package.Controller;
import pjcbm9.quicket.Quicket_Package.Ticket;

/**
 * Created by Jason Crow on 12/6/2014.
 */
public class TicketCounter {
    private CounterLabel counterLabel;
    private DigitTextView counter;
    private SpartanButton linkedButton;
    private Ticket.Status status;
    public TicketCounter(CounterLabel counterLabel,DigitTextView counter,SpartanButton linkedButton,Ticket.Status status){
        this.counter = counter;
        this.counterLabel = counterLabel;
        this.linkedButton = linkedButton;
        this.status = status;
        counterLabel.setup(status);
    }
    public void update(Controller controller){
        update(controller.requestTicketCount(status));
    }
    private void update(int count){
        linkedButton.setEnabled(counterLabel.update(count))     ;
        counter.setText("" + count);
    }
    public static void ButtonEnabler(Button button,Controller controller,Ticket.Status status){
        if(controller.requestTicketCount(status) > 0){
            button.setEnabled(true);
        } else {
            button.setEnabled(false);
        }
    }
}
