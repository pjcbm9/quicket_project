package pjcbm9.quicket;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pjcbm9.quicket.CustomViews.DigitTextView;
import pjcbm9.quicket.CustomViews.SpartanButton;
import pjcbm9.quicket.CustomViews.SpartanTextView;

import static pjcbm9.quicket.Static_Helpers.DelayIntent;
import static pjcbm9.quicket.Static_Helpers.setUpAnimation;
import static pjcbm9.quicket.Static_Helpers.updateActionBar;


public class MainActivity extends Activity implements View.OnClickListener {
    Button CreateNewTicket,ViewActiveTickets,ViewCompletedTickets,LoadDraftTickets,OverdueTickets;
    TextView ActiveCount,OverdueCount,CompletedCount;
    TextView ActiveLabel,OverdueLabel,CompletedLabel;
    DBAdapter myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openDB();
        initializeVariables();
        addListeners();
        setCounters();
        updateActionBar(this,"QUICKET");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCounters();
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

    public void initializeVariables() {
        OverdueTickets = (SpartanButton)findViewById(R.id.OverdueB);
        CreateNewTicket = (SpartanButton)findViewById(R.id.NewTicketB);
        ViewActiveTickets = (SpartanButton)findViewById(R.id.activetickets);
        ViewCompletedTickets = (SpartanButton)findViewById(R.id.ViewCompletedB);
        LoadDraftTickets = (SpartanButton)findViewById(R.id.loadticketb);
        ActiveCount = (DigitTextView)findViewById(R.id.activecount);
        OverdueCount = (DigitTextView)findViewById(R.id.overduecount);
        CompletedCount = (DigitTextView)findViewById(R.id.completedcount);
        ActiveLabel = (SpartanTextView)findViewById(R.id.ActiveLabel);
        OverdueLabel = (SpartanTextView)findViewById(R.id.OverdueLabel);
        CompletedLabel = (SpartanTextView)findViewById(R.id.CompletedLabel);
    }

    private void enableButtons() {
        ViewActiveTickets.setEnabled(true);
        ViewCompletedTickets.setEnabled(true);
        LoadDraftTickets.setEnabled(true);
        OverdueTickets.setEnabled(true);
        ActiveLabel.setBackgroundResource( R.drawable.trendyblue2 );
        OverdueLabel.setBackgroundResource( R.drawable.trendyred2 );
        CompletedLabel.setBackgroundResource( R.drawable.trendygreen4 );
    }
    private void setCounters() {
        enableButtons();
        myDb.updateTickets();
        int ActiveTicketCount = myDb.findTicketCountByStatus(Ticket.Status.ACTIVE);
        int OverdueTicketCount = myDb.findTicketCountByStatus(Ticket.Status.OVERDUE);
        int CompletedTicketCount = myDb.findTicketCountByStatus(Ticket.Status.COMPLETED);
        int DraftTicketCount = myDb.findTicketCountByStatus(Ticket.Status.DRAFT);
        ActiveCount.setText("" + ActiveTicketCount);
        OverdueCount.setText("" + OverdueTicketCount);
        CompletedCount.setText("" + CompletedTicketCount);
        if(ActiveTicketCount == 0){
            ViewActiveTickets.setEnabled(false);
            ActiveLabel.setBackgroundResource( R.drawable.silver_disabled );
        }
        if(OverdueTicketCount == 0){
            OverdueTickets.setEnabled(false);
            OverdueLabel.setBackgroundResource( R.drawable.silver_disabled );
        }
        if(CompletedTicketCount == 0){
            ViewCompletedTickets.setEnabled(false);
            CompletedLabel.setBackgroundResource( R.drawable.silver_disabled );
        }
        if(DraftTicketCount == 0){
            LoadDraftTickets.setEnabled(false);
        }
    }
    public void addListeners(){
        CreateNewTicket.setOnClickListener(this);
        ViewActiveTickets.setOnClickListener(this);
        ViewCompletedTickets.setOnClickListener(this);
        LoadDraftTickets.setOnClickListener(this);
        OverdueTickets.setOnClickListener(this);
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
            DelayIntent(this, 500, Maintenance.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case(R.id.NewTicketB):
                setUpAnimation("test", this, view);
                DelayIntent(this, 500, NewTicket.class);
                break;
            case(R.id.loadticketb):
                setUpAnimation("test1", this, view);
                DelayIntent(this,500,Ticket_List.class,"status",Ticket.Status.DRAFT.toString(),null);
                break;
            case(R.id.activetickets):
                setUpAnimation("test2", this, view);
                DelayIntent(this,500,Ticket_List.class,"status",Ticket.Status.ACTIVE.toString(),null);
                break;
            case(R.id.ViewCompletedB):
                setUpAnimation("test3", this, view);
                DelayIntent(this,500,Ticket_List.class,"status",Ticket.Status.COMPLETED.toString(),null);
                break;
            case(R.id.OverdueB):
                setUpAnimation("test3", this, view);
                DelayIntent(this,500,Ticket_List.class,"status",Ticket.Status.OVERDUE.toString(),null);
                break;
        }
    }
}
