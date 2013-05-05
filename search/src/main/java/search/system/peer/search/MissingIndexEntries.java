package search.system.peer.search;

import java.util.List;
import se.sics.kompics.address.Address;
import se.sics.kompics.network.Message;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timeout;

/**
 * User: jdowling
 */
public class MissingIndexEntries  {

    public static class Request extends Message {

        List<Range> missingRanges;

        public Request(Address source, Address destination, List<Range> missingRanges) {
            super(source, destination);
            this.missingRanges = missingRanges;
        }

        public List<Range> getMissingRanges() {
            return missingRanges;
        }
    }
    
    public static class Response extends Message {

        private final List<IndexEntry> entries;

        public Response(Address source, Address destination, List<IndexEntry> entries) {
            super(source, destination);
            assert(entries != null);
            this.entries = entries;
        }
        public List<IndexEntry> getEntries() {
            return entries;
        }
    }
    
    public static class RequestTimeout extends Timeout {
        private final Address destination;
        RequestTimeout(ScheduleTimeout st, Address destination) {
            super(st);
            this.destination = destination;
        }

        public Address getDestination() {
            return destination;
        }
    }
}
