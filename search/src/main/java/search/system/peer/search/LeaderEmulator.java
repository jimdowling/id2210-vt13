/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package search.system.peer.search;

/**
 *
 * @author jdowling
 */
public class LeaderEmulator {
    
    private static int indexId = 0;

    public synchronized static int incIndexId() {
        return ++indexId;
    }
    
    public synchronized static int getIndexId() {
        return indexId;
    }
    
    
}
