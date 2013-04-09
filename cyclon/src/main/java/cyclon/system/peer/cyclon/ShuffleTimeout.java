package cyclon.system.peer.cyclon;

import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timeout;

import common.peer.PeerAddress;

public class ShuffleTimeout extends Timeout {

	private final PeerAddress peer;

//-------------------------------------------------------------------
	public ShuffleTimeout(ScheduleTimeout request, PeerAddress peer) {
		super(request);
		this.peer = peer;
	}

//-------------------------------------------------------------------
	public PeerAddress getPeer() {
		return peer;
	}
}
