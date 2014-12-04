package pjcbm9.quicket;

/**
 * Created by Jason Crow on 12/3/2014.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class CheckBox extends Button {
    // private variables
    private Boolean isChecked;
    private CheckBox.Style style;
    public enum Style {
        DELETE,
        COMPLETE,
    }
    // constructors
    public CheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CheckBox(Context context) {
        super(context);
        init();
    }
    public void init(){
        this.isChecked = false;
    }

    public Boolean isChecked() {
        return isChecked;
    }

    public void setStyle(Style Style){
        this.style = Style;
        updateCheckMark();
    }

    public void setCheckMark(Boolean IsChecked){
        this.isChecked = IsChecked;
        updateCheckMark();
    }

    private void updateCheckMark(){
        if(isChecked){
            check();
        } else {
            uncheck();
        }
    }

    private void check(){
        switch(style){
            case DELETE:
                setBackgroundResource(R.drawable.checkmark_checked);
                break;
            case COMPLETE:
                setBackgroundResource(R.drawable.checkbox_checked2);
                break;
        }
    }
    private void uncheck(){
        setBackgroundResource(R.drawable.checkbox);
    }
}