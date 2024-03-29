package com.devteam.common.exception.user;

/**
 * Verification code invalidation exception
 * 
 */
public class CaptchaExpireException extends UserException
{
    private static final long serialVersionUID = 1L;

    public CaptchaExpireException()
    {
        super("user.jcaptcha.expire", null);
    }
}
