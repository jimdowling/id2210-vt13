package common.simulation.scenarios;

import se.sics.kompics.p2p.experiment.dsl.SimulationScenario;

@SuppressWarnings("serial")
public class Scenario2 extends Scenario {
	private static SimulationScenario scenario = new SimulationScenario() {{
		StochasticProcess process1 = new StochasticProcess() {{
			eventInterArrivalTime(constant(100));
			raise(1, Operations.peerJoin(0), uniform(13));
		}};
		
		StochasticProcess process2 = new StochasticProcess() {{
			eventInterArrivalTime(constant(100));
			raise(30, Operations.peerJoin(0), uniform(13));
		}};
                
		StochasticProcess process3 = new StochasticProcess() {{
			eventInterArrivalTime(constant(100));
			raise(12, Operations.peerJoin(0), uniform(13));
		}};
		process1.start();
		process2.startAfterTerminationOf(2000, process1);
		process3.startAfterTerminationOf(2000, process1);
	}};
	
//-------------------------------------------------------------------
	public Scenario2() {
		super(scenario);
	} 
}
