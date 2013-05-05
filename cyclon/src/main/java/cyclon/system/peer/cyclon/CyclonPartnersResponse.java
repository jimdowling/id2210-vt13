package cyclon.system.peer.cyclon;

import java.util.ArrayList;


import se.sics.kompics.Event;
import se.sics.kompics.address.Address;


public class CyclonPartnersResponse extends Event {
	ArrayList<Address> partners = new ArrayList<Address>();

//-------------------------------------------------------------------
	public CyclonPartnersResponse(ArrayList<Address> partners) {
		this.partners = partners;
	}
        
	public CyclonPartnersResponse() {
	}

//-------------------------------------------------------------------
	public ArrayList<Address> getPartners() {
		return this.partners;
	}
}
