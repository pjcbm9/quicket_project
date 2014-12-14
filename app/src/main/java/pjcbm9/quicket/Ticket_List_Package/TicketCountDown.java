package pjcbm9.quicket.Ticket_List_Package;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;

import pjcbm9.quicket.CustomViews.CustomTypefaceSpan;
import pjcbm9.quicket.CustomViews.DigitTextView;
import pjcbm9.quicket.R;

/**
 * Created by Jason Crow on 12/4/2014.
 */
public class TicketCountDown extends DigitTextView {
    public TicketCountDown(Context context) {
        super(context);
        setBackground();
    }

    public TicketCountDown(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackground();
    }

    public TicketCountDown(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setBackground();
    }
    private void setBackground(){
        setBackgroundResource(R.drawable.active_ticket_right);
    }
    public void formatText(String countDownTimer){
        Spannable wordtoSpan = new SpannableString(countDownTimer);
        Typeface spartanFont = Typeface.createFromAsset(getContext().getAssets(),"fonts/spartan.otf");
        wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new CustomTypefaceSpan("", spartanFont), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new RelativeSizeSpan(0.8f), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(wordtoSpan);
    }
}
