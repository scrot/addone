package nl.rdewildt.addone.fam.animation;

import nl.rdewildt.addone.fam.FloatingActionMenu;

/**
 * Created by roydewildt on 02/01/2017.
 */

//TODO Superclass for collapse and expansion?
public class AccordionCollapse extends Accordion {
    public AccordionCollapse(FloatingActionMenu floatingActionMenu) {
        super(floatingActionMenu, 135, 0, DIRECTION_DOWN);
    }
}
