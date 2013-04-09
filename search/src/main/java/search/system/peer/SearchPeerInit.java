package search.system.peer;

import common.peer.PeerAddress;
import common.configuration.SearchConfiguration;
import common.configuration.CyclonConfiguration;
import se.sics.kompics.Init;
import se.sics.kompics.p2p.bootstrap.BootstrapConfiguration;

public final class SearchPeerInit extends Init {

	private final PeerAddress peerSelf;
	private final int num;
	private final BootstrapConfiguration bootstrapConfiguration;
	private final CyclonConfiguration cyclonConfiguration;
	private final SearchConfiguration applicationConfiguration;

//-------------------------------------------------------------------	
	public SearchPeerInit(PeerAddress peerSelf, int num, BootstrapConfiguration bootstrapConfiguration, CyclonConfiguration cyclonConfiguration, SearchConfiguration applicationConfiguration) {
		super();
		this.peerSelf = peerSelf;
		this.num = num;
		this.bootstrapConfiguration = bootstrapConfiguration;
		this.cyclonConfiguration = cyclonConfiguration;
		this.applicationConfiguration = applicationConfiguration;
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

//-------------------------------------------------------------------	
	public SearchConfiguration getApplicationConfiguration() {
		return this.applicationConfiguration;
	}

}