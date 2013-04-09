package cyclon.system.peer.cyclon;


import common.peer.PeerAddress;
import java.io.Serializable;
import java.util.ArrayList;


public class DescriptorBuffer implements Serializable {
	private static final long serialVersionUID = -4414783055393007206L;
	private final PeerAddress from;
	private final ArrayList<PeerDescriptor> descriptors;

//-------------------------------------------------------------------
	public DescriptorBuffer(PeerAddress from,
			ArrayList<PeerDescriptor> descriptors) {
		super();
		this.from = from;
		this.descriptors = descriptors;
	}

//-------------------------------------------------------------------
	public PeerAddress getFrom() {
		return from;
	}

//-------------------------------------------------------------------
	public int getSize() {
		return descriptors.size();
	}

//-------------------------------------------------------------------
	public ArrayList<PeerDescriptor> getDescriptors() {
		return descriptors;
	}
}
