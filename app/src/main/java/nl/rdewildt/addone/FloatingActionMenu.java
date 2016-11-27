package nl.rdewildt.addone;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

import java.util.List;

/**
 * Created by roydewildt on 10/11/2016.
 */

@CoordinatorLayout.DefaultBehavior(FloatingActionMenu.Behavior.class)
public class FloatingActionMenu extends ViewGroup {
    private FloatingActionButton mFloatingActionMenu;

    private enum MenuState {
        COLLAPSED,
        EXPANDED,
        HIDDEN
    }
    private MenuState menuState = MenuState.COLLAPSED;
    private MenuState lastMenuState;

    //Animation
    private Interpolator mInterpolator = new LinearInterpolator();
    private boolean mFillAfter = true;
    private int mDuration;
    private int mFadeDuration = 200;


    //Menu attributes
    private int mSpacing = 0;
    private boolean mLongPressMenuToggle = false;


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
        this.mSpacing = array.getDimensionPixelSize(R.styleable.FloatingActionMenu_spacing, this.mSpacing);
        this.mLongPressMenuToggle = array.getBoolean(R.styleable.FloatingActionMenu_longPressMenuToggle, this.mLongPressMenuToggle);
        array.recycle();

        addMainFloatingActionButton();
    }

    private void addMainFloatingActionButton(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFloatingActionMenu = (FloatingActionButton) inflater.inflate(R.layout.main_fab, null);

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
            requestLayout();

            //Animate fam
            Animation animationFam = AnimationUtils.loadAnimation(getContext(), R.anim.menu_collapse);
            mFloatingActionMenu.startAnimation(animationFam);

            //Animate menu children
            int stackedHeight = getMeasuredHeight() - mFloatingActionMenu.getMeasuredHeight();
            for (int i = 0; i < getChildCount(); i++) {
                final View child = getChildAt(i);

                if (!child.equals(mFloatingActionMenu)) {
                    final int childHeight = child.getMeasuredHeight();
                    final int childTop = stackedHeight - childHeight;
                    translateY(child, 0, childTop, duration);
                    stackedHeight += (childHeight + mSpacing);
                    child.setAlpha(0f);
                }
            }
        }
    }

    public void expand(int duration) {
        if(menuState == MenuState.COLLAPSED) {
            menuState = MenuState.EXPANDED;
            requestLayout();

            //Animate fam
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.menu_expand);
            mFloatingActionMenu.startAnimation(animation);


            //Animate menu children
            int stackedHeight = getMeasuredHeight() - (mFloatingActionMenu.getMeasuredHeight() + mSpacing);
            for (int i = 0; i < getChildCount(); i++) {
                final View child = getChildAt(i);

                if (!child.equals(mFloatingActionMenu)) {
                    final int childHeight = child.getMeasuredHeight();
                    final int childTop = stackedHeight - childHeight;
                    translateY(child, childTop, 0, duration);
                    stackedHeight += (childHeight + mSpacing);
                    child.setAlpha(1f);
                }
            }
        }
    }

    public void fadeIn(){
        if(menuState == MenuState.HIDDEN) {
            menuState = lastMenuState;

            for (int i = 0; i < getChildCount(); i++) {
                final View child = getChildAt(i);
                translateAlpha(child, 0f, 1f, mFadeDuration);
                //child.setVisibility(View.VISIBLE);
            }
        }
    }

    public void fadeOut(){
        if(menuState != MenuState.HIDDEN) {
            lastMenuState = menuState;
            menuState = MenuState.HIDDEN;

            for (int i = 0; i < getChildCount(); i++) {
                final View child = getChildAt(i);
                translateAlpha(child, 1f, 0f, mFadeDuration);
                //child.setVisibility(View.GONE);
            }
        }
    }

    public void translateAlpha(View child, float fromA, float toA, int duration){
        final Animation fadeOutAnimation = new AlphaAnimation(fromA, toA);
        fadeOutAnimation.setDuration(duration);
        fadeOutAnimation.setFillAfter(mFillAfter);
        child.startAnimation(fadeOutAnimation);
    }

    private void translateY(View child, int fromY, int toY, int duration){
        final TranslateAnimation animationChild = new TranslateAnimation(0, 0, fromY, toY);
        animationChild.setInterpolator(mInterpolator);
        animationChild.setFillAfter(mFillAfter);
        animationChild.setDuration(duration > 0 ? duration : (getChildCount() - 1) * 60);
        child.startAnimation(animationChild);
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

            //if (child.getVisibility() != View.GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

                //Calculate child dimensions including the margins of parent
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin);
                maxHeight += child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin + mSpacing;
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            //}
        }
        //Correct for spacing of the FAM, that is none existing
        maxHeight = Math.max(maxHeight - mSpacing, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState), resolveSizeAndState(maxHeight, heightMeasureSpec, childState));
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

                if (!child.equals(mFloatingActionMenu)) {
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
        //Bring menu button to top of stack
        mFloatingActionMenu.bringToFront();

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
