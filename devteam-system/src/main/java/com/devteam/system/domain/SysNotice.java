package com.devteam.system.domain;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.devteam.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * Notification form sys_notice
 */
@Data
public class SysNotice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Announcement ID */
    private Long noticeId;

    /** Announcement title */
    private String noticeTitle;

    /** Announcement type (1 notification 2 announcement) */
    private String noticeType;

    /** Announcement content */
    private String noticeContent;

    /** Announcement status (0 normal and 1 closed) */
    private String status;

    public Long getNoticeId()
    {
        return noticeId;
    }

    public void setNoticeId(Long noticeId)
    {
        this.noticeId = noticeId;
    }

    public void setNoticeTitle(String noticeTitle)
    {
        this.noticeTitle = noticeTitle;
    }

    @NotBlank(message = "Announcement title cannot be empty")
    @Size(min = 0, max = 50, message = "Announcement title cannot exceed 50 characters")
    public String getNoticeTitle()
    {
        return noticeTitle;
    }



    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("noticeId", getNoticeId())
            .append("noticeTitle", getNoticeTitle())
            .append("noticeType", getNoticeType())
            .append("noticeContent", getNoticeContent())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
