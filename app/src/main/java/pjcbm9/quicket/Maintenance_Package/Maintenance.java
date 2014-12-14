package pjcbm9.quicket.Maintenance_Package;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import pjcbm9.quicket.Quicket_Package.Controller;
import pjcbm9.quicket.R;

import static pjcbm9.quicket.Quicket_Package.Static_Helpers.loadDefaultSpinnerContents;
import static pjcbm9.quicket.Quicket_Package.Static_Helpers.updateActionBar;


public class Maintenance extends Activity implements View.OnClickListener{


    private Spinner mSpinner;
    private ListView maintenanceList;
    private LinearLayout countdown_mode;
    private Controller controller;
    private MaintenanceAdapter mAdapter;
    private Button addItem,clearItems,saveChanges,UndoChanges;
    private TextView label;
    private ArrayList<MItem> items;
    private ArrayList<MItem> savedItems;
    private Countdown_Editor countdownEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.controller = new Controller(this);
        setContentView(R.layout.activity_maintenance);
        initializeVariables();
        this.countdownEditor = new Countdown_Editor(this);
        setFonts();
        setListeners();
        setUpSpinner();
        setSpinnerListener();
        loadListContents();
        updateMaintenanceMode();
        updateActionBar(this,"MAINTENANCE MODE");
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }
    private Boolean updateMaintenanceMode(){
        Boolean isCountdownEditorMode = countdownEditor.getCountdownEditorMode();
        if(isCountdownEditorMode){
            countdown_mode.setVisibility(View.VISIBLE);
            maintenanceList.setVisibility(View.GONE);
            addItem.setVisibility(View.GONE);
            clearItems.setVisibility(View.GONE);
        } else {
            countdown_mode.setVisibility(View.GONE);
            maintenanceList.setVisibility(View.VISIBLE);
            addItem.setVisibility(View.VISIBLE);
            clearItems.setVisibility(View.VISIBLE);
        }
        return isCountdownEditorMode;
    }
    private void setSpinnerListener() {
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                System.out.println("here");
                loadListContents();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }
    private void setUpSpinner() {
        ArrayList<String> spinnerItems = new ArrayList<String>();
        spinnerItems.add("TYPES");
        spinnerItems.add("USERS");
        spinnerItems.add("LOCATIONS");
        spinnerItems.add("COUNTDOWN");

        ArrayAdapter TicketsTypesAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerItems);
        mSpinner.setAdapter(TicketsTypesAdapter);
    }
    private void initializeVariables() {
        countdown_mode = (LinearLayout)findViewById(R.id.CountdownEditor);
        label = (TextView)findViewById(R.id.MaintenanceLabel);
        mSpinner = (Spinner)findViewById(R.id.MaintenanceSpinner);
        maintenanceList = (ListView) findViewById(R.id.MaintenanceListView);
        addItem = (Button) findViewById(R.id.MaintenanceAddItem);
        clearItems = (Button) findViewById(R.id.MaintenanceClearItems);
        saveChanges = (Button) findViewById(R.id.MaintenanceSaveChanges);
        UndoChanges = (Button) findViewById(R.id.MaintenanceUndoChanges);
        items = new ArrayList<MItem>();
        savedItems = new ArrayList<MItem>();
    }
    private void setFonts() {
        Typeface mycustomFont = Typeface.createFromAsset(getAssets(),"fonts/spartan.otf");
        addItem.setTypeface(mycustomFont);
        clearItems.setTypeface(mycustomFont);
        saveChanges.setTypeface(mycustomFont);
        UndoChanges.setTypeface(mycustomFont);
        label.setTypeface(mycustomFont);
    }
    private void setListeners() {
        addItem.setOnClickListener(this);
        clearItems.setOnClickListener(this);
        saveChanges.setOnClickListener(this);
        UndoChanges .setOnClickListener(this);
    }
    private Boolean updateCountdownEditorMode(String MaintenanceType){
        if(MaintenanceType.equals("COUNTDOWN")){
            countdownEditor.setCountdownEditorMode(true);
        } else {
            countdownEditor.setCountdownEditorMode(false);
        }
        System.out.println(countdownEditor.getCountdownEditorMode());
        updateMaintenanceMode();
        return countdownEditor.getCountdownEditorMode();
    }
    public void loadListContents() {
        String MaintenanceType = (String)mSpinner.getSelectedItem();
        if(!updateCountdownEditorMode(MaintenanceType)){
            MItem.mType type = MItem.mType.valueOf(MaintenanceType);
            savedItems = controller.getMItems(type);
            if(savedItems.isEmpty()){
                savedItems = loadDefaultSpinnerContents(controller,type);
            }
            items.clear();
            items.addAll(savedItems);
            System.out.println(items.size());
            mAdapter = new MaintenanceAdapter(this,items);
            maintenanceList.setAdapter(mAdapter);
        }
    }
    private enum action {
        CLEAR,
        SAVE,
        UNDO
    }
    private void alertdialog(String title,final action type) {
        new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_DARK)
                .setTitle(title)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        updateCountdownEditorMode((String)mSpinner.getSelectedItem());
                        switch(type){
                            case CLEAR: clearItems();
                                break;
                            case SAVE: saveItems();
                                break;
                            case UNDO: undoItems();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    private void clearItems() {
        System.out.println("before: " + savedItems.size());
        items.clear();
        System.out.println("after: " + savedItems.size());
        mAdapter = new MaintenanceAdapter(this,items);
        maintenanceList.setAdapter(mAdapter);
        createToast("All Items Cleared");
    }
    private void saveItems() {
        if(countdownEditor.getCountdownEditorMode()){
            countdownEditor.saveCountdown();
        } else {
            items = mAdapter.getMItems();
            savedItems.addAll(items);
            controller.deleteMaintenanceItemsByType((String) mSpinner.getSelectedItem());
            controller.insertMItems(items);
        }
        createToast("Changes Saved");
    }
    private void undoItems() {
        if(countdownEditor.getCountdownEditorMode()){
            countdownEditor.loadInitialCountdown();
        } else {
            //System.out.println(savedItems.size() + "");
            items.addAll(savedItems);
            mAdapter = new MaintenanceAdapter(this,savedItems);
            maintenanceList.setAdapter(mAdapter);
        }
        createToast("Changes Undone");
    }
    private void addItem() {
        items.add(new MItem(items.size()+1,MItem.mType.valueOf((String)mSpinner.getSelectedItem()),""));
        mAdapter = new MaintenanceAdapter(this,items);
        maintenanceList.setAdapter(mAdapter);
        maintenanceList.setSelection(maintenanceList.getAdapter().getCount()-1);
        createToast("Item Added");
    }
    private void createToast(String text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case(R.id.MaintenanceAddItem):
                //setUpAnimation("silver_anim", this, view);
                addItem();
                break;
            case(R.id.MaintenanceClearItems):
                //setUpAnimation("silver_anim", this, view);
                alertdialog("Clear All Items?", action.CLEAR);
                break;
            case(R.id.MaintenanceSaveChanges):
                //setUpAnimation("silver_anim", this, view);
                alertdialog("Save Changes?",action.SAVE);
                break;
            case(R.id.MaintenanceUndoChanges):
                //setUpAnimation("silver_anim", this, view);
                alertdialog("Undo Changes?",action.UNDO);
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.maintenance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }else if(id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
