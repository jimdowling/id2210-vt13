package common.simulation;


import se.sics.kompics.Event;

public final class PeerFail extends Event {

	private final Long id;

//-------------------------------------------------------------------	
	public PeerFail(Long id) {
		this.id = id;
	}

//-------------------------------------------------------------------	
	public Long getId() {
		return id;
	}
}
