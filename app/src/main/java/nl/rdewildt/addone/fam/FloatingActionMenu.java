package nl.rdewildt.addone.fam;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import nl.rdewildt.addone.R;
import nl.rdewildt.addone.fam.animation.FamAnimation;
import nl.rdewildt.addone.fam.animation.FamAnimationFactory;
import nl.rdewildt.addone.fam.fab.FloatingActionMenuButton;
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

    // Attribute set
    private int childSpacing;
    private boolean mLongPressMenuToggle;
    private boolean mIsSingleButton;

    public FloatingActionMenu(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public FloatingActionMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public FloatingActionMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public FloatingActionMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FloatingActionMenu);
        try {
            this.mRestoredMenuState = a.getInteger(R.styleable.FloatingActionMenu_fam_menu_state, mMenuState);
            this.childSpacing = a.getDimensionPixelSize(R.styleable.FloatingActionMenu_fam_menu_spacing, DpConverter.dpToPx(8));
            this.mLongPressMenuToggle = a.getBoolean(R.styleable.FloatingActionMenu_fam_long_press, false);
            this.mIsSingleButton = a.getBoolean(R.styleable.FloatingActionMenu_fam_single_button, false);
        } finally {
            a.recycle();
        }

        if(isSingleButton()){
            this.mLongPressMenuToggle = false;
            this.mRestoredMenuState = MENU_STATE_NONE;
            this.childSpacing = 0;
        }

        addMainFloatingActionButton(context, attrs, defStyleAttr, defStyleRes);
        addOnLayoutChangeListener((view, i, i1, i2, i3, i4, i5, i6, i7) -> restoreMenu());
    }

    public FloatingActionMenuButton getFloatingActionMenuMainButton() {
        return floatingActionMenuMainButton;
    }

    private void addMainFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        floatingActionMenuMainButton = new FloatingActionMenuButton(context, attrs, defStyleAttr, defStyleRes);

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

    @Override
    protected void onFinishInflate() {
        floatingActionMenuMainButton.bringToFront();
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        if(getFloatingActionMenuMainButton() != null){
            getFloatingActionMenuMainButton().setOnClickListener(l);
        }
    }

    public int getMenuState(){
        return mMenuState;
    }

    public int getChildSpacing() {
        return childSpacing;
    }

    public boolean isSingleButton() { return mIsSingleButton; }

    /*
     * Menu Behaviour
     */

    public void collapse(boolean instant) {
        if(mMenuState != MENU_STATE_COLLAPSED) {
            FamAnimation anim = mAnimFactory.collapse(this);
            anim.addFamAnimationListener(new FamAnimation.FamAnimationListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onEnd() {
                    switchButtonVisibility(View.GONE);
                }
            });
            anim.setDuration(instant ? 0 : COLLAPSE_ANIM_DURATION)
                    .setChildInterpolator(new FastOutLinearInInterpolator())
                    .setMainInterpolator(new OvershootInterpolator())
                    .start();
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
                    switchButtonVisibility(View.VISIBLE);
                }

                @Override
                public void onEnd() {

                }
            });
            anim.setDuration(instant ? 0 : EXPAND_ANIM_DURATION)
                    .setChildInterpolator(new FastOutLinearInInterpolator())
                    .setMainInterpolator(new OvershootInterpolator())
                    .start();
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

    private void switchButtonVisibility(int visibility) {
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if(!child.equals(floatingActionMenuMainButton)){
                child.setVisibility(visibility);
            }
        }
        //Log.v(TAG, String.format("Children button visibility set to %s", visibilityToString(visibility)));
    }

    /*
     * Menu Layout
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //To be measured
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        for (int i = 0; i < getChildCount(); i++) {
            final FloatingActionMenuButton child = (FloatingActionMenuButton) getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                final LayoutParams childLp = (LayoutParams) child.getLayoutParams();
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                int childCenteringSpacing = (floatingActionMenuMainButton.getIcon().getMeasuredWidth() - child.getIcon().getMeasuredWidth()) / 2;
                int childWidth = childLp.leftMargin + child.getMeasuredWidth() + childLp.rightMargin + childCenteringSpacing;
                maxWidth = Math.max(maxWidth, childWidth);
                maxHeight += child.getMeasuredHeight() + childLp.topMargin + childLp.bottomMargin + childSpacing;
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }

        //Correct for spacing of the FAM
        maxHeight = Math.max(maxHeight - childSpacing, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        //Log.v(TAG, String.format("Measure menu: height=%s, width=%s", maxHeight, maxWidth));
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState), resolveSizeAndState(maxHeight, heightMeasureSpec, childState));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int parentWidth = right - left;
        final int parentHeight = bottom - top;

        layoutMain(parentWidth, parentHeight);

        //Log.v(TAG, String.format("Menu state is %s", menuStateToString(mMenuState)));

        layoutChildren(parentWidth, parentHeight);
    }

    private void layoutMain(int parentWidth, int parentHeight){
        if(floatingActionMenuMainButton.getVisibility() != View.GONE) {
            final int famWidth = floatingActionMenuMainButton.getMeasuredWidth();
            final int famHeight = floatingActionMenuMainButton.getMeasuredHeight();

            final int famTop = parentHeight - famHeight;
            final int famBottom = famTop + famHeight;
            final int famRight = parentWidth;
            final int famLeft = famRight - famWidth;
            //Log.v(TAG, String.format("Layout main button: left=%s, top=%s, right=%s, bottom=%s", famLeft, famTop, famRight, famBottom));
            floatingActionMenuMainButton.layout(famLeft, famTop, famRight, famBottom);
        }
    }

    private void layoutChildren(int parentWidth, int parentHeight) {
        int stackedHeight = parentHeight - (floatingActionMenuMainButton.getMeasuredHeight() + childSpacing);
        for (int i = 0; i < getChildCount(); i++) {
            final FloatingActionMenuButton child = (FloatingActionMenuButton) getChildAt(i);

            if (!child.equals(floatingActionMenuMainButton)) {
                final int childWidth = child.getMeasuredWidth();
                final int childHeight = child.getMeasuredHeight();

                final int childTop = stackedHeight - childHeight;
                final int childBottom = childTop + childHeight;
                final int childRight = parentWidth - (floatingActionMenuMainButton.getIcon().getMeasuredWidth() - child.getIcon().getMeasuredWidth()) / 2;
                final int childLeft = childRight - childWidth;
                //Log.v(TAG, String.format("Layout child button %s: left=%s, top=%s, right=%s, bottom=%s", i, childLeft, childTop, childRight, childBottom));
                child.layout(childLeft, childTop, childRight, childBottom);
                stackedHeight = stackedHeight - (childHeight + childSpacing);
            }
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

    //TODO state cannot be cased to SavedState (caused by toolbar?)
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

        restoreMenu();

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
