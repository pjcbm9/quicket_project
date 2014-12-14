package pjcbm9.quicket.New_Ticket_Package;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import pjcbm9.quicket.CustomViews.SpartanButton;
import pjcbm9.quicket.MAIN_ACTIVITY_PACKAGE.MainActivity;
import pjcbm9.quicket.R;
import pjcbm9.quicket.Quicket_Package.Ticket;
import pjcbm9.quicket.Quicket_Package.Controller;
import pjcbm9.quicket.Ticket_View_Package.ticketView;

import static pjcbm9.quicket.Alarm_Package.AlarmManagerHelper.setAlarm;
import static pjcbm9.quicket.Quicket_Package.Static_Helpers.DelayIntent;
import static pjcbm9.quicket.Quicket_Package.Static_Helpers.setUpAnimation;
import static pjcbm9.quicket.Quicket_Package.Static_Helpers.updateActionBar;


public class NewTicket extends Activity implements TextWatcher,
    View.OnClickListener, AdapterView.OnItemSelectedListener,View.OnTouchListener {
    // PRIVATE VARIABLES
    private TicketEditText CustomerText,NameText;
    private TicketTextView DateLabel,countDownLabel;
    private TicketLabel LocationLabel,CustomerLabel,TypeLabel,AssigneeLabel,NameLabel;
    private TicketSpinner LocationSpinner,AssigneeSpinner,TypeSpinner;
    private DescriptionDialog descriptionDialog;
    private Controller controller;
    public static final String filename = "savedFile";
    private SharedPreferences savedData;
    private SpartanButton SubmitTicket,SetDescription,SaveDraft;
    private TicketFieldsHandler criticalFieldsHandler;
    private DraftTicketHandler draftTicketHandler;
    private Boolean isNewTicket;
    private Ticket ticket;
    public static final Boolean loadDefaults = false;
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // New Ticket Activity
    //   This Activity Creates New and Draft Tickets
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ticket);
        savedData = getSharedPreferences(filename,0);
        controller = new Controller(this);
        initializeVariables();
        initializeTicket();
        initializeSpinners();
        initializeHandlers();
        initialSetup();
        addListeners();
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void initializeSpinners(){
        LocationSpinner.loadController(controller);
        AssigneeSpinner.loadController(controller);
        TypeSpinner.loadController(controller);
    }
    private void initialSetup(){
        draftTicketHandler.trimDraftFromTicketName();
        criticalFieldsHandler.syncFieldsWithTicket();
    }
    private void initializeTicket(){
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            long countDownDuration = savedData.getLong("countdown",86400000*7);
            ticket = new Ticket(countDownDuration);
            updateActionBar(this,"NEW TICKET");
            isNewTicket = true;
        } else {
            ticket = (Ticket)getIntent().getSerializableExtra("ticket");
            updateActionBar(this,"EDIT TICKET");
            isNewTicket = false;
        }
    }
    private void submitNewTicket(){
        ticketSumbissionUpdates();
        if(isNewTicket){
            SetAlarm();
            insertTicket();
        } else {
            updateTicket();
        }
    }
    private void submitDraftTicket(){
        ticketSumbissionUpdates();
        if(isNewTicket){
            insertTicket();
        } else {
            updateTicket();
        }
    }
    private void ticketSumbissionUpdates(){
        draftTicketHandler.handleTicketName();
        criticalFieldsHandler.syncTicketWithFields();
    }
    private void SetAlarm(){
        setAlarm(this,ticket.getID(),ticket.getCountDownDuration());
    }
    private void insertTicket(){
        controller.requestTicketInsertion(ticket);
    }
    private void updateTicket(){
        controller.requestTicketUpdate(ticket);
    }
    private void initializeHandlers(){
        draftTicketHandler = new DraftTicketHandler(savedData,NameText);
        TicketField[] fields = {new TicketRow(Ticket.Field.NAME,NameLabel,NameText),
                new TicketRow(Ticket.Field.CUSTOMER,CustomerLabel,CustomerText),
                new TicketRow(Ticket.Field.TYPE,TypeLabel,TypeSpinner),
                new TicketRow(Ticket.Field.ASSIGNEE,AssigneeLabel,AssigneeSpinner),
                new TicketRow(Ticket.Field.LOCATION,LocationLabel,LocationSpinner),
                new TicketRow(Ticket.Field.DATE,DateLabel),
                new TicketRow(Ticket.Field.COUNTDOWN, countDownLabel),
                new TicketRow(Ticket.Field.DESCRIPTION,descriptionDialog)};
        criticalFieldsHandler = new TicketFieldsHandler(fields,ticket,SubmitTicket);
    }
    private void addListeners() {
        SetDescription.setOnClickListener(this);
        SubmitTicket.setOnClickListener(this);
        SaveDraft.setOnClickListener(this);
        NameText.addTextChangedListener(this);
        CustomerText.addTextChangedListener(this);
        TypeSpinner.setOnTouchListener(this);
        LocationSpinner.setOnTouchListener(this);
        AssigneeSpinner.setOnTouchListener(this);
    }

    public void initializeVariables() {
        TypeSpinner = (TicketSpinner) findViewById(R.id.TypeSpinner);
        LocationSpinner = (TicketSpinner) findViewById(R.id.LocationSpinner);
        AssigneeSpinner = (TicketSpinner) findViewById(R.id.AssigneeSpinner);
        DateLabel = (TicketTextView) findViewById(R.id.datebox);
        countDownLabel = (TicketTextView) findViewById(R.id.countdown_label);
        NameLabel = (TicketLabel) findViewById(R.id.NameLabel);
        AssigneeLabel = (TicketLabel) findViewById(R.id.AssigneeLabel);
        TypeLabel = (TicketLabel) findViewById(R.id.TypeLabel);
        CustomerLabel = (TicketLabel) findViewById(R.id.CustomerLabel);
        LocationLabel = (TicketLabel) findViewById(R.id.LocationLabel);
        NameText = (TicketEditText) findViewById(R.id.NameText);
        CustomerText = (TicketEditText) findViewById(R.id.CustomerText);
        SetDescription = (SpartanButton) findViewById(R.id.AddDescriptionB);
        SubmitTicket = (SpartanButton) findViewById(R.id.SubmitB);
        SaveDraft = (SpartanButton) findViewById(R.id.SaveDraftB);
        descriptionDialog = new DescriptionDialog(this,SetDescription);
    }

    @Override
    protected void onPause() {
        super.onPause();
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
            case R.id.home_icon:
                delayIntent(0,MainActivity.class);
                finish();
                break;
            case R.id.action_settings: {
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void delayIntent(int milliseconds, Class<?> c){
        DelayIntent(this,milliseconds,c);
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {}

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.close();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case (R.id.SubmitB):
                submitNewTicket();
                SetUpAnimation("silver_anim", view);
                delayIntent(500, ticketView.class, ticket);
                break;
            case (R.id.AddDescriptionB):
                SetUpAnimation("description_anim", view);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        descriptionDialog.show();
                    }
                }, 500);
                break;
            case (R.id.SaveDraftB):
                submitDraftTicket();
                SetUpAnimation("silver_anim", view);
                finish();
                break;
        }
    }
    private void SetUpAnimation(String anim_name,View view){
        setUpAnimation(anim_name,this,view);
    }
    private void delayIntent(int milliseconds, Class<?> c, Ticket ticket){
        DelayIntent(this,milliseconds,c,ticket);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        criticalFieldsHandler.handleFieldsState();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case (R.id.TypeSpinner):
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    TypeSpinner.updateData();
                }
                break;
            case (R.id.LocationSpinner):
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    LocationSpinner.updateData();
                }
                break;
            case (R.id.AssigneeSpinner):
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    AssigneeSpinner.updateData();
                }
                break;
        }
        criticalFieldsHandler.handleFieldsState();
        return false;
    }
}