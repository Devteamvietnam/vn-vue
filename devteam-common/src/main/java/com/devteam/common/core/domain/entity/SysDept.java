package com.devteam.common.core.domain.entity;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.devteam.common.core.domain.BaseEntity;

import lombok.Data;

/**
 * Department table sys_dept
 *
 */
@Data
public class SysDept extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Department ID */
    private Long deptId;

    /** Parent department ID */
    private Long parentId;

    /** List of ancestors */
    private String ancestors;

    /** Department name */
    private String deptName;

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
    private List<SysDept> children = new ArrayList<SysDept>();


    @NotBlank(message = "Department name cannot be empty")
    @Size(min = 0, max = 30, message = "The length of the department name cannot exceed 30 characters")
    public String getDeptName()
    {
        return deptName;
    }
    @NotBlank(message = "The display order cannot be empty")
    public String getOrderNum()
    {
        return orderNum;
    }


    @Size(min = 0, max = 10, message = "The length of the contact number cannot exceed 10 characters")
    public String getPhone()
    {
        return phone;
    }


    @Email(message = "The email format is incorrect")
    @Size(min = 0, max = 50, message = "The length of the mailbox cannot exceed 50 characters")
    public String getEmail()
    {
        return email;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("deptId", getDeptId())
            .append("parentId", getParentId())
            .append("ancestors", getAncestors())
            .append("deptName", getDeptName())
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
