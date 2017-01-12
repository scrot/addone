package nl.rdewildt.addone.fam;

import nl.rdewildt.addone.fam.animation.Accordion;
import nl.rdewildt.addone.fam.animation.Fade;
import nl.rdewildt.addone.fam.animation.FamAnimation;
import nl.rdewildt.addone.fam.animation.FamAnimationFactory;

/**
 * Created by roydewildt on 02/01/2017.
 */

public class DefaultFamAnimationFactory implements FamAnimationFactory {
    @Override
    public FamAnimation show(FloatingActionMenu fam) {
        return new Fade(fam, 0f, 1f);
    }

    @Override
    public FamAnimation hide(FloatingActionMenu fam) {
        return new Fade(fam, 1f, 0f);
    }

    @Override
    public FamAnimation expand(FloatingActionMenu fam) {
        return new Accordion(fam, 0, 135, Accordion.DIRECTION_UP);
    }

    @Override
    public FamAnimation collapse(FloatingActionMenu fam) {
        return new Accordion(fam, 135, 0, Accordion.DIRECTION_DOWN);
    }
}
