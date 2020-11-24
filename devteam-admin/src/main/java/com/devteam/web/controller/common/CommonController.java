package com.devteam.web.controller.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.devteam.common.config.DevteamConfig;
import com.devteam.common.constant.Constants;
import com.devteam.common.core.domain.AjaxResult;
import com.devteam.common.utils.StringUtils;
import com.devteam.common.utils.file.FileUploadUtils;
import com.devteam.common.utils.file.FileUtils;
import com.devteam.framework.config.ServerConfig;

/**
 * General request processing
 *
 */
@RestController
public class CommonController
{
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ServerConfig serverConfig;

    /**
     * Universal download request
     *
     * @param fileName file name
     * @param delete whether to delete
     */
    @GetMapping("common/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request)
    {
        try
        {
            if (!FileUtils.checkAllowDownload(fileName))
            {
                throw new Exception(StringUtils.format("File name ({}) is illegal and download is not allowed. ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = DevteamConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete)
            {
                FileUtils.deleteFile(filePath);
            }
        }
        catch (Exception e)
        {
            log.error("Failed to download file", e);
        }
    }

    /**
     * Universal upload request
     */
    @PostMapping("/common/upload")
    public AjaxResult uploadFile(MultipartFile file) throws Exception
    {
        try
        {
            // Upload file path
            String filePath = DevteamConfig.getUploadPath();
            // Upload and return the new file name
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileName", fileName);
            ajax.put("url", url);
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * General download of local resources
     */
    @GetMapping("/common/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        try
        {
            if (!FileUtils.checkAllowDownload(resource))
            {
                throw new Exception(StringUtils.format("The resource file ({}) is illegal and downloading is not allowed. ", resource));
            }
            // local resource path
            String localPath = DevteamConfig.getProfile();
            // Database resource address
            String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
            // download name
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        }
        catch (Exception e)
        {
            log.error("Failed to download file", e);
        }
    }
}
