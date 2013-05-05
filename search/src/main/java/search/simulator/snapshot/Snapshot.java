package search.simulator.snapshot;

import java.util.ArrayList;
import java.util.HashMap;
import se.sics.kompics.address.Address;


public class Snapshot {
	private static HashMap<Address, PeerInfo> peers = new HashMap<Address, PeerInfo>();
	private static int counter = 0;
	private static String FILENAME = "search.out";

//-------------------------------------------------------------------
	public static void init(int numOfStripes) {
		FileIO.write("", FILENAME);
	}

//-------------------------------------------------------------------
	public static void addPeer(Address address) {
		peers.put(address, new PeerInfo());
	}

//-------------------------------------------------------------------
	public static void removePeer(Address address) {
		peers.remove(address);
	}

//-------------------------------------------------------------------
	public static void updateNum(Address address, double num) {
		PeerInfo peerInfo = peers.get(address);
		
		if (peerInfo == null)
			return;
		
		peerInfo.updateNum(num);
	}
	
//-------------------------------------------------------------------
	public static void updateCyclonPartners(Address address, ArrayList<Address> partners) {
		PeerInfo peerInfo = peers.get(address);
		
		if (peerInfo == null)
			return;
		
		peerInfo.updateCyclonPartners(partners);
	}

//-------------------------------------------------------------------
	public static void report() {
            if (counter % 1000 == 0) {
		String str = new String();
		str += "current time: " + counter++ + "\n";
		str += reportNetworkState();
		str += reportDetails();
		str += "###\n";
		
		System.out.println(str);
		FileIO.append(str, FILENAME);
            }
	}

//-------------------------------------------------------------------
	private static String reportNetworkState() {
		String str = new String("---\n");
		int totalNumOfPeers = peers.size();
		str += "total number of peers: " + totalNumOfPeers + "\n";

		return str;		
	}
	
//-------------------------------------------------------------------
	private static String reportDetails() {
		PeerInfo peerInfo;
		String str = new String("---\n");

		return str;
	}


}
