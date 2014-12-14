package pjcbm9.quicket.Ticket_List_Package;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import pjcbm9.quicket.New_Ticket_Package.NewTicket;
import pjcbm9.quicket.Quicket_Package.Controller;
import pjcbm9.quicket.Quicket_Package.Ticket;
import pjcbm9.quicket.R;
import pjcbm9.quicket.Ticket_View_Package.ticketView;

import static pjcbm9.quicket.Quicket_Package.Static_Helpers.DateToString;
import static pjcbm9.quicket.Quicket_Package.Static_Helpers.DelayIntent;
import static pjcbm9.quicket.Quicket_Package.Static_Helpers.reverseBoolean;
import static pjcbm9.quicket.Quicket_Package.Static_Helpers.setUpAnimation;

/**
 * Created by Jason Crow on 9/27/2014.
 */
public class TicketListAdapter extends BaseAdapter  {
    private Context context;
    private ArrayList<Ticket> tickets,uncheckedTickets;
    private LayoutInflater layoutInflater;
    private Boolean showCheckboxes,changesMade;
    private ArrayList<Boolean> checkboxes;
    private CheckBox.Style checkboxStyle;
    private Controller controller;
    static class ViewHolder {
        CheckBox checkBox;
        TicketButton ticketButton;
        TicketCountDown ticketCountDown;
    }
    // constructor
    public TicketListAdapter(Context _context,ArrayList<Ticket> _tickets){
        this.context = _context;
        this.tickets = _tickets;
        this.controller = new Controller(context);
        resetCheckboxes();
        layoutInflater = LayoutInflater.from(context);
        this.showCheckboxes = false;
    }
    public void cancelCheckBoxMode(){
        toggleCheckboxVisibility();
        resetCheckboxes();
    }

    private void resetCheckboxes(){
        this.checkboxes = new ArrayList<Boolean>();
        for(int i = 0; i < getCount();i++){
            checkboxes.add(false);
        }
    }

    public Boolean getCheckBoxState(int position){
        return checkboxes.get(position);
    }
    public void setCheckboxStyle(CheckBox.Style style){
        checkboxStyle = style;
        showCheckboxes = true;
        notifyDataSetChanged();
    }

    public void toggleCheckboxVisibility(){
        showCheckboxes = reverseBoolean(showCheckboxes);
        notifyDataSetChanged();
    }
    public Boolean handleCheckedTickets() {
        resetChangedMade();
        resetUncheckedTickets();
        iterateTickets();
        conditionallyUpdateList();
        return changesMade;
    }
    private long getTicketID(int position){
        return ((Ticket)getItem(position)).getID();
    }
    @Override
    public int getCount() {
        if (tickets != null){
            return tickets.size();
        }else
            return 0;
    }

    @Override
    public Object getItem(int i) {
        return tickets.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null){
            // inflate layout
            view = layoutInflater.inflate(R.layout.ticket_row,viewGroup,false);
            //view = layoutInflater.inflate(R.layout.ticket_row,null);
            // set up view holder
            viewHolder = new ViewHolder();
            viewHolder.checkBox = (CheckBox)view.findViewById(R.id.checkBox);
            viewHolder.ticketButton = (TicketButton)view.findViewById(R.id.TicketButton);
            viewHolder.ticketCountDown = (TicketCountDown)view.findViewById(R.id.ticketcountdown);
            // store the holder with the view
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }
        setupCheckBox(viewHolder,i);
        setupTicket(viewHolder,i);
        return view;
    }
    private void setupTicket(ViewHolder viewHolder,int position){
        updateTicketTexts(viewHolder, position);
        Ticket.Status ticket_status = tickets.get(position).getStatus();
        setTicketBackground(viewHolder,ticket_status);
        setTicketListener(viewHolder.ticketButton,ticket_status,position);
    }
    private void updateTicketTexts(ViewHolder viewHolder,int position){
        String ticketTitle = getTicketButtonTitle(position);
        String countDownTimer = tickets.get(position).getCountDownTimer();
        int ticketNameLength = getTicketNameLength(position);
        viewHolder.ticketButton.formatText(ticketTitle, ticketNameLength);
        viewHolder.ticketCountDown.formatText(countDownTimer);
    }
    private void handleCheckBoxVisibility(ViewHolder viewHolder){
        if(showCheckboxes){
            viewHolder.checkBox.setVisibility(View.VISIBLE);
        }else{
            viewHolder.checkBox.setVisibility(View.GONE);
        }
    }

    private void setupCheckBoxListener(final CheckBox checkBox,final int position){
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                checkboxes.set(position,reverseBoolean(checkboxes.get(position)));
                notifyDataSetChanged();
            }
        });
    }
    private void setupCheckBox(ViewHolder viewHolder,final int position) {
        viewHolder.checkBox.setStyle(checkboxStyle);
        viewHolder.checkBox.setCheckMark(checkboxes.get(position));
        handleCheckBoxVisibility(viewHolder);
        setupCheckBoxListener(viewHolder.checkBox,position);
    }
    private void setTicketBackground(ViewHolder viewHolder,Ticket.Status Status){
        viewHolder.ticketButton.updateBackground(Status, showCheckboxes);
        handleCountdownVisibility(viewHolder, Status);
    }
    private void setTicketListener(final TicketButton ticketButton,final Ticket.Status ticket_status, final int position) {
        ticketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (ticket_status) {
                    case ACTIVE:
                        setUpAnimation("active_ticket_anim", context, v);
                        ((Ticket_List) context).stopCountdownTimers();
                        delayIntent(500, ticketView.class, tickets.get(position));
                        break;
                    case OVERDUE:
                        delayIntent(500, ticketView.class, tickets.get(position));
                        break;
                    case COMPLETED:
                        setUpAnimation("completed_ticket_anim", context, v);
                        delayIntent(500, ticketView.class, tickets.get(position));
                        break;
                    case DRAFT:
                        setUpAnimation("draft_ticket_anim", context, v);
                        delayIntent(500, NewTicket.class, tickets.get(position));
                        break;
                }
            }
        });
    }
    private void SetUpAnimation(String anim_name,View view){
        setUpAnimation(anim_name,context,view);
    }
    private void delayIntent(int milliseconds, Class<?> c){
        DelayIntent(context,milliseconds,c);
    }
    private void delayIntent(int milliseconds, Class<?> c, Ticket ticket){
        DelayIntent(context,milliseconds,c,ticket);
    }

    private void handleCountdownVisibility(ViewHolder viewHolder,Ticket.Status ticket_status) {
        if(!showCheckboxes && ticket_status.equals(Ticket.Status.ACTIVE)) {
            viewHolder.ticketCountDown.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ticketCountDown.setVisibility(View.GONE);
        }
    }
    private String getTicketButtonTitle(int position){
        String ticketName = "" + tickets.get(position).getTicketName();
        String date = DateToString(tickets.get(position).getTicketDate());
        return ticketName + "\n" + date;
    }
    private int getTicketNameLength(int position) {
        return tickets.get(position).getTicketName().length();
    }

    private void updateTicket(long ticketID){
        switch(checkboxStyle){
            case DELETE: controller.requestTicketDeletion(ticketID);
                break;
            case COMPLETE: controller.requestTicketCompletion(ticketID);
                break;
        }
    }
    private void handleCheckBox(int position){
        if (getCheckBoxState(position)) {
            updateTicket(getTicketID(position));
            changesMade = true;
        } else {
            uncheckedTickets.add((Ticket)getItem(position));
        }
    }
    private void iterateTickets(){
        for (int i = 0; i < getCount(); i++) {
            handleCheckBox(i);
        }
    }
    private void resetChangedMade(){
        changesMade = false;
    }
    private void resetUncheckedTickets(){
        uncheckedTickets = new ArrayList<Ticket>();
    }
    private void updateList(){
        tickets.clear();
        tickets.addAll(uncheckedTickets);
        resetCheckboxes();
        showCheckboxes = false;
        notifyDataSetChanged();
    }
    private void conditionallyUpdateList(){
        if(changesMade){
            updateList();
        }
    }
}