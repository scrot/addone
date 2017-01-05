package nl.rdewildt.addone.fam.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import nl.rdewildt.addone.fam.FloatingActionMenu;

/**
 * Created by roydewildt on 02/01/2017.
 */

public abstract class Fade extends FamAnimation {
    private AnimatorSet animatorSet;

    private final float fromA;
    private final float toA;

    public Fade(FloatingActionMenu floatingActionMenu, float fromA, float toA) {
        super(floatingActionMenu);
        this.fromA = fromA;
        this.toA = toA;

    }

    @Override
    public void start() {
        animatorSet = new AnimatorSet();
        for (int i = 0; i < getFloatingActionMenu().getChildCount(); i++) {
            final View child = getFloatingActionMenu().getChildAt(i);
            animatorSet.play(ObjectAnimator.ofFloat(child, "alpha", fromA, toA));
        }
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                onFamAnimationStart();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                onFamAnimationEnd();
            }
        });
        animatorSet.setDuration(getDuration());
        animatorSet.start();
    }

    @Override
    public void end() {
        if(animatorSet != null){
            animatorSet.end();
        }
    }
}
