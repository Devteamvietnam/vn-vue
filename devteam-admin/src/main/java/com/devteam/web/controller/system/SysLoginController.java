package com.devteam.web.controller.system;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.devteam.system.service.ISysMenuService;
import com.devteam.common.constant.Constants;
import com.devteam.common.core.domain.AjaxResult;
import com.devteam.common.core.domain.entity.SysMenu;
import com.devteam.common.core.domain.entity.SysUser;
import com.devteam.common.core.domain.model.LoginBody;
import com.devteam.common.core.domain.model.LoginUser;
import com.devteam.common.utils.ServletUtils;
import com.devteam.framework.web.service.SysLoginService;
import com.devteam.framework.web.service.SysPermissionService;
import com.devteam.framework.web.service.TokenService;

/**
 * Login authentication
 *
 */
@RestController
public class SysLoginController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    /**
     * Login method
     *
     * @param loginBody login information
     * @return result
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        // Generate token
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * Get user information
     *
     * @return user information
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        // role collection
        Set<String> roles = permissionService.getRolePermission(user);
        // permission collection
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * Obtain routing information
     *
     * @return routing information
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // User Info
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
