package tman.system.peer.tman;

import java.util.UUID;

import java.util.List;
import se.sics.kompics.address.Address;
import se.sics.kompics.network.Message;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timeout;

public class ExchangeMsg {

    public static class Request extends Message {

        private static final long serialVersionUID = 8493601671018888143L;
        private final UUID requestId;
        private final List<Address> selectedBuffer;

//-------------------------------------------------------------------
        public Request(UUID requestId, List<Address> randomBuffer, Address source, 
                Address destination) {
            super(source, destination);
            this.requestId = requestId;
            this.selectedBuffer = randomBuffer;
        }

//-------------------------------------------------------------------
        public UUID getRequestId() {
            return requestId;
        }

        //-------------------------------------------------------------------
        public List<Address> getSelectedBuffer() {
            return selectedBuffer;
        }

//-------------------------------------------------------------------
        public int getSize() {
            return 0;
        }
    }

    public static class Response extends Message {

        private static final long serialVersionUID = -5022051054665787770L;
        private final UUID requestId;
        private final List<Address> selectedBuffer;

//-------------------------------------------------------------------
        public Response(UUID requestId, List<Address> selectedBuffer, Address source, Address destination) {
            super(source, destination);
            this.requestId = requestId;
            this.selectedBuffer = selectedBuffer;
        }

//-------------------------------------------------------------------
        public UUID getRequestId() {
            return requestId;
        }

//-------------------------------------------------------------------
        public List<Address> getSelectedBuffer() {
            return selectedBuffer;
        }

//-------------------------------------------------------------------
        public int getSize() {
            return 0;
        }
    }

    public static class RequestTimeout extends Timeout {

        private final Address peer;

//-------------------------------------------------------------------
        public RequestTimeout(ScheduleTimeout request, Address peer) {
            super(request);
            this.peer = peer;
        }

//-------------------------------------------------------------------
        public Address getPeer() {
            return peer;
        }
    }
}