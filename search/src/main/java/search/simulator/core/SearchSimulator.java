package search.simulator.core;

import common.simulation.SimulatorPort;
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
import search.simulator.snapshot.Snapshot;
import common.configuration.SearchConfiguration;
import common.configuration.Configuration;
import common.configuration.CyclonConfiguration;
import common.simulation.AddIndexEntry;
import common.simulation.ConsistentHashtable;
import common.simulation.GenerateReport;
import common.simulation.PeerFail;
import common.simulation.PeerJoin;
import common.simulation.SimulatorInit;
import java.net.InetAddress;
import java.util.Random;
import se.sics.ipasdistances.AsIpGenerator;
import se.sics.kompics.Negative;
import se.sics.kompics.web.Web;
import se.sics.kompics.web.WebRequest;
import se.sics.kompics.web.WebResponse;
import search.system.peer.AddIndexText;
import search.system.peer.IndexPort;

public final class SearchSimulator extends ComponentDefinition {

    Positive<SimulatorPort> simulator = positive(SimulatorPort.class);
    Positive<Network> network = positive(Network.class);
    Positive<Timer> timer = positive(Timer.class);
    Negative<Web> webIncoming = negative(Web.class);
    private final HashMap<Long, Component> peers;
    private final HashMap<Long, Address> peersAddress;
    private BootstrapConfiguration bootstrapConfiguration;
    private CyclonConfiguration cyclonConfiguration;
    private SearchConfiguration searchConfiguration;
    private Long identifierSpaceSize;
    private ConsistentHashtable<Long> ringNodes;
    private AsIpGenerator ipGenerator = AsIpGenerator.getInstance(125);
    
    static String[] articles = {" ", "The ", "A "};
    static String[] verbs = {"fires ", "walks ", "talks ", "types ", "programs "};
    static String[] subjects = {"computer ", "Lucene ", "torrent "};
    static String[] objects = {"computer", "java", "video"};
    Random r = new Random(System.currentTimeMillis());
    
    
//-------------------------------------------------------------------	
    public SearchSimulator() {
        peers = new HashMap<Long, Component>();
        peersAddress = new HashMap<Long, Address>();
        ringNodes = new ConsistentHashtable<Long>();

        subscribe(handleInit, control);
        subscribe(handleGenerateReport, timer);
        subscribe(handlePeerJoin, simulator);
        subscribe(handlePeerFail, simulator);
        subscribe(handleAddIndexEntry, simulator);
        subscribe(handleWebRequest, webIncoming);
    }
//-------------------------------------------------------------------	
    Handler<SimulatorInit> handleInit = new Handler<SimulatorInit>() {
        @Override
        public void handle(SimulatorInit init) {
            peers.clear();

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
    Handler<WebRequest> handleWebRequest = new Handler<WebRequest>() {
        @Override
        public void handle(WebRequest event) {
            // Find closest peer and send web request on to it.
            long peerId = ringNodes.getNode((long) event.getDestination());
            Component peer = peers.get(peerId);
            trigger(event, peer.getPositive(Web.class));
        }
    };
    
    Handler<WebResponse> handleWebResponse = new Handler<WebResponse>() {
        @Override
        public void handle(WebResponse event) {
           trigger(event, webIncoming);
        }
    };
    
//-------------------------------------------------------------------	
    Handler<AddIndexEntry> handleAddIndexEntry = new Handler<AddIndexEntry>() {
        @Override
        public void handle(AddIndexEntry event) {
            Long successor = ringNodes.getNode(event.getId());
            Component peer = peers.get(successor);
            trigger(new AddIndexText(randomText()), peer.getNegative(IndexPort.class));
        }
    };
//-------------------------------------------------------------------	
    Handler<PeerJoin> handlePeerJoin = new Handler<PeerJoin>() {
        @Override
        public void handle(PeerJoin event) {
            Long id = event.getPeerId();

            // join with the next id if this id is taken
            Long successor = ringNodes.getNode(id);

            while (successor != null && successor.equals(id)) {
                id = (id +1) % identifierSpaceSize;
                successor = ringNodes.getNode(id);
            }

            createAndStartNewPeer(id);
            ringNodes.addNode(id);
        }
    };
//-------------------------------------------------------------------	
    Handler<PeerFail> handlePeerFail = new Handler<PeerFail>() {
        @Override
        public void handle(PeerFail event) {
            Long id = ringNodes.getNode(event.getId());

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
        @Override
        public void handle(GenerateReport event) {
            Snapshot.report();
        }
    };

//-------------------------------------------------------------------	
    private void createAndStartNewPeer(long id) {
        Component peer = create(SearchPeer.class);
        InetAddress ip = ipGenerator.generateIP();
        Address address = new Address(ip, 8058, (int) id);

        connect(network, peer.getNegative(Network.class), new MessageDestinationFilter(address));
        connect(timer, peer.getNegative(Timer.class));
//        connect(webIncoming, peer.getPositive(Web.class));
        subscribe(handleWebResponse, peer.getPositive(Web.class));
        
        trigger(new SearchPeerInit(address, bootstrapConfiguration, cyclonConfiguration, searchConfiguration), peer.getControl());

        trigger(new Start(), peer.getControl());
        peers.put(id, peer);
        peersAddress.put(id, address);
        Snapshot.addPeer(address);
    }

//-------------------------------------------------------------------	
    private void stopAndDestroyPeer(Long id) {
        Component peer = peers.get(id);

        trigger(new Stop(), peer.getControl());

        disconnect(network, peer.getNegative(Network.class));
        disconnect(timer, peer.getNegative(Timer.class));

        peers.remove(id);
        Address addr = peersAddress.remove(id);
        Snapshot.removePeer(addr);

        destroy(peer);
    }

//-------------------------------------------------------------------	
    private final static class MessageDestinationFilter extends ChannelFilter<Message, Address> {

        public MessageDestinationFilter(Address address) {
            super(Message.class, address, true);
        }

        @Override
        public Address getValue(Message event) {
            return event.getDestination();
        }
    }
}
