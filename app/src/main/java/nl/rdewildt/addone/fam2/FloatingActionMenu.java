package nl.rdewildt.addone.fam2;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import nl.rdewildt.addone.R;

/**
 * Created by roydewildt on 10/11/2016.
 */

@CoordinatorLayout.DefaultBehavior(FloatingActionMenu.Behavior.class)
public class FloatingActionMenu extends LinearLayout {
    private MainFloatingActionButton mab;
    private boolean menuExpanded;


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

    public void expand(){
        if(!menuExpanded) {
            menuExpanded = true;
            Animation plusToCross = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim);
            mab.startAnimation(plusToCross);
        }
    }

    public void collapse(){
        if(menuExpanded) {
            menuExpanded = false;
            Animation CrossToPlus = AnimationUtils.loadAnimation(getContext(), R.anim.rotateback_anim);
            mab.startAnimation(CrossToPlus);
        }
    }

    public void hide(){
        mab.hide();
        this.collapse();
    }

    public void show(){
        mab.show();
    }

    private void initialize(){
        menuExpanded = false;
        addMainFloatingActionButton();
    }

    private void addMainFloatingActionButton(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mab = (MainFloatingActionButton) inflater.inflate(R.layout.main_fab, null);

        //Set main fab expand/collapse animation
        mab.setOnLongClickListener(view -> {
            if(menuExpanded){
                collapse();
            } else {
                expand();
            }
            return true;
        });

        addView(mab, super.generateDefaultLayoutParams());
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
