package com.devteam.common.core.domain.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.devteam.common.annotation.Excel;
import com.devteam.common.annotation.Excel.ColumnType;
import com.devteam.common.constant.UserConstants;
import com.devteam.common.core.domain.BaseEntity;

import lombok.Data;

/**
 * Dictionary data table sys_dict_data
 *
 */
@Data
public class SysDictData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Dictionary encoding */
    @Excel(name = "Dictionary Encoding", cellType = ColumnType.NUMERIC)
    private Long dictCode;

    /** Dictionary sort */
    @Excel(name = "Dictionary Sort", cellType = ColumnType.NUMERIC)
    private Long dictSort;

    /** Dictionary tag */
    @Excel(name = "Dictionary Tag")
    private String dictLabel;

    /** Dictionary key value */
    @Excel(name = "Dictionary key value")
    private String dictValue;

    /** Dictionary type */
    @Excel(name = "Dictionary Type")
    private String dictType;

    /** Style attributes (other style extensions) */
    private String cssClass;

    /** Table dictionary style */
    private String listClass;

    /** Whether the default (Y is N no) */
    @Excel(name = "Is it the default", readConverterExp = "Y=Yes, N=No")
    private String isDefault;

    /** Status (0 normal 1 disabled) */
    @Excel(name = "status", readConverterExp = "0=normal, 1=disabled")
    private String status;


    @NotBlank(message = "Dictionary tag cannot be empty")
    @Size(min = 0, max = 100, message = "The length of the dictionary tag cannot exceed 100 characters")
    public String getDictLabel()
    {
        return dictLabel;
    }


    @NotBlank(message = "Dictionary key value cannot be empty")
    @Size(min = 0, max = 100, message = "The length of the dictionary key cannot exceed 100 characters")
    public String getDictValue()
    {
        return dictValue;
    }


    @NotBlank(message = "The dictionary type cannot be empty")
    @Size(min = 0, max = 100, message = "The length of the dictionary type cannot exceed 100 characters")
    public String getDictType()
    {
        return dictType;
    }



    @Size(min = 0, max = 100, message = "The length of the style attribute cannot exceed 100 characters")
    public String getCssClass()
    {
        return cssClass;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("dictCode", getDictCode())
            .append("dictSort", getDictSort())
            .append("dictLabel", getDictLabel())
            .append("dictValue", getDictValue())
            .append("dictType", getDictType())
            .append("cssClass", getCssClass())
            .append("listClass", getListClass())
            .append("isDefault", getIsDefault())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
