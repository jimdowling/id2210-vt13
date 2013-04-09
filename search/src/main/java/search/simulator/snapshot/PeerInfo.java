package search.simulator.snapshot;

import java.util.ArrayList;

import common.peer.PeerAddress;

public class PeerInfo {
	private double num;
	private ArrayList<PeerAddress> cyclonPartners;

//-------------------------------------------------------------------
	public PeerInfo() {
		this.cyclonPartners = new ArrayList<PeerAddress>();
	}

//-------------------------------------------------------------------
	public void updateNum(double num) {
		this.num = num;
	}

//-------------------------------------------------------------------
	public void updateNum(int num) {
		this.num = num;
	}

//-------------------------------------------------------------------
	public void updateCyclonPartners(ArrayList<PeerAddress> partners) {
		this.cyclonPartners = partners;
	}

//-------------------------------------------------------------------
	public double getNum() {
		return this.num;
	}

//-------------------------------------------------------------------
	public ArrayList<PeerAddress> getCyclonPartners() {
		return this.cyclonPartners;
	}
}
