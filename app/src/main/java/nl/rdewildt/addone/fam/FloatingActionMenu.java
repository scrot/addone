package nl.rdewildt.addone.fam;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import java.security.InvalidParameterException;

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

    private int mMenuState = MENU_STATE_EXPANDED;
    private int mPrevMenuState = MENU_STATE_NONE;

    // Animation Factory
    static final FamAnimationFactory mAnimFactory = new DefaultFamAnimationFactory();

    // Attribute set
    private int childSpacing;
    private boolean mLongPressMenuToggle;
    private int mSize;
    private Drawable mIcon;
    private int mIconSize;

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
        this.childSpacing = array.getDimensionPixelSize(R.styleable.FloatingActionMenu_fam_menu_spacing, DpConverter.dpToPx(8));
        this.mLongPressMenuToggle = array.getBoolean(R.styleable.FloatingActionMenu_fam_long_press, false);

        this.mSize = array.getDimensionPixelSize(R.styleable.FloatingActionMenu_fam_size, DpConverter.dpToPx(56));
        this.mIcon = array.getDrawable(R.styleable.FloatingActionMenu_fam_icon_src);
        this.mIconSize = array.getDimensionPixelSize(R.styleable.FloatingActionMenu_fam_icon_size, DpConverter.dpToPx(24));
        array.recycle();
    }

    public FloatingActionMenuButton getFloatingActionMenuMainButton() {
        return floatingActionMenuMainButton;
    }

    public int getChildSpacing() {
        return childSpacing;
    }

    @Override
    protected void onFinishInflate() {
        addMainFloatingActionButton();
        collapse();
    }

    private void addMainFloatingActionButton(){
        floatingActionMenuMainButton = new FloatingActionMenuButton(getContext(), mSize, mIcon, mIconSize);

        //Set fam expand/collapse animation
        floatingActionMenuMainButton.setOnLongClickListener(view -> {
            if(mMenuState == MENU_STATE_EXPANDED){
                collapse();
            }
            else if (mMenuState == MENU_STATE_COLLAPSED){
                expand();
            }
            return true;
        });

        addView(floatingActionMenuMainButton, super.generateDefaultLayoutParams());
    }

    public void collapse() {
        if(mMenuState == MENU_STATE_EXPANDED) {
            FamAnimation anim = mAnimFactory.collapse(this);
            anim.addFamAnimationListener(() -> {
                mMenuState = MENU_STATE_COLLAPSED;
                switchChildrenVisibility(GONE);
                requestLayout();
            });
            anim.start();
        }
    }

    public void expand() {
        if(mMenuState == MENU_STATE_COLLAPSED) {
            switchChildrenVisibility(VISIBLE);
            FamAnimation anim = mAnimFactory.expand(this);
            anim.addFamAnimationListener(() -> {
                mMenuState = MENU_STATE_EXPANDED;
                requestLayout();
            });
            anim.start();
        }
    }

    public void fadeIn(){
        if(mMenuState == MENU_STATE_HIDDEN) {
            FamAnimation anim = mAnimFactory.show(this);
            anim.addFamAnimationListener(() -> mMenuState = mPrevMenuState);
            anim.start();

        }
    }

    public void fadeOut(){
        if(mMenuState != MENU_STATE_HIDDEN) {
            mPrevMenuState = mMenuState;
            FamAnimation anim = mAnimFactory.hide(this);
            anim.addFamAnimationListener(() -> mMenuState = MENU_STATE_HIDDEN);
            anim.start();
        }
    }

    private void switchChildrenVisibility(int visibility) {
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if(!child.equals(floatingActionMenuMainButton)){
                child.setVisibility(visibility);
            }
        }
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
            if(child.getVisibility() != View.GONE) {
                final LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();

                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

                maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin);
                maxHeight += child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin + childSpacing;
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }

        //Correct for spacing of the FAM
        maxHeight = Math.max(maxHeight - childSpacing, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        Log.v(TAG, String.format("measure menu: height=%s, width=%s", maxHeight, maxWidth));
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

            if (!child.equals(floatingActionMenuMainButton) && child.getVisibility() != View.GONE) {
                final int childWidth = child.getMeasuredWidth();
                final int childHeight = child.getMeasuredHeight();

                final int childTop = stackedHeight - childHeight;
                final int childBottom = childTop + childHeight;
                final int childLeft = (parentWidth - childWidth) / 2;
                final int childRight = childLeft + childWidth;
                Log.v(TAG, String.format("Layout child %s: left=%s, top=%s, right=%s, bottom=%s", i, childLeft, childTop, childRight, childBottom));

                child.layout(childLeft, childTop, childRight, childBottom);

                stackedHeight = stackedHeight - (childHeight + childSpacing);
            }
        }
        /*
        if (mMenuState == MENU_STATE_COLLAPSED) {
            for (int i = 0; i < getChildCount(); i++) {
                final View child = getChildAt(i);

                if (!child.equals(floatingActionMenuMainButton)) {
                    final int childHeight = child.getMeasuredHeight();
                    final int childWidth = child.getMeasuredWidth();

                    final int childTop = parentHeight - childHeight;
                    final int childBottom = parentHeight;
                    final int childLeft = (parentWidth - childWidth) / 2;
                    final int childRight = childLeft + childWidth;

                    Log.v(TAG, String.format("Layout child %s: left=%s, top=%s, right=%s, bottom=%s", i, childLeft, childTop, childRight, childBottom));

                    child.layout(childLeft, childTop, childRight, childBottom);
                }
            }
        }
        */
    }

    private void layoutFam(int parentWidth, int parentHeight){
        final int famWidth = floatingActionMenuMainButton.getMeasuredWidth();
        final int famHeight = floatingActionMenuMainButton.getMeasuredHeight();

        final int famTop = parentHeight - famHeight;
        final int famBottom = famTop + famHeight;
        final int famLeft = (parentWidth - famWidth) / 2;
        final int famRight = famLeft + famWidth;
        floatingActionMenuMainButton.layout(famLeft, famTop, famRight, famBottom);
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
        SavedState savedState = new SavedState(superState);
        savedState.menuState = mMenuState;
        savedState.prevMenuState = mPrevMenuState;
        return  savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mMenuState = ss.menuState;
        mPrevMenuState = ss.prevMenuState;
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
                child.fadeOut();
            } else if (dyConsumed < 0) {
                child.fadeIn();
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
        else throw new InvalidParameterException("invalid menuState");
    }
}
