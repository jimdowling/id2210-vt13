package tman.system.peer.tman;

import common.configuration.TManConfiguration;
import common.peer.PeerAddress;
import se.sics.kompics.Init;

public final class TManInit extends Init {

	private final PeerAddress peerSelf;
	private final TManConfiguration configuration;

//-------------------------------------------------------------------
	public TManInit(PeerAddress peerSelf, TManConfiguration configuration) {
		super();
		this.peerSelf = peerSelf;

		this.configuration = configuration;
	}

//-------------------------------------------------------------------
	public PeerAddress getSelf() {
		return this.peerSelf;
	}

//-------------------------------------------------------------------
	public TManConfiguration getConfiguration() {
		return this.configuration;
	}
}