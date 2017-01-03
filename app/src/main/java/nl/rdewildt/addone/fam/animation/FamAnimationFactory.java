package nl.rdewildt.addone.fam.animation;

import nl.rdewildt.addone.fam.FloatingActionMenu;

/**
 * Created by roydewildt on 02/01/2017.
 */

public interface FamAnimationFactory {
    FamAnimation show(FloatingActionMenu fam);
    FamAnimation hide(FloatingActionMenu fam);
    FamAnimation expand(FloatingActionMenu fam);
    FamAnimation collapse(FloatingActionMenu fam);
}
