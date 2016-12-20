package nl.rdewildt.addone.helper;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by roydewildt on 06/12/2016.
 */

public class DpConverter {
    public static int dpToPx(int dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static int pxToDp(int px){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return px / (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
