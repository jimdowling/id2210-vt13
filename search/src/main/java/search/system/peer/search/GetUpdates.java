package search.system.peer.search;

import common.peer.PeerAddress;
import common.peer.PeerMessage;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: kazarindn
 * Date: 4/22/13
 * Time: 4:13 PM
 */
public class GetUpdates extends PeerMessage {
    ArrayList<Range> missingRanges;
    int lastExisting;

    public GetUpdates(ArrayList<Range> missingRanges, int lastExisting, PeerAddress source, PeerAddress destination) {
        super(source, destination);
        this.missingRanges = missingRanges;
        this.lastExisting = lastExisting;
    }

    public ArrayList<Range> getMissingRanges() {
        return missingRanges;
    }

    public int getLastExisting() {
        return lastExisting;
    }
}
