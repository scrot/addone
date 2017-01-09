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
    private int sizes;
    private Drawable icon;
    private int iconSize;
    private String label;
    //TODO add label bg color
    //TODO add label text color

    public FloatingActionMenuButton(Context context, int size, Drawable icon, int iconSize){
        super(context);
        this.sizes = size;
        this.icon = icon;
        this.iconSize = iconSize;
        initialize();
    }

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

    public int getSize() {
        return sizes;
    }

    public Drawable getIcon() {
        return icon;
    }

    public int getIconSize() {
        return iconSize;
    }

    public String getLabel() {
        return label;
    }

    public boolean hasLabel() {
        return label != null && !label.equals("");
    }

    private void setAttributes(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FloatingActionMenuButton);
        this.sizes = a.getDimensionPixelSize(R.styleable.FloatingActionMenuButton_famb_size, DpConverter.dpToPx(56));
        this.icon = a.getDrawable(R.styleable.FloatingActionMenuButton_famb_icon_src);
        this.iconSize = a.getDimensionPixelSize(R.styleable.FloatingActionMenuButton_famb_icon_size, 0);
        this.label = a.getString(R.styleable.FloatingActionMenuButton_famb_label);
        a.recycle();
    }

    private Drawable getFabBackground(){
        return ContextCompat.getDrawable(getContext(), R.drawable.fab_background);
    }

    private Drawable getFabIcon(){
        if(icon == null){
            icon = ContextCompat.getDrawable(getContext(), R.drawable.fab_icon);
        }
        return icon;
    }

    private Drawable generateFabIcon(Drawable background, Drawable icon){
        LayerDrawable layerDrawable = mergeDrawables(background, icon);

        //Layout Background
        layerDrawable.setLayerSize(0, sizes, sizes);
        layerDrawable.setLayerGravity(0, Gravity.CENTER);


        //Layout Icon
        layerDrawable.setLayerSize(1, iconSize, iconSize);
        layerDrawable.setLayerGravity(1, Gravity.CENTER);

        return layerDrawable;
    }

    private LayerDrawable mergeDrawables(Drawable... drawables){
        return new LayerDrawable(drawables);
    }
}
