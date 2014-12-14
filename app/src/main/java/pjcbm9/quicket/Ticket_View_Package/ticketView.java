package pjcbm9.quicket.Ticket_View_Package;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

import pjcbm9.quicket.CustomViews.DigitTextView;
import pjcbm9.quicket.CustomViews.SpartanButton;
import pjcbm9.quicket.CustomViews.SpartanTextView;
import pjcbm9.quicket.MAIN_ACTIVITY_PACKAGE.MainActivity;
import pjcbm9.quicket.New_Ticket_Package.NewTicket;
import pjcbm9.quicket.Quicket_Package.Controller;
import pjcbm9.quicket.Quicket_Package.Ticket;
import pjcbm9.quicket.R;

import static pjcbm9.quicket.Quicket_Package.Static_Helpers.DelayIntent;
import static pjcbm9.quicket.Quicket_Package.Static_Helpers.getResourceId;
import static pjcbm9.quicket.Quicket_Package.Static_Helpers.setUpAnimation;
import static pjcbm9.quicket.Quicket_Package.Static_Helpers.updateActionBar;


public class ticketView extends Activity implements View.OnClickListener {
    private Ticket ticket;
    private SpartanTextView ticketName,ticketType,ticketAssignee,ticketCustomer,ticketDescription,
            ticketDate,ticketLocation,ticketStatus,TicketNameLabel,TicketTypeLabel,TicketAssigneeLabel,
            TicketCustomerLabel,TicketDescriptionLabel,TicketDateLabel,
            TicketLocationLabel,TicketStatusLabel,TicketTimerLabel;
    private DigitTextView ticketTimer;
    private SpartanButton deleteb,editb,completeb;
    private LinearLayout background;
    private Controller controller;
    private Boolean timersAreActive = false;
    private Runnable mRunnable = new Runnable() {
        public void run() {
            updateCountdownTimers();
            mHandler.postDelayed(mRunnable, 1000);
        }
    };
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_view);
        ticket = (Ticket)getIntent().getSerializableExtra("ticket");
        controller = new Controller(this);
        initializeVariables();
        disableButtons();
        setListeners();
        setTextViews();
        updateBackgrounds();
        updateActionBar(this,"TICKET DETAILS");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        startCountdownTimers();
    }
    // if the ticket status is ACTIVE, then onResume we need to restart the countdown timers
    @Override
    protected void onResume() {
        super.onResume();
        startCountdownTimers();
    }
    // if the ticket status is ACTIVE, then when paused,destroyed, or stopped, we need to stop the countdown timers
    @Override
    protected void onPause() {
        super.onPause();
        stopCountdownTimers();
    }
    @Override
    protected void onStop() {
        super.onStop();
        stopCountdownTimers();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.close();
        stopCountdownTimers();
    }
    private void updateCountdownTimers() {
        long millis = System.currentTimeMillis();
        ticket.setCountDownTimer2(millis);
        ticketTimer.setText(ticket.getCountDownTimer());
    }
    public void stopCountdownTimers() {
        if (timersAreActive) {
            timersAreActive = false;
            mHandler.removeCallbacks(mRunnable);
        }
    }
    private void startCountdownTimers() {
        if (ticket.getStatus().equals(Ticket.Status.ACTIVE)) {
            timersAreActive = true;
            mHandler.postDelayed(mRunnable, 1000);
        }
    }

    private void initializeVariables() {
        mHandler = new Handler();
        ticketName = (SpartanTextView)findViewById(R.id.ticketnametv);
        ticketType = (SpartanTextView)findViewById(R.id.tickettypetv);
        ticketAssignee = (SpartanTextView)findViewById(R.id.ticketassigneetv);
        ticketCustomer = (SpartanTextView)findViewById(R.id.ticketcustomertv);
        ticketDescription = (SpartanTextView)findViewById(R.id.ticketdescriptiontv);
        ticketDate = (SpartanTextView)findViewById(R.id.ticketdatetv);
        ticketLocation = (SpartanTextView)findViewById(R.id.ticketlocationtv);
        ticketStatus = (SpartanTextView)findViewById(R.id.ticketstatustv);
        ticketTimer = (DigitTextView)findViewById(R.id.tickettimertv);
        TicketNameLabel = (SpartanTextView)findViewById(R.id.TicketNameLabel);
        TicketTypeLabel = (SpartanTextView)findViewById(R.id.TicketTypeLabel);
        TicketAssigneeLabel = (SpartanTextView)findViewById(R.id.TicketAssigneeLabel);
        TicketCustomerLabel = (SpartanTextView)findViewById(R.id.TicketCustomerLabel);
        TicketDescriptionLabel = (SpartanTextView)findViewById(R.id.TicketDescriptionLabel);
        TicketDateLabel = (SpartanTextView)findViewById(R.id.TicketDateLabel);
        TicketLocationLabel = (SpartanTextView)findViewById(R.id.TicketLocationLabel);
        TicketStatusLabel = (SpartanTextView)findViewById(R.id.TicketStatusLabel);
        TicketTimerLabel = (SpartanTextView)findViewById(R.id.TicketTimerLabel);
        deleteb = (SpartanButton)findViewById(R.id.deleteb);
        editb = (SpartanButton)findViewById(R.id.editb);
        completeb = (SpartanButton)findViewById(R.id.completeb);
        background =(LinearLayout)findViewById(R.id.TicketBackground);
    }
    private void disableButtons(){
        if(ticket.getStatus().equals(Ticket.Status.COMPLETED)){
            completeb.setEnabled(false);
            completeb.setBackgroundResource( R.drawable.generic_button );
            editb.setEnabled(false);
            editb.setBackgroundResource( R.drawable.generic_button );
        } else if(ticket.getStatus().equals(Ticket.Status.OVERDUE)){
            editb.setEnabled(false);
            editb.setBackgroundResource( R.drawable.generic_button );
        }
    }
    private void setTextViews() {
        // extract values
        String TicketName = ticket.getTicketName();
        String TicketType = ticket.getTicketType();
        String TicketAssignee = ticket.getAssignee();
        String TicketCustomer = ticket.getCustomerName();
        String TicketDescription = ticket.getDescription();
        String TicketStatus = ticket.getStatus().toString();
        Date TicketDate = ticket.getTicketDate();
        SimpleDateFormat dt = new SimpleDateFormat("LLLL dd, yyyy hh:mm a");
        String date = dt.format(TicketDate);
        String TicketLocation = ticket.getIncidentLocation();
        // load values
        ticketName.setText(TicketName);
        ticketType.setText(TicketType);
        ticketAssignee.setText(TicketAssignee);
        ticketCustomer.setText(TicketCustomer);
        ticketDescription.setText(TicketDescription);
        ticketDate.setText(date);
        ticketLocation.setText(TicketLocation);
        ticketStatus.setText(TicketStatus);
    }
    private void changeLabelBackground(String background_name){
        int resourceID = getResourceId(background_name, "drawable", getPackageName(), this);
        TicketNameLabel.setBackgroundResource(resourceID);
        TicketTypeLabel.setBackgroundResource(resourceID);
        TicketAssigneeLabel.setBackgroundResource(resourceID);
        TicketCustomerLabel.setBackgroundResource(resourceID);
        TicketDateLabel.setBackgroundResource(resourceID);
        TicketLocationLabel.setBackgroundResource(resourceID);
        TicketStatusLabel.setBackgroundResource(resourceID);
        TicketTimerLabel.setBackgroundResource(resourceID);
    }
    private void updateBackgrounds(){
        Ticket.Status status = ticket.getStatus();
        switch(status){
            case ACTIVE: changeLabelBackground("active_label");
                TicketDescriptionLabel.setBackgroundResource( R.drawable.long_active_label );
                background.setBackgroundResource(R.drawable.active_background);
                break;
            case OVERDUE: changeLabelBackground("alert_label_background");
                TicketDescriptionLabel.setBackgroundResource( R.drawable.long_alert_label_background );
                background.setBackgroundResource(R.drawable.alert_background);
                break;
            case COMPLETED: changeLabelBackground("complete_label");
                TicketDescriptionLabel.setBackgroundResource( R.drawable.long_complete_label );
                background.setBackgroundResource(R.drawable.complete_background);
                break;
        }
    }
    private void setListeners() {
        deleteb.setOnClickListener(this);
        editb.setOnClickListener(this);
        completeb.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ticket_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
            case R.id.home_icon:
                delayIntent(0,MainActivity.class);
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.deleteb):
                controller.requestTicketDeletion(ticket.getID());
                setUpAnimation("delete_button_anim", this, view);
                delayIntent(500, MainActivity.class);
                finish();
                break;
            case (R.id.editb):
                setUpAnimation("edit_button_anim", this, view);
                delayIntent(500, NewTicket.class, ticket);
                finish();
                break;
            case (R.id.completeb):
                controller.requestCompleteTicket(ticket.getID());
                setUpAnimation("complete_button_anim", this, view);
                delayIntent(500, MainActivity.class);
                finish();
                break;
        }
    }
    private void SetUpAnimation(String anim_name,View view){
        setUpAnimation(anim_name,this,view);
    }
    private void delayIntent(int milliseconds, Class<?> c){
        DelayIntent(this,milliseconds,c);
    }
    private void delayIntent(int milliseconds, Class<?> c, Ticket ticket){
        DelayIntent(this,milliseconds,c,ticket);
    }

}
