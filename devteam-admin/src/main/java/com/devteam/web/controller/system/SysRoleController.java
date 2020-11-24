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

import com.devteam.system.service.ISysRoleService;
import com.devteam.system.service.ISysUserService;
import com.devteam.common.annotation.Log;
import com.devteam.common.constant.UserConstants;
import com.devteam.common.core.controller.BaseController;
import com.devteam.common.core.domain.AjaxResult;
import com.devteam.common.core.domain.entity.SysRole;
import com.devteam.common.core.domain.model.LoginUser;
import com.devteam.common.core.page.TableDataInfo;
import com.devteam.common.enums.BusinessType;
import com.devteam.common.utils.SecurityUtils;
import com.devteam.common.utils.ServletUtils;
import com.devteam.common.utils.StringUtils;
import com.devteam.common.utils.poi.ExcelUtil;
import com.devteam.framework.web.service.SysPermissionService;
import com.devteam.framework.web.service.TokenService;

/**
 * Role information
 */
@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController
{
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private SysPermissionService permissionService;
    
    @Autowired
    private ISysUserService userService;

    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysRole role)
    {
        startPage();
        List<SysRole> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    @Log(title = "Role Management", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:role:export')")
    @GetMapping("/export")
    public AjaxResult export(SysRole role)
    {
        List<SysRole> list = roleService.selectRoleList(role);
        ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
        return util.exportExcel(list, "role data");
    }

    /**
     * Get detailed information according to the character number
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/{roleId}")
    public AjaxResult getInfo(@PathVariable Long roleId)
    {
        return AjaxResult.success(roleService.selectRoleById(roleId));
    }

    /**
     * New role
     */
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    @Log(title = "Role Management", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysRole role)
    {
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role)))
        {
            return AjaxResult.error("New role'" + role.getRoleName() + "'Failed, role name already exists");
        }
        else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role)))
        {
            return AjaxResult.error("New role'" + role.getRoleName() + "'Failed, role permissions already exist");
        }
        role.setCreateBy(SecurityUtils.getUsername());
        return toAjax(roleService.insertRole(role));

    }

    /**
     * Modify and save roles
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "Role Management", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysRole role)
    {
        roleService.checkRoleAllowed(role);
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role)))
        {
            return AjaxResult.error("Modify role'" + role.getRoleName() + "'Failed, role name already exists");
        }
        else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role)))
        {
            return AjaxResult.error("Modify role'" + role.getRoleName() + "'Failed, role permissions already exist");
        }
        role.setUpdateBy(SecurityUtils.getUsername());
        
        if (roleService.updateRole(role)> 0)
        {
            // Update cache user permissions
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            if (StringUtils.isNotNull(loginUser.getUser()) && !loginUser.getUser().isAdmin())
            {
                loginUser.setPermissions(permissionService.getMenuPermission(loginUser.getUser()));
                loginUser.setUser(userService.selectUserByUserName(loginUser.getUser().getUserName()));
                tokenService.setLoginUser(loginUser);
            }
            return AjaxResult.success();
        }
        return AjaxResult.error("Modify role'" + role.getRoleName() + "'Failed, please contact the administrator");
    }

    /**
     * Modify save data permissions
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "Role Management", businessType = BusinessType.UPDATE)
    @PutMapping("/dataScope")
    public AjaxResult dataScope(@RequestBody SysRole role)
    {
        roleService.checkRoleAllowed(role);
        return toAjax(roleService.authDataScope(role));
    }

    /**
     * Status modification
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "Role Management", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysRole role)
    {
        roleService.checkRoleAllowed(role);
        role.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(roleService.updateRoleStatus(role));
    }

    /**
     * Delete role
     */
    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    @Log(title = "Role Management", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public AjaxResult remove(@PathVariable Long[] roleIds)
    {
        return toAjax(roleService.deleteRoleByIds(roleIds));
    }

    /**
     * Get the list of role selection boxes
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping("/optionselect")
    public AjaxResult optionselect()
    {
        return AjaxResult.success(roleService.selectRoleAll());
    }
}
