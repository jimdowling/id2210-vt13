package cyclon.system.peer.cyclon;

import java.util.LinkedList;

import se.sics.kompics.Event;
import se.sics.kompics.address.Address;


public final class CyclonJoin extends Event {
	private final Address self;
	private final LinkedList<Address> cyclonInsiders;

//-------------------------------------------------------------------
	public CyclonJoin(Address self, LinkedList<Address> cyclonInsiders) {
		super();
		this.self = self;
		this.cyclonInsiders = cyclonInsiders;
	}

//-------------------------------------------------------------------
	public final Address getSelf() {
		return self;
	}

	//-------------------------------------------------------------------
	public LinkedList<Address> getCyclonInsiders() {
		return cyclonInsiders;
	}
}
