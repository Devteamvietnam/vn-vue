package com.devteam.system.mapper;

import java.util.List;

import com.devteam.system.domain.SysUserPost;

/**
 * User and position association table Data layer
 */
public interface SysUserPostMapper
{
    /**
     * Delete user and post association through user ID
     *
     * @param userId user ID
     * @return result
     */
    public int deleteUserPostByUserId(Long userId);

    /**
     * Query the number of posts used by post ID
     *
     * @param postId post ID
     * @return result
     */
    public int countUserPostById(Long postId);

    /**
     * Delete users and post associations in batches
     *
     * @param ids ID of the data to be deleted
     * @return result
     */
    public int deleteUserPost(Long[] ids);

    /**
     * Add user post information in batches
     *
     * @param userPostList user role list
     * @return result
     */
    public int batchUserPost(List<SysUserPost> userPostList);
}
