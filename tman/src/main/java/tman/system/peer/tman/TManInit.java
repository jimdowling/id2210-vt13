package tman.system.peer.tman;

import common.configuration.TManConfiguration;
import se.sics.kompics.Init;
import se.sics.kompics.address.Address;

public final class TManInit extends Init {

	private final Address peerSelf;
	private final TManConfiguration configuration;

//-------------------------------------------------------------------
	public TManInit(Address peerSelf, TManConfiguration configuration) {
		super();
		this.peerSelf = peerSelf;

		this.configuration = configuration;
	}

//-------------------------------------------------------------------
	public Address getSelf() {
		return this.peerSelf;
	}

//-------------------------------------------------------------------
	public TManConfiguration getConfiguration() {
		return this.configuration;
	}
}