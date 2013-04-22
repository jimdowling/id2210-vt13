package search.simulator.core;

import common.simulation.*;

import java.math.BigInteger;
import java.util.HashMap;

import se.sics.kompics.ChannelFilter;
import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Positive;
import se.sics.kompics.Start;
import se.sics.kompics.Stop;
import se.sics.kompics.address.Address;
import se.sics.kompics.network.Message;
import se.sics.kompics.network.Network;
import se.sics.kompics.p2p.bootstrap.BootstrapConfiguration;
import se.sics.kompics.timer.SchedulePeriodicTimeout;
import se.sics.kompics.timer.Timer;

import search.system.peer.SearchPeer;
import search.system.peer.SearchPeerInit;
import common.peer.PeerAddress;
import search.simulator.snapshot.Snapshot;
import common.configuration.SearchConfiguration;
import common.configuration.Configuration;
import common.configuration.CyclonConfiguration;

import java.net.InetAddress;
import java.util.Random;
import se.sics.ipasdistances.AsIpGenerator;
import se.sics.kompics.Negative;
import se.sics.kompics.web.Web;
import search.system.peer.AddIndexText;
import search.system.peer.IndexPort;

public final class SearchSimulator extends ComponentDefinition {

    Positive<SimulatorPort> simulator = positive(SimulatorPort.class);
    Positive<Network> network = positive(Network.class);
    Positive<Timer> timer = positive(Timer.class);
    Negative<Web> webIncoming = negative(Web.class);
    private final HashMap<BigInteger, Component> peers;
    private final HashMap<BigInteger, PeerAddress> peersAddress;
    private BootstrapConfiguration bootstrapConfiguration;
    private CyclonConfiguration cyclonConfiguration;
    private SearchConfiguration searchConfiguration;
    private int peerIdSequence;
    private BigInteger identifierSpaceSize;
    private ConsistentHashtable<BigInteger> ringNodes;
    private AsIpGenerator ipGenerator = AsIpGenerator.getInstance(125);

    static String[] articles = {" ", "The ", "A "};
    static String[] verbs = {"fires ", "walks ", "talks ", "types ", "programs "};
    static String[] subjects = {"computer ", "Lucene ", "torrent "};
    static String[] objects = {"computer", "java", "video"};
    Random r = new Random(System.currentTimeMillis());
    
    
//-------------------------------------------------------------------	
    public SearchSimulator() {
        peers = new HashMap<BigInteger, Component>();
        peersAddress = new HashMap<BigInteger, PeerAddress>();
        ringNodes = new ConsistentHashtable<BigInteger>();

        subscribe(handleInit, control);
        subscribe(handleGenerateReport, timer);
        subscribe(handlePeerJoin, simulator);
        subscribe(handlePeerFail, simulator);
        subscribe(handleAddIndexEntry, simulator);
    }
//-------------------------------------------------------------------	
    Handler<SimulatorInit> handleInit = new Handler<SimulatorInit>() {
        public void handle(SimulatorInit init) {
            peers.clear();
            peerIdSequence = 0;

            bootstrapConfiguration = init.getBootstrapConfiguration();
            cyclonConfiguration = init.getCyclonConfiguration();
            searchConfiguration = init.getAggregationConfiguration();

            identifierSpaceSize = cyclonConfiguration.getIdentifierSpaceSize();

            // generate periodic report
            int snapshotPeriod = Configuration.SNAPSHOT_PERIOD;
            SchedulePeriodicTimeout spt = new SchedulePeriodicTimeout(snapshotPeriod, snapshotPeriod);
            spt.setTimeoutEvent(new GenerateReport(spt));
            trigger(spt, timer);

        }
    };
    
    String randomText() {
        StringBuilder sb = new StringBuilder();
        int clauses = Math.max(1, r.nextInt(3));
        for (int i = 0; i < clauses; i++) {
                sb.append(articles[r.nextInt(articles.length)]);
                sb.append(subjects[r.nextInt(subjects.length)]);
                sb.append(verbs[r.nextInt(verbs.length)]);
                sb.append(objects[r.nextInt(objects.length)]);
                sb.append(". ");
        }
        return sb.toString();
    }
    
//-------------------------------------------------------------------	
    Handler<AddIndexEntry> handleAddIndexEntry = new Handler<AddIndexEntry>() {
        @Override
        public void handle(AddIndexEntry event) {
            BigInteger successor = ringNodes.getNode(event.getId());
            Component peer = peers.get(successor);
            
            trigger(new AddIndexText(randomText()), peer.getNegative(IndexPort.class));
        }
    };
//-------------------------------------------------------------------	
    Handler<PeerJoin> handlePeerJoin = new Handler<PeerJoin>() {
        public void handle(PeerJoin event) {
            int num = event.getNum();
            BigInteger id = event.getPeerId();

            // join with the next id if this id is taken
            BigInteger successor = ringNodes.getNode(id);

            while (successor != null && successor.equals(id)) {
                id = id.add(BigInteger.ONE).mod(identifierSpaceSize);
                successor = ringNodes.getNode(id);
            }

            createAndStartNewPeer(id, num);
            ringNodes.addNode(id);
        }
    };
//-------------------------------------------------------------------	
    Handler<PeerFail> handlePeerFail = new Handler<PeerFail>() {
        public void handle(PeerFail event) {
            BigInteger id = ringNodes.getNode(event.getCyclonId());

            if (ringNodes.size() == 0) {
                System.err.println("Empty network");
                return;
            }

            ringNodes.removeNode(id);
            stopAndDestroyPeer(id);
        }
    };
//-------------------------------------------------------------------	
    Handler<GenerateReport> handleGenerateReport = new Handler<GenerateReport>() {
        public void handle(GenerateReport event) {
            Snapshot.report();
        }
    };

//-------------------------------------------------------------------	
    private final void createAndStartNewPeer(BigInteger id, int num) {
        Component peer = create(SearchPeer.class);
        int peerId = ++peerIdSequence;
        InetAddress ip = ipGenerator.generateIP();
        Address address = new Address(ip, 8058, peerId);
        PeerAddress peerAddress = new PeerAddress(address, id);

        connect(network, peer.getNegative(Network.class), new MessageDestinationFilter(address));
        connect(timer, peer.getNegative(Timer.class));
        connect(peer.getPositive(Web.class), webIncoming); //, new WebDestinationFilter(peerId));

        trigger(new SearchPeerInit(peerAddress, num, bootstrapConfiguration, cyclonConfiguration, searchConfiguration), peer.getControl());

        trigger(new Start(), peer.getControl());
        peers.put(id, peer);
        peersAddress.put(id, peerAddress);

    }

//-------------------------------------------------------------------	
    private final void stopAndDestroyPeer(BigInteger id) {
        Component peer = peers.get(id);

        trigger(new Stop(), peer.getControl());

        disconnect(network, peer.getNegative(Network.class));
        disconnect(timer, peer.getNegative(Timer.class));

        Snapshot.removePeer(peersAddress.get(id));

        peers.remove(id);
        peersAddress.remove(id);

        destroy(peer);
    }

//-------------------------------------------------------------------	
    private final static class MessageDestinationFilter extends ChannelFilter<Message, Address> {

        public MessageDestinationFilter(Address address) {
            super(Message.class, address, true);
        }

        public Address getValue(Message event) {
            return event.getDestination();
        }
    }
}
