package nl.rdewildt.addone.fam.animation;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

import nl.rdewildt.addone.fam.FloatingActionMenu;

/**
 * Created by roydewildt on 02/01/2017.
 */

public abstract class FamAnimation {
    static final int ANIM_DURATION = 600;
    static final Interpolator ANIM_MAIN_INTERPOLATOR = new LinearInterpolator();
    static final Interpolator ANIM_CHILD_INTERPOLATOR = new LinearInterpolator();

    private FloatingActionMenu floatingActionMenu;
    private List<FamAnimationListener> famAnimationListeners;

    private int duration = -1;
    private Interpolator mainInterpolator;
    private Interpolator childInterpolator;

    public FamAnimation(FloatingActionMenu floatingActionMenu){
        this.floatingActionMenu = floatingActionMenu;
        this.famAnimationListeners = new ArrayList<>();
    }

    public abstract void start();

    public abstract void end();

    public FloatingActionMenu getFloatingActionMenu() {
        return floatingActionMenu;
    }

    public FamAnimation setDuration(int duration){
        this.duration = duration;
        return this;
    }

    public int getDuration(){
        return duration != -1 ? duration : ANIM_DURATION;
    }

    public FamAnimation setMainInterpolator(Interpolator interpolator){
        this.mainInterpolator = interpolator;
        return this;
    }

    public Interpolator getMainInterpolator(){
        return mainInterpolator != null ? mainInterpolator : ANIM_MAIN_INTERPOLATOR;
    }

    public FamAnimation setChildInterpolator(Interpolator interpolator){
        this.childInterpolator = interpolator;
        return this;
    }

    public Interpolator getChildInterpolator(){
        return childInterpolator != null ? childInterpolator : ANIM_CHILD_INTERPOLATOR;
    }

    /*
     *  Fam Animation Listener
     */

    public interface FamAnimationListener{
        void onStart();
        void onEnd();
    }

    public void addFamAnimationListener(FamAnimationListener f){
        famAnimationListeners.add(f);
    }

    public void onFamAnimationStart(){
        for(FamAnimationListener f : famAnimationListeners){
            f.onStart();
        }
    }

    public void onFamAnimationEnd(){
        for(FamAnimationListener f : famAnimationListeners){
            f.onEnd();
        }
    }
}
