package pjcbm9.quicket.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Jason Crow on 11/21/2014.
 */
public class DigitTextView extends TextView {
    public DigitTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public DigitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DigitTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/dsdigit.ttf");
            setTypeface(tf);
        }
    }
}
