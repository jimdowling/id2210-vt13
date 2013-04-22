package common.simulation.scenarios;

import common.simulation.AddIndexEntry;
import common.simulation.PeerFail;
import common.simulation.PeerJoin;
import common.simulation.Publish;
import java.math.BigInteger;
import se.sics.kompics.p2p.experiment.dsl.adaptor.Operation;
import se.sics.kompics.p2p.experiment.dsl.adaptor.Operation1;

@SuppressWarnings("serial")
public class Operations {

  	public static Operation1<AddIndexEntry, BigInteger> addIndexEntry() {
		return new Operation1<AddIndexEntry, BigInteger>() {
                        @Override
			public AddIndexEntry generate(BigInteger id) {
				return new AddIndexEntry(id);
			}
		};
	}
//-------------------------------------------------------------------
	public static Operation1<PeerJoin, BigInteger> peerJoin(final int num) {
		return new Operation1<PeerJoin, BigInteger>() {
			public PeerJoin generate(BigInteger id) {
				return new PeerJoin(id, num);
			}
		};
	}

//-------------------------------------------------------------------
	public static Operation1<PeerFail, BigInteger> peerFail = new Operation1<PeerFail, BigInteger>() {
		public PeerFail generate(BigInteger id) {
			return new PeerFail(id);
		}
	};

//-------------------------------------------------------------------
	public static Operation<Publish> publish = new Operation<Publish>() {
		public Publish generate() {
			return new Publish();
		}
	};
}
