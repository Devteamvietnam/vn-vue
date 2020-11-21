package com.devteam.common.exception.file;

import com.devteam.common.exception.BaseException;

/**
 * File information abnormal
 *
 * @author ruoyi
 */
public class FileException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args)
    {
        super("file", code, args, null);
    }

}
