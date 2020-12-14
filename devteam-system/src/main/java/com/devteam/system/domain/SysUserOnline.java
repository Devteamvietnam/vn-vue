package com.devteam.system.domain;

import lombok.Data;

/**
 * Current online session
 */
@Data
public class SysUserOnline
{
    /** Session number */
    private String tokenId;

    /** Department name */
    private String deptName;

    /** user name */
    private String userName;

    /** Login IP address */
    private String ipaddr;

    /** Login address */
    private String loginLocation;

    /** Browser type */
    private String browser;

    /** operating system */
    private String os;

    /** Log in time */
    private Long loginTime;

    public String getTokenId()
    {
        return tokenId;
    }

    public void setTokenId(String tokenId)
    {
        this.tokenId = tokenId;
    }

 
}
