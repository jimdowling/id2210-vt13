package common.simulation;

import java.io.Serializable;
import se.sics.kompics.Event;

public final class PeerJoin extends Event implements Serializable{

    private final Long peerId;

//-------------------------------------------------------------------	
    public PeerJoin(Long peerId) {
        this.peerId = peerId;
    }

//-------------------------------------------------------------------	
    public Long getPeerId() {
        return this.peerId;
    }
}
