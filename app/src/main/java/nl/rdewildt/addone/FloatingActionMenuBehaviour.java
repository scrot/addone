package nl.rdewildt.addone;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

/**
 * Created by roydewildt on 18/10/2016.
 */

public class FloatingActionMenuBehaviour extends CoordinatorLayout.Behavior<FloatingActionsMenu> {
    private Boolean floatingActionMenuVisible = true;
    private Float floatingActionMenuY;

    public FloatingActionMenuBehaviour(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionsMenu child, View directTargetChild, View target, int nestedScrollAxes) {
        if (floatingActionMenuY == null){
            floatingActionMenuY = child.getY();
        }
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionsMenu child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (dyConsumed > 0 && floatingActionMenuVisible) {
            hide(child, coordinatorLayout);
        } else if (dyConsumed < 0 && !floatingActionMenuVisible) {
            show(child, coordinatorLayout);
        }
    }

    private void show(FloatingActionsMenu child, CoordinatorLayout layout) {
        ViewCompat.animate(child).y(floatingActionMenuY).setInterpolator(new LinearInterpolator()).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {

            }

            @Override
            public void onAnimationEnd(View view) {
                floatingActionMenuVisible = true;
            }

            @Override
            public void onAnimationCancel(View view) {

            }
        }).start();
    }

    private void hide(FloatingActionsMenu child, CoordinatorLayout layout){
        ViewCompat.animate(child).y(child.getBottom()).setInterpolator(new LinearInterpolator()).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {
                child.collapse();
            }

            @Override
            public void onAnimationEnd(View view) {
                floatingActionMenuVisible = false;
            }

            @Override
            public void onAnimationCancel(View view) {
            }
        }).start();
    }
}
