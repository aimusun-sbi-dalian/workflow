package com.sbi.expo.commons.utils;

import com.sbi.expo.commons.CommonConstant;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class FileHelper {

    private FileHelper() {}

    /** get file suffix without . */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /** JAVA file operation get file name without suffix */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if (dot > -1) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /** transform file size */
    public static String getSize(long size) {
        String resultSize;
        if (size / CommonConstant.GB >= 1) {
            // if byte value lager than 1GB
            resultSize = CommonConstant.DF.format(size / (float) CommonConstant.GB) + "GB   ";
        } else if (size / CommonConstant.MB >= 1) {
            // if byte value larger than 1MB
            resultSize = CommonConstant.DF.format(size / (float) CommonConstant.MB) + "MB   ";
        } else if (size / CommonConstant.KB >= 1) {
            // if byte value larger than 1KB
            resultSize = CommonConstant.DF.format(size / (float) CommonConstant.KB) + "KB   ";
        } else {
            resultSize = size + "B   ";
        }
        return resultSize;
    }

    /** inputStream2File */
    static File inputStreamToFile(InputStream ins, String name) {
        File file = new File(CommonConstant.SYS_TEM_DIR + name);
        if (file.exists()) {
            return file;
        }
        try (OutputStream os = new FileOutputStream(file)) {
            int bytesRead;
            int len = 8192;
            byte[] buffer = new byte[len];
            while ((bytesRead = ins.read(buffer, 0, len)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return file;
    }

    public static File upload(MultipartFile file, String filePath) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssS");
        String name = getFileNameNoEx(file.getOriginalFilename());
        String suffix = getExtensionName(file.getOriginalFilename());
        String nowStr = "-" + format.format(date);
        try {
            String fileName = name + nowStr + "." + suffix;
            String path = filePath + fileName;
            // getCanonicalFile get file correct path
            File dest = new File(path).getCanonicalFile();
            // check if dir exist
            if (!dest.getParentFile().exists() && !dest.getParentFile().mkdirs()) {
                log.info("was not successful.");
            }
            // write file
            file.transferTo(dest);
            return dest;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static String getFileType(String type) {
        String documents = "txt doc pdf ppt pps xlsx xls docx";
        String music = "mp3 wav wma mpa ram ra aac aif m4a";
        String video = "avi mpg mpe mpeg asf wmv mov qt rm mp4 flv m4v webm ogv ogg";
        String image = "bmp dib pcp dif wmf gif jpg tif eps psd cdr iff tga pcd mpt png jpeg";
        if (image.contains(type)) {
            return CommonConstant.IMAGE;
        } else if (documents.contains(type)) {
            return CommonConstant.TXT;
        } else if (music.contains(type)) {
            return CommonConstant.MUSIC;
        } else if (video.contains(type)) {
            return CommonConstant.VIDEO;
        } else {
            return CommonConstant.OTHER;
        }
    }

    public static void downloadFile(
            HttpServletRequest request,
            HttpServletResponse response,
            File file,
            boolean deleteOnExit) {
        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/octet-stream");
        try (FileInputStream fis = new FileInputStream(file)) {
            response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
            IOUtils.copy(fis, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (deleteOnExit) {
                file.deleteOnExit();
            }
        }
    }
}
