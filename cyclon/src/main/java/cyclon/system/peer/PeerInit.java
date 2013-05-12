package cyclon.system.peer;

import common.configuration.CyclonConfiguration;
import se.sics.kompics.Init;
import se.sics.kompics.address.Address;
import se.sics.kompics.p2p.bootstrap.BootstrapConfiguration;

public final class PeerInit extends Init {

	private final Address peerSelf;
	private final BootstrapConfiguration bootstrapConfiguration;
	private final CyclonConfiguration cyclonConfiguration;

//-------------------------------------------------------------------	
	public PeerInit(Address peerSelf, BootstrapConfiguration bootstrapConfiguration, CyclonConfiguration cyclonConfiguration) {
		super();
		this.peerSelf = peerSelf;
		this.bootstrapConfiguration = bootstrapConfiguration;
		this.cyclonConfiguration = cyclonConfiguration;
	}

//-------------------------------------------------------------------	
	public Address getPeerSelf() {
		return this.peerSelf;
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
