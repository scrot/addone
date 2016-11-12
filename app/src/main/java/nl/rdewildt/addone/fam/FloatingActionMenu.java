package nl.rdewildt.addone.fam;

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

import nl.rdewildt.addone.R;

/**
 * Created by roydewildt on 10/11/2016.
 */

@CoordinatorLayout.DefaultBehavior(FloatingActionMenu.Behavior.class)
public class FloatingActionMenu extends ViewGroup {
    private FloatingActionButton mFloatingActionMenu;
    private boolean mMenuExpanded;


    public FloatingActionMenu(Context context) {
        super(context,null);
        initialize();

    }

    public FloatingActionMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public FloatingActionMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public FloatingActionMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int childCount = getChildCount();

        //To be measured
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        for(int i = 0; i < childCount; i++){
            final View child = getChildAt(i);

            if(child.getVisibility() != View.GONE){
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

                final LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();

                //Calculate child dimensions including the margins from margins of parent
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin);
                maxHeight += child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }

        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState), resolveSizeAndState(maxHeight, heightMeasureSpec, childState));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();

        int stackedHeight = 0;
        final int parentWidth = right - left;

        for(int i = count - 1; i >= 0; i--){
            final View child = getChildAt(i);

            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();

            final int childTop = stackedHeight;
            final int childBottom = childTop + childHeight;
            final int childLeft = (parentWidth - childWidth) / 2;
            final int childRight = childLeft + childWidth;

            child.layout(childLeft, childTop, childRight, childBottom);

            stackedHeight += childHeight;
        }
    }

    public void expand(){
        if(!mMenuExpanded) {
            mMenuExpanded = true;
            Animation plusToCross = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim);
            mFloatingActionMenu.startAnimation(plusToCross);
        }
    }

    public void collapse(){
        if(mMenuExpanded) {
            mMenuExpanded = false;
            Animation CrossToPlus = AnimationUtils.loadAnimation(getContext(), R.anim.rotateback_anim);
            mFloatingActionMenu.startAnimation(CrossToPlus);
        }
    }

    public void hide(){
        mFloatingActionMenu.hide();
        this.collapse();
    }

    public void show(){
        mFloatingActionMenu.show();
    }

    private void initialize(){
        mMenuExpanded = false;
        addMainFloatingActionButton();
    }

    private void addMainFloatingActionButton(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFloatingActionMenu = (FloatingActionButton) inflater.inflate(R.layout.main_fab, null);

        //Set main fab expand/collapse animation
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

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
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

        public LayoutParams(MarginLayoutParams source) {
            super(source);
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
