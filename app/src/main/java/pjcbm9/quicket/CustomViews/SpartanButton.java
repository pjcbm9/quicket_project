package pjcbm9.quicket.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Jason Crow on 11/21/2014.
 */
public class SpartanButton extends Button {
    public SpartanButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SpartanButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpartanButton(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/spartan.otf");
            setTypeface(tf);
        }
    }
}
