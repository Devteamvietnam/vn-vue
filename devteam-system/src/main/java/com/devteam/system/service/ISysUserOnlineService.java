package com.devteam.system.service;

import com.devteam.system.domain.SysUserOnline;
import com.devteam.common.core.domain.model.LoginUser;

/**
 * Online user service layer
 */
public interface ISysUserOnlineService
{
    /**
     * Query information through the login address
     *
     * @param ipaddr login address
     * @param user user information
     * @return online user information
     */
    public SysUserOnline selectOnlineByIpaddr(String ipaddr, LoginUser user);

    /**
     * Query information by user name
     *
     * @param userName user name
     * @param user user information
     * @return online user information
     */
    public SysUserOnline selectOnlineByUserName(String userName, LoginUser user);

    /**
     * Query information by login address/user name
     *
     * @param ipaddr login address
     * @param userName user name
     * @param user user information
     * @return online user information
     */
    public SysUserOnline selectOnlineByInfo(String ipaddr, String userName, LoginUser user);

    /**
     * Set online user information
     *
     * @param user user information
     * @return online users
     */
    public SysUserOnline loginUserToUserOnline(LoginUser user);
}