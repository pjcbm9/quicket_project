package pjcbm9.quicket.MAIN_ACTIVITY_PACKAGE;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import pjcbm9.quicket.CustomViews.DigitTextView;
import pjcbm9.quicket.CustomViews.SpartanButton;
import pjcbm9.quicket.InfoActivity;
import pjcbm9.quicket.Maintenance_Package.Maintenance;
import pjcbm9.quicket.New_Ticket_Package.NewTicket;
import pjcbm9.quicket.Quicket_Package.Controller;
import pjcbm9.quicket.Quicket_Package.Ticket;
import pjcbm9.quicket.R;
import pjcbm9.quicket.SendMailActivity;
import pjcbm9.quicket.Ticket_List_Package.Ticket_List;

import static pjcbm9.quicket.MAIN_ACTIVITY_PACKAGE.TicketCounter.ButtonEnabler;
import static pjcbm9.quicket.Quicket_Package.Static_Helpers.DelayIntent;
import static pjcbm9.quicket.Quicket_Package.Static_Helpers.setUpAnimation;
import static pjcbm9.quicket.Quicket_Package.Static_Helpers.startIntent;
import static pjcbm9.quicket.Quicket_Package.Static_Helpers.updateActionBar;


public class MainActivity extends Activity implements View.OnClickListener {
    private SpartanButton CreateNewTicket,ViewActiveTickets,ViewCompletedTickets,ViewDraftTickets,ViewOverdueTickets;
    private DigitTextView ActiveCounter,OverdueCounter,CompletedCounter;
    private CounterLabel ActiveLabel,OverdueLabel,CompletedLabel;
    private ArrayList<TicketCounter> ticketCounters;
    private Controller controller;
    private Button sendemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeVariables();
        addListeners();
        loadTicketCounters();
        updateTicketCounters();
        updateActionBar(this,"QUICKET");
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTicketCounters();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.close();
    }

    private void initializeVariables() {
        controller = new Controller(this);
        ticketCounters = new ArrayList<TicketCounter>();
        ViewOverdueTickets = (SpartanButton)findViewById(R.id.OverdueB);
        CreateNewTicket = (SpartanButton)findViewById(R.id.NewTicketB);
        ViewActiveTickets = (SpartanButton)findViewById(R.id.activetickets);
        ViewCompletedTickets = (SpartanButton)findViewById(R.id.ViewCompletedB);
        ViewDraftTickets = (SpartanButton)findViewById(R.id.loadticketb);
        ActiveCounter = (DigitTextView)findViewById(R.id.activecount);
        OverdueCounter = (DigitTextView)findViewById(R.id.overduecount);
        CompletedCounter = (DigitTextView)findViewById(R.id.completedcount);
        ActiveLabel = (CounterLabel)findViewById(R.id.ActiveLabel);
        OverdueLabel = (CounterLabel)findViewById(R.id.OverdueLabel);
        CompletedLabel = (CounterLabel)findViewById(R.id.CompletedLabel);
        sendemail = (Button)findViewById(R.id.sendemail);
    }
    private void addListeners(){
        CreateNewTicket.setOnClickListener(this);
        ViewActiveTickets.setOnClickListener(this);
        ViewCompletedTickets.setOnClickListener(this);
        ViewDraftTickets.setOnClickListener(this);
        ViewOverdueTickets.setOnClickListener(this);
    }
    private void loadTicketCounters(){
        controller.requestUpdateTickets();
        ticketCounters.add(new TicketCounter(ActiveLabel, ActiveCounter, ViewActiveTickets, Ticket.Status.ACTIVE));
        ticketCounters.add(new TicketCounter(OverdueLabel, OverdueCounter, ViewOverdueTickets, Ticket.Status.OVERDUE));
        ticketCounters.add(new TicketCounter(CompletedLabel,CompletedCounter,ViewCompletedTickets,Ticket.Status.COMPLETED));
    }
    private void updateTicketCounters(){
        controller.requestUpdateTickets();
        for(TicketCounter tc : ticketCounters){
            tc.update(controller);
        }
        ButtonEnabler(ViewDraftTickets,controller,Ticket.Status.DRAFT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.maintenance) {
            delayIntent(500, Maintenance.class);
            return true;
        } else if(id == R.id.about) {
            delayIntent(500, InfoActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case(R.id.sendemail):
                startIntent(this, SendMailActivity.class);
                break;
            case(R.id.NewTicketB):
                SetUpAnimation("test", view);
                delayIntent(500,NewTicket.class);
                break;
            case(R.id.loadticketb):
                SetUpAnimation("test1", view);
                delayIntent(500,Ticket_List.class,Ticket.Status.DRAFT);
                break;
            case(R.id.activetickets):
                SetUpAnimation("test2", view);
                delayIntent(500,Ticket_List.class,Ticket.Status.ACTIVE);
                break;
            case(R.id.ViewCompletedB):
                SetUpAnimation("test3", view);
                delayIntent(500,Ticket_List.class,Ticket.Status.COMPLETED);
                break;
            case(R.id.OverdueB):
                SetUpAnimation("test3", view);
                delayIntent(500,Ticket_List.class,Ticket.Status.OVERDUE);
                break;
        }
    }
    private void SetUpAnimation(String anim_name,View view){
        setUpAnimation(anim_name,this,view);
    }
    private void delayIntent(int milliseconds, Class<?> c){
        DelayIntent(this,milliseconds,c);
    }
    private void delayIntent(int milliseconds, Class<?> c, Ticket.Status status){
        DelayIntent(this,milliseconds,c,status);
    }
}
