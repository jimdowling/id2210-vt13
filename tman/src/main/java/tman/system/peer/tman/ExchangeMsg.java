package tman.system.peer.tman;

import java.util.UUID;

import common.peer.PeerMessage;
import common.peer.PeerAddress;
import cyclon.system.peer.cyclon.DescriptorBuffer;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timeout;

public class ExchangeMsg {

    public static class Request extends PeerMessage {

        private static final long serialVersionUID = 8493601671018888143L;
        private final UUID requestId;
        private final DescriptorBuffer randomBuffer;

//-------------------------------------------------------------------
        public Request(UUID requestId, DescriptorBuffer randomBuffer, PeerAddress source, PeerAddress destination) {
            super(source, destination);
            this.requestId = requestId;
            this.randomBuffer = randomBuffer;
        }

//-------------------------------------------------------------------
        public UUID getRequestId() {
            return requestId;
        }

        //-------------------------------------------------------------------
        public DescriptorBuffer getRandomBuffer() {
            return randomBuffer;
        }

//-------------------------------------------------------------------
        public int getSize() {
            return 0;
        }
    }

    public static class Response extends PeerMessage {

        private static final long serialVersionUID = -5022051054665787770L;
        private final UUID requestId;
        private final DescriptorBuffer selectedBuffer;

//-------------------------------------------------------------------
        public Response(UUID requestId, DescriptorBuffer selectedBuffer, PeerAddress source, PeerAddress destination) {
            super(source, destination);
            this.requestId = requestId;
            this.selectedBuffer = selectedBuffer;
        }

//-------------------------------------------------------------------
        public UUID getRequestId() {
            return requestId;
        }

//-------------------------------------------------------------------
        public DescriptorBuffer getSelectedBuffer() {
            return selectedBuffer;
        }

//-------------------------------------------------------------------
        public int getSize() {
            return 0;
        }
    }

    public static class RequestTimeout extends Timeout {

        private final PeerAddress peer;

//-------------------------------------------------------------------
        public RequestTimeout(ScheduleTimeout request, PeerAddress peer) {
            super(request);
            this.peer = peer;
        }

//-------------------------------------------------------------------
        public PeerAddress getPeer() {
            return peer;
        }
    }
}