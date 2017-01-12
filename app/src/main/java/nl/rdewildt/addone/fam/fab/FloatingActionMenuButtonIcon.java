package nl.rdewildt.addone.fam.fab;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.VectorDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by roydewildt on 05/12/2016.
 */

public class FloatingActionMenuButtonIcon extends ImageButton {
    private int mSize;
    private int mColor;
    private Drawable mIconSrc;
    private int mIconSize;
    private int mIconColor;

    public FloatingActionMenuButtonIcon(Context context, int color, int size, Drawable icon, int iconSize, int iconColor){
        super(context);
        this.mSize = size;
        this.mColor = color;
        this.mIconSrc = icon;
        this.mIconSize = iconSize;
        this.mIconColor = iconColor;
        init(context, null, 0, 0);
    }

    public FloatingActionMenuButtonIcon(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public FloatingActionMenuButtonIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public FloatingActionMenuButtonIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public FloatingActionMenuButtonIcon(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        ShapeDrawable bg = new ShapeDrawable(new OvalShape());
        bg.getPaint().setColor(mColor);
        setBackground(bg);

        Drawable icon = mIconSrc;
        if(icon != null) {
            icon.setColorFilter(mIconColor, PorterDuff.Mode.DST);
        }
        setImageDrawable(icon);
    }
}
