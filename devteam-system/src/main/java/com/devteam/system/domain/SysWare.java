package com.devteam.system.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.devteam.common.annotation.Excel;
import com.devteam.common.annotation.Excel.ColumnType;
import com.devteam.common.core.domain.BaseEntity;

/**
 *  WareHouse table management
 * @author jungj
 *
 */

public class SysWare extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3272852245025604408L;
	
	/** wareHouse Id */
	
	@Excel(name = "Ware serial number", cellType = ColumnType.NUMERIC)
	private Long wareId;
	
	/** wareHouse Code */
	@Excel(name = "Ware Code")
    private String wareCode;
	
    /** position Name */
    @Excel(name = "Ware name")
    private String wareName;

    /** Job sorting */
    @Excel(name = "Ware sorting")
    private String wareSort;

    /** Status (0 normal 1 disabled) */
    @Excel(name = "status", readConverterExp = "0=normal, 1=disabled")
    private String status;
    
    /** Does the user have this ware ID by default */
    private boolean flag = false;
	
    public Long getWareId()
    {
        return wareId;
    }

    public void setWareId(Long wareId)
    {
        this.wareId = wareId;
    }

    @NotBlank(message = "Ware code cannot be empty")
    @Size(min = 0, max = 64, message = "Ware code length cannot exceed 64 characters")
    public String getWareCode()
    {
        return wareCode;
    }

    public void setWareCode(String wareCode)
    {
        this.wareCode = wareCode;
    }

    @NotBlank(message = "Ware name cannot be empty")
    @Size(min = 0, max = 50, message = "The length of the ware name cannot exceed 50 characters")
    public String getWareName()
    {
        return wareName;
    }

    public void setWareName(String wareName)
    {
        this.wareName = wareName;
    }

    @NotBlank(message = "The display order cannot be empty")
    public String getWareSort()
    {
        return wareSort;
    }

    public void setWareSort(String wareSort)
    {
        this.wareSort = wareSort;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public boolean isFlag()
    {
        return flag;
    }

    public void setFlag(boolean flag)
    {
        this.flag = flag;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("wareId", getWareId())
            .append("wareCode", getWareCode())
            .append("wareName", getWareName())
            .append("wareSort", getWareSort())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
