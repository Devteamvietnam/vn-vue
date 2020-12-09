package com.devteam.system.service;

import java.util.List;

import com.devteam.common.core.domain.entity.SysPer;


/**
 * User business layer
 */
public interface ISysPerService
{
    /**
     * Paging query user list according to conditions
     *
     * @param user user information
     * @return user information collection information
     */
    public List<SysPer> selectUserList(SysPer user);

    /**
     * Query users by username
     *
     * @param userName username
     * @return user object information
     */
    public SysPer selectUserByUserName(String userName);

    /**
     * Query users by user ID
     *
     * @param userId user ID
     * @return user object information
     */
    public SysPer selectUserById(Long userId);

    /**
     * Query the role group the user belongs to according to the user ID
     *
     * @param userName username
     * @return result
     */
    public String selectUserRoleGroup(String userName);

    /**
     * Query the user's position group according to the user ID
     *
     * @param userName username
     * @return result
     */
    public String selectUserPostGroup(String userName);

    /**
     * Verify that the user name is unique
     *
     * @param userName user name
     * @return result
     */
    public String checkUserNameUnique(String userName);

    /**
     * Verify that the mobile phone number is unique
     *
     * @param user user information
     * @return result
     */
    public String checkPhoneUnique(SysPer user);

    /**
     * Verify that the email is unique
     *
     * @param user user information
     * @return result
     */
    public String checkEmailUnique(SysPer user);

    /**
     * Verify whether the user allows the operation
     *
     * @param user user information
     */
    public void checkUserAllowed(SysPer user);

    /**
     * Add user information
     *
     * @param user user information
     * @return result
     */
    public int insertUser(SysPer user);

    /**
     * Modify user information
     *
     * @param user user information
     * @return result
     */
    public int updateUser(SysPer user);

    /**
     * Modify user status
     *
     * @param user user information
     * @return result
     */
    public int updateUserStatus(SysPer user);

    /**
     * Modify user basic information
     *
     * @param user user information
     * @return result
     */
    public int updateUserProfile(SysPer user);

    /**
     * Modify user avatar
     *
     * @param userName username
     * @param avatar avatar address
     * @return result
     */
    public boolean updateUserAvatar(String userName, String avatar);


    /**
     * Delete user by user ID
     *
     * @param userId user ID
     * @return result
     */
    public int deleteUserById(Long userId);

    /**
     * Delete user information in batch
     *
     * @param userIds The user ID to be deleted
     * @return result
     */
    public int deleteUserByIds(Long[] userIds);

    /**
     * Import user data
     *
     * @param userList user data list
     * @param isUpdateSupport Whether to update support, if it already exists, update the data
     * @param operName operation user
     * @return result
     */
    public String importUser(List<SysPer> userList, Boolean isUpdateSupport, String operName);
}