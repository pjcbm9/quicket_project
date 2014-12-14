package pjcbm9.quicket.Ticket_List_Package;

import android.view.View;
import android.widget.Button;

import pjcbm9.quicket.Quicket_Package.Ticket;

import static pjcbm9.quicket.Quicket_Package.Static_Helpers.conditionalToggle;

/**
 * Created by Jason Crow on 11/29/2014.
 */
public class ListEditorManager {
    private Button delete_icon,complete_icon,search_icon;
    private Boolean completeMenuOn,deleteMenuOn,searchBarOn;
    private Ticket.Status status;
    public ListEditorManager(Button delete, Button complete, Button search){
        this.delete_icon = delete;
        this.complete_icon = complete;
        this.search_icon = search;
    }
    public void setStatus(Ticket.Status type){
        this.status = type;
    }
    public void UpdateIcons(Ticket.Status type){
        this.status = type;
        defaultEnabledIcons();
        switch(type){
            case ACTIVE:
                complete_icon.setVisibility(View.VISIBLE);
                break;
            case OVERDUE:
                complete_icon.setVisibility(View.VISIBLE);
                break;
            case COMPLETED:
                complete_icon.setVisibility(View.GONE);
                break;
            case DRAFT:
                complete_icon.setVisibility(View.GONE);
                break;
            case SEARCH:
                complete_icon.setVisibility(View.GONE);
                break;
        }
    }

    private void defaultEnabledIcons(){
        conditionalToggle(true,delete_icon);
        conditionalToggle(true,search_icon);
        conditionalToggle(true,complete_icon);
    }
    public void disableIcon(ControlBarManager.Type type){
        UpdateIcons(status);
        switch(type){
            case COMPLETE:
                conditionalToggle(false,complete_icon);
                break;
            case DELETE:
                conditionalToggle(false,delete_icon);
                break;
            case SEARCH:
                conditionalToggle(false,search_icon);
                break;
        }
    }
}
