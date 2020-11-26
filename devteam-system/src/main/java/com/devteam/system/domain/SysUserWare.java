package com.devteam.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * User and ware association sys_user_post
 */
public class SysUserWare
{
    /** User ID */
    private Long userId;
   
    /** Post ID */

    private Long wareId;

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getWareId()
    {
        return wareId;
    }

    public void setWareId(Long wareId)
    {
        this.wareId = wareId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("wareId", getWareId())
            .toString();
    }
}