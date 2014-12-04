package pjcbm9.quicket;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import pjcbm9.quicket.CustomViews.SpartanButton;
import pjcbm9.quicket.CustomViews.SpartanTextView;

import static pjcbm9.quicket.Static_Helpers.DelayFinish;
import static pjcbm9.quicket.Static_Helpers.DelayIntent;
import static pjcbm9.quicket.Static_Helpers.setUpAnimation;
import static pjcbm9.quicket.Static_Helpers.updateActionBar;


public class AlarmActivity extends Activity implements View.OnClickListener{
    private Ticket ticket;
    private TextView overdueTicketName,overdueTicketAssignee,overdueTicketType,overdueTicketCustomer,
            overdueTicketDescription,overdueTicketDate,overdueTicketLocation,overdueTicketNameLabel,
            overdueTicketTypeLabel,overdueTicketAssigneeLabel,overdueTicketCustomerLabel,
            overdueTicketDescriptionLabel,overdueTicketDateLabel,overdueTicketLocationLabel;
    private Button overdue_delete,overdue_edit,overdue_complete,overdue_dismiss;
    private LinearLayout alert_background;
    private DBAdapter myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        openDB();
        ticket = (Ticket)getIntent().getSerializableExtra("new_ticket");
        initializeVariables();
        setTextViews();
        addListeners();
        updateActionBar(this, "QUICKET ALERT");
        setUpAnimation("alert_backbround_anim", this, alert_background);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }
    private void closeDB() {
        myDb.close();
    }
    private void initializeVariables() {
        overdueTicketName = (SpartanTextView)findViewById(R.id.overdueticketnametv);
        overdueTicketType = (SpartanTextView)findViewById(R.id.overduetickettypetv);
        overdueTicketAssignee = (SpartanTextView)findViewById(R.id.overdueticketassigneetv);
        overdueTicketCustomer = (SpartanTextView)findViewById(R.id.overdueticketcustomertv);
        overdueTicketDescription = (SpartanTextView)findViewById(R.id.overdueticketdescriptiontv);
        overdueTicketDate = (SpartanTextView)findViewById(R.id.overdueticketdatetv);
        overdueTicketLocation = (SpartanTextView)findViewById(R.id.overdueticketlocationtv);
        overdueTicketNameLabel = (SpartanTextView)findViewById(R.id.OverdueTicketNameLabel);
        overdueTicketTypeLabel = (SpartanTextView)findViewById(R.id.OverdueTicketTypeLabel);
        overdueTicketAssigneeLabel = (SpartanTextView)findViewById(R.id.OverdueTicketAssigneeLabel);
        overdueTicketCustomerLabel = (SpartanTextView)findViewById(R.id.OverdueTicketCustomerLabel);
        overdueTicketDescriptionLabel = (SpartanTextView)findViewById(R.id.OverdueTicketDescriptionLabel);
        overdueTicketDateLabel = (SpartanTextView)findViewById(R.id.OverdueTicketDateLabel);
        overdueTicketLocationLabel = (SpartanTextView)findViewById(R.id.OverdueTicketLocationLabel);
        overdue_delete = (SpartanButton)findViewById(R.id.overduedeleteb);
        overdue_edit = (SpartanButton)findViewById(R.id.overdueeditb);
        overdue_complete = (SpartanButton)findViewById(R.id.overduecompleteb);
        overdue_dismiss = (SpartanButton)findViewById(R.id.dismissb);
        alert_background = (LinearLayout)findViewById(R.id.alert_background);
    }
    private void setTextViews() {
        // extract values
        String TicketName = ticket.getTicketName();
        String TicketType = ticket.getTicketType();
        String TicketAssignee = ticket.getAssignee();
        String TicketCustomer = ticket.getCustomerName();
        String TicketDescription = ticket.getDescription();
        Date TicketDate = ticket.getTicketDate();
        SimpleDateFormat dt = new SimpleDateFormat("LLLL dd, yyyy hh:mm a");
        String date = dt.format(TicketDate);
        String TicketLocation = ticket.getIncidentLocation();
        // load values
        overdueTicketName.setText(TicketName);
        overdueTicketType.setText(TicketType);
        overdueTicketAssignee.setText(TicketAssignee);
        overdueTicketCustomer.setText(TicketCustomer);
        overdueTicketDescription.setText(TicketDescription);
        overdueTicketDate.setText(date);
        overdueTicketLocation.setText(TicketLocation);
    }
    public void addListeners(){
        overdue_delete.setOnClickListener(this);
        overdue_edit.setOnClickListener(this);
        overdue_complete.setOnClickListener(this);
        overdue_dismiss.setOnClickListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.home_icon2) {
            DelayIntent(this, 500, MainActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case(R.id.overduecompleteb):
                myDb.updateTicketStatus(ticket.getID(), Ticket.Status.COMPLETED.toString());
                setUpAnimation("complete_button_anim", this, view);
                DelayFinish(this, 500);
                break;
            case(R.id.overdueeditb):
                setUpAnimation("edit_button_anim", this, view);
                DelayIntent(this, 500, NewTicket.class, "new_ticket", "", ticket);
                finish();
                break;
            case(R.id.overduedeleteb):
                myDb.deleteTicket(ticket.getID());
                setUpAnimation("delete_button_anim", this, view);
                DelayFinish(this,500);
                break;
            case(R.id.dismissb):
                setUpAnimation("long_button_anim", this, view);
                DelayFinish(this, 500);
                break;
        }
    }
}
