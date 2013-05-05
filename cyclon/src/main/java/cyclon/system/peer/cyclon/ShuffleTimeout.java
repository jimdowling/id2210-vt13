package cyclon.system.peer.cyclon;

import se.sics.kompics.address.Address;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timeout;


public class ShuffleTimeout extends Timeout {

	private final Address peer;

//-------------------------------------------------------------------
	public ShuffleTimeout(ScheduleTimeout request, Address peer) {
		super(request);
		this.peer = peer;
	}

//-------------------------------------------------------------------
	public Address getPeer() {
		return peer;
	}
}
