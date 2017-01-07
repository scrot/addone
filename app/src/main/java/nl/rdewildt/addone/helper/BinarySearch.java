package nl.rdewildt.addone.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by roydewildt on 23/12/2016.
 */

public class BinarySearch {

    public static <T extends Comparable<T>> int binaryInsertIndex(T x, List<T> xs){
        if(xs.isEmpty()){
            return 0;
        }

        int start = 0;
        int end = xs.size() - 1;

        while(start != end) {
            int middle = (end - start) / 2 + start;
            T y = xs.get(middle);

            if(x.compareTo(y) == 1){
                start = middle + 1;
            }
            else {
                end = middle;
            }
        }

        T y = xs.get(start);
        if(x.compareTo(y) == -1){
            return start;
        }
        else {
            return start + 1;
        }
    }

    public static <T extends Comparable<T>> int binarySearchClosestIndex(T key, List<T> a) {
        if(a.isEmpty()){
            return -1;
        }
        int lo = 0;
        int hi = a.size() - 1;
        int mid = 0;
        while (lo != hi) {
            mid = lo + (hi - lo) / 2;
            if      (key.compareTo(a.get(mid)) == -1){
                hi = mid - 1;
            }
            else if (key.compareTo(a.get(mid)) == 1){
                lo = mid + 1;
            }
        }
        return mid;
    }
}
