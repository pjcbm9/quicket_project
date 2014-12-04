package pjcbm9.quicket;

import android.view.View;
import android.widget.LinearLayout;

import static pjcbm9.quicket.Static_Helpers.conditionalToggle;

/**
 * Created by Jason Crow on 11/29/2014.
 */
public class ControlBarManager {
    private LinearLayout searchBar,deleteBar,completeBar;
    public enum Type {
        SEARCH,COMPLETE,DELETE,NONE
    }
    public ControlBarManager(LinearLayout search_bar,LinearLayout complete_bar,LinearLayout delete_bar){
        this.searchBar = search_bar;
        this.deleteBar = delete_bar;
        this.completeBar = complete_bar;
    }
    public void disableControlBars(){
        conditionalToggle(false,searchBar);
        conditionalToggle(false,deleteBar);
        conditionalToggle(false,completeBar);      
    }
    public void disableControlBar(Type type){
        switch(type){
            case SEARCH:
                searchBar.setVisibility(View.GONE);
                break;
            case DELETE:
                deleteBar.setVisibility(View.GONE);
                break;
            case COMPLETE:
                completeBar.setVisibility(View.GONE);
                break;
        }
    }
    public void enableControlBar(Type type){
        disableControlBars();
        switch(type){
            case SEARCH:
                searchBar.setVisibility(View.VISIBLE);
                break;
            case DELETE:
                deleteBar.setVisibility(View.VISIBLE);
                break;
            case COMPLETE:
                completeBar.setVisibility(View.VISIBLE);
                break;
        }
    }
}
