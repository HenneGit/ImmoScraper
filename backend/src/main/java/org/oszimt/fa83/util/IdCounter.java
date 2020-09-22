package org.oszimt.fa83.util;

import java.rmi.server.UID;
import java.util.UUID;

public final class IdCounter {

    private IdCounter(){
        //hide constructor
    }

    public static Comparable<?> createId(){
        return UUID.randomUUID();
    }
}
