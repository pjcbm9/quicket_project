package pjcbm9.quicket;

import android.app.Activity;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import pjcbm9.quicket.CustomViews.SpartanButton;
import pjcbm9.quicket.CustomViews.SpartanEditText;

import static pjcbm9.quicket.Static_Helpers.DelayIntent;
import static pjcbm9.quicket.Static_Helpers.updateActionBar;


public class Ticket_List extends Activity implements View.OnClickListener,AbsListView.OnScrollListener {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // THERE ARE 3 STATIC CONTROL PANELS IN TICKET_LIST ACTIVITY
    //    1) ACTION BAR
    //    2) VERTICAL BAR
    //    3) LIST EDITOR
    // ! ONCLICK EVENTS TRIGGERED BY THE LIST EDITOR
    //   SHOW A 4th CONTROL PANEL: LIST CONTROL BAR
    // ! MANAGEMENT OF THESE CONTROL PANELS, WITH THE EXCEPTION OF THE ACTION BAR,
    //   HAVE BEEN ENCAPSULATED INTO MANAGER CLASSES
    // ! INSTANCES OF THESE MANAGER CLASSES HAVE BEEN ABREVIATED INTO ACRONYMS FOR
    //   SIMPLICITY
    // ! LEM = LIST EDITOR MANAGER
    //   CBM = CONTROL BAR MANAGER
    //   VBM = VERTICAL BAR MANAGER
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // NAVIGATION LEGEND
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // PRIVATE VARIABLES
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private DBAdapter DB;
    private Button activeB, overdueB, completedB, draftB;
    private Button deleteSelectedTickets, cancelDeleteMode, completeSelectedTickets, cancelCompleteMode,
            search_button, delete_icon, complete_icon, search_icon, cancelSearchMode, bar_button;
    private EditText search_dialog;
    private LinearLayout searchBar, deleteBar, completeBar, list_background, vertical_bar;
    private Ticket.Status status;
    private ArrayList<Ticket> Tickets;
    private ListView ticketList;
    private TicketListAdapter listAdapter;
    private Runnable mRunnable = new Runnable() {
        public void run() {
            updateCountdownTimers();
            mHandler.postDelayed(mRunnable, 1000);
        }
    };
    private ListEditorManager LEM;
    private ControlBarManager CBM;
    private VerticalBarManager VBM;
    private Boolean timersAreActive;
    private int counter;
    private Handler mHandler;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ACTIVITY ABSTRACT METHOD IMPLEMENTATIONS
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);
        openDB();
        initializeVariables();
        timersAreActive = false;
        setListeners();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        initialListSetup();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (status.equals(Ticket.Status.ACTIVE)) {
            startCountdownTimers();
        }
    }

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
        closeDB();
        stopCountdownTimers();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ticket_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.home_icon:
                DelayIntent(this, 0, MainActivity.class);
                finish();
                break;
            case R.id.new_ticket_icon:
                DelayIntent(this, 0, NewTicket.class);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // IMPLEMENTED INTERFACE METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.ActiveB):
                handleVBActions(Ticket.Status.ACTIVE);
                break;
            case (R.id.OverdueB):
                handleVBActions(Ticket.Status.OVERDUE);
                break;
            case (R.id.CompletedB):
                handleVBActions(Ticket.Status.COMPLETED);
                break;
            case (R.id.DraftB):
                handleVBActions(Ticket.Status.DRAFT);
                break;
            case (R.id.ListDeleteB):
                handleCBActions(ControlBarManager.Type.DELETE);
                break;
            case (R.id.search_button):
                handleCBActions(ControlBarManager.Type.SEARCH);
                break;
            case (R.id.ListCompleteB):
                handleCBActions(ControlBarManager.Type.COMPLETE);
                break;
            case (R.id.search):
                handleLEActions(ControlBarManager.Type.SEARCH);
                break;
            case (R.id.delete):
                handleLEActions(ControlBarManager.Type.DELETE);
                break;
            case (R.id.complete):
                handleLEActions(ControlBarManager.Type.COMPLETE);
                break;
            case (R.id.CancelListDeleteB):
                cancelControlBar(ControlBarManager.Type.DELETE);
                break;
            case (R.id.CancelListCompleteB):
                cancelControlBar(ControlBarManager.Type.COMPLETE);
                break;
            case (R.id.search_cancel_button):
                cancelControlBar(ControlBarManager.Type.SEARCH);
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            startCountdownTimers();
        } else {
            stopCountdownTimers();
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // PRIVATE METHODS IN ORDER CALLED IN ONCREATE
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void openDB() {
        DB = new DBAdapter(this);
        try {
            DB.open();
        } catch (SQLiteException e) {
            Log.e("SQLite Error", e.toString(), e);
        }

    }

    private void initializeVariables() {
        ticketList = (ListView) findViewById(R.id.TicketListView);
        activeB = (Button) findViewById(R.id.ActiveB);
        overdueB = (Button) findViewById(R.id.OverdueB);
        completedB = (Button) findViewById(R.id.CompletedB);
        draftB = (Button) findViewById(R.id.DraftB);
        bar_button = (SpartanButton) findViewById(R.id.bar_button);
        completeSelectedTickets = (SpartanButton) findViewById(R.id.ListCompleteB);
        deleteSelectedTickets = (SpartanButton) findViewById(R.id.ListDeleteB);
        cancelCompleteMode = (Button) findViewById(R.id.CancelListCompleteB);
        cancelDeleteMode = (Button) findViewById(R.id.CancelListDeleteB);
        cancelSearchMode = (Button) findViewById(R.id.search_cancel_button);
        search_dialog = (SpartanEditText) findViewById(R.id.search_dialog);
        search_button = (SpartanButton) findViewById(R.id.search_button);
        search_icon = (Button) findViewById(R.id.search);
        delete_icon = (Button) findViewById(R.id.delete);
        complete_icon = (Button) findViewById(R.id.complete);
        searchBar = (LinearLayout) findViewById(R.id.search_bar);
        deleteBar = (LinearLayout) findViewById(R.id.delete_bar);
        completeBar = (LinearLayout) findViewById(R.id.complete_bar);
        list_background = (LinearLayout) findViewById(R.id.list_background);
        vertical_bar = (LinearLayout) findViewById(R.id.vertical_bar);
        mHandler = new Handler();
        VBM = new VerticalBarManager(DB, activeB, overdueB, completedB, draftB, bar_button);
        CBM = new ControlBarManager(searchBar, completeBar, deleteBar);
        LEM = new ListEditorManager(delete_icon, complete_icon, search_icon);
    }

    private void setListeners() {
        activeB.setOnClickListener(this);
        overdueB.setOnClickListener(this);
        completedB.setOnClickListener(this);
        draftB.setOnClickListener(this);
        completeSelectedTickets.setOnClickListener(this);
        cancelCompleteMode.setOnClickListener(this);
        deleteSelectedTickets.setOnClickListener(this);
        cancelDeleteMode.setOnClickListener(this);
        cancelSearchMode.setOnClickListener(this);
        search_button.setOnClickListener(this);
        search_icon.setOnClickListener(this);
        delete_icon.setOnClickListener(this);
        complete_icon.setOnClickListener(this);
        ticketList.setOnScrollListener(this);
    }

    private void initialListSetup() {
        if (getIntent().getExtras() == null) {
            throw new AssertionError("Status is null!");
        }
        handleVBActions(Ticket.Status.valueOf(getIntent().getStringExtra("status")));
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ACTION BAR HANDLING METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ! updates action bar title according to the ticket status of the ticket list
    // ! the function "updateActionBar" is a public static method defined in the Static_Helpers class
    private void updateActionBarByStatus() {
        switch (status) {
            case ACTIVE:
                updateActionBar(this, "ACTIVE TICKETS");
                break;
            case OVERDUE:
                updateActionBar(this, "OVERDUE TICKETS");
                break;
            case COMPLETED:
                updateActionBar(this, "COMPLETED TICKETS");
                break;
            case DRAFT:
                updateActionBar(this, "DRAFT TICKETS");
                break;
            case SEARCH:
                updateActionBar(this, "SEARCH TICKETS");
                break;
        }
    }
    private void updateListBackground() {
        switch (status) {
            case ACTIVE:
                list_background.setBackgroundResource(R.drawable.active_list_background);
                break;
            case OVERDUE:
                list_background.setBackgroundResource(R.drawable.overdue_list_background);
                break;
            case COMPLETED:
                list_background.setBackgroundResource(R.drawable.completed_list_background);
                break;
            case DRAFT:
                list_background.setBackgroundResource(R.drawable.draft_list_background);
                break;
            case SEARCH:
                list_background.setBackgroundResource(R.drawable.search_list_background);
                break;
        }
    }
    private void updateVerticalBarBackground() {
        switch (status) {
            case ACTIVE:
                vertical_bar.setBackgroundResource(R.drawable.active_vertical_bar);
                break;
            case OVERDUE:
                vertical_bar.setBackgroundResource(R.drawable.overdue_vertical_bar);
                break;
            case COMPLETED:
                vertical_bar.setBackgroundResource(R.drawable.completed_vertical_bar);
                break;
            case DRAFT:
                vertical_bar.setBackgroundResource(R.drawable.draft_vertical_bar);
                break;
            case SEARCH:
                vertical_bar.setBackgroundResource(R.drawable.search_vertical_bar);
                break;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // MAIN EVENT HANDLING METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // handles VERTICAL BAR actions
    private void handleVBActions(Ticket.Status Status) {
        this.status = Status;
        updateControlPanels();
        setupListView();
    }
    // handles CONTROL BAR actions
    // ! currently both delete and complete run the same routine because their execution is parallel
    //   however, in the event their paths differ, the switch statement allows that transition to be
    //   easier
    private void handleCBActions(ControlBarManager.Type type) {
        switch (type) {
            case DELETE:
                CB_CompleteOrDelete(ControlBarManager.Type.DELETE);
                break;
            case COMPLETE:
                CB_CompleteOrDelete(ControlBarManager.Type.COMPLETE);
                break;
            case SEARCH:
                CB_Search();
                break;
        }
    }
    // handles LIST EDITOR actions
    private void handleLEActions(ControlBarManager.Type type) {
        stopCountdownTimers();
        switch (type) {
            case DELETE: LE_delete();
                break;
            case COMPLETE: LE_complete();
                break;
            case SEARCH: LE_search();
                break;
        }
        // common actions for all cases
        CBM.enableControlBar(type);
        LEM.disableIcon(type);
    }
    // handles CONTROL BAR cancellations
    private void cancelControlBar(ControlBarManager.Type type) {
        switch (type) {
            case DELETE:
                cancelCompleteOrDelete();
                break;
            case COMPLETE:
                cancelCompleteOrDelete();
                break;
            case SEARCH:
                cancelSearch();
                break;
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // ANCILLARY EVENT HANDLING METHODS
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // METHODS ANCILLARY TO "handleVBActions(Ticket.Status Status)"
    // listed in order called
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // updates the components of the 4 control panels
    //   1) updateActionBarByStatus() - updates ACTIONBAR title
    //   2) VBM.UpdateVerticalButtons(status) - updates which buttons in the VERTICAL BAR are enabled
    //   3) LEM.UpdateIcons(status) - updates the Visibility of the icons in the LIST EDITOR
    //   4) CBM.disableControlBars() - ensures the control bar is not shown
    private void updateControlPanels() {
        updateActionBarByStatus();
        updateListBackground();
        updateVerticalBarBackground();
        VBM.UpdateVerticalButtons(status);
        LEM.UpdateIcons(status);
        CBM.disableControlBars();
    }
    // retrieves tickets from database and sets up the list adapter (contents of listview)
    public void setupListView() {
        updateTickets();
        setupAdapter();
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // METHODS ANCILLARY TO "handleCBActions(ControlBarManager.Type type)"
    // listed in order called
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // runs when the button "DELETE" or "COMPLETE" are clicked from the CONTROL BAR
    //   1) method "handleCheckedTickets()" updates the listview and database for all tickets deleted or completed
    //   2)
    private void CB_CompleteOrDelete(ControlBarManager.Type type) {
        if (listAdapter.handleCheckedTickets()) {
            CBM.disableControlBar(type);
            updateCountdownHandler();
            CBM.disableControlBar(type);
        }
    }
    private void CB_Search() {
        setupListView(search_dialog.getText().toString());
    }


    private void setupSearchBar() {
        status = Ticket.Status.SEARCH;
        stopCountdownTimers();
        updateActionBarByStatus();
        updateListBackground();
        updateVerticalBarBackground();
        LEM.setStatus(status);
        VBM.UpdateVerticalButtons(status);
        ticketList.setAdapter(null);
    }
    private void LE_complete() {
        listAdapter.setCheckboxStyle(CheckBox.Style.COMPLETE);
    }
    private void LE_delete(){
        listAdapter.setCheckboxStyle(CheckBox.Style.DELETE);
    }
    private void LE_search() {
        setupSearchBar();
    }










    //////////////////////////////////////////////////////////////////////////////////////////////////
    // COUNTDOWN TIMER HANDLER METHODS
    //////////////////////////////////////////////////////////////////////////////////////////////
    private void updateCountdownHandler() {
        if (status.equals(Ticket.Status.ACTIVE)) {
            startCountdownTimers();
        } else {
            stopCountdownTimers();
        }
    }

    public void stopCountdownTimers() {
        if (timersAreActive) {
            timersAreActive = false;
            mHandler.removeCallbacks(mRunnable);
        }
    }

    private void cancelCompleteOrDelete() {
        CBM.disableControlBars();
        LEM.UpdateIcons(status);
        listAdapter.cancelCheckBoxMode();
        startCountdownTimers();
    }

    private void cancelSearch() {
        handleVBActions(status);
    }


    private void setupAdapter() {
        listAdapter = new TicketListAdapter(this, Tickets, DB);
        ticketList.setAdapter(listAdapter);
        updateCountdownHandler();

    }



    public void setupListView(String keyword) {
        try {
            Tickets = DB.findTicketsByKeyword(keyword);
        } catch (SQLiteException e) {
            Log.e("SQLite Error retrieving tickets", e.toString(), e);
        }
        setupAdapter();
    }



    private void updateTickets() {
        try {
            Tickets = DB.findTicketsByStatus(status);
        } catch (SQLiteException e) {
            Log.e("SQLite Error retrieving tickets", e.toString(), e);
        }
    }

    private void updateCountdownTimers() {
        long millis = System.currentTimeMillis();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            Ticket temp = (Ticket) listAdapter.getItem(i);
            temp.setCountDownTimer(millis);
        }
        counter++;
        System.out.println("CountDown: " + counter);
        listAdapter.notifyDataSetChanged();
    }

    private void startCountdownTimers() {
        if (status.equals(Ticket.Status.ACTIVE)) {
            timersAreActive = true;
            counter = 0;
            mHandler.postDelayed(mRunnable, 1000);
        }
    }


    private void closeDB() {
        DB.close();
    }



}

