package cyclon.system.peer.cyclon;

import common.peer.PeerAddress;
import java.util.LinkedList;

import se.sics.kompics.Event;


public final class CyclonJoin extends Event {
	private final PeerAddress self;
	private final LinkedList<PeerAddress> cyclonInsiders;

//-------------------------------------------------------------------
	public CyclonJoin(PeerAddress self, LinkedList<PeerAddress> cyclonInsiders) {
		super();
		this.self = self;
		this.cyclonInsiders = cyclonInsiders;
	}

//-------------------------------------------------------------------
	public final PeerAddress getSelf() {
		return self;
	}

	//-------------------------------------------------------------------
	public LinkedList<PeerAddress> getCyclonInsiders() {
		return cyclonInsiders;
	}
}
