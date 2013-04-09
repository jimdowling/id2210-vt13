package cyclon.system.peer.cyclon;

import se.sics.kompics.PortType;

public final class CyclonPartnersPort extends PortType {{
	negative(CyclonPartnersRequest.class);
	positive(CyclonPartnersResponse.class);
}}
