package search.simulator.snapshot;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import se.sics.kompics.address.Address;

public class Snapshot {

    private static ConcurrentHashMap<Address, PeerInfo> peers = 
            new ConcurrentHashMap<Address, PeerInfo>();
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
    public static void incNumIndexEntries(Address address) {
        PeerInfo peerInfo = peers.get(address);

        if (peerInfo == null) {
            return;
        }

        peerInfo.incNumIndexEntries();
    }

//-------------------------------------------------------------------
    public static void updateNeighbours(Address address, ArrayList<Address> partners) {
        PeerInfo peerInfo = peers.get(address);

        if (peerInfo == null) {
            return;
        }

        peerInfo.setNeighbours(partners);
    }

//-------------------------------------------------------------------
    public static void report() {
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
        String str = "---\n";
        int totalNumOfPeers = peers.size();
        str += "total number of peers: " + totalNumOfPeers + "\n";

        return str;
    }

//-------------------------------------------------------------------
    private static String reportDetails() {
        String str = "---\n";
        int maxNumIndexEntries = 0;
        int minNumIndexEntries = Integer.MAX_VALUE;
        for (PeerInfo p : peers.values()) {
            if (p.getNumIndexEntries() < minNumIndexEntries) {
                minNumIndexEntries = p.getNumIndexEntries();
            }
            if (p.getNumIndexEntries() > maxNumIndexEntries) {
                maxNumIndexEntries = p.getNumIndexEntries();
            }
        }
        str += "Peer with max num of index entries: " + maxNumIndexEntries + "\n";
        str += "Peer with min num of index entries: " + minNumIndexEntries + "\n";

        return str;
    }
}
