package nl.rdewildt.addone.fam.fab;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.widget.TextView;

import nl.rdewildt.addone.R;
import nl.rdewildt.addone.helper.DpConverter;

/**
 * Created by roydewildt on 12/01/2017.
 */

public class FloatingActionMenuButtonLabel extends TextView {
    private int mLabelRadius = DpConverter.dpToPx(2);
    private int mLabelTextColor = Color.WHITE;
    private int mLabelBackgroundColor = Color.GRAY;
    private int mLabelAlpha = 150;
    private int mLabelPaddingStart = DpConverter.dpToPx(8);
    private int mLabelPaddingEnd = DpConverter.dpToPx(8);
    private int mLabelPaddingTop = DpConverter.dpToPx(4);
    private int mLabelPaddingBottom = DpConverter.dpToPx(4);
    private int mLabelTextSize = DpConverter.dpToPx(5);

    public FloatingActionMenuButtonLabel(Context context, String labelText){
        super(context);
        setText(labelText);
        init(context, null, 0, 0);
    }

    public FloatingActionMenuButtonLabel(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public FloatingActionMenuButtonLabel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public FloatingActionMenuButtonLabel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public FloatingActionMenuButtonLabel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        layoutText();
        setBackground(getBackgroundDrawable());
    }

    private void layoutText(){
        setTextSize(mLabelTextSize);
        setTextColor(getResources().getColor(R.color.textColor, null));
        setPadding(mLabelPaddingStart, mLabelPaddingTop, mLabelPaddingEnd, mLabelPaddingBottom);
    }

    private Drawable getBackgroundDrawable(){
        float[] rs = {mLabelRadius, mLabelRadius, mLabelRadius, mLabelRadius,mLabelRadius, mLabelRadius, mLabelRadius, mLabelRadius};
        ShapeDrawable bg = new ShapeDrawable(new RoundRectShape(rs, null, null));
        bg.getPaint().setColor(mLabelBackgroundColor);
        bg.setAlpha(mLabelAlpha);
        return bg;
    }
}
