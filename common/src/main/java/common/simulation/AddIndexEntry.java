package common.simulation;


import java.io.Serializable;
import se.sics.kompics.Event;

public final class AddIndexEntry extends Event implements Serializable {

    private final Long id;

    public AddIndexEntry(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
