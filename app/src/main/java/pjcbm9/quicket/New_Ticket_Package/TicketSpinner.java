package pjcbm9.quicket.New_Ticket_Package;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

import java.util.ArrayList;

import pjcbm9.quicket.Maintenance_Package.MItem;
import pjcbm9.quicket.Quicket_Package.Ticket;
import pjcbm9.quicket.Quicket_Package.Controller;

import static pjcbm9.quicket.Quicket_Package.Static_Helpers.convertMItem;
import static pjcbm9.quicket.Quicket_Package.Static_Helpers.convertTicketField;
import static pjcbm9.quicket.Quicket_Package.Static_Helpers.loadDefaultSpinnerContents;

/**
 * Created by Jason Crow on 12/7/2014.
 */
// TicketSpinner is a spinner that implements CriticalData and handles its own contents
// TicketSpinner's contents correspond to MItem.mType's (A type of MaintenanceItem)
// ** maintenance item types can be converted to a ticket field and vice versa
public class TicketSpinner extends Spinner implements TicketField {
    private ArrayList<String> data = new ArrayList<String>(); // contains Spinner's data
    private Controller controller; // enables spinner to request data from dataBase
    private Context context; // context from activity spinner is implemented in
    private MItem.mType type; // type of maintenance item
    private SpinnerAdapter adapter; // spinner's adapter
    private Boolean isSet = false;
    public static final int criticalIndex = 0; // index of data containing critical data
    public static final String false_condition = "-------";
    // constructors
    public TicketSpinner(Context context) {
        super(context);
        this.context = context;
    }
    public TicketSpinner(Context context, int mode) {
        super(context, mode);
        this.context = context;
    }
    public TicketSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
    public TicketSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }
    public TicketSpinner(Context context, AttributeSet attrs, int defStyle, int mode) {
        super(context, attrs, defStyle, mode);
        this.context = context;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // CRITICAL: this function must be called before ticketfield calls can be made
    // normally this should be set in constructor, but because the spinner has to be linked
    // to its xml id, i don't know how to expand the contructor from its super's parameters
    // therefore, until that is resolved this function must MUST be called right after its linked to its xml id
    public void loadController(Controller controller){
        this.controller = controller;
    }
    // called when a spinner is clicked, if it the first time being clicked, the data will still contain the
    // false_condition. If that is the case, the false_condition is removed, and the the spinner's contents are updated
    public void updateData(){
        if(data.get(criticalIndex).equals(false_condition)){ // checks if false_condition is still present
            data.remove(criticalIndex); // removes false_condition
            adapter.notifyDataSetChanged(); // updates spinner's contents
            isSet = true; // once the false condition has been removed from the critical index, the spinner will always be set
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////
    // TICKETFIELD IMPLEMENTATIONS
    /////////////////////////////////////////////////////////////////////////////////////////////////
    // this function provides the last missing piece (MType) to load spinner's contents
    @Override
    public void setField(Ticket.Field field) {
        this.type = convertTicketField(field);
        // by default when a spinner is initialized the first item of the data is the false_condition
        // once spinner is clicked, updateData() is called, which removes this false_condition, thus setting the state
        // of the critical data to true or "set"
        data.add(false_condition);
        setupSpinner();
    }
    @Override
    public Ticket.Field getField(){
        return convertMItem(type);
    }
    // sets the spinner's selection
    @Override
    public void setData(String data) {
        if(!data.equals("")){updateData();}
        setSelection(data);
    }

    // gets spinner's selection
    @Override
    public String getData() {
        return getSelection();
    }
    // discover if field is set
    @Override
    public Boolean isSet() {
        return isSet;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //  PRIVATE METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // sets up the spinner's contents
    private void setupSpinner(){
        loadData();
        setupAdapter();
        setAdapter(adapter);
    }
    // retrieves data corresponding to spinner's maintenance item type
    private void loadData(){
        loadData(getMItems());
    }

    // middle-man routine for getting the maintenance items associated with the spinner's maintenance item type,
    // via a request to the database from the controller object
    private ArrayList<MItem> getMItems(){
        ArrayList<MItem> items;
        if(NewTicket.loadDefaults){ // static final boolean set in NewTicket activity, if true, load default spinner contents
            items = loadDefaultSpinnerContents(controller,type);
        } else { // otherwise get items associated with spinner's maintenance item type
            items = controller.requestMItems(type);
        }
        return items;
    }
    // extracts maintenance item's name, the adds that String value to the data container
    // ** only a maintenance item's name is exhibited in the spinner
    private void loadData(ArrayList<MItem> items){
        for(MItem item: items){
            data.add(item.getItemName());
        }
    }
    // sets up the spinner's adapter
    private void setupAdapter(){
        adapter = new SpinnerAdapter(context,
                android.R.layout.simple_spinner_dropdown_item,
                data);
    }
    // setSelection to targetSelection
    public void setSelection(String targetSelection){
        setSelection(adapter.getPosition(targetSelection));
    }
    // returns data at current selection
    public String getSelection(){
        return (String)getSelectedItem();
    }
}
