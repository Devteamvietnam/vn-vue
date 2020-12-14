package com.devteam.system.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import com.devteam.common.annotation.Excel;
import com.devteam.common.annotation.Excel.ColumnType;
import com.devteam.common.core.domain.BaseEntity;


/**
 * System access record table sys_logininfor
 */
@Data
public class SysLogininfor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @Excel(name = "serial number", cellType = ColumnType.NUMERIC)
    private Long infoId;

    /** user account */
    @Excel(name = "User Account")
    private String userName;

    /** Login status 0 success 1 failure */
    @Excel(name = "Login status", readConverterExp = "0=success, 1=failure")
    private String status;

    /** Login IP address */
    @Excel(name = "Login address")
    private String ipaddr;

    /** Login location */
    @Excel(name = "Login Location")
    private String loginLocation;

    /** Browser type */
    @Excel(name = "Browser")
    private String browser;

    /** operating system */
    @Excel(name = "Operating System")
    private String os;

    /** Prompt message */
    @Excel(name = "Prompt Message")
    private String msg;

    /** interview time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "Access time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;
    
    public Long getInfoId()
    {
        return infoId;
    }

    public void setInfoId(Long infoId)
    {
        this.infoId = infoId;
    }

}
