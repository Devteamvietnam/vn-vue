package com.devteam.common.utils.file;

import java.io.File;
import org.apache.commons.lang3.StringUtils;

/**
 * File type tools
 *
 */
public class FileTypeUtils
{
    /**
     * Get file type
     * <p>
     * For example: devteam.txt, return: txt
     *
     * @param file file name
     * @return suffix (without ".")
     */
    public static String getFileType(File file)
    {
        if (null == file)
        {
            return StringUtils.EMPTY;
        }
        return getFileType(file.getName());
    }

    /**
     * Get file type
     * <p>
     * For example: ruoyi.txt, return: txt
     *
     * @param fileName file name
     * @return suffix (without ".")
     */
    public static String getFileType(String fileName)
    {
        int separatorIndex = fileName.lastIndexOf(".");
        if (separatorIndex <0)
        {
            return "";
        }
        return fileName.substring(separatorIndex + 1).toLowerCase();
    }
    
    /**
     * Get file type
     *
     * @param photoByte file bytecode
     * @return suffix (without ".")
     */
    public static String getFileExtendName(byte[] photoByte)
    {
        String strFileExtendName = "JPG";
        if ((photoByte[0] == 71) && (photoByte[1] == 73) && (photoByte[2] == 70) && (photoByte[3] == 56)
                && ((photoByte[4] == 55) || (photoByte[4] == 57)) && (photoByte[5] == 97))
        {
            strFileExtendName = "GIF";
        }
        else if ((photoByte[6] == 74) && (photoByte[7] == 70) && (photoByte[8] == 73) && (photoByte[9] == 70))
        {
            strFileExtendName = "JPG";
        }
        else if ((photoByte[0] == 66) && (photoByte[1] == 77))
        {
            strFileExtendName = "BMP";
        }
        else if ((photoByte[1] == 80) && (photoByte[2] == 78) && (photoByte[3] == 71))
        {
            strFileExtendName = "PNG";
        }
        return strFileExtendName;
    }
}