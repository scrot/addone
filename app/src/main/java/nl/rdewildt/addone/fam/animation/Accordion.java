package nl.rdewildt.addone.fam.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import nl.rdewildt.addone.fam.FloatingActionMenu;
import nl.rdewildt.addone.fam.FloatingActionMenuButton;

/**
 * Created by roydewildt on 02/01/2017.
 */

public abstract class Accordion extends FamAnimation {
    public static final int DIRECTION_DOWN = 0;
    public static final int DIRECTION_UP = 1;

    private AnimatorSet animatorSet;

    private final int rotateFrom;
    private final int rotateTo;
    private final int translateDirection;

    public Accordion(FloatingActionMenu floatingActionMenu, int rotateFrom, int rotateTo, int translateDirection) {
        super(floatingActionMenu);

        this.rotateFrom = rotateFrom;
        this.rotateTo = rotateTo;
        this.translateDirection = translateDirection;
    }

    @Override
    public void start() {
        animate(rotateFrom, rotateTo, translateDirection);
    }

    @Override
    public void end() {
        if(animatorSet != null){
            animatorSet.end();
        }
    }

    private void animate(int rotateFrom, int rotateTo, int translateDirection){
        animatorSet = new AnimatorSet();
        int count = getFloatingActionMenu().getChildCount();
        int spacing = getFloatingActionMenu().getChildSpacing();
        FloatingActionMenuButton main = getFloatingActionMenu().getFloatingActionMenuMainButton();
        //animate main button
        animatorSet.play(centeredRotation(main, rotateFrom, rotateTo, getDuration()));

        //set child animations
        int pos = getFloatingActionMenu().getFloatingActionMenuMainButton().getMeasuredHeight() + spacing;
        for (int i = 0; i < count; i++) {
            final View child = getFloatingActionMenu().getChildAt(i);
            if (!child.equals(main)) {
                if(translateDirection == DIRECTION_UP) {
                    animatorSet.play(yTranslation(child, pos, 0, getDuration()));
                }
                else if (translateDirection == DIRECTION_DOWN) {
                    animatorSet.play(yTranslation(child, 0, pos, getDuration()));

                }
                pos += child.getMeasuredHeight() + spacing;
            }
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

        animatorSet.start();
    }

    private Animator yTranslation(View child, int fromY, int toY, int duration){
        final Animator animation = ObjectAnimator.ofFloat(child, "translationY", fromY, toY);
        animation.setDuration(duration);
        animation.setInterpolator(getChildInterpolator());
        return animation;
    }

    private Animator centeredRotation(View child, int fromDegree, int toDegree, int duration){
        final Animator animation = ObjectAnimator.ofFloat(child , "rotation", fromDegree, toDegree);
        animation.setInterpolator(getMainInterpolator());
        animation.setDuration(duration);
        return animation;
    }
}
