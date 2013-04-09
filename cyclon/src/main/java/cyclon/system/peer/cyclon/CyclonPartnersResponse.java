package cyclon.system.peer.cyclon;

import java.util.ArrayList;

import common.peer.PeerAddress;

import se.sics.kompics.Event;


public class CyclonPartnersResponse extends Event {
	ArrayList<PeerAddress> partners = new ArrayList<PeerAddress>();

//-------------------------------------------------------------------
	public CyclonPartnersResponse(ArrayList<PeerAddress> partners) {
		this.partners = partners;
	}
        
	public CyclonPartnersResponse() {
	}

//-------------------------------------------------------------------
	public ArrayList<PeerAddress> getPartners() {
		return this.partners;
	}
}
