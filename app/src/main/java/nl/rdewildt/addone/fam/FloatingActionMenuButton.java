package nl.rdewildt.addone.fam;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageButton;

import nl.rdewildt.addone.R;

/**
 * Created by roydewildt on 05/12/2016.
 */

public class FloatingActionMenuButton extends ImageButton {

    public static final int SIZE_SMALL = 0;
    public static final int SIZE_NORMAL = 1;
    public static final int SIZE_LARGE = 2;

    private int mSize;
    private Drawable mIcon;

    public FloatingActionMenuButton(Context context) {
        this(context, null);
    }

    public FloatingActionMenuButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingActionMenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public FloatingActionMenuButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        // Get custom attributes and set fields
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FloatingActionMenuButton);
        this.mSize = a.getInt(R.styleable.FloatingActionMenuButton_size, SIZE_NORMAL);
        this.mIcon = a.getDrawable(R.styleable.FloatingActionMenuButton_iconsrc);
        a.recycle();

        // Set the fab drawables
        Drawable fabButton = mergeDrawables(generateBackground(), generateIcon());
        setImageDrawable(fabButton);
    }

    private Drawable generateBackground(){
        // Parse background
        Drawable fabBackground = null;

        if(mSize == SIZE_SMALL){
            fabBackground = ContextCompat.getDrawable(getContext(), R.drawable.fab_background_small);
        } else if (mSize == SIZE_NORMAL){
            fabBackground = ContextCompat.getDrawable(getContext(), R.drawable.fab_background_normal);
        } else if (mSize == SIZE_LARGE){
            fabBackground = ContextCompat.getDrawable(getContext(), R.drawable.fab_background_large);
        }

        return fabBackground;
    }

    private Drawable generateIcon(){
        if(mIcon == null){
            mIcon = ContextCompat.getDrawable(getContext(), R.drawable.fab_icon);
        }
        return mIcon;
    }

    private Drawable mergeDrawables(Drawable... drawables){
        return new LayerDrawable(drawables);
    }
}
