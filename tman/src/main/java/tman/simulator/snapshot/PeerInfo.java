package tman.simulator.snapshot;

import common.peer.PeerAddress;
import java.util.ArrayList;


public class PeerInfo {
	private ArrayList<PeerAddress> tmanPartners;
	private ArrayList<PeerAddress> cyclonPartners;

//-------------------------------------------------------------------
	public PeerInfo() {
		this.tmanPartners = new ArrayList<PeerAddress>();
		this.cyclonPartners = new ArrayList<PeerAddress>();
	}

//-------------------------------------------------------------------
	public void updateTManPartners(ArrayList<PeerAddress> partners) {
		this.tmanPartners = partners;
	}

//-------------------------------------------------------------------
	public void updateCyclonPartners(ArrayList<PeerAddress> partners) {
		this.cyclonPartners = partners;
	}

//-------------------------------------------------------------------
	public ArrayList<PeerAddress> getTManPartners() {
		return this.tmanPartners;
	}

//-------------------------------------------------------------------
	public ArrayList<PeerAddress> getCyclonPartners() {
		return this.cyclonPartners;
	}
}
