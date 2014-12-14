package pjcbm9.quicket.New_Ticket_Package;

import android.content.SharedPreferences;

/**
 * Created by Jason Crow on 12/7/2014.
 */
public class DraftTicketHandler {
    private int draftNumber;
    private TicketEditText ticketNameEditText;
    private SharedPreferences savedData;
    private Boolean trimmedTicketName = false;
    public DraftTicketHandler(SharedPreferences savedData,TicketEditText TicketNameEditText){
        this.ticketNameEditText = TicketNameEditText;
        this.savedData = savedData;
        initializeDraftNumber();
    }
    private void initializeDraftNumber(){
        this.draftNumber = savedData.getInt("totalDrafts",0);
    }
    private void updateDraftNumber(){
        draftNumber++;
        saveDraftNumber(draftNumber);
    }
    private void saveDraftNumber(int draftNumber){
        SharedPreferences.Editor editor = savedData.edit();
        editor.putInt("totalDrafts", draftNumber);
        editor.commit();
    }
    public void trimDraftFromTicketName(){
        if(ticketNameEditText.getText().toString().contains("Draft")){
            ticketNameEditText.setText("");
            trimmedTicketName = true;
        }
    }
    public void handleTicketName(){
        if(trimmedTicketName && ticketNameEditText.toString().equals("")){
            ticketNameEditText.setText("Draft #" + draftNumber);
            updateDraftNumber();
        } else if(!trimmedTicketName && ticketNameEditText.getText().toString().equals("")){
            updateDraftNumber();
            ticketNameEditText.setText("Draft #" + draftNumber);
        } else {
            updateDraftNumber();
        }
    }
}
