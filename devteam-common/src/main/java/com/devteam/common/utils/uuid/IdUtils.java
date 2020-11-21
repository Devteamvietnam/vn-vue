package com.devteam.common.utils.uuid;

import com.devteam.common.utils.uuid.UUID;

/**
 * ID generator tools
 *
 * @author ruoyi
 */
public class IdUtils
{
    /**
     * Get random UUID
     *
     * @return random UUID
     */
    public static String randomUUID()
    {
        return UUID.randomUUID().toString();
    }

    /**
     * Simplified UUID, with horizontal lines removed
     *
     * @return Simplified UUID, remove the horizontal line
     */
    public static String simpleUUID()
    {
        return UUID.randomUUID().toString(true);
    }

    /**
     * Get a random UUID and use the better-performing ThreadLocalRandom to generate UUID
     *
     * @return random UUID
     */
    public static String fastUUID()
    {
        return UUID.fastUUID().toString();
    }

    /**
     * Simplified UUID, remove the horizontal line, use better performance ThreadLocalRandom to generate UUID
     *
     * @return Simplified UUID, remove the horizontal line
     */
    public static String fastSimpleUUID()
    {
        return UUID.fastUUID().toString(true);
    }
}
