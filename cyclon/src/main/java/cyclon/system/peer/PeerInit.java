package cyclon.system.peer;

import common.configuration.CyclonConfiguration;
import common.peer.PeerAddress;
import se.sics.kompics.Init;
import se.sics.kompics.p2p.bootstrap.BootstrapConfiguration;

public final class PeerInit extends Init {

	private final PeerAddress peerSelf;
	private final int num;
	private final BootstrapConfiguration bootstrapConfiguration;
	private final CyclonConfiguration cyclonConfiguration;

//-------------------------------------------------------------------	
	public PeerInit(PeerAddress peerSelf, int num, BootstrapConfiguration bootstrapConfiguration, CyclonConfiguration cyclonConfiguration) {
		super();
		this.peerSelf = peerSelf;
		this.num = num;
		this.bootstrapConfiguration = bootstrapConfiguration;
		this.cyclonConfiguration = cyclonConfiguration;
	}

//-------------------------------------------------------------------	
	public PeerAddress getPeerSelf() {
		return this.peerSelf;
	}

//-------------------------------------------------------------------	
	public int getNum() {
		return this.num;
	}

//-------------------------------------------------------------------	
	public BootstrapConfiguration getBootstrapConfiguration() {
		return this.bootstrapConfiguration;
	}

//-------------------------------------------------------------------	
	public CyclonConfiguration getCyclonConfiguration() {
		return this.cyclonConfiguration;
	}
}
