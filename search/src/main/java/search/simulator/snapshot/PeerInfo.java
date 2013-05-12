package search.simulator.snapshot;

import java.util.ArrayList;
import se.sics.kompics.address.Address;

public class PeerInfo {

    private int numIndexEntries;
    private ArrayList<Address> neighbours;

//-------------------------------------------------------------------
    public PeerInfo() {
        this.neighbours = new ArrayList<Address>();
        this.numIndexEntries = 0;
    }

    public synchronized int getNumIndexEntries() {
        return numIndexEntries;
    }

    public synchronized void incNumIndexEntries() {
        this.numIndexEntries++;
    }

    public synchronized void setNeighbours(ArrayList<Address> partners) {
        this.neighbours = partners;
    }

    public synchronized ArrayList<Address> getNeighbours() {
        return new ArrayList<Address>(neighbours);
    }
}