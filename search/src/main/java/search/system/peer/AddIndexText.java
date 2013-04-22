package search.system.peer;


import se.sics.kompics.Event;

public final class AddIndexText extends Event {

    private final String text;

    public AddIndexText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
