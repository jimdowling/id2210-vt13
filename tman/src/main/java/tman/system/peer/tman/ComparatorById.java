/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tman.system.peer.tman;

import java.util.Comparator;
import se.sics.kompics.address.Address;

/**
 * Make Node with Highest Id Leader in the Gradient
 */
public class ComparatorById implements Comparator<Address> {
    Address self;

    public ComparatorById(Address self) {
        this.self = self;
    }

    @Override
    public int compare(Address o1, Address o2) {
        assert (o1.getId() == o2.getId());
        if (o1.getId() < self.getId() && o2.getId() > self.getId()) {
            return 1;
        } else if (o2.getId() < self.getId() && o1.getId() > self.getId()) {
            return -1;
        } else if (Math.abs(o1.getId() - self.getId()) < Math.abs(o2.getId() - self.getId())) {
            return -1;
        }
        return 1;
    }
    
}
