/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.simulation;

import se.sics.kompics.p2p.experiment.dsl.distribution.Distribution;

/**
 *
 * @author jdowling
 */
public class MonotonicLongDistribution extends
		Distribution<Long> {
	private static final long serialVersionUID = 3313121212125L;

	private static Long value = new Long(0);

	public MonotonicLongDistribution() {
		super(Distribution.Type.OTHER, Long.class);
	}

	@Override
	public Long draw() {
		return value++;
	}
}