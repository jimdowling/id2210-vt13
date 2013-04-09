package search.system.peer.search;

import se.sics.kompics.timer.SchedulePeriodicTimeout;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timeout;

public class UpdateIndexTimeout extends Timeout {

	public UpdateIndexTimeout(SchedulePeriodicTimeout request) {
		super(request);
	}

//-------------------------------------------------------------------
	public UpdateIndexTimeout(ScheduleTimeout request) {
		super(request);
	}
}
