package common.peer;


import se.sics.kompics.address.Address;
import se.sics.kompics.p2p.overlay.OverlayAddress;

public final class PeerAddress extends OverlayAddress implements Comparable<PeerAddress> {
	private static final long serialVersionUID = -7582889514221620065L;

//-------------------------------------------------------------------
	public PeerAddress(Address address) {
		super(address);
	}

//-------------------------------------------------------------------
	@Override
	public int compareTo(PeerAddress that) {
            if (id() > that.getPeerAddress().getId()) {
                return 1;
           } 
            return -1;
	}

        private int id() {
            return this.getPeerAddress().getId();
        }
        
//-------------------------------------------------------------------
	@Override
	public String toString() {
		return Integer.toString(id());
	}

//-------------------------------------------------------------------
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ (id() );
		return result;
	}

//-------------------------------------------------------------------
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PeerAddress other = (PeerAddress) obj;
		if (id() != other.id())
			return false;
		return true;
	}
}
