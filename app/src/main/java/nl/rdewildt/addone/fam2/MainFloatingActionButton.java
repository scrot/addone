package nl.rdewildt.addone.fam2;

import android.animation.AnimatorSet;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by roydewildt on 10/11/2016.
 */
@CoordinatorLayout.DefaultBehavior(MainFloatingActionButton.Behavior.class)
public class MainFloatingActionButton extends FloatingActionButton {

    public MainFloatingActionButton(Context context) {
        super(context);
    }

    public MainFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static class Behavior extends CoordinatorLayout.Behavior<MainFloatingActionButton> {
        public Behavior() {
            super();
        }

        public Behavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, MainFloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
            return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
        }

        @Override
        public void onNestedScroll(CoordinatorLayout coordinatorLayout, MainFloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
            if(child.getVisibility() == View.VISIBLE && dyConsumed > 0){
                child.hide();
            } else if (child.getVisibility() != View.VISIBLE && dyConsumed < 0) {
                child.show();
            }
        }
    }
}
