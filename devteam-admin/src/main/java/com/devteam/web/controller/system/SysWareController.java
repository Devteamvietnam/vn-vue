package com.devteam.web.controller.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devteam.common.annotation.Log;
import com.devteam.common.constant.UserConstants;
import com.devteam.common.core.controller.BaseController;
import com.devteam.common.core.domain.AjaxResult;
import com.devteam.common.core.page.TableDataInfo;
import com.devteam.common.enums.BusinessType;
import com.devteam.common.utils.SecurityUtils;
import com.devteam.common.utils.poi.ExcelUtil;
import com.devteam.system.domain.SysWare;
import com.devteam.system.service.ISysWareService;

/**
 *  Ware House information processing 
 */
@RestController
@RequestMapping("/system/ware")
public class SysWareController extends BaseController {

	@Autowired
	private ISysWareService wareService;
	
	/** 
	 * Get warehouse list 
	 */
	@PreAuthorize("@ss.hasPermi('system:ware:list')")
	@GetMapping("/list")
	public TableDataInfo list(SysWare ware) {
		startPage();
		List<SysWare> list = wareService.selectWareList(ware);
		return getDataTable(list);
	}
	
    @Log(title = "Ware Management", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:ware:export')")
    @GetMapping("/export")
    public AjaxResult export(SysWare ware)
    {
        List<SysWare> list = wareService.selectWareList(ware);
        ExcelUtil<SysWare> util = new ExcelUtil<SysWare>(SysWare.class);
        return util.exportExcel(list, "Ware data");
    }
    
    /**
     * Get detailed information according to the job number
     */
    @PreAuthorize("@ss.hasPermi('system:ware:query')")
    @GetMapping(value = "/{wareId}")
    public AjaxResult getInfo(@PathVariable Long wareId)
    {
        return AjaxResult.success(wareService.selectWareById(wareId));
    }
    
    /**
     * New wares
     */
    @PreAuthorize("@ss.hasPermi('system:ware:add')")
    @Log(title = "Ware Management", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysWare ware)
    {
        if (UserConstants.NOT_UNIQUE.equals(wareService.checkWareNameUnique(ware)))
        {
            return AjaxResult.error("New Ware'" + ware.getWareName() + "'Failed, ware name already exists");
        }
        else if (UserConstants.NOT_UNIQUE.equals(wareService.checkWareCodeUnique(ware)))
        {
            return AjaxResult.error("New post'" + ware.getWareName() + "'Failed, ware code already exists");
        }
        ware.setCreateBy(SecurityUtils.getUsername());
        return toAjax(wareService.insertWare(ware));
    }
    
    /**
     * Modify warehouse
     */
    @PreAuthorize("@ss.hasPermi('system:ware:edit')")
    @Log(title = "Ware Management", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysWare ware)
    {
        if (UserConstants.NOT_UNIQUE.equals(wareService.checkWareNameUnique(ware)))
        {
            return AjaxResult.error("Modify ware'" + ware.getWareName() + "'Failed, ware name already exists");
        }
        else if (UserConstants.NOT_UNIQUE.equals(wareService.checkWareCodeUnique(ware)))
        {
            return AjaxResult.error("Modify Ware'" + ware.getWareName() + "'Failed, ware code already exists");
        }
        ware.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(wareService.updateWare(ware));
    }

    /**
     * Delete ware
     */
    @PreAuthorize("@ss.hasPermi('system:ware:remove')")
    @Log(title = "Ware Management", businessType = BusinessType.DELETE)
    @DeleteMapping("/{wareIds}")
    public AjaxResult remove(@PathVariable Long[] wareIds)
    {
        return toAjax(wareService.deleteWareByIds(wareIds));
    }

    /**
     * Get the list of post selection boxes
     */
    @GetMapping("/optionselect")
    public AjaxResult optionselect()
    {
        List<SysWare> wares = wareService.selectWareAll();
        return AjaxResult.success(wares);
    }
}
