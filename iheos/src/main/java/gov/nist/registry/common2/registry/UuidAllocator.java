package gov.nist.registry.common2.registry;

import org.freebxml.omar.common.UUID;
import org.freebxml.omar.common.UUIDFactory;

public class UuidAllocator {
    static UUIDFactory fact = null;

    static public String allocate() {
        if (fact == null)
            fact = UUIDFactory.getInstance();
        UUID uu = fact.newUUID();
        return "urn:uuid:" + uu;
    }

}
