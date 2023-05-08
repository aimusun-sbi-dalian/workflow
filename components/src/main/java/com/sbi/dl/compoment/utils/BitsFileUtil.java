package com.sbi.dl.compoment.utils;

import cn.hutool.core.util.IdUtil;
import com.sbi.dl.compoment.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class BitsFileUtil extends cn.hutool.core.io.FileUtil {

    /** MultipartFile2File */
    public static File toFile(MultipartFile multipartFile) {
        // get file name
        String fileName = multipartFile.getOriginalFilename();
        // get file suffix
        String prefix = "." + getExtensionName(fileName);
        File file = null;
        try {
            // use uuid as file name in case of duplicate file name
            file = new File(CommonConstant.SYS_TEM_DIR + IdUtil.simpleUUID() + prefix);
            // MultipartFile to File
            multipartFile.transferTo(file);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return file;
    }

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
            if ((dot > -1) && (dot < (filename.length()))) {
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
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int bytesRead;
            int len = 8192;
            byte[] buffer = new byte[len];
            while ((bytesRead = ins.read(buffer, 0, len)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BitsCloseUtil.close(os);
            BitsCloseUtil.close(ins);
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
            if (!dest.getParentFile().exists()) {
                if (!dest.getParentFile().mkdirs()) {
                    System.out.println("was not successful.");
                }
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

    /** check if two files are same */
    public static boolean check(File file1, File file2) {
        String img1Md5 = getMd5(file1);
        String img2Md5 = getMd5(file2);
        if (img1Md5 != null) {
            return img1Md5.equals(img2Md5);
        }
        return false;
    }

    /** check if two files are same */
    public static boolean check(String file1Md5, String file2Md5) {
        return file1Md5.equals(file2Md5);
    }

    private static byte[] getByte(File file) {
        // get file length
        byte[] b = new byte[(int) file.length()];
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            try {
                System.out.println(in.read(b));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            BitsCloseUtil.close(in);
        }
        return b;
    }

    private static String getMd5(byte[] bytes) {
        // 16bit char
        char[] hexDigits = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
        };
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(bytes);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            // move bit , output str
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static void downloadFile(
            HttpServletRequest request,
            HttpServletResponse response,
            File file,
            boolean deleteOnExit) {
        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/octet-stream");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
            IOUtils.copy(fis, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                    if (deleteOnExit) {
                        file.deleteOnExit();
                    }
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public static String getMd5(File file) {
        return getMd5(getByte(file));
    }
}
