package search.simulator.core;

import common.configuration.SearchConfiguration;
import common.configuration.CyclonConfiguration;
import common.simulation.SimulatorInit;
import common.simulation.SimulatorPort;
import java.io.IOException;
import java.net.InetAddress;

import se.sics.kompics.ChannelFilter;
import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Kompics;
import se.sics.kompics.address.Address;
import se.sics.kompics.network.Message;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.model.king.KingLatencyMap;
import se.sics.kompics.p2p.bootstrap.BootstrapConfiguration;
import se.sics.kompics.p2p.bootstrap.server.BootstrapServer;
import se.sics.kompics.p2p.bootstrap.server.BootstrapServerInit;
import se.sics.kompics.p2p.experiment.dsl.SimulationScenario;
import se.sics.kompics.p2p.orchestrator.P2pOrchestrator;
import se.sics.kompics.p2p.orchestrator.P2pOrchestratorInit;
import se.sics.kompics.timer.Timer;
import se.sics.kompics.web.Web;
import se.sics.kompics.web.jetty.JettyWebServer;
import se.sics.kompics.web.jetty.JettyWebServerConfiguration;
import se.sics.kompics.web.jetty.JettyWebServerInit;

public final class SearchExecutionMain extends ComponentDefinition {

    private static SimulationScenario scenario = SimulationScenario.load(System.getProperty("scenario"));

//-------------------------------------------------------------------	
    public static void main(String[] args) {
        
        Kompics.createAndStart(SearchExecutionMain.class, 1);
    }

//-------------------------------------------------------------------	
    public SearchExecutionMain() throws IOException {
        P2pOrchestrator.setSimulationPortType(SimulatorPort.class);

        // create
        Component bootstrapServer = create(BootstrapServer.class);
        Component p2pOrchestrator = create(P2pOrchestrator.class);
        Component simulator = create(SearchSimulator.class);
        Component web = create(JettyWebServer.class);


        final BootstrapConfiguration bootConfiguration = BootstrapConfiguration.load(System.getProperty("bootstrap.configuration"));
        final CyclonConfiguration cyclonConfiguration = CyclonConfiguration.load(System.getProperty("cyclon.configuration"));
        final SearchConfiguration searchConfiguration = SearchConfiguration.load(System.getProperty("search.configuration"));

        trigger(new BootstrapServerInit(bootConfiguration), bootstrapServer.getControl());
        trigger(new SimulatorInit(bootConfiguration, cyclonConfiguration, null,
                searchConfiguration), simulator.getControl());

        // connect
        connect(bootstrapServer.getNegative(Network.class), p2pOrchestrator.getPositive(Network.class), new MessageDestinationFilter(bootConfiguration.getBootstrapServerAddress()));
        connect(bootstrapServer.getNegative(Timer.class), p2pOrchestrator.getPositive(Timer.class));
        connect(simulator.getNegative(Network.class), p2pOrchestrator.getPositive(Network.class));
        connect(simulator.getNegative(Timer.class), p2pOrchestrator.getPositive(Timer.class));
        connect(simulator.getNegative(SimulatorPort.class), p2pOrchestrator.getPositive(SimulatorPort.class));
        connect(simulator.getPositive(Web.class), web.getNegative(Web.class));



        InetAddress ip = InetAddress.getLocalHost();
        int webPort = 9999;
        String webServerAddr = "http://" + ip.getHostAddress() + ":" + webPort;
        final JettyWebServerConfiguration webConfiguration =
                new JettyWebServerConfiguration(ip, webPort,
                30 * 1000, 2, webServerAddr);
        trigger(new JettyWebServerInit(webConfiguration), web.getControl());
        System.out.println("Webserver Started. Address=" + webServerAddr + "/1/search");

        // Must init P2pOrchestrator last of all components, otherwise events will be dropped
        trigger(new P2pOrchestratorInit(scenario, new KingLatencyMap()), p2pOrchestrator.getControl());
    
    
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
