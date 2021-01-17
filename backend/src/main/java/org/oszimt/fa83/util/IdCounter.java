package org.oszimt.fa83.util;

import java.util.UUID;

/**
 * Util class for creating UUIDs
 */
public final class IdCounter {

    private IdCounter(){
        //hide constructor
    }

    /**
     * creates a new uuid
     * @return the new uuid.
     */
    public static String createId(){
        return UUID.randomUUID().toString();
    }
}
