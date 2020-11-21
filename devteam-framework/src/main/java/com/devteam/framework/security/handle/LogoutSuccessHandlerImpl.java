package com.devteam.framework.security.handle;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import com.alibaba.fastjson.JSON;
import com.devteam.common.constant.Constants;
import com.devteam.common.constant.HttpStatus;
import com.devteam.common.core.domain.AjaxResult;
import com.devteam.common.core.domain.model.LoginUser;
import com.devteam.common.utils.ServletUtils;
import com.devteam.common.utils.StringUtils;
import com.devteam.framework.manager.AsyncManager;
import com.devteam.framework.manager.factory.AsyncFactory;
import com.devteam.framework.web.service.TokenService;

/**
 * Custom exit processing class returns success
 *
 * @author ruoyi
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler
{
    @Autowired
    private TokenService tokenService;

    /**
     * Exit processing
     *
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException
    {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser))
        {
            String userName = loginUser.getUsername();
            // Delete user cache record
            tokenService.delLoginUser(loginUser.getToken());
            // Record user exit log
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, "Exit successfully"));
        }
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(HttpStatus.SUCCESS, "Exit successfully")));
    }
}
