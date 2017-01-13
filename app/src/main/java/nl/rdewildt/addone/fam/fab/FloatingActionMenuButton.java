package nl.rdewildt.addone.fam.fab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.Objects;

import nl.rdewildt.addone.R;
import nl.rdewildt.addone.helper.DpConverter;

/**
 * Created by roydewildt on 12/01/2017.
 */

public class FloatingActionMenuButton extends RelativeLayout {
    private FloatingActionMenuButtonIcon icon;
    private FloatingActionMenuButtonLabel label;

    private int mButtonSize;
    private Drawable mButtonIcon;
    private int mButtonBgColor;

    private int mSpacing;
    private String mLabelText;

    public FloatingActionMenuButton(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public FloatingActionMenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public FloatingActionMenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public FloatingActionMenuButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FloatingActionMenuButton);
        try{
            mSpacing = a.getDimensionPixelSize(R.styleable.FloatingActionMenuButton_fab_label_spacing, DpConverter.dpToPx(6));
            mButtonSize = a.getDimensionPixelSize(R.styleable.FloatingActionMenuButton_fab_button_size, DpConverter.dpToPx(54));
            mLabelText = a.getString(R.styleable.FloatingActionMenuButton_fab_label_text);
            mButtonIcon = a.getDrawable(R.styleable.FloatingActionMenuButton_fab_icon_src);
            mButtonBgColor = a.getColor(R.styleable.FloatingActionMenuButton_fab_background_color, getResources().getColor(R.color.colorAccent, null));
        }finally {
            a.recycle();
        }

        if(hasLabel()) {
            LayoutParams labelLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            labelLayoutParams.rightMargin = mSpacing;
            labelLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            label = new FloatingActionMenuButtonLabel(context, mLabelText);
            label.setId(generateViewId());
            label.setClickable(false);
            addView(label, labelLayoutParams);
        }

        LayoutParams iconLayoutParams = new LayoutParams(mButtonSize, mButtonSize);
        if(hasLabel()){
            iconLayoutParams.addRule(RelativeLayout.RIGHT_OF, label.getId());
        } else {
            iconLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        }
        icon = new FloatingActionMenuButtonIcon(context, mButtonBgColor, mButtonIcon);
        icon.setClickable(false);
        icon.setId(generateViewId());
        addView(icon, iconLayoutParams);
    }

    public FloatingActionMenuButtonIcon getIcon() {
        return icon;
    }

    public FloatingActionMenuButtonLabel getLabel() {
        return label;
    }

    public boolean hasLabel(){
        return mLabelText != null && !Objects.equals(mLabelText, "");
    }

    static class LayoutParams extends RelativeLayout.LayoutParams{
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(RelativeLayout.LayoutParams source) {
            super(source);
        }
    }
}
