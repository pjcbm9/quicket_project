package pjcbm9.quicket.Quicket_Package;

import android.content.Context;

import java.util.ArrayList;

import pjcbm9.quicket.Maintenance_Package.MItem;

import static pjcbm9.quicket.Alarm_Package.AlarmManagerHelper.cancelAlarm;

/**
 * Created by Jason Crow on 12/5/2014.
 */
public class Controller  {
    private Context context;
    private DBAdapter DB;
    public Controller(Context context){
        this.context = context;
        this.DB = new DBAdapter(context);
        DB.open();
    }
    public int requestTicketCount(Ticket.Status status){
        return DB.findTicketCountByStatus(status);
    }
    public void close(){
        DB.close();
    }
    public void requestTicketDeletion(long ticketID){
        cancelAlarm(context,ticketID);
        DB.deleteTicket(ticketID);
    }
    public ArrayList<Ticket> requestTicketsByKeyword(String keyword){
        return DB.findTicketsByKeyword(keyword);
    }
    public ArrayList<Ticket> requestTicketsByStatus(Ticket.Status status){
        return DB.findTicketsByStatus(status);
    }
    public void requestUpdateTickets(){
        DB.updateTickets();
    }
    public void requestTicketCompletion(long ticketID){
        DB.updateTicketStatus(ticketID, Ticket.Status.COMPLETED.toString());
    }
    public void requestInsertMaintenanceArray(ArrayList<MItem> mArray) {
        DB.insertMItemArray(mArray);
    }
    public ArrayList<MItem> requestMItems(MItem.mType type){
        return DB.findMaintenanceItemsByType(type.toString());
    }
    public void requestCompleteTicket(long ticketID){
        DB.updateTicketStatus(ticketID,Ticket.Status.COMPLETED.toString());
    }
    public ArrayList<MItem> getMItems(MItem.mType type){
        return DB.findMaintenanceItemsByType(type.toString());
    }
    public void insertMItems(ArrayList<MItem> items){
        DB.insertMItemArray(items);
    }
    public void deleteMaintenanceItemsByType(String type){
        DB.deleteMaintenanceItemsByType(type);
    }
    public void requestTicketUpdate(Ticket ticket){
        DB.updateTicket(ticket);
    }
    public void requestTicketInsertion(Ticket ticket){
        DB.insertNewTicket(ticket);
    }
}
