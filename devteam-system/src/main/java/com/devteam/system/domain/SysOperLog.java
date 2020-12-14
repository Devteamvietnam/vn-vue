package com.devteam.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import com.devteam.common.annotation.Excel;
import com.devteam.common.annotation.Excel.ColumnType;
import com.devteam.common.core.domain.BaseEntity;

/**
 * Operation log record table oper_log
 */
@Data
public class SysOperLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Log primary key */
    @Excel(name = "operation serial number", cellType = ColumnType.NUMERIC)
    private Long operId;

    /** Operation module */
    @Excel(name = "Operation Module")
    private String title;

    /** Business type (0 other 1 added 2 modified 3 deleted) */
    @Excel(name = "Business Type", readConverterExp = "0=other, 1=add, 2=modify, 3=delete, 4=authorize, 5=export, 6=import, 7=return, 8=generate Code, 9=Clear data")
    private Integer businessType;

    /** Business type array */
    private Integer[] businessTypes;

    /** Request method */
    @Excel(name = "Request method")
    private String method;

    /** Request method */
    @Excel(name = "Request method")
    private String requestMethod;

    /** Operation category (0 other 1 background user 2 mobile phone user) */
    @Excel(name = "Operation category", readConverterExp = "0=other, 1=background user, 2=mobile terminal user")
    private Integer operatorType;

    /** operator */
    @Excel(name = "operator")
    private String operName;

    /** Department name */
    @Excel(name = "Department Name")
    private String deptName;

    /** Request url */
    @Excel(name = "Request address")
    private String operUrl;

    /** Operation address */
    @Excel(name = "Operation Address")
    private String operIp;

    /** Operating location */
    @Excel(name = "Operation Location")
    private String operLocation;

    /** Request parameters */
    @Excel(name = "Request parameters")
    private String operParam;

    /** Return parameter */
    @Excel(name = "Return Parameters")
    private String jsonResult;

    /** Operating status (0 normal and 1 abnormal) */
    @Excel(name = "status", readConverterExp = "0=normal, 1=abnormal")
    private Integer status;

    /** wrong information */
    @Excel(name = "error message")
    private String errorMsg;

    /** Operation time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "Operation time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date operTime;

    public Long getOperId()
    {
        return operId;
    }

    public void setOperId(Long operId)
    {
        this.operId = operId;
    }
}
