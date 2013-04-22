
package cyclon.simulator.core;

import common.configuration.CyclonConfiguration;
import common.simulation.SimulatorInit;
import common.simulation.SimulatorPort;
import java.io.IOException;

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
import se.sics.kompics.p2p.simulator.P2pSimulator;
import se.sics.kompics.p2p.simulator.P2pSimulatorInit;
import se.sics.kompics.simulation.SimulatorScheduler;
import se.sics.kompics.timer.Timer;

public final class CyclonSimulationMain extends ComponentDefinition {
	private static SimulatorScheduler simulatorScheduler = new SimulatorScheduler();
	private static SimulationScenario scenario = SimulationScenario.load(System.getProperty("scenario"));

//-------------------------------------------------------------------	
	public static void main(String[] args) {
		Kompics.setScheduler(simulatorScheduler);
		Kompics.createAndStart(CyclonSimulationMain.class, 1);
	}

//-------------------------------------------------------------------	
	public CyclonSimulationMain() throws IOException {
		P2pSimulator.setSimulationPortType(SimulatorPort.class);

		// create
		Component bootstrapServer = create(BootstrapServer.class);
		Component p2pSimulator = create(P2pSimulator.class);
		Component simulator = create(CyclonSimulator.class);
                
		// loading component configurations
		final BootstrapConfiguration bootConfiguration = BootstrapConfiguration.load(System.getProperty("bootstrap.configuration"));
		final CyclonConfiguration cyclonConfiguration = CyclonConfiguration.load(System.getProperty("cyclon.configuration"));

		trigger(new BootstrapServerInit(bootConfiguration), bootstrapServer.getControl());
		trigger(new P2pSimulatorInit(simulatorScheduler, scenario, new KingLatencyMap()), p2pSimulator.getControl());
		trigger(new SimulatorInit(bootConfiguration, cyclonConfiguration, 
                        null, null), simulator.getControl());
                
            

		// connect
		connect(bootstrapServer.getNegative(Network.class), 
                        p2pSimulator.getPositive(Network.class), new CyclonSimulationMain.MessageDestinationFilter(bootConfiguration.getBootstrapServerAddress()));
		connect(bootstrapServer.getNegative(Timer.class), p2pSimulator.getPositive(Timer.class));
		connect(simulator.getNegative(Network.class), p2pSimulator.getPositive(Network.class));
		connect(simulator.getNegative(Timer.class), p2pSimulator.getPositive(Timer.class));
		connect(simulator.getNegative(SimulatorPort.class), p2pSimulator.getPositive(SimulatorPort.class));                
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
