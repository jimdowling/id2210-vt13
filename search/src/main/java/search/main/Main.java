package search.main;

import search.simulator.core.SearchExecutionMain;
import common.configuration.Configuration;
import common.simulation.scenarios.Scenario;
import common.simulation.scenarios.Scenario1;

public class Main {
	public static void main(String[] args) throws Throwable {
		Configuration configuration = new Configuration();
		configuration.set();
		
		Scenario scenario = new Scenario1();
		scenario.setSeed(System.currentTimeMillis());
		scenario.getScenario().execute(SearchExecutionMain.class);
	}
}
