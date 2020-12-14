package com.devteam.common.core.domain.model;

import lombok.Data;

/**
 * User login object
 *
 */
@Data
public class LoginBody
{
    /**
     * username
     */
    private String username;

    /**
     * user password
     */
    private String password;

    /**
     * Verification code
     */
    private String code;

    /**
     * Uniquely identifies
     */
    private String uuid = "";

  
}