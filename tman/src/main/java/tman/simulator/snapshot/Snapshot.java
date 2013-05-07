package tman.simulator.snapshot;

import java.util.ArrayList;
import java.util.TreeMap;
import se.sics.kompics.address.Address;


public class Snapshot {
	private static TreeMap<Address, PeerInfo> peers = new TreeMap<Address, PeerInfo>();
	private static int counter = 0;
	private static String FILENAME = "tman.out";

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
	public static void updateTManPartners(Address address, ArrayList<Address> partners) {
		PeerInfo peerInfo = peers.get(address);
		
		if (peerInfo == null)
			return;
		
		peerInfo.updateTManPartners(partners);
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
		Address[] peersList = new Address[peers.size()];
		peers.keySet().toArray(peersList);
		
		String str = new String();
		str += "current time: " + counter++ + "\n";
		str += reportNetworkState();
		str += reportDetails();
		str += "###\n";
		
		System.out.println(str);
		FileIO.append(str, FILENAME);
	}

//-------------------------------------------------------------------
	private static String reportNetworkState() {
		String str = new String("---\n");
		int totalNumOfPeers = peers.size() - 1;
		str += "total number of peers: " + totalNumOfPeers + "\n";

		return str;		
	}
	
//-------------------------------------------------------------------
	private static String reportDetails() {
		PeerInfo peerInfo;
		String str = new String("---\n");

		for (Address peer : peers.keySet()) {
			peerInfo = peers.get(peer);
		
			str += "peer: " + peer;
			str += ", cyclon parters: " + peerInfo.getCyclonPartners();
			str += ", tman parters: " + peerInfo.getTManPartners();
			str += "\n";
		}
		
		return str;
	}
	

}
