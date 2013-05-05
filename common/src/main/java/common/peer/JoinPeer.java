package common.peer;

import se.sics.kompics.Event;

public class JoinPeer extends Event {

	private final Long peerId;

//-------------------------------------------------------------------
	public JoinPeer(Long peerId) {
		this.peerId = peerId;
	}

//-------------------------------------------------------------------
	public Long getPeerId() {
		return this.peerId;
	}
}