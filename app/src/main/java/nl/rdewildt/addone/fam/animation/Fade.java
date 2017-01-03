package nl.rdewildt.addone.fam.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import nl.rdewildt.addone.fam.FloatingActionMenu;

/**
 * Created by roydewildt on 02/01/2017.
 */

public abstract class Fade extends FamAnimation {
    private final float fromA;
    private final float toA;
    private int mDuration;

    public Fade(FloatingActionMenu floatingActionMenu, float fromA, float toA, int duration) {
        super(floatingActionMenu);
        this.fromA = fromA;
        this.toA = toA;
        this.mDuration = duration;

    }

    @Override
    public void start() {
        AnimatorSet animatorSet = new AnimatorSet();
        for (int i = 0; i < getFloatingActionMenu().getChildCount(); i++) {
            final View child = getFloatingActionMenu().getChildAt(i);
            animatorSet.play(ObjectAnimator.ofFloat(child, "alpha", fromA, toA));
        }
        animatorSet.setDuration(mDuration);
        animatorSet.start();
    }
}
