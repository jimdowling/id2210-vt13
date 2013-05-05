package search.simulator.snapshot;

import java.util.ArrayList;
import se.sics.kompics.address.Address;


public class PeerInfo {
	private double num;
	private ArrayList<Address> cyclonPartners;

//-------------------------------------------------------------------
	public PeerInfo() {
		this.cyclonPartners = new ArrayList<Address>();
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
	public void updateCyclonPartners(ArrayList<Address> partners) {
		this.cyclonPartners = partners;
	}

//-------------------------------------------------------------------
	public double getNum() {
		return this.num;
	}

//-------------------------------------------------------------------
	public ArrayList<Address> getCyclonPartners() {
		return this.cyclonPartners;
	}
}
