package common.simulation;


import java.math.BigInteger;
import se.sics.kompics.Event;

public final class AddIndexEntry extends Event {

    private final BigInteger id;

    public AddIndexEntry(BigInteger id) {
        this.id = id;
    }

    public BigInteger getId() {
        return id;
    }

}
