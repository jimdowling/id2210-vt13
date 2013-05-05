package common.simulation;


import se.sics.kompics.Event;

public final class AddIndexEntry extends Event {

    private final Long id;

    public AddIndexEntry(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
