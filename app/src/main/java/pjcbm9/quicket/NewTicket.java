package pjcbm9.quicket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static pjcbm9.quicket.AlarmManagerHelper.setAlarm;
import static pjcbm9.quicket.Static_Helpers.DelayIntentAndFinish;
import static pjcbm9.quicket.Static_Helpers.setUpAnimation;
import static pjcbm9.quicket.Static_Helpers.updateActionBar;


public class NewTicket extends Activity implements TextWatcher,
    View.OnClickListener, AdapterView.OnItemSelectedListener {
    ArrayList<String> ticketTypes;
    int DraftNumber;
    ArrayList<String> ticketLocations;
    ArrayList<String> ticketAssignees;
    Spinner TicketTypeSpinner;
    Spinner TicketLocationSpinner;
    Spinner TicketAssigneeSpinner;
    TextView DateBox;
    TextView NameLabel;
    TextView AssigneeLabel;
    TextView TypeLabel;
    TextView CustomerLabel;
    TextView LocationLabel;
    TextView DescriptionLabel;
    TextView DateLabel;
    EditText NameText;
    EditText CustomerText;
    Button SaveDraft;
    Button SetDescription;
    Button SubmitTicket;
    String Description;
    Date date;
    public static final String filename = "savedFile";
    SharedPreferences savedData;
    Ticket ticket;
    ArrayList<Ticket> tickets;
    DBAdapter myDb;
    Boolean isNewTicket;
    AnimationDrawable TicketNameAnim;
    AnimationDrawable TicketCustomerAnim;
    public static final Boolean loadDefaults = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ticket);
        savedData = getSharedPreferences(filename,0);
        openDB();
        if(loadDefaults){
            loadDefaultSpinnerContents();
        }
        initializeVariables();
        setDefaultAnim();
        setFonts();
        addListeners();
        setSpinnerAdapters();
        date = new Date();
        Description = "";
        // extract ticket details if available
        Bundle extras = getIntent().getExtras();
        isNewTicket = true;
        if(extras != null) {
            ticket = (Ticket)getIntent().getSerializableExtra("new_ticket");
            setReturningVariables();
            isNewTicket = false;
        }
        setDateBox();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        updateActionBar(this,"NEW TICKET");
        //updateActionBar();
    }
    private void setDateBox() {
        SimpleDateFormat dt = new SimpleDateFormat("LLLL dd, yyyy hh:mm a");
        String Date = dt.format(date);
        DateBox.setText(Date);
    }

    public void setReturningVariables() {
        NameText.setText(ticket.getTicketName());
        CustomerText.setText(ticket.getCustomerName());
        Description = ticket.getDescription();
        if(!ticket.getStatus().equals(Ticket.Status.DRAFT)){
            date = ticket.getTicketDate();
        }
        TicketLocationSpinner.setSelection(((ArrayAdapter)TicketLocationSpinner.getAdapter()).getPosition(ticket.getIncidentLocation()));
        TicketAssigneeSpinner.setSelection(((ArrayAdapter)TicketAssigneeSpinner.getAdapter()).getPosition(ticket.getAssignee()));
        TicketTypeSpinner.setSelection(((ArrayAdapter)TicketTypeSpinner.getAdapter()).getPosition(ticket.getTicketType()));
    }

    public void addListeners() {
        SetDescription.setOnClickListener(this);
        SubmitTicket.setOnClickListener(this);
        SaveDraft.setOnClickListener(this);
        NameText.addTextChangedListener(this);
        CustomerText.addTextChangedListener(this);
    }

    public void initializeVariables() {
        TicketTypeSpinner = (Spinner) findViewById(R.id.TypeSpinner);
        TicketLocationSpinner = (Spinner) findViewById(R.id.LocationSpinner);
        TicketAssigneeSpinner = (Spinner) findViewById(R.id.AssigneeSpinner);
        DateBox = (TextView) findViewById(R.id.datebox);
        NameLabel = (TextView) findViewById(R.id.NameLabel);
        AssigneeLabel = (TextView) findViewById(R.id.AssigneeLabel);
        TypeLabel = (TextView) findViewById(R.id.TypeLabel);
        CustomerLabel = (TextView) findViewById(R.id.CustomerLabel);
        LocationLabel = (TextView) findViewById(R.id.LocationLabel);
        DescriptionLabel = (TextView) findViewById(R.id.DescriptionLabel);
        DateLabel = (TextView) findViewById(R.id.DateLabel);
        NameText = (EditText) findViewById(R.id.NameText);
        CustomerText = (EditText) findViewById(R.id.CustomerText);
        SetDescription = (Button) findViewById(R.id.AddDescriptionB);
        SubmitTicket = (Button) findViewById(R.id.SubmitB);
        SubmitTicket.setEnabled(false);
        SaveDraft = (Button) findViewById(R.id.SaveDraftB);
    }
    private void setDefaultAnim() {
        NameLabel.setBackgroundResource(R.drawable.blue_anim);
        CustomerLabel.setBackgroundResource(R.drawable.blue_anim);
        TicketNameAnim = (AnimationDrawable) NameLabel.getBackground();
        TicketCustomerAnim = (AnimationDrawable) CustomerLabel.getBackground();
        TicketNameAnim.start();
        TicketCustomerAnim.start();
    }
    private void setFonts() {
        Typeface mycustomFont = Typeface.createFromAsset(getAssets(),"fonts/spartan.otf");
        DateLabel.setTypeface(mycustomFont);
        NameLabel.setTypeface(mycustomFont);
        AssigneeLabel.setTypeface(mycustomFont);
        TypeLabel.setTypeface(mycustomFont);
        CustomerLabel.setTypeface(mycustomFont);
        LocationLabel.setTypeface(mycustomFont);
        DescriptionLabel.setTypeface(mycustomFont);
        SubmitTicket.setTypeface(mycustomFont);
        SaveDraft.setTypeface(mycustomFont);
        NameText.setTypeface(mycustomFont);
        CustomerText.setTypeface(mycustomFont);
        //TicketAssigneeSpinner.setTypeface(mycustomFont);
        //TicketLocationSpinner.setTypeface(mycustomFont);
        //TicketTypeSpinner.setTypeface(mycustomFont);
        DateBox.setTypeface(mycustomFont);
        SetDescription.setTypeface(mycustomFont);

    }
    public void setSpinnerAdapters() {
        loadSpinnerArrays();
        ArrayAdapter TicketsTypesAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                ticketTypes);
        TicketTypeSpinner.setAdapter(TicketsTypesAdapter);
        ArrayAdapter TicketLocationsAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                ticketLocations);
        TicketLocationSpinner.setAdapter(TicketLocationsAdapter);
        ArrayAdapter TicketAssigneesAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                ticketAssignees);
        TicketAssigneeSpinner.setAdapter(TicketAssigneesAdapter);
    }
    public void loadSpinnerArrays() {
        ticketTypes = setupStringArray(ticketTypes, MItem.mType.TYPES);
        ticketLocations = setupStringArray(ticketLocations, MItem.mType.LOCATIONS);
        ticketAssignees = setupStringArray(ticketAssignees, MItem.mType.USERS);
    }
    public ArrayList<String> setupStringArray(ArrayList<String> string_array, MItem.mType type ) {
        ArrayList<MItem> items = myDb.findMaintenanceItemsByType(type.toString());
        string_array = new ArrayList<String>();
        System.out.println(type.toString() + ": " + items.size());
        for(MItem item: items){
            string_array.add(item.getItemName());
        }
        return string_array;
    }
    public void loadDefaultSpinnerContents() {
        ArrayList<MItem> TicketTypeItems = new ArrayList<MItem>();
        TicketTypeItems.add(new MItem(1, MItem.mType.TYPES,"Projector Issue"));
        TicketTypeItems.add(new MItem(2, MItem.mType.TYPES,"Computer Issue"));
        TicketTypeItems.add(new MItem(3, MItem.mType.TYPES,"Audio Issue"));
        TicketTypeItems.add(new MItem(4, MItem.mType.TYPES,"TideBreak Issue"));
        TicketTypeItems.add(new MItem(5, MItem.mType.TYPES,"Network Issue"));
        TicketTypeItems.add(new MItem(6, MItem.mType.TYPES,"Crestron Issue"));
        TicketTypeItems.add(new MItem(7, MItem.mType.TYPES,"Information Request"));
        TicketTypeItems.add(new MItem(8, MItem.mType.TYPES,"Printer Issue"));
        TicketTypeItems.add(new MItem(9, MItem.mType.TYPES,"Other"));
        myDb.insertMItemArray(TicketTypeItems);
        ArrayList<MItem> TicketLocations = new ArrayList<MItem>();
        TicketLocations.add(new MItem(1, MItem.mType.LOCATIONS,"211"));
        TicketLocations.add(new MItem(2, MItem.mType.LOCATIONS,"212"));
        TicketLocations.add(new MItem(3, MItem.mType.LOCATIONS,"213"));
        TicketLocations.add(new MItem(4, MItem.mType.LOCATIONS,"218"));
        TicketLocations.add(new MItem(5, MItem.mType.LOCATIONS,"220"));
        TicketLocations.add(new MItem(6, MItem.mType.LOCATIONS,"320"));
        myDb.insertMItemArray(TicketLocations);
        ArrayList<MItem> TicketUsers = new ArrayList<MItem>();
        TicketUsers.add(new MItem(1, MItem.mType.USERS,"Jason Crow"));
        TicketUsers.add(new MItem(2, MItem.mType.USERS,"Abe Rotich"));
        TicketUsers.add(new MItem(3, MItem.mType.USERS,"Scott Morris"));
        TicketUsers.add(new MItem(4, MItem.mType.USERS,"Nate Baker"));
        TicketUsers.add(new MItem(5, MItem.mType.USERS,"Fed"));
        TicketUsers.add(new MItem(6, MItem.mType.USERS,"Matt Sunderland"));
        myDb.insertMItemArray(TicketUsers);
    }
    @Override
    protected void onPause() {
        super.onPause();
        /*SharedPreferences.Editor editor = savedData.edit();
        editor.clear();
        editor.putString("NameText", NameText.getText().toString());
        editor.putString("CustomerText", CustomerText.getText().toString());
        editor.putInt("TicketType", TicketTypeSpinner.getSelectedItemPosition());
        editor.putInt("TicketLocation", TicketLocationSpinner.getSelectedItemPosition());
        editor.putInt("TicketAssignee", TicketAssigneeSpinner.getSelectedItemPosition());
        editor.putString("Description", Description);
        editor.putString("Date", Date);
        editor.commit();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_ticket, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch(item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
            case R.id.action_settings: {
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void createTicket() {
        String Ticket_Name = NameText.getText().toString();
        if(Ticket_Name.equals("")){
            saveTicketNumber();
            Ticket_Name = "Draft #" + DraftNumber;
        }
        String Ticket_Customer = CustomerText.getText().toString();
        String Ticket_Type = (String) TicketTypeSpinner.getSelectedItem();
        String Ticket_Location = (String) TicketLocationSpinner.getSelectedItem();
        String Ticket_Assignee = (String) TicketAssigneeSpinner.getSelectedItem();
        if(isNewTicket){
            ticket = new Ticket(Ticket_Name, Ticket_Type, Ticket_Location,
                    date, Ticket_Assignee,
                    Description,Ticket_Customer, Ticket.Status.DRAFT);
        } else {
            ticket.setTicketName(Ticket_Name);
            ticket.setTicketType(Ticket_Type);
            ticket.setIncidentLocation(Ticket_Location);
            ticket.setAssignee(Ticket_Assignee);
            ticket.setDescription(Description);
            ticket.setCustomerName(Ticket_Customer);
        }
        ticket.setCountDownDuration(savedData.getLong("countdown",86400000*7));

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
    private void saveTicketNumber() {
        handleTicketNumber();
        SharedPreferences.Editor editor = savedData.edit();
        editor.putInt("totalDrafts", DraftNumber);
        editor.commit();
    }
    private void handleTicketNumber() {
        DraftNumber = savedData.getInt("totalDrafts",0);
        DraftNumber++;
    }
    private void UpdateTicket(Ticket.Status status) {
        ticket.setStatus(status);
        UpdateDB();
        if(status.equals(Ticket.Status.ACTIVE)) {
            saveTicketNumber();
        }
    }
    private void UpdateDB() {
        if(isNewTicket) {
            myDb.insertNewTicket(ticket);
        } else {
            myDb.updateTicket(ticket);
        }
    }
    @Override
    public void onClick(View view) {
        createTicket();
        switch (view.getId()) {
            case (R.id.SubmitB):
                setAlarm(this,ticket.getID(),ticket.getCountDownDuration());
                UpdateTicket(Ticket.Status.ACTIVE);
                setUpAnimation("silver_anim", this, view);
                DelayIntentAndFinish(this, 500, ticketView.class, "new_ticket", "", ticket);
                break;
            case (R.id.AddDescriptionB):
                setUpAnimation("silver_anim", this, view);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        createDescriptionDialog();
                    }
                }, 500);
                break;
            case (R.id.SaveDraftB):
                setUpAnimation("silver_anim", this, view);
                UpdateDB();
                DelayIntentAndFinish(this, 500, MainActivity.class);
                break;
        }
    }

    private void createDescriptionDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View DescriptionDialogView = factory.inflate(
                R.layout.add_description_dialog, null);
        final AlertDialog DescriptionDialog = new AlertDialog.Builder(this).create();
        DescriptionDialog.setView(DescriptionDialogView);

        Button ClearButton = (Button)DescriptionDialogView.findViewById(R.id.clearb);
        Button SubmitDescriptionButton = (Button)DescriptionDialogView.findViewById(R.id.Submitb);

        TextView AddDescriptionLabel = (TextView)DescriptionDialogView.findViewById(R.id.AddDescriptionLabel);
        Typeface mycustomFont = Typeface.createFromAsset(getAssets(),"fonts/spartan.otf");
        AddDescriptionLabel.setTypeface(mycustomFont);
        ClearButton.setTypeface(mycustomFont);
        SubmitDescriptionButton.setTypeface(mycustomFont);

        final TextView DescriptionTV = (TextView)DescriptionDialogView.findViewById(R.id.DescriptionText);
        ClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpAnimation("silver_anim", getBaseContext(), v);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DescriptionTV.setText("");
                    }
                }, 500);
            }
        });
        SubmitDescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpAnimation("silver_anim", getBaseContext(), view);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Description = DescriptionTV.getText().toString();
                        DescriptionDialog.dismiss();
                    }
                }, 500);
            }
        });
        DescriptionDialog.show();
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(NameText.length() > 0) {
            TicketNameAnim.setOneShot(true); // finish animation cycle
        } else { // restart animation cycle
            TicketNameAnim.stop();
            TicketNameAnim.start();
            TicketNameAnim.setOneShot(false);
        }
        if(CustomerText.length() > 0) {
            TicketCustomerAnim.setOneShot(true); // finish animation cycle
        } else { // restart animation cycle
            TicketCustomerAnim.stop();
            TicketCustomerAnim.start();
            TicketCustomerAnim.setOneShot(false);
        }
        if(NameText.length() > 0 && CustomerText.length() > 0) {
            SubmitTicket.setEnabled(true);
        } else {
            SubmitTicket.setEnabled(false);
        }
    }
}
