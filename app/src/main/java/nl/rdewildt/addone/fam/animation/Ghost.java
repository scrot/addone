package nl.rdewildt.addone.fam.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.rdewildt.addone.fam.FloatingActionMenu;
import nl.rdewildt.addone.fam.fab.FloatingActionMenuButton;

/**
 * Created by roydewildt on 13/01/2017.
 */

public class Ghost extends FamAnimation {
    public static final int DIRECTION_DOWN = 0;
    public static final int DIRECTION_UP = 1;

    private AnimatorSet animatorSet;

    private float fromAlpha;
    private float toAlpha;
    private int rotateFrom;
    private int rotateTo;
    private final int animationDirection;

    public Ghost(FloatingActionMenu floatingActionMenu, float fromAlpha, float toAlpha, int rotateFrom, int rotateTo, int animationDirection) {
        super(floatingActionMenu);
        this.fromAlpha = fromAlpha;
        this.toAlpha = toAlpha;
        this.rotateFrom = rotateFrom;
        this.rotateTo = rotateTo;
        this.animationDirection = animationDirection;

        this.animatorSet = new AnimatorSet();
    }

    @Override
    public void start() {
        List<Animator> animators = new ArrayList<>();
        AnimatorSet buttonAnimators = new AnimatorSet();
        FloatingActionMenu fam = getFloatingActionMenu();


        Animator mainAnimator = centeredRotation(fam.getFloatingActionMenuMainButton(), rotateFrom, rotateTo, getDuration());

        for (int i = 0; i < fam.getChildCount(); i++){
            FloatingActionMenuButton child = (FloatingActionMenuButton) fam.getChildAt(i);
            if(!child.equals(fam.getFloatingActionMenuMainButton())) {
                Animator childAnimator = ObjectAnimator.ofFloat(child, "alpha", fromAlpha, toAlpha);
                childAnimator.setDuration(getDuration() / fam.getChildCount());
                childAnimator.setInterpolator(getChildInterpolator());
                animators.add(childAnimator);
            }
        }

        if(animationDirection == DIRECTION_DOWN){
            Collections.reverse(animators);
        }

        buttonAnimators.playSequentially(animators);
        animatorSet.playTogether(mainAnimator, buttonAnimators);

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
        animatorSet.start();
    }

    @Override
    public void end() {

    }


    private Animator centeredRotation(View child, int fromDegree, int toDegree, int duration){
        final Animator animation = ObjectAnimator.ofFloat(child , "rotation", fromDegree, toDegree);
        animation.setInterpolator(getMainInterpolator());
        animation.setDuration(duration);
        return animation;
    }
}
