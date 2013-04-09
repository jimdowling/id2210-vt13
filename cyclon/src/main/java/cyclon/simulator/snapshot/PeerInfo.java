package cyclon.simulator.snapshot;


import common.peer.PeerAddress;
import java.util.ArrayList;

public class PeerInfo {
	private int selectedTimes = 0;
	private int registerdNodes;
	private ArrayList<PeerAddress> partners;
	
//-------------------------------------------------------------------
	public PeerInfo() {
		this.selectedTimes = 0;
	}

//-------------------------------------------------------------------
	public void updateCyclonPartners(ArrayList<PeerAddress> partners) {
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
	public ArrayList<PeerAddress> getPartners() {
		return this.partners;
	}

//-------------------------------------------------------------------
	public boolean isPartner(PeerAddress peer) {
		for (PeerAddress node : this.partners) {
			if (node.equals(peer))
				return true;
		}
		
		return false;
	}

}