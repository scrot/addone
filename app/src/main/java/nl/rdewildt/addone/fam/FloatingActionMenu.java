package nl.rdewildt.addone.fam;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.rdewildt.addone.R;
import nl.rdewildt.addone.fam.animation.FamAnimation;
import nl.rdewildt.addone.fam.animation.FamAnimationFactory;
import nl.rdewildt.addone.helper.DpConverter;

/**
 * Created by roydewildt on 10/11/2016.
 */

@CoordinatorLayout.DefaultBehavior(FloatingActionMenu.Behavior.class)
public class FloatingActionMenu extends ViewGroup {
    public static final String TAG = "FloatingActionMenu";
    private FloatingActionMenuButton floatingActionMenuMainButton;

    // Menu menuState
    public static final int MENU_STATE_NONE = 0;
    public static final int MENU_STATE_HIDDEN = 1;
    public static final int MENU_STATE_COLLAPSED = 2;
    public static final int MENU_STATE_EXPANDED = 3;

    private int mMenuState = MENU_STATE_NONE;
    private int mPrevMenuState = MENU_STATE_NONE;

    private int mRestoredMenuState = MENU_STATE_NONE;
    private int mRestoredPrevMenuState = MENU_STATE_NONE;

    // Animations
    public static final int COLLAPSE_ANIM_DURATION = 150;
    public static final int EXPAND_ANIM_DURATION = 150;
    public static final int FADE_IN_ANIM_DURATION = 300;
    public static final int FADE_OUT_ANIM_DURATION = 300;

    static final FamAnimationFactory mAnimFactory = new DefaultFamAnimationFactory();

    // Labels
    private Map<View, View> labels;

    // Attribute set
    private int childSpacing;
    private boolean mLongPressMenuToggle;
    private int mSize;
    private Drawable mIcon;
    private int mIconSize;
    //TODO add label spacing for FAM

    public FloatingActionMenu(Context context) {
        this(context,null);
    }

    public FloatingActionMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingActionMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public FloatingActionMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FloatingActionMenu);
        try {
            this.mRestoredMenuState = array.getInteger(R.styleable.FloatingActionMenu_fam_menu_state, mMenuState);
            this.childSpacing = array.getDimensionPixelSize(R.styleable.FloatingActionMenu_fam_menu_spacing, DpConverter.dpToPx(8));
            this.mLongPressMenuToggle = array.getBoolean(R.styleable.FloatingActionMenu_fam_long_press, false);
            this.mSize = array.getDimensionPixelSize(R.styleable.FloatingActionMenu_fam_size, DpConverter.dpToPx(56));
            this.mIcon = array.getDrawable(R.styleable.FloatingActionMenu_fam_icon_src);
            this.mIconSize = array.getDimensionPixelSize(R.styleable.FloatingActionMenu_fam_icon_size, DpConverter.dpToPx(24));
        } finally {
            array.recycle();
            Log.v(TAG, "Restored styled attributes");
        }

        this.labels = new HashMap<>();
        addOnLayoutChangeListener((view, i, i1, i2, i3, i4, i5, i6, i7) -> restoreMenu());
    }

    public FloatingActionMenuButton getFloatingActionMenuMainButton() {
        return floatingActionMenuMainButton;
    }

    public int getChildSpacing() {
        return childSpacing;
    }

    @Override
    protected void onFinishInflate() {
        addLabels();
        addMainFloatingActionButton();
    }

    private void addLabels() {
        int count = getChildCount();
        for(int i = 0; i < count; i++){
            FloatingActionMenuButton child = (FloatingActionMenuButton) getChildAt(i);
            if(child.hasLabel()){
                TextView label = new TextView(getContext());
                label.setBackgroundResource(R.drawable.fab_label);
                label.setText(child.getLabel());
                labels.put(child, label);
                addView(label);
            }
        }
    }

    private void addMainFloatingActionButton(){
        floatingActionMenuMainButton = new FloatingActionMenuButton(getContext(), mSize, mIcon, mIconSize);

        //Set fam expand/collapse animation
        if(mLongPressMenuToggle) {
            floatingActionMenuMainButton.setOnLongClickListener(view -> {
                if (mMenuState == MENU_STATE_EXPANDED) {
                    collapse(false);
                } else if (mMenuState == MENU_STATE_COLLAPSED) {
                    expand(false);
                }
                return true;
            });
        } else {
            floatingActionMenuMainButton.setOnClickListener(view -> {
                if (mMenuState == MENU_STATE_EXPANDED) {
                    collapse(false);
                } else if (mMenuState == MENU_STATE_COLLAPSED) {
                    expand(false);
                }
            });
        }

        addView(floatingActionMenuMainButton, super.generateDefaultLayoutParams());
        Log.v(TAG, "Main floating action button added to menu");
    }

    public void collapse(boolean instant) {
        if(mMenuState != MENU_STATE_COLLAPSED) {
            FamAnimation anim = mAnimFactory.collapse(this);
            anim.addFamAnimationListener(new FamAnimation.FamAnimationListener() {
                @Override
                public void onStart() {}

                @Override
                public void onEnd() {
                    switchChildrenVisibility(View.GONE);
                }
            });
            anim.setDuration(instant ? 0 : COLLAPSE_ANIM_DURATION).start();
            mMenuState = MENU_STATE_COLLAPSED;
            Log.v(TAG, "Started collapse animation");
        }
    }

    public void expand(boolean instant) {
        if(mMenuState != MENU_STATE_EXPANDED) {
            FamAnimation anim = mAnimFactory.expand(this);
            anim.addFamAnimationListener(new FamAnimation.FamAnimationListener() {
                @Override
                public void onStart() {
                    switchChildrenVisibility(View.VISIBLE);
                }

                @Override
                public void onEnd() {}
            });
            anim.setDuration(instant ? 0 : EXPAND_ANIM_DURATION).start();
            mMenuState = MENU_STATE_EXPANDED;
            Log.v(TAG, "Started expand animation");
        }
        requestLayout();
    }

    public void show(boolean instant){
        if(mMenuState == MENU_STATE_HIDDEN) {
            FamAnimation anim = mAnimFactory.show(this);
            anim.addFamAnimationListener(new FamAnimation.FamAnimationListener() {
                @Override
                public void onStart() {
                    mRestoredMenuState = mPrevMenuState;
                    restoreMenu();
                }

                @Override
                public void onEnd() {}
            });
            anim.setDuration(instant ? 0 : FADE_IN_ANIM_DURATION).start();
            Log.v(TAG, "Started show animation");
        }
    }

    public void hide(boolean instant){
        if(mMenuState != MENU_STATE_HIDDEN) {
            mPrevMenuState = mRestoredPrevMenuState != MENU_STATE_NONE ? mRestoredPrevMenuState : mMenuState;
            FamAnimation anim = mAnimFactory.hide(this);
            anim.addFamAnimationListener(new FamAnimation.FamAnimationListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onEnd() {
                }
            });
            anim.setDuration(instant ? 0 : FADE_OUT_ANIM_DURATION).start();
            mMenuState = MENU_STATE_HIDDEN;
            Log.v(TAG, "Started hide animation");
        }
    }

    private void switchChildrenVisibility(int visibility) {
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if(!child.equals(floatingActionMenuMainButton)){
                child.setVisibility(visibility);
            }
        }
        Log.v(TAG, String.format("Children button visibility set to %s", visibilityToString(visibility)));
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        if(floatingActionMenuMainButton != null) {
            floatingActionMenuMainButton.setOnClickListener(l);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //To be measured
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child instanceof FloatingActionMenuButton) {
                final View label = labels.get(child);

                if (child.getVisibility() != View.GONE) {
                    final LayoutParams childLp = (LayoutParams) child.getLayoutParams();
                    measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

                    int labelWidth = 0;
                    if (label != null) {
                        final LayoutParams labelLp = (LayoutParams) label.getLayoutParams();
                        measureChildWithMargins(label, widthMeasureSpec, 0, heightMeasureSpec, 0);
                        labelWidth = labelLp.leftMargin + label.getMeasuredWidth() + labelLp.rightMargin;
                    }

                    int childWidth = childLp.leftMargin + child.getMeasuredWidth() + childLp.rightMargin;

                    maxWidth = Math.max(maxWidth, childWidth + labelWidth);
                    maxHeight += child.getMeasuredHeight() + childLp.topMargin + childLp.bottomMargin + childSpacing;
                    childState = combineMeasuredStates(childState, child.getMeasuredState());
                }
            }
        }

        //Correct for spacing of the FAM
        maxHeight = Math.max(maxHeight - childSpacing, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        Log.v(TAG, String.format("Measure menu: height=%s, width=%s", maxHeight, maxWidth));
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState), resolveSizeAndState(maxHeight, heightMeasureSpec, childState));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int parentWidth = right - left;
        final int parentHeight = bottom - top;

        layoutFam(parentWidth, parentHeight);

        Log.v(TAG, String.format("Menu state is %s", menuStateToString(mMenuState)));

        int stackedHeight = parentHeight - (floatingActionMenuMainButton.getMeasuredHeight() + childSpacing);
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            final View label = labels.get(child);

            if (child instanceof FloatingActionMenuButton && !child.equals(floatingActionMenuMainButton) && child.getVisibility() != View.GONE) {
                final int childWidth = child.getMeasuredWidth();
                final int childHeight = child.getMeasuredHeight();

                final int childTop = stackedHeight - childHeight;
                final int childBottom = childTop + childHeight;
                final int childRight = parentWidth - childWidth + (getFloatingActionMenuMainButton().getMeasuredWidth() - childWidth) / 2; //TODO check rightpadding
                final int childLeft = childRight - childWidth;
                Log.v(TAG, String.format("Layout child button %s: left=%s, top=%s, right=%s, bottom=%s", i, childLeft, childTop, childRight, childBottom));

                child.layout(childLeft, childTop, childRight, childBottom);

                if(label != null) {
                    final int labelWidth = label.getMeasuredWidth();
                    final int labelHeight = label.getMeasuredHeight();

                    final int labelTop = childTop + childHeight / 2 - labelHeight / 2;
                    final int labelBottom = labelTop + labelHeight;
                    final int labelRight = childLeft;
                    final int labelLeft = labelRight - labelWidth;
                    label.layout(labelLeft, labelTop, labelRight, labelBottom);
                }

                stackedHeight = stackedHeight - (childHeight + childSpacing);
            }
        }
    }

    private void layoutFam(int parentWidth, int parentHeight){
        if(floatingActionMenuMainButton.getVisibility() != View.GONE) {
            final int famWidth = floatingActionMenuMainButton.getMeasuredWidth();
            final int famHeight = floatingActionMenuMainButton.getMeasuredHeight();

            final int famTop = parentHeight - famHeight;
            final int famBottom = famTop + famHeight;
            final int famLeft = parentWidth - famWidth;
            final int famRight = famLeft + famWidth;
            Log.v(TAG, String.format("Layout main button: left=%s, top=%s, right=%s, bottom=%s", famLeft, famTop, famRight, famBottom));
            floatingActionMenuMainButton.layout(famLeft, famTop, famRight, famBottom);
        }
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public static class LayoutParams extends MarginLayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray array = c.obtainStyledAttributes(attrs, R.styleable.FloatingActionMenu);
            array.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }


        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

    }

    /*
     *  State restore
     */

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);

        ss.menuState = mMenuState;
        ss.prevMenuState = mPrevMenuState;

        Log.v(TAG, String.format("Saved state: menustate=%s, prevmenustate=%s", menuStateToString(mMenuState), menuStateToString(mPrevMenuState)));
        return  ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(!(state instanceof SavedState)){
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        mRestoredMenuState = ss.menuState;
        mRestoredPrevMenuState = ss.prevMenuState;

        Log.v(TAG, String.format("Restored state: menustate=%s, prevmenustate=%s", menuStateToString(mRestoredMenuState), menuStateToString(mRestoredPrevMenuState)));
    }

    private void restoreMenu(){
        if(mRestoredMenuState == MENU_STATE_COLLAPSED) {
            collapse(true);
        }
        else if (mRestoredMenuState == MENU_STATE_EXPANDED){
            expand(true);
        }
        else if (mRestoredMenuState == MENU_STATE_HIDDEN){
            hide(true);
        }
        mRestoredMenuState = MENU_STATE_NONE;
        mRestoredPrevMenuState = MENU_STATE_NONE;
    }

    static class SavedState extends BaseSavedState {
        int menuState;
        int prevMenuState;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            menuState = in.readInt();
            prevMenuState = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(menuState);
            out.writeInt(prevMenuState);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    /*
     *  Nested scroll behaviour
     */

    public static class Behavior extends CoordinatorLayout.Behavior<FloatingActionMenu> {
        public Behavior() {
            super();
        }

        public Behavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionMenu child, View directTargetChild, View target, int nestedScrollAxes) {
            return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
        }

        @Override
        public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionMenu child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
            if(dyConsumed > 0){
                child.hide(false);
            } else if (dyConsumed < 0) {
                child.show(false);
            }
        }
    }

    /*
     * Helpers
     */

    private String menuStateToString(int menuState){
        if(menuState == MENU_STATE_COLLAPSED) return "collapsed";
        else if(menuState == MENU_STATE_HIDDEN) return "hidden";
        else if(menuState == MENU_STATE_EXPANDED) return "expanded";
        else if(menuState == MENU_STATE_NONE) return "none";
        else return "invalid";
    }

    private String visibilityToString(int visibility){
        if(visibility == View.VISIBLE) return "visible";
        else if(visibility == View.INVISIBLE) return "invisible";
        else if(visibility == View.GONE) return "gone";
        else return "invalid";
    }
}
