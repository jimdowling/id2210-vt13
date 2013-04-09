package common.simulation;

import common.configuration.CyclonConfiguration;
import common.configuration.SearchConfiguration;
import common.configuration.TManConfiguration;
import se.sics.kompics.Init;
import se.sics.kompics.p2p.bootstrap.BootstrapConfiguration;

public final class SimulatorInit extends Init {

    private final BootstrapConfiguration bootstrapConfiguration;
    private final CyclonConfiguration cyclonConfiguration;
    private final TManConfiguration tmanConfiguration;
    private final SearchConfiguration aggregationConfiguration;

//-------------------------------------------------------------------	
    public SimulatorInit(BootstrapConfiguration bootstrapConfiguration,
            CyclonConfiguration cyclonConfiguration, TManConfiguration tmanConfiguration,
            SearchConfiguration aggregationConfiguration) {
        super();
        this.bootstrapConfiguration = bootstrapConfiguration;
        this.cyclonConfiguration = cyclonConfiguration;
        this.tmanConfiguration = tmanConfiguration;
        this.aggregationConfiguration = aggregationConfiguration;
    }

    public SearchConfiguration getAggregationConfiguration() {
        return aggregationConfiguration;
    }

//-------------------------------------------------------------------	
    public BootstrapConfiguration getBootstrapConfiguration() {
        return this.bootstrapConfiguration;
    }

//-------------------------------------------------------------------	
    public CyclonConfiguration getCyclonConfiguration() {
        return this.cyclonConfiguration;
    }

//-------------------------------------------------------------------	
    public TManConfiguration getTmanConfiguration() {
        return this.tmanConfiguration;
    }

}
