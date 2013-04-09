package cyclon.system.peer.cyclon;

import java.util.ArrayList;

import common.peer.PeerAddress;

import se.sics.kompics.Event;


public class CyclonSample extends Event {
	ArrayList<PeerAddress> nodes = new ArrayList<PeerAddress>();

//-------------------------------------------------------------------
	public CyclonSample(ArrayList<PeerAddress> nodes) {
		this.nodes = nodes;
	}
        
	public CyclonSample() {
	}

//-------------------------------------------------------------------
	public ArrayList<PeerAddress> getSample() {
		return this.nodes;
	}
}
