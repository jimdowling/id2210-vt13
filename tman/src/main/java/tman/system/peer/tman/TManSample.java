package tman.system.peer.tman;

import java.util.ArrayList;


import se.sics.kompics.Event;
import se.sics.kompics.address.Address;


public class TManSample extends Event {
	ArrayList<Address> partners = new ArrayList<Address>();

//-------------------------------------------------------------------
	public TManSample(ArrayList<Address> partners) {
		this.partners = partners;
	}
        
	public TManSample() {
	}

//-------------------------------------------------------------------
	public ArrayList<Address> getSample() {
		return this.partners;
	}
}
