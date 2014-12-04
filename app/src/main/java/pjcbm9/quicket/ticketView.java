package pjcbm9.quicket;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import static pjcbm9.quicket.AlarmManagerHelper.cancelAlarm;
import static pjcbm9.quicket.Static_Helpers.DelayIntent;
import static pjcbm9.quicket.Static_Helpers.getResourceId;
import static pjcbm9.quicket.Static_Helpers.setUpAnimation;


public class ticketView extends Activity implements View.OnClickListener {
    Ticket new_ticket;
    TextView ticketName,ticketType,ticketAssignee,ticketCustomer,ticketDescription,
            ticketDate,ticketLocation,ticketStatus,ticketTimer;
    TextView TicketNameLabel,TicketTypeLabel,TicketAssigneeLabel,TicketCustomerLabel,TicketDescriptionLabel,
        TicketDateLabel,TicketLocationLabel,TicketStatusLabel,TicketTimerLabel;
    Button deleteb,editb,completeb;
    private LinearLayout background;
    DBAdapter myDb;
    Runnable mRunnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_view);
        new_ticket = (Ticket)getIntent().getSerializableExtra("new_ticket");
        initializeVariables();
        disableButtons();
        setListeners();
        setTextViews();
        setFonts();
        updateBackgrounds();
        //setCountDownTimer();

        mRunnable = new Runnable() {
            public void run() {
                long millis = System.currentTimeMillis();
                new_ticket.setCountDownTimer2(millis);
                ticketTimer.setText(new_ticket.getCountDownTimer());
                ticketTimer.postDelayed(this, 1000);
            }
        };
        mRunnable.run();
        openDB();
        updateActionBar();
    }

    private void updateActionBar() {
        Typeface mycustomFont = Typeface.createFromAsset(getAssets(),"fonts/spartan.otf");
        Spannable wordtoSpan = new SpannableString(" TICKET DETAILS ");
        wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLACK), 0, wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new BackgroundColorSpan(Color.LTGRAY), 0, wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new CustomTypefaceSpan("", mycustomFont), 0, wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbarbackground));
        bar.setTitle(wordtoSpan);
        bar.setDisplayHomeAsUpEnabled(true);
    }
    private void setFonts() {
        Typeface mycustomFont = Typeface.createFromAsset(getAssets(),"fonts/spartan.otf");
        ticketName.setTypeface(mycustomFont);
        ticketType.setTypeface(mycustomFont);
        ticketAssignee.setTypeface(mycustomFont);
        ticketCustomer.setTypeface(mycustomFont);
        ticketDescription.setTypeface(mycustomFont);
        ticketDate.setTypeface(mycustomFont);
        ticketLocation.setTypeface(mycustomFont);
        ticketStatus.setTypeface(mycustomFont);
        TicketNameLabel.setTypeface(mycustomFont);
        TicketTypeLabel.setTypeface(mycustomFont);
        TicketAssigneeLabel.setTypeface(mycustomFont);
        TicketCustomerLabel.setTypeface(mycustomFont);
        TicketDescriptionLabel.setTypeface(mycustomFont);
        TicketDateLabel.setTypeface(mycustomFont);
        TicketLocationLabel.setTypeface(mycustomFont);
        TicketStatusLabel.setTypeface(mycustomFont);
        TicketTimerLabel.setTypeface(mycustomFont);
        deleteb.setTypeface(mycustomFont);
        editb.setTypeface(mycustomFont);
        completeb.setTypeface(mycustomFont);
        Typeface mycustomFont2 = Typeface.createFromAsset(getAssets(),"fonts/dsdigit.ttf");
        ticketTimer.setTypeface(mycustomFont);
        //
    }
    private void initializeVariables() {
        ticketName = (TextView)findViewById(R.id.ticketnametv);
        ticketType = (TextView)findViewById(R.id.tickettypetv);
        ticketAssignee = (TextView)findViewById(R.id.ticketassigneetv);
        ticketCustomer = (TextView)findViewById(R.id.ticketcustomertv);
        ticketDescription = (TextView)findViewById(R.id.ticketdescriptiontv);
        ticketDate = (TextView)findViewById(R.id.ticketdatetv);
        ticketLocation = (TextView)findViewById(R.id.ticketlocationtv);
        ticketStatus = (TextView)findViewById(R.id.ticketstatustv);
        ticketTimer = (TextView)findViewById(R.id.tickettimertv);
        TicketNameLabel = (TextView)findViewById(R.id.TicketNameLabel);
        TicketTypeLabel = (TextView)findViewById(R.id.TicketTypeLabel);
        TicketAssigneeLabel = (TextView)findViewById(R.id.TicketAssigneeLabel);
        TicketCustomerLabel = (TextView)findViewById(R.id.TicketCustomerLabel);
        TicketDescriptionLabel = (TextView)findViewById(R.id.TicketDescriptionLabel);
        TicketDateLabel = (TextView)findViewById(R.id.TicketDateLabel);
        TicketLocationLabel = (TextView)findViewById(R.id.TicketLocationLabel);
        TicketStatusLabel = (TextView)findViewById(R.id.TicketStatusLabel);
        TicketTimerLabel = (TextView)findViewById(R.id.TicketTimerLabel);
        deleteb = (Button)findViewById(R.id.deleteb);
        editb = (Button)findViewById(R.id.editb);
        completeb = (Button)findViewById(R.id.completeb);
        background =(LinearLayout)findViewById(R.id.TicketBackground);
    }
    private void disableButtons(){
        if(new_ticket.getStatus().equals(Ticket.Status.COMPLETED)){
            completeb.setEnabled(false);
            completeb.setBackgroundResource( R.drawable.generic_button );
            editb.setEnabled(false);
            editb.setBackgroundResource( R.drawable.generic_button );
        } else if(new_ticket.getStatus().equals(Ticket.Status.OVERDUE)){
            editb.setEnabled(false);
            editb.setBackgroundResource( R.drawable.generic_button );
        }
    }
    private void setTextViews() {
        // extract values
        String TicketName = new_ticket.getTicketName();
        String TicketType = new_ticket.getTicketType();
        String TicketAssignee = new_ticket.getAssignee();
        String TicketCustomer = new_ticket.getCustomerName();
        String TicketDescription = new_ticket.getDescription();
        String TicketStatus = new_ticket.getStatus().toString();
        Date TicketDate = new_ticket.getTicketDate();
        SimpleDateFormat dt = new SimpleDateFormat("LLLL dd, yyyy hh:mm a");
        String date = dt.format(TicketDate);
        String TicketLocation = new_ticket.getIncidentLocation();
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
        Ticket.Status status = new_ticket.getStatus();
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
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
        //myDb.deleteAll();
    }
    private void closeDB() {
        myDb.close();
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
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.deleteb):
                myDb.deleteTicket(new_ticket.getID());
                cancelAlarm(this,new_ticket.getID());
                setUpAnimation("delete_button_anim", this, view);
                DelayIntent(this,500,MainActivity.class);
                finish();
                break;
            case (R.id.editb):
                setUpAnimation("edit_button_anim", this, view);
                DelayIntent(this,500,NewTicket.class,"new_ticket","",new_ticket);
                finish();
                break;
            case (R.id.completeb):
                myDb.updateTicketStatus(new_ticket.getID(), Ticket.Status.COMPLETED.toString());
                setUpAnimation("complete_button_anim", this, view);
                DelayIntent(this,500,MainActivity.class);
                finish();
                break;
        }
    }

}
