package com.devteam.common.core.domain.entity;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.devteam.common.core.domain.BaseEntity;

/**
 * CTN table sys_ctn
 *
 */

public class SysCtn extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7973688492118156284L;

    /** CTN ID */
    private Long ctnId;

    /** Parent department ID */
    private Long parentId;

    /** List of ancestors */
    private String ancestors;

    /** CTN name */
    private String ctnName;

    /** display order */
    private String orderNum;

    /** principal */
    private String leader;

    /** contact number */
    private String phone;

    /** Email */
    private String email;

    /** Department status: 0 normal, 1 disabled */
    private String status;

    /** Delete flag (0 means existence 2 means deletion) */
    private String delFlag;

    /** Parent department name */
    private String parentName;
    
    /** sub-department */
    private List<SysCtn> children = new ArrayList<SysCtn>();

    public Long getCtnId()
    {
        return ctnId;
    }

    public void setCtnId(Long ctnId)
    {
        this.ctnId = ctnId;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public String getAncestors()
    {
        return ancestors;
    }

    public void setAncestors(String ancestors)
    {
        this.ancestors = ancestors;
    }

    @NotBlank(message = "Department name cannot be empty")
    @Size(min = 0, max = 30, message = "The length of the department name cannot exceed 30 characters")
    public String getCtnName()
    {
        return ctnName;
    }

    public void setCtnName(String ctnName)
    {
        this.ctnName = ctnName;
    }

    @NotBlank(message = "The display order cannot be empty")
    public String getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(String orderNum)
    {
        this.orderNum = orderNum;
    }

    public String getLeader()
    {
        return leader;
    }

    public void setLeader(String leader)
    {
        this.leader = leader;
    }

    @Size(min = 0, max = 10, message = "The length of the contact number cannot exceed 10 characters")
    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    @Email(message = "The email format is incorrect")
    @Size(min = 0, max = 50, message = "The length of the mailbox cannot exceed 50 characters")
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public List<SysCtn> getChildren()
    {
        return children;
    }

    public void setChildren(List<SysCtn> children)
    {
        this.children = children;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("ctnId", getCtnId())
            .append("parentId", getParentId())
            .append("ancestors", getAncestors())
            .append("ctnName", getCtnName())
            .append("orderNum", getOrderNum())
            .append("leader", getLeader())
            .append("phone", getPhone())
            .append("email", getEmail())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
