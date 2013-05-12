package search.system.peer.search;

import common.configuration.SearchConfiguration;
import se.sics.kompics.Init;
import se.sics.kompics.address.Address;

public final class SearchInit extends Init {

	private final Address peerSelf;
	private final SearchConfiguration configuration;

//-------------------------------------------------------------------
	public SearchInit(Address peerSelf, SearchConfiguration configuration) {
		super();
		this.peerSelf = peerSelf;
		this.configuration = configuration;
	}

//-------------------------------------------------------------------
	public Address getSelf() {
		return this.peerSelf;
	}

//-------------------------------------------------------------------
	public SearchConfiguration getConfiguration() {
		return this.configuration;
	}
}