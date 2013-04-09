package cyclon.system.peer.cyclon;

import se.sics.kompics.PortType;

public final class CyclonPort extends PortType {{
	negative(CyclonJoin.class);
	positive(JoinCompleted.class);
}}
