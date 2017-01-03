package nl.rdewildt.addone.fam.animation;

import android.animation.ObjectAnimator;
import android.view.View;

import nl.rdewildt.addone.fam.FloatingActionMenu;

/**
 * Created by roydewildt on 02/01/2017.
 */

public class FadeOut extends Fade {
    public FadeOut(FloatingActionMenu floatingActionMenu) {
        super(floatingActionMenu, 1.0f, 0.0f);
    }
}
