package tman.system.peer.tman;

import java.util.ArrayList;

import common.peer.PeerAddress;

import se.sics.kompics.Event;


public class TManSample extends Event {
	ArrayList<PeerAddress> partners = new ArrayList<PeerAddress>();

//-------------------------------------------------------------------
	public TManSample(ArrayList<PeerAddress> partners) {
		this.partners = partners;
	}
        
	public TManSample() {
	}

//-------------------------------------------------------------------
	public ArrayList<PeerAddress> getSample() {
		return this.partners;
	}
}
