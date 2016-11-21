package nl.rdewildt.addone;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by roydewildt on 10/11/2016.
 */

@CoordinatorLayout.DefaultBehavior(FloatingActionMenu.Behavior.class)
public class FloatingActionMenu extends ViewGroup {
    private FloatingActionButton mFloatingActionMenu;
    private boolean mMenuExpanded = false;

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
            if(mMenuExpanded){
                collapse();
            } else {
                expand();
            }
            return true;
        });

        addView(mFloatingActionMenu, super.generateDefaultLayoutParams());
    }

    public void expand(){
        if(!mMenuExpanded) {
            mMenuExpanded = true;
            requestLayout();
            /*
            //Animate menu-button
            int percentage = (int) (getMeasuredWidth() * .50);

            Animation plusToCross = new RotateAnimation(0, 135, percentage, percentage);
            plusToCross.setDuration(400);
            plusToCross.setFillAfter(true);
            plusToCross.setInterpolator(new OvershootInterpolator());
            mFloatingActionMenu.startAnimation(plusToCross);

            //Animate children
            final int count = getChildCount();
            int stackedHeight = getMeasuredHeight() + getChildAt(0).getHeight() + mSpacing;

            for(int i = 1; i < count; i++) {
                final View child = getChildAt(i);
                int expandedHeight = stackedHeight - (child.getMeasuredHeight() + mSpacing);

                final Animation expandChild = new TranslateAnimation(0, 0, getMeasuredHeight(), expandedHeight);
                expandChild.setDuration(400);
                expandChild.setFillAfter(true);
                expandChild.setInterpolator(new OvershootInterpolator());
                child.startAnimation(expandChild);

                stackedHeight -= (child.getMeasuredHeight() + mSpacing);
            }
            */
        }
    }

    public void collapse(){
        if(mMenuExpanded) {
            mMenuExpanded = false;
            requestLayout();

            /*
            //Animate menu-button
            int percentage = (int) (getMeasuredWidth() * .50);

            Animation crossToPlus = new RotateAnimation(135, 0, percentage, percentage);
            crossToPlus.setDuration(400);
            crossToPlus.setFillAfter(true);
            crossToPlus.setInterpolator(new OvershootInterpolator());
            mFloatingActionMenu.startAnimation(crossToPlus);

            //Animate children
            final int count = getChildCount();
            for(int i = 1; i < count; i++) {
                final View child = getChildAt(i);
                int expandedHeight = (child.getMeasuredHeight() + mSpacing) * i;
                final Animation collapseChild = new TranslateAnimation(0, 0, 0, -expandedHeight);
                collapseChild.setDuration(400);
                collapseChild.setFillAfter(true);
                collapseChild.setInterpolator(new OvershootInterpolator());
                child.startAnimation(collapseChild);
            }*/
        }
    }

    public void hide(){
        mFloatingActionMenu.hide();
        this.collapse();
    }

    public void show(){
        mFloatingActionMenu.show();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //To be measured
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        if(mMenuExpanded) {
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
        }
        else {
            measureChildWithMargins(mFloatingActionMenu, widthMeasureSpec, 0, heightMeasureSpec, 0);
            maxHeight = mFloatingActionMenu.getMeasuredHeight();
            maxWidth = mFloatingActionMenu.getMeasuredWidth();
            childState = combineMeasuredStates(childState, mFloatingActionMenu.getMeasuredState());
        }

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState), resolveSizeAndState(maxHeight, heightMeasureSpec, childState));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //Bring menu button to front
        mFloatingActionMenu.bringToFront();

        final int parentWidth = right - left;
        final int parentHeight = bottom - top;

        //Layout FAM
        final int famWidth = mFloatingActionMenu.getMeasuredWidth();
        final int famHeight = mFloatingActionMenu.getMeasuredHeight();

        final int famTop = parentHeight - famHeight;
        final int famBottom = famTop + famHeight;
        final int famLeft = (parentWidth - famWidth) / 2;
        final int famRight = famLeft + famWidth;

        mFloatingActionMenu.layout(famLeft, famTop, famRight, famBottom);

        if(mMenuExpanded) {
            //Total height of already rendered buttons and their spacing
            int stackedHeight = parentHeight - (famHeight + mSpacing);

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
                child.hide();
            } else if (dyConsumed < 0) {
                child.show();
            }
        }
    }
}
