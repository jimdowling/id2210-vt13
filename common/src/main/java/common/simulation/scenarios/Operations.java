package common.simulation.scenarios;

import common.simulation.AddIndexEntry;
import common.simulation.PeerFail;
import common.simulation.PeerJoin;
import common.simulation.Publish;
import se.sics.kompics.p2p.experiment.dsl.adaptor.Operation;
import se.sics.kompics.p2p.experiment.dsl.adaptor.Operation1;

@SuppressWarnings("serial")
public class Operations {

  	public static Operation1<AddIndexEntry, Long> addIndexEntry() {
		return new Operation1<AddIndexEntry, Long>() {
                        @Override
			public AddIndexEntry generate(Long id) {
				return new AddIndexEntry(id);
			}
		};
	}
//-------------------------------------------------------------------
	public static Operation1<PeerJoin, Long> peerJoin() {
		return new Operation1<PeerJoin, Long>() {
                        @Override
			public PeerJoin generate(Long id) {
				return new PeerJoin(id);
			}
		};
	}

//-------------------------------------------------------------------
	public static Operation1<PeerFail, Long> peerFail = new Operation1<PeerFail, Long>() {
                @Override
		public PeerFail generate(Long id) {
			return new PeerFail(id);
		}
	};

//-------------------------------------------------------------------
	public static Operation<Publish> publish = new Operation<Publish>() {
                @Override
		public Publish generate() {
			return new Publish();
		}
	};
}
