package search.system.peer.search;

import java.io.Serializable;

/**
 * @jim
 */
public class Range implements Serializable {
    private final int lower;
    private final int upper;
    
    public Range(int lower, int upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public int getLower() {
        return lower;
    }

    public int getUpper() {
        return upper;
    }
}
