package com.devteam.system.mapper;

import java.util.List;

import com.devteam.system.domain.SysUserWare;

/**
 * User and ware association table Data layer
 */
public interface SysUserWareMapper
{
    /**
     * Delete user and ware association through user ID
     *
     * @param userId user ID
     * @return result
     */
    public int deleteUserWareByUserId(Long userId);

    /**
     * Query the number of posts used by ware ID
     *
     * @param wareId ware ID
     * @return result
     */
    public int countUserWareById(Long wareId);

    /**
     * Delete users and ware associations in batches
     *
     * @param ids ID of the data to be deleted
     * @return result
     */
    public int deleteUserWare(Long[] ids);

    /**
     * Add user ware information in batches
     *
     * @param userWareList user role list
     * @return result
     */
    public int batchUserWare(List<SysUserWare> userWareList);
}
