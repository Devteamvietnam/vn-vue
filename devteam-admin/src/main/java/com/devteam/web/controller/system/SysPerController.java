package com.devteam.web.controller.system;

import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.multipart.MultipartFile;

import com.devteam.common.annotation.Log;
import com.devteam.common.constant.UserConstants;
import com.devteam.common.core.controller.BaseController;
import com.devteam.common.core.domain.AjaxResult;
import com.devteam.common.core.domain.entity.SysPer;
import com.devteam.common.core.domain.entity.SysRole;
import com.devteam.common.core.domain.model.LoginUser;
import com.devteam.common.core.page.TableDataInfo;
import com.devteam.common.enums.BusinessType;
import com.devteam.common.utils.SecurityUtils;
import com.devteam.common.utils.ServletUtils;
import com.devteam.common.utils.StringUtils;
import com.devteam.common.utils.poi.ExcelUtil;
import com.devteam.framework.web.service.TokenService;
import com.devteam.system.service.ISysPerService;
import com.devteam.system.service.ISysPostService;
import com.devteam.system.service.ISysRoleService;

/**
 * User Info
 *
 */
@RestController
@RequestMapping("/system/per")
public class SysPerController extends BaseController{

	@Autowired
	private ISysPerService userService;
	
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPostService postService;
    
    @Autowired
    private TokenService tokenService;

    /**
     * Get user list
     */
    @PreAuthorize("@ss.hasPermi('system:per:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysPer user)
    {
        startPage();
        List<SysPer> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    @Log(title = "User Management", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:per:export')")
    @GetMapping("/export")
    public AjaxResult export(SysPer user)
    {
        List<SysPer> list = userService.selectUserList(user);
        ExcelUtil<SysPer> util = new ExcelUtil<SysPer>(SysPer.class);
        return util.exportExcel(list, "User Data");
    }

    @Log(title = "User Management", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:per:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<SysPer> util = new ExcelUtil<SysPer>(SysPer.class);
        List<SysPer> userList = util.importExcel(file.getInputStream());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String operName = loginUser.getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate()
    {
        ExcelUtil<SysPer> util = new ExcelUtil<SysPer>(SysPer.class);
        return util.importTemplateExcel("User Data");
    }

    /**
     * Get detailed information according to user number
     */
    @PreAuthorize("@ss.hasPermi('system:per:query')")
    @GetMapping(value = {"/", "/{userId}" })
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId)
    {
        AjaxResult ajax = AjaxResult.success();
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", SysPer.isAdmin(userId)? roles: roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        ajax.put("posts", postService.selectPostAll());
        if (StringUtils.isNotNull(userId))
        {
            ajax.put(AjaxResult.DATA_TAG, userService.selectUserById(userId));
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            ajax.put("roleIds", roleService.selectRoleListByUserId(userId));
        }
        return ajax;
    }

    /**
     * New users
     */
    @PreAuthorize("@ss.hasPermi('system:per:add')")
    @Log(title = "User Management", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysPer user)
    {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName())))
        {
            return AjaxResult.error("New user'" + user.getUserName() + "'Failed, login account already exists");
        }
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return AjaxResult.error("New user'" + user.getUserName() + "'Failed, mobile phone number already exists");
        }
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return AjaxResult.error("New user'" + user.getUserName() + "'Failed, the email account already exists");
        }
        user.setCreateBy(SecurityUtils.getUsername());
        return toAjax(userService.insertUser(user));
    }

    /**
     * Modify user
     */
    @PreAuthorize("@ss.hasPermi('system:per:edit')")
    @Log(title = "User Management", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysPer user)
    {
        userService.checkUserAllowed(user);
        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return AjaxResult.error("Modify user'" + user.getUserName() + "'Failed, mobile phone number already exists");
        }
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return AjaxResult.error("Modify user'" + user.getUserName() + "'Failed, the email account already exists");
        }
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUser(user));
    }

    /**
     * delete users
     */
    @PreAuthorize("@ss.hasPermi('system:per:remove')")
    @Log(title = "User Management", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(userService.deleteUserByIds(userIds));
    }


    /**
     * Status modification
     */
    @PreAuthorize("@ss.hasPermi('system:per:edit')")
    @Log(title = "User Management", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysPer user)
    {
        userService.checkUserAllowed(user);
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUserStatus(user));
    }
}
