package cyclon.main;

import common.configuration.Configuration;
import common.simulation.scenarios.Scenario;
import common.simulation.scenarios.Scenario2;
import cyclon.simulator.core.CyclonExecutionMain;


public class Main {
	public static void main(String[] args) throws Throwable {
		Configuration configuration = new Configuration();
		configuration.set();
		
		Scenario scenario = new Scenario2();
		scenario.setSeed(System.currentTimeMillis());
		scenario.getScenario().simulate(CyclonExecutionMain.class);
	}
}
