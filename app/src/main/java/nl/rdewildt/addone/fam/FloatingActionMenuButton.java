package nl.rdewildt.addone.fam;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageButton;

import nl.rdewildt.addone.R;
import nl.rdewildt.addone.helper.DpConverter;

/**
 * Created by roydewildt on 05/12/2016.
 */

public class FloatingActionMenuButton extends ImageButton {
    private int mSize;
    private Drawable mIcon;
    private int mIconSize;

    public FloatingActionMenuButton(Context context) {
        super(context);
    }

    public FloatingActionMenuButton(Context context, int size, Drawable icon, int iconSize){
        super(context);
        this.mSize = size;
        this.mIcon = icon;
        this.mIconSize = iconSize;
        initialize();
    }

    public FloatingActionMenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(context, attrs);
        initialize();
    }

    public FloatingActionMenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttributes(context, attrs);
        initialize();
    }

    public FloatingActionMenuButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setAttributes(context, attrs);
        initialize();
    }

    private void initialize(){
        // Set the fab drawables
        Drawable fabButton = generateFabIcon(getFabBackground(), getFabIcon());
        setImageDrawable(fabButton);

        //Clear default background and remove default padding
        setBackgroundResource(0);
        setPadding(0,0,0,0);
    }

    private void setAttributes(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FloatingActionMenuButton);
        this.mSize = a.getDimensionPixelSize(R.styleable.FloatingActionMenuButton_famb_size, DpConverter.dpToPx(56));
        this.mIcon = a.getDrawable(R.styleable.FloatingActionMenuButton_famb_icon_src);
        this.mIconSize = a.getDimensionPixelSize(R.styleable.FloatingActionMenuButton_famb_icon_size, 0);
        a.recycle();
    }

    private Drawable getFabBackground(){
        return ContextCompat.getDrawable(getContext(), R.drawable.fab_background);
    }

    private Drawable getFabIcon(){
        if(mIcon == null){
            mIcon = ContextCompat.getDrawable(getContext(), R.drawable.fab_icon);
        }
        return mIcon;
    }

    private Drawable generateFabIcon(Drawable background, Drawable icon){
        LayerDrawable layerDrawable = mergeDrawables(background, icon);

        //Layout Background
        layerDrawable.setLayerSize(0, mSize, mSize);
        layerDrawable.setLayerGravity(0, Gravity.CENTER);


        //Layout Icon
        layerDrawable.setLayerSize(1, mIconSize, mIconSize);
        layerDrawable.setLayerGravity(1, Gravity.CENTER);

        return layerDrawable;
    }

    private LayerDrawable mergeDrawables(Drawable... drawables){
        return new LayerDrawable(drawables);
    }
}
