package pjcbm9.quicket.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Jason Crow on 11/30/2014.
 */
public class SpartanEditText extends EditText {
    public SpartanEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SpartanEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpartanEditText(Context context) {
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