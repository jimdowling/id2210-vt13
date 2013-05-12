package search.system.peer;

import common.configuration.SearchConfiguration;
import common.configuration.CyclonConfiguration;
import se.sics.kompics.Init;
import se.sics.kompics.address.Address;
import se.sics.kompics.p2p.bootstrap.BootstrapConfiguration;

public final class SearchPeerInit extends Init {

	private final Address peerSelf;
	private final BootstrapConfiguration bootstrapConfiguration;
	private final CyclonConfiguration cyclonConfiguration;
	private final SearchConfiguration applicationConfiguration;

//-------------------------------------------------------------------	
	public SearchPeerInit(Address peerSelf, BootstrapConfiguration bootstrapConfiguration, CyclonConfiguration cyclonConfiguration, SearchConfiguration applicationConfiguration) {
		super();
		this.peerSelf = peerSelf;
		this.bootstrapConfiguration = bootstrapConfiguration;
		this.cyclonConfiguration = cyclonConfiguration;
		this.applicationConfiguration = applicationConfiguration;
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

//-------------------------------------------------------------------	
	public SearchConfiguration getApplicationConfiguration() {
		return this.applicationConfiguration;
	}

}