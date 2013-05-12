package common.simulation;


import java.io.Serializable;
import se.sics.kompics.Event;

public final class PeerFail extends Event implements Serializable {

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
