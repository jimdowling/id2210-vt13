package search.system.peer.search;

import common.configuration.SearchConfiguration;
import se.sics.kompics.Init;
import se.sics.kompics.address.Address;

public final class SearchInit extends Init {

	private final Address peerSelf;
	private final int num;
	private final SearchConfiguration configuration;

//-------------------------------------------------------------------
	public SearchInit(Address peerSelf, int num, SearchConfiguration configuration) {
		super();
		this.peerSelf = peerSelf;
		this.num = num;
		this.configuration = configuration;
	}

//-------------------------------------------------------------------
	public Address getSelf() {
		return this.peerSelf;
	}

//-------------------------------------------------------------------
	public int getNum() {
		return this.num;
	}

//-------------------------------------------------------------------
	public SearchConfiguration getConfiguration() {
		return this.configuration;
	}
}