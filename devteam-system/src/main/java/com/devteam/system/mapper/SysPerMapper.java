package com.devteam.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.devteam.common.core.domain.entity.SysPer;

/**
 * User table data layer
 */
public interface SysPerMapper
{
    /**
     * Paging query user list according to conditions
     *
     * @param sysUser user information
     * @return user information collection information
     */
    public List<SysPer> selectUserList(SysPer sysUser);

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
     * Modify user avatar
     *
     * @param userName username
     * @param avatar avatar address
     * @return result
     */
    public int updateUserAvatar(@Param("userName") String userName, @Param("avatar") String avatar);


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
     * Verify that the user name is unique
     *
     * @param userName user name
     * @return result
     */
    public int checkUserNameUnique(String userName);

    /**
     * Verify that the mobile phone number is unique
     *
     * @param phonenumber phone number
     * @return result
     */
    public SysPer checkPhoneUnique(String phonenumber);

    /**
     * Verify that the email is unique
     *
     * @param email user mailbox
     * @return result
     */
    public SysPer checkEmailUnique(String email);
}
