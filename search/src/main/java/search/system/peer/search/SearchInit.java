package search.system.peer.search;

import common.configuration.SearchConfiguration;
import common.peer.PeerAddress;
import se.sics.kompics.Init;

public final class SearchInit extends Init {

	private final PeerAddress peerSelf;
	private final int num;
	private final SearchConfiguration configuration;

//-------------------------------------------------------------------
	public SearchInit(PeerAddress peerSelf, int num, SearchConfiguration configuration) {
		super();
		this.peerSelf = peerSelf;
		this.num = num;
		this.configuration = configuration;
	}

//-------------------------------------------------------------------
	public PeerAddress getSelf() {
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