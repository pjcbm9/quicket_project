package pjcbm9.quicket.Alarm_Package;

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
import pjcbm9.quicket.MAIN_ACTIVITY_PACKAGE.MainActivity;
import pjcbm9.quicket.New_Ticket_Package.NewTicket;
import pjcbm9.quicket.Quicket_Package.Controller;
import pjcbm9.quicket.Quicket_Package.Ticket;
import pjcbm9.quicket.R;
import pjcbm9.quicket.Ticket_View_Package.SendMailTask;

import static pjcbm9.quicket.Quicket_Package.Static_Helpers.DelayFinish;
import static pjcbm9.quicket.Quicket_Package.Static_Helpers.DelayIntent;
import static pjcbm9.quicket.Quicket_Package.Static_Helpers.setUpAnimation;
import static pjcbm9.quicket.Quicket_Package.Static_Helpers.updateActionBar;

// ALARM ACTIVITY : is the activity that launches when a ticket first becomes overdue
//      This class is pretty similar to TicketView, except it has a animated background, a dismiss button, and a label
//      indicating the ticket has become overdue
public class AlarmActivity extends Activity implements View.OnClickListener{
    private Ticket ticket;
    private TextView overdueTicketName,overdueTicketAssignee,overdueTicketType,overdueTicketCustomer,
            overdueTicketDescription,overdueTicketDate,overdueTicketLocation;
    private Button overdue_delete,overdue_edit,overdue_complete,overdue_dismiss;
    private LinearLayout alert_background;
    private Controller controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        controller = new Controller(this);
        ticket = (Ticket)getIntent().getSerializableExtra("ticket");
        initializeVariables();
        setTextViews();
        addListeners();
        updateActionBar(this, "QUICKET ALERT");
        setUpAnimation("alert_backbround_anim", this, alert_background);
        new SendMailTask(ticket,SendMailTask.type.OVERDUE).execute((Void[]) null);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.close();
    }

    private void initializeVariables() {
        overdueTicketName = (SpartanTextView)findViewById(R.id.overdueticketnametv);
        overdueTicketType = (SpartanTextView)findViewById(R.id.overduetickettypetv);
        overdueTicketAssignee = (SpartanTextView)findViewById(R.id.overdueticketassigneetv);
        overdueTicketCustomer = (SpartanTextView)findViewById(R.id.overdueticketcustomertv);
        overdueTicketDescription = (SpartanTextView)findViewById(R.id.overdueticketdescriptiontv);
        overdueTicketDate = (SpartanTextView)findViewById(R.id.overdueticketdatetv);
        overdueTicketLocation = (SpartanTextView)findViewById(R.id.overdueticketlocationtv);
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
            delayIntent(500, MainActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case(R.id.overduecompleteb):
                controller.requestCompleteTicket(ticket.getID());
                setUpAnimation("complete_button_anim", this, view);
                DelayFinish(this, 500);
                break;
            case(R.id.overdueeditb):
                setUpAnimation("edit_button_anim", this, view);
                delayIntent(500, NewTicket.class, ticket);
                finish();
                break;
            case(R.id.overduedeleteb):
                controller.requestTicketDeletion(ticket.getID());
                setUpAnimation("delete_button_anim", this, view);
                DelayFinish(this,500);
                break;
            case(R.id.dismissb):
                setUpAnimation("long_button_anim", this, view);
                DelayFinish(this, 500);
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
