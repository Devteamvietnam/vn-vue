package com.devteam.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Role and ctn association sys_role_ctn
 */

public class SysRoleCtn {

    /** Role ID */
    private Long roleId;
   
    /** Department ID */
    private Long ctnId;

    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public Long getCtnId()
    {
        return ctnId;
    }

    public void setCtnId(Long ctnId)
    {
        this.ctnId = ctnId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roleId", getRoleId())
            .append("ctnId", getCtnId())
            .toString();
    }
}
