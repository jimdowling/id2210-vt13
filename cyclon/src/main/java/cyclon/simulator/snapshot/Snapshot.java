package cyclon.simulator.snapshot;


import java.util.ArrayList;
import java.util.HashMap;
import se.sics.kompics.address.Address;

public class Snapshot {
	private static HashMap<Address, PeerInfo> peers = new HashMap<Address, PeerInfo>();
	private static HashMap<Address, Integer> fanout = new HashMap<Address, Integer>();
	private static HashMap<Address, Integer> fanin = new HashMap<Address, Integer>();
	private static int counter = 0;
	private static String FILENAME = "cyclon.out";
	private static GraphUtil g = new GraphUtil();

//-------------------------------------------------------------------
	public static void init(int numOfStripes) {
		FileIO.write("", FILENAME);
	}

//-------------------------------------------------------------------
	public static void addPeer(Address address) {
		peers.put(address, new PeerInfo());
		fanin.put(address, 0);
		fanout.put(address, 0);
	}

//-------------------------------------------------------------------
	public static void removePeer(Address address) {
		peers.remove(address);
		fanin.remove(address);
		fanout.remove(address);
	}

//-------------------------------------------------------------------
	public static void incSelectedTimes(Address address) {
		PeerInfo peerInfo = peers.get(address);
		
		if (peerInfo == null)
			return;
		
		peerInfo.incSelectedTimes();
	}
	
//-------------------------------------------------------------------
	public static void updateCyclonPartners(Address address, ArrayList<Address> partners) {
		PeerInfo peerInfo = peers.get(address);
		
		if (peerInfo == null)
			return;
		
		peerInfo.updateCyclonPartners(partners);
		fanout.put(address, partners.size());
	}
	
//-------------------------------------------------------------------
	public static void report() {
		String str = new String();
		str += "current time: " + counter++ + "\n";
		str += reportNetworkState();
		str += reportRandomness();
		str += reportFanoutHistogram();
		str += reportFaninHistogram();
		str += reportGraphStat();
		str += reportDetailes();
		str += "###\n";
		
		System.out.println(str);
		FileIO.append(str, FILENAME);
	}

//-------------------------------------------------------------------
	private static String reportNetworkState() {
		String str = new String("---\n");
		str += "total number of peers: " + peers.size() + "\n";
		
		return str;		
	}

//-------------------------------------------------------------------
	private static String reportRandomness() {
		String str = new String("---\n");
		HashMap<Integer, Integer> randomness = new HashMap<Integer, Integer>();

		int selectedTimes;
		Integer count;
		for (PeerInfo info : peers.values()) {
			selectedTimes = info.getSelectedTimes() / 10;
			count = randomness.get(selectedTimes);
			
			if (count == null)
				randomness.put(selectedTimes, 1);
			else
				randomness.put(selectedTimes, count + 1);
		}
		
		str += "global randomness: " + randomness.toString() + "\n";			

		return str;
	}

//-------------------------------------------------------------------
	private static String reportFanoutHistogram() {
		HashMap<Integer, Integer> fanoutHistogram = new HashMap<Integer, Integer>();
		String str = new String("---\n");

		Integer n;
		for (Integer num : fanout.values()) {
			n = fanoutHistogram.get(num);
			
			if (n == null)
				fanoutHistogram.put(num, 1);
			else
				fanoutHistogram.put(num, n + 1);
			
		}
		
		str += "out-degree: " + fanoutHistogram.toString() + "\n";
		
		return str;
	}

//-------------------------------------------------------------------
	private static String reportFaninHistogram() {
		HashMap<Integer, Integer> faninHistogram = new HashMap<Integer, Integer>();
		String str = new String("---\n");

		int count;
		for (Address node : fanin.keySet()) {
			count = 0;
			for (PeerInfo peerInfo : peers.values()) {
				if (peerInfo.getPartners() != null && peerInfo.isPartner(node))
					count++;					
			}
			
			fanin.put(node, count);
		}

		Integer n;
		for (Integer num : fanin.values()) {
			n = faninHistogram.get(num);
			
			if (n == null)
				faninHistogram.put(num, 1);
			else
				faninHistogram.put(num, n + 1);
			
		}
		
		str += "in-degree: " + faninHistogram.toString() + "\n";
		
		return str;
	}
//-------------------------------------------------------------------
	private static String reportDetailes() {
		PeerInfo peerInfo;
		String str = new String("---\n");

		for (Address peer : peers.keySet()) {
			peerInfo = peers.get(peer);		
			str += "peer: " + peer + ", prtners: " + peerInfo.getPartners() + "\n";
		}
		
		return str;
	}

//-------------------------------------------------------------------
	private static String reportGraphStat() {
		String str = new String("---\n");
		double id, od, cc, pl, istd, cs;
		int diameter;
		
		g.init(peers);
		id = g.getMeanInDegree();
		istd = g.getInDegreeStdDev();
		od = g.getMeanOutDegree();
		cc = g.getMeanClusteringCoefficient();
		pl = g.getMeanPathLength();
		cs = g.getMaxClusterSize();
		diameter = g.getDiameter();

		str += "graph statistics: " + "\n";
		str += "Diameter: " + diameter + "\n";
		str += "Average path length: " + String.format("%.4f", pl) + "\n";
		str += "Clustering-coefficient: " + String.format("%.4f", cc) + "\n";
		str += "Average in-degree: " + String.format("%.4f", id) + "\n";
		str += "In-degree standard deviation: " + String.format("%.4f", istd) + "\n";
		str += "Average out-degree: " + String.format("%.4f", od) + "\n";
		str += "Biggest cluster size: " + cs + "\n";
		
		return str;
	}
}