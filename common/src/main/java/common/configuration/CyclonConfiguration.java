package common.configuration;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

public final class CyclonConfiguration {

    /**
     * Random number seed
     */
    private final long seed;
    /**
     * the number of descriptors exchanged during a shuffle.
     */
    private final int shuffleLength;
    /**
     * the size of the cache of each Cyclon node.
     */
    private final int randomViewSize;
    /**
     * the number of milliseconds between two consecutive shuffles initiated by
     * a node.
     */
    private final long shufflePeriod;
    /**
     * the number of milliseconds after which a node that does not respond to a
     * shuffle request is considered dead.
     */
    private final long shuffleTimeout;
    /**
     * used by the simulator as the range of identifiers assigned to Cyclon
     * nodes.
     */
    private final Long identifierSpaceSize;
    /**
     * the number of peers that should be requested from the bootstrap service
     * by a new Cyclon node.
     */
    private final int bootstrapRequestPeerCount;

//-------------------------------------------------------------------
    public CyclonConfiguration(long seed, int shuffleLength, int randomViewSize,
            long shufflePeriod, long shuffleTimeout,
            Long identifierSpaceSize, int bootstrapRequestPeerCount) {
        super();
        this.seed = seed;
        this.shuffleLength = shuffleLength;
        this.randomViewSize = randomViewSize;
        this.shufflePeriod = shufflePeriod;
        this.shuffleTimeout = shuffleTimeout;
        this.identifierSpaceSize = identifierSpaceSize;
        this.bootstrapRequestPeerCount = bootstrapRequestPeerCount;
    }

//-------------------------------------------------------------------
    public long getSeed() {
        return seed;
    }
    
//-------------------------------------------------------------------
    public int getShuffleLength() {
        return shuffleLength;
    }

//-------------------------------------------------------------------
    public int getRandomViewSize() {
        return randomViewSize;
    }

//-------------------------------------------------------------------
    public long getShufflePeriod() {
        return shufflePeriod;
    }

//-------------------------------------------------------------------
    public long getShuffleTimeout() {
        return shuffleTimeout;
    }

//-------------------------------------------------------------------
    public Long getIdentifierSpaceSize() {
        return identifierSpaceSize;
    }

//-------------------------------------------------------------------
    public int getBootstrapRequestPeerCount() {
        return bootstrapRequestPeerCount;
    }

//-------------------------------------------------------------------
    public void store(String file) throws IOException {
        Properties p = new Properties();
        p.setProperty("seed", "" + seed);
        p.setProperty("shuffle.length", "" + shuffleLength);
        p.setProperty("random.view.size", "" + randomViewSize);
        p.setProperty("shuffle.period", "" + shufflePeriod);
        p.setProperty("shuffle.timeout", "" + shuffleTimeout);
        p.setProperty("id.space.size", "" + identifierSpaceSize);
        p.setProperty("bootstrap.request.peer.count", "" + bootstrapRequestPeerCount);

        Writer writer = new FileWriter(file);
        p.store(writer, "se.sics.kompics.p2p.overlay.cyclon");
    }

//-------------------------------------------------------------------
    public static CyclonConfiguration load(String file) throws IOException {
        Properties p = new Properties();
        Reader reader = new FileReader(file);
        p.load(reader);

        long seed = Long.parseLong(p.getProperty("seed"));
        
        int shuffleLength = Integer.parseInt(p.getProperty("shuffle.length"));
        int randomViewSize = Integer.parseInt(p.getProperty("random.view.size"));
        long shufflePeriod = Long.parseLong(p.getProperty("shuffle.period"));
        long shuffleTimeout = Long.parseLong(p.getProperty("shuffle.timeout"));
        Long identifierSpaceSize = Long.parseLong(p.getProperty("id.space.size"));
        int bootstrapRequestPeerCount = Integer.parseInt(p.getProperty("bootstrap.request.peer.count"));

        return new CyclonConfiguration(seed, shuffleLength, randomViewSize, 
                shufflePeriod, shuffleTimeout, identifierSpaceSize, bootstrapRequestPeerCount);
    }
}
