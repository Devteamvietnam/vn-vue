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

import com.devteam.system.domain.SysConfig;
import com.devteam.system.service.ISysConfigService;
import com.devteam.common.annotation.Log;
import com.devteam.common.annotation.RepeatSubmit;
import com.devteam.common.constant.UserConstants;
import com.devteam.common.core.controller.BaseController;
import com.devteam.common.core.domain.AjaxResult;
import com.devteam.common.core.page.TableDataInfo;
import com.devteam.common.enums.BusinessType;
import com.devteam.common.utils.SecurityUtils;
import com.devteam.common.utils.poi.ExcelUtil;

/**
 * Parameter configuration information operation processing
 *
 */
@RestController
@RequestMapping("/system/config")
public class SysConfigController extends BaseController
{
    @Autowired
    private ISysConfigService configService;

    /**
     * Get the parameter configuration list
     */
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysConfig config)
    {
        startPage();
        List<SysConfig> list = configService.selectConfigList(config);
        return getDataTable(list);
    }

    @Log(title = "Parameter Management", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:config:export')")
    @GetMapping("/export")
    public AjaxResult export(SysConfig config)
    {
        List<SysConfig> list = configService.selectConfigList(config);
        ExcelUtil<SysConfig> util = new ExcelUtil<SysConfig>(SysConfig.class);
        return util.exportExcel(list, "parameter data");
    }

    /**
     * Get detailed information according to the parameter number
     */
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping(value = "/{configId}")
    public AjaxResult getInfo(@PathVariable Long configId)
    {
        return AjaxResult.success(configService.selectConfigById(configId));
    }

    /**
     * Query the parameter value according to the parameter key name
     */
    @GetMapping(value = "/configKey/{configKey}")
    public AjaxResult getConfigKey(@PathVariable String configKey)
    {
        return AjaxResult.success(configService.selectConfigByKey(configKey));
    }

    /**
     * New parameter configuration
     */
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @Log(title = "Parameter Management", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@Validated @RequestBody SysConfig config)
    {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config)))
        {
            return AjaxResult.error("New parameter'" + config.getConfigName() + "'Failed, parameter key name already exists");
        }
        config.setCreateBy(SecurityUtils.getUsername());
        return toAjax(configService.insertConfig(config));
    }

    /**
     * Modify parameter configuration
     */
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @Log(title = "Parameter Management", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysConfig config)
    {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config)))
        {
            return AjaxResult.error("Modify parameter'" + config.getConfigName() + "'Failed, parameter key name already exists");
        }
        config.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(configService.updateConfig(config));
    }

    /**
     * Delete parameter configuration
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "Parameter Management", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    public AjaxResult remove(@PathVariable Long[] configIds)
    {
        return toAjax(configService.deleteConfigByIds(configIds));
    }

    /**
     * Empty the cache
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "Parameter Management", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clearCache")
    public AjaxResult clearCache()
    {
        configService.clearCache();
        return AjaxResult.success();
    }
}