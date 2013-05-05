package cyclon.simulator.snapshot;


import java.util.ArrayList;
import se.sics.kompics.address.Address;

public class PeerInfo {
	private int selectedTimes = 0;
	private int registerdNodes;
	private ArrayList<Address> partners;
	
//-------------------------------------------------------------------
	public PeerInfo() {
		this.selectedTimes = 0;
	}

//-------------------------------------------------------------------
	public void updateCyclonPartners(ArrayList<Address> partners) {
		this.partners = partners;
	}

//-------------------------------------------------------------------
	public int getRegisterdNodes() {
		return this.registerdNodes;
	}

//-------------------------------------------------------------------
	public void incSelectedTimes() {
		this.selectedTimes++;
	}

//-------------------------------------------------------------------
	public int getSelectedTimes() {
		return this.selectedTimes;
	}

//-------------------------------------------------------------------
	public ArrayList<Address> getPartners() {
		return this.partners;
	}

//-------------------------------------------------------------------
	public boolean isPartner(Address peer) {
		for (Address node : this.partners) {
			if (node.equals(peer))
				return true;
		}
		
		return false;
	}

}