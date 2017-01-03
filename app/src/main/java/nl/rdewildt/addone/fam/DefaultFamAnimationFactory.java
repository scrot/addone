package nl.rdewildt.addone.fam;

import nl.rdewildt.addone.fam.animation.FadeIn;
import nl.rdewildt.addone.fam.animation.FadeOut;
import nl.rdewildt.addone.fam.animation.FamAnimation;
import nl.rdewildt.addone.fam.animation.FamAnimationFactory;
import nl.rdewildt.addone.fam.animation.AccordionCollapse;
import nl.rdewildt.addone.fam.animation.AccordionExpand;

/**
 * Created by roydewildt on 02/01/2017.
 */

public class DefaultFamAnimationFactory implements FamAnimationFactory {
    @Override
    public FamAnimation show(FloatingActionMenu fam) {
        return new FadeIn(fam);
    }

    @Override
    public FamAnimation hide(FloatingActionMenu fam) {
        return new FadeOut(fam);
    }

    @Override
    public FamAnimation expand(FloatingActionMenu fam) {
        return new AccordionExpand(fam);
    }

    @Override
    public FamAnimation collapse(FloatingActionMenu fam) {
        return new AccordionCollapse(fam);
    }
}
