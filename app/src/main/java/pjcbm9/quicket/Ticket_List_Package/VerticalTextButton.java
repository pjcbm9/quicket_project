package pjcbm9.quicket.Ticket_List_Package;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;

/**
 * Created by Jason Crow on 11/5/2014.
 */
public class VerticalTextButton extends Button {
    final boolean topDown;
    public VerticalTextButton(Context context, AttributeSet attrs){
        super(context, attrs);
        final int gravity = getGravity();
        if(Gravity.isVertical(gravity) && (gravity&Gravity.VERTICAL_GRAVITY_MASK) == Gravity.CENTER) {
            setGravity((gravity&Gravity.HORIZONTAL_GRAVITY_MASK) | Gravity.BOTTOM);
            topDown = true;
        }else
            topDown = false;
        Typeface mycustomFont = Typeface.createFromAsset(context.getAssets(),"fonts/spartan.otf");
        setTypeface(mycustomFont);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas canvas){
        TextPaint textPaint = getPaint();
        textPaint.setColor(getCurrentTextColor());
        textPaint.drawableState = getDrawableState();

        canvas.save();

        if(topDown){
            canvas.translate(getWidth(), 0);
            canvas.rotate(90);
        }else {
            canvas.translate(0, getHeight());
            canvas.rotate(-90);
        }

        canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());
        getLayout().draw(canvas);
        canvas.restore();
    }
}
