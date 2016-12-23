package nl.rdewildt.addone.fam;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import nl.rdewildt.addone.R;
import nl.rdewildt.addone.helper.DpConverter;

/**
 * Created by roydewildt on 10/11/2016.
 */

@CoordinatorLayout.DefaultBehavior(FloatingActionMenu.Behavior.class)
public class FloatingActionMenu extends ViewGroup {
    private FloatingActionMenuButton mFloatingActionMenu;

    private enum MenuState {
        COLLAPSED,
        EXPANDED,
        HIDDEN
    }
    private MenuState menuState = MenuState.COLLAPSED;
    private MenuState lastMenuState;
    private int mMaxMenuHeight;

    //Animation FAM
    private int mFamRotateDuration = 400;
    private int mRotateToDegree = 135;
    private Interpolator mFamInterpolator = new OvershootInterpolator();

    //Animation FAMB
    private int mDuration;
    private int mFadeDuration = 200;
    private Interpolator mInterpolator = new LinearInterpolator();


    //Menu attributes
    private int mSpacing;
    private boolean mLongPressMenuToggle;

    //Menu-button attributes
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
        this.mSpacing = array.getDimensionPixelSize(R.styleable.FloatingActionMenu_fam_menu_spacing, DpConverter.dpToPx(8));
        this.mLongPressMenuToggle = array.getBoolean(R.styleable.FloatingActionMenu_fam_long_press, false);

        this.mSize = array.getDimensionPixelSize(R.styleable.FloatingActionMenu_fam_size, DpConverter.dpToPx(56));
        this.mIcon = array.getDrawable(R.styleable.FloatingActionMenu_fam_icon_src);
        this.mIconSize = array.getDimensionPixelSize(R.styleable.FloatingActionMenu_fam_icon_size, DpConverter.dpToPx(24));
        array.recycle();
    }

    private void addMainFloatingActionButton(){
        mFloatingActionMenu = new FloatingActionMenuButton(getContext(), mSize, mIcon, mIconSize);

        //Set fam expand/collapse animation
        mFloatingActionMenu.setOnLongClickListener(view -> {
            if(menuState == MenuState.EXPANDED){
                collapse(mDuration);
            }
            else if (menuState == MenuState.COLLAPSED){
                expand(mDuration);
            }
            return true;
        });

        addView(mFloatingActionMenu, super.generateDefaultLayoutParams());
    }

    public void collapse(int duration) {
        if(menuState == MenuState.EXPANDED) {
            menuState = MenuState.COLLAPSED;

            //Animate fam
            rotateCentered(mFloatingActionMenu, mRotateToDegree, 0, mFamRotateDuration);

            //Animate menu children
            switchChildrenVisibility(View.VISIBLE);
            int stackedHeight = getMeasuredHeight() - mFloatingActionMenu.getMeasuredHeight();
            for (int i = 0; i < getChildCount(); i++) {
                final View child = getChildAt(i);

                if (!child.equals(mFloatingActionMenu)) {
                    final int childHeight = child.getMeasuredHeight();
                    final int childTop = stackedHeight - childHeight;
                    translateY(child, 0, childTop, duration, View.GONE);
                    stackedHeight += (childHeight + mSpacing);
                }
            }
        }
    }

    public void expand(int duration) {
        if(menuState == MenuState.COLLAPSED) {
            menuState = MenuState.EXPANDED;

            //Animate fam
            rotateCentered(mFloatingActionMenu, 0, mRotateToDegree, mFamRotateDuration);

            //Animate menu children
            switchChildrenVisibility(View.VISIBLE);
            int stackedHeight = getMeasuredHeight() - (mFloatingActionMenu.getMeasuredHeight() + mSpacing);
            for (int i = 0; i < getChildCount(); i++) {
                final View child = getChildAt(i);

                if (!child.equals(mFloatingActionMenu)) {
                    final int childHeight = child.getMeasuredHeight();
                    final int childTop = stackedHeight - childHeight;
                    translateY(child, childTop, 0, duration, View.VISIBLE);
                    stackedHeight += (childHeight + mSpacing);
                }
            }
        }
    }

    public void fadeIn(){
        if(menuState == MenuState.HIDDEN) {
            menuState = lastMenuState;

            mFloatingActionMenu.setVisibility(View.VISIBLE);

            if(menuState == MenuState.EXPANDED){
                switchChildrenVisibility(View.VISIBLE);
            }

            for (int i = 0; i < getChildCount(); i++) {
                final View child = getChildAt(i);
                translateAlpha(child, 0f, 1f, mFadeDuration, View.VISIBLE);
            }
        }
    }

    public void fadeOut(){
        if(menuState != MenuState.HIDDEN) {
            lastMenuState = menuState;

            if(menuState == MenuState.COLLAPSED){
                switchChildrenVisibility(View.GONE);
            }

            menuState = MenuState.HIDDEN;

            for (int i = 0; i < getChildCount(); i++) {
                final View child = getChildAt(i);
                translateAlpha(child, 1f, 0f, mFadeDuration, View.GONE);
            }
        }
    }

    public void translateAlpha(View child, float fromA, float toA, int duration, int visibility){
        final ObjectAnimator childAnimation = ObjectAnimator.ofFloat(child, "alpha", fromA, toA);
        childAnimation.setDuration(duration);

        //Set visibility after animation
        childAnimation.addListener(switchVisibility(child, visibility));

        childAnimation.start();
    }

    private void translateY(View child, int fromY, int toY, int duration, int visibility){
        final ObjectAnimator animationChild = ObjectAnimator.ofFloat(child, "translationY", fromY, toY);
        animationChild.setInterpolator(mInterpolator);
        animationChild.setDuration(duration > 0 ? duration : (getChildCount() - 1) * 60);

        //Set visibility after animation
        animationChild.addListener(switchVisibility(child, visibility));

        animationChild.start();
    }

    private void rotateCentered(View child, int fromDegree, int toDegree, int duration){
        final ObjectAnimator animationChild = ObjectAnimator.ofFloat(child, "rotation", fromDegree, toDegree);
        animationChild.setInterpolator(mFamInterpolator);
        animationChild.setDuration(duration);
        animationChild.start();
    }

    private void switchChildrenVisibility(int visibility) {
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if(!child.equals(mFloatingActionMenu)){
                child.setVisibility(visibility);
            }
        }
        measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
    }

    private Animator.AnimatorListener switchVisibility(View child, int visibility){
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                child.setVisibility(visibility);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        };
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        if(mFloatingActionMenu != null) {
            mFloatingActionMenu.setOnClickListener(l);
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
            final LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();

            if (child.getVisibility() != View.GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

                //Calculate child dimensions including the margins of parent
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin);
                maxHeight += child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin + mSpacing;
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }
        //Correct for spacing of the FAM, that is none existing
        maxHeight = Math.max(maxHeight - mSpacing, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState), resolveSizeAndState(maxHeight, heightMeasureSpec, childState));
    }

    @Override
    protected void onFinishInflate() {
        addMainFloatingActionButton();
        switchChildrenVisibility(View.GONE);
        super.onFinishInflate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int parentWidth = right - left;
        final int parentHeight = bottom - top;

        layoutFAM(parentWidth, parentHeight);

        //Layout children
        if(menuState == MenuState.EXPANDED) {
            //Total height of already rendered buttons and their spacing
            int stackedHeight = parentHeight - (mFloatingActionMenu.getMeasuredHeight() + mSpacing);

            //Layout remaining children
            for (int i = 0; i < getChildCount(); i++) {
                final View child = getChildAt(i);

                if (!child.equals(mFloatingActionMenu) && child.getVisibility() == View.VISIBLE) {
                    final int childWidth = child.getMeasuredWidth();
                    final int childHeight = child.getMeasuredHeight();

                    final int childTop = stackedHeight - childHeight;
                    final int childBottom = childTop + childHeight;
                    final int childLeft = (parentWidth - childWidth) / 2;
                    final int childRight = childLeft + childWidth;

                    child.layout(childLeft, childTop, childRight, childBottom);

                    stackedHeight = stackedHeight - (childHeight + mSpacing);
                }
            }
        }
    }

    private void layoutFAM(int parentWidth, int parentHeight){
        final int famWidth = mFloatingActionMenu.getMeasuredWidth();
        final int famHeight = mFloatingActionMenu.getMeasuredHeight();

        final int famTop = parentHeight - famHeight;
        final int famBottom = famTop + famHeight;
        final int famLeft = (parentWidth - famWidth) / 2;
        final int famRight = famLeft + famWidth;
        mFloatingActionMenu.layout(famLeft, famTop, famRight, famBottom);
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
}
