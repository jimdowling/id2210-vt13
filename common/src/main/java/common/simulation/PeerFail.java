package common.simulation;

import java.math.BigInteger;

import se.sics.kompics.Event;

public final class PeerFail extends Event {

	private final BigInteger cyclonId;

//-------------------------------------------------------------------	
	public PeerFail(BigInteger cyclonId) {
		this.cyclonId = cyclonId;
	}

//-------------------------------------------------------------------	
	public BigInteger getCyclonId() {
		return cyclonId;
	}
}
